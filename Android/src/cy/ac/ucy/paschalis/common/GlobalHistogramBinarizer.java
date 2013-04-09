/*
 This file is part of SmartLib Project.

    SmartLib is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    SmartLib is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with SmartLib.  If not, see <http://www.gnu.org/licenses/>.
    
	Author: Paschalis Mpeis

	Affiliation:
	Data Management Systems Laboratory 
	Dept. of Computer Science 
	University of Cyprus 
	P.O. Box 20537 
	1678 Nicosia, CYPRUS 
	Web: http://dmsl.cs.ucy.ac.cy/
	Email: dmsl@cs.ucy.ac.cy
	Tel: +357-22-892755
	Fax: +357-22-892701
	

 */

package cy.ac.ucy.paschalis.common;

import cy.ac.ucy.paschalis.Binarizer;
import cy.ac.ucy.paschalis.LuminanceSource;
import cy.ac.ucy.paschalis.NotFoundException;

/**
 * This Binarizer implementation uses the old ZXing global histogram approach. It is suitable
 * for low-end mobile devices which don't have enough CPU or memory to use a local thresholding
 * algorithm. However, because it picks a global black point, it cannot handle difficult shadows
 * and gradients.
 *
 * Faster mobile devices and all desktop applications should probably use HybridBinarizer instead.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public class GlobalHistogramBinarizer extends Binarizer {

  private static final int LUMINANCE_BITS = 5;
  private static final int LUMINANCE_SHIFT = 8 - LUMINANCE_BITS;
  private static final int LUMINANCE_BUCKETS = 1 << LUMINANCE_BITS;
  private static final byte[] EMPTY = new byte[0];

  private byte[] luminances;
  private final int[] buckets;

  public GlobalHistogramBinarizer(LuminanceSource source) {
    super(source);
    luminances = EMPTY;
    buckets = new int[LUMINANCE_BUCKETS];
  }

  // Applies simple sharpening to the row data to improve performance of the 1D Readers.
  @Override
  public BitArray getBlackRow(int y, BitArray row) throws NotFoundException {
    LuminanceSource source = getLuminanceSource();
    int width = source.getWidth();
    if (row == null || row.getSize() < width) {
      row = new BitArray(width);
    } else {
      row.clear();
    }

    initArrays(width);
    byte[] localLuminances = source.getRow(y, luminances);
    int[] localBuckets = buckets;
    for (int x = 0; x < width; x++) {
      int pixel = localLuminances[x] & 0xff;
      localBuckets[pixel >> LUMINANCE_SHIFT]++;
    }
    int blackPoint = estimateBlackPoint(localBuckets);

    int left = localLuminances[0] & 0xff;
    int center = localLuminances[1] & 0xff;
    for (int x = 1; x < width - 1; x++) {
      int right = localLuminances[x + 1] & 0xff;
      // A simple -1 4 -1 box filter with a weight of 2.
      int luminance = ((center << 2) - left - right) >> 1;
      if (luminance < blackPoint) {
        row.set(x);
      }
      left = center;
      center = right;
    }
    return row;
  }

  // Does not sharpen the data, as this call is intended to only be used by 2D Readers.
  @Override
  public BitMatrix getBlackMatrix() throws NotFoundException {
    LuminanceSource source = getLuminanceSource();
    int width = source.getWidth();
    int height = source.getHeight();
    BitMatrix matrix = new BitMatrix(width, height);

    // Quickly calculates the histogram by sampling four rows from the image. This proved to be
    // more robust on the blackbox tests than sampling a diagonal as we used to do.
    initArrays(width);
    int[] localBuckets = buckets;
    for (int y = 1; y < 5; y++) {
      int row = height * y / 5;
      byte[] localLuminances = source.getRow(row, luminances);
      int right = (width << 2) / 5;
      for (int x = width / 5; x < right; x++) {
        int pixel = localLuminances[x] & 0xff;
        localBuckets[pixel >> LUMINANCE_SHIFT]++;
      }
    }
    int blackPoint = estimateBlackPoint(localBuckets);

    // We delay reading the entire image luminance until the black point estimation succeeds.
    // Although we end up reading four rows twice, it is consistent with our motto of
    // "fail quickly" which is necessary for continuous scanning.
    byte[] localLuminances = source.getMatrix();
    for (int y = 0; y < height; y++) {
      int offset = y * width;
      for (int x = 0; x< width; x++) {
        int pixel = localLuminances[offset + x] & 0xff;
        if (pixel < blackPoint) {
          matrix.set(x, y);
        }
      }
    }

    return matrix;
  }

  @Override
  public Binarizer createBinarizer(LuminanceSource source) {
    return new GlobalHistogramBinarizer(source);
  }

  private void initArrays(int luminanceSize) {
    if (luminances.length < luminanceSize) {
      luminances = new byte[luminanceSize];
    }
    for (int x = 0; x < LUMINANCE_BUCKETS; x++) {
      buckets[x] = 0;
    }
  }

  private static int estimateBlackPoint(int[] buckets) throws NotFoundException {
    // Find the tallest peak in the histogram.
    int numBuckets = buckets.length;
    int maxBucketCount = 0;
    int firstPeak = 0;
    int firstPeakSize = 0;
    for (int x = 0; x < numBuckets; x++) {
      if (buckets[x] > firstPeakSize) {
        firstPeak = x;
        firstPeakSize = buckets[x];
      }
      if (buckets[x] > maxBucketCount) {
        maxBucketCount = buckets[x];
      }
    }

    // Find the second-tallest peak which is somewhat far from the tallest peak.
    int secondPeak = 0;
    int secondPeakScore = 0;
    for (int x = 0; x < numBuckets; x++) {
      int distanceToBiggest = x - firstPeak;
      // Encourage more distant second peaks by multiplying by square of distance.
      int score = buckets[x] * distanceToBiggest * distanceToBiggest;
      if (score > secondPeakScore) {
        secondPeak = x;
        secondPeakScore = score;
      }
    }

    // Make sure firstPeak corresponds to the black peak.
    if (firstPeak > secondPeak) {
      int temp = firstPeak;
      firstPeak = secondPeak;
      secondPeak = temp;
    }

    // If there is too little contrast in the image to pick a meaningful black point, throw rather
    // than waste time trying to decode the image, and risk false positives.
    if (secondPeak - firstPeak <= numBuckets >> 4) {
      throw NotFoundException.getNotFoundInstance();
    }

    // Find a valley between them that is low and closer to the white peak.
    int bestValley = secondPeak - 1;
    int bestValleyScore = -1;
    for (int x = secondPeak - 1; x > firstPeak; x--) {
      int fromFirst = x - firstPeak;
      int score = fromFirst * fromFirst * (secondPeak - x) * (maxBucketCount - buckets[x]);
      if (score > bestValleyScore) {
        bestValley = x;
        bestValleyScore = score;
      }
    }

    return bestValley << LUMINANCE_SHIFT;
  }

}
