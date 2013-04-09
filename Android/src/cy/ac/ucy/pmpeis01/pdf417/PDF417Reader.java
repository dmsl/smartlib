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

package cy.ac.ucy.pmpeis01.pdf417;

import java.util.Map;

import cy.ac.ucy.pmpeis01.BarcodeFormat;
import cy.ac.ucy.pmpeis01.BinaryBitmap;
import cy.ac.ucy.pmpeis01.ChecksumException;
import cy.ac.ucy.pmpeis01.DecodeHintType;
import cy.ac.ucy.pmpeis01.FormatException;
import cy.ac.ucy.pmpeis01.NotFoundException;
import cy.ac.ucy.pmpeis01.Reader;
import cy.ac.ucy.pmpeis01.Result;
import cy.ac.ucy.pmpeis01.ResultPoint;
import cy.ac.ucy.pmpeis01.common.BitMatrix;
import cy.ac.ucy.pmpeis01.common.DecoderResult;
import cy.ac.ucy.pmpeis01.common.DetectorResult;
import cy.ac.ucy.pmpeis01.pdf417.decoder.Decoder;
import cy.ac.ucy.pmpeis01.pdf417.detector.Detector;

/**
 * This implementation can detect and decode PDF417 codes in an image.
 *
 * @author SITA Lab (kevin.osullivan@sita.aero)
 */
public final class PDF417Reader implements Reader {

  private static final ResultPoint[] NO_POINTS = new ResultPoint[0];

  private final Decoder decoder = new Decoder();

  /**
   * Locates and decodes a PDF417 code in an image.
   *
   * @return a String representing the content encoded by the PDF417 code
   * @throws NotFoundException if a PDF417 code cannot be found,
   * @throws FormatException if a PDF417 cannot be decoded
   */
  @Override
  public Result decode(BinaryBitmap image) throws NotFoundException, FormatException, ChecksumException {
    return decode(image, null);
  }

  @Override
  public Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws NotFoundException, FormatException, ChecksumException {
    DecoderResult decoderResult;
    ResultPoint[] points;
    if (hints != null && hints.containsKey(DecodeHintType.PURE_BARCODE)) {
      BitMatrix bits = extractPureBits(image.getBlackMatrix());
      decoderResult = decoder.decode(bits);
      points = NO_POINTS;
    } else {
      DetectorResult detectorResult = new Detector(image).detect();
      decoderResult = decoder.decode(detectorResult.getBits());
      points = detectorResult.getPoints();
    }
    return new Result(decoderResult.getText(), decoderResult.getRawBytes(), points,
        BarcodeFormat.PDF_417);
  }

  @Override
  public void reset() {
    // do nothing
  }

  /**
   * This method detects a code in a "pure" image -- that is, pure monochrome image
   * which contains only an unrotated, unskewed, image of a code, with some white border
   * around it. This is a specialized method that works exceptionally fast in this special
   * case.
   *
   * @see cy.ac.ucy.pmpeis01.qrcode.QRCodeReader#extractPureBits(BitMatrix)
   * @see cy.ac.ucy.pmpeis01.datamatrix.DataMatrixReader#extractPureBits(BitMatrix)
   */
  private static BitMatrix extractPureBits(BitMatrix image) throws NotFoundException {

    int[] leftTopBlack = image.getTopLeftOnBit();
    int[] rightBottomBlack = image.getBottomRightOnBit();
    if (leftTopBlack == null || rightBottomBlack == null) {
      throw NotFoundException.getNotFoundInstance();
    }

    int moduleSize = moduleSize(leftTopBlack, image);

    int top = leftTopBlack[1];
    int bottom = rightBottomBlack[1];
    int left = findPatternStart(leftTopBlack[0], top, image);
    int right = findPatternEnd(leftTopBlack[0], top, image);

    int matrixWidth = (right - left + 1) / moduleSize;
    int matrixHeight = (bottom - top + 1) / moduleSize;
    if (matrixWidth <= 0 || matrixHeight <= 0) {
      throw NotFoundException.getNotFoundInstance();
    }

    // Push in the "border" by half the module width so that we start
    // sampling in the middle of the module. Just in case the image is a
    // little off, this will help recover.
    int nudge = moduleSize >> 1;
    top += nudge;
    left += nudge;

    // Now just read off the bits
    BitMatrix bits = new BitMatrix(matrixWidth, matrixHeight);
    for (int y = 0; y < matrixHeight; y++) {
      int iOffset = top + y * moduleSize;
      for (int x = 0; x < matrixWidth; x++) {
        if (image.get(left + x * moduleSize, iOffset)) {
          bits.set(x, y);
        }
      }
    }
    return bits;
  }

  private static int moduleSize(int[] leftTopBlack, BitMatrix image) throws NotFoundException {
    int x = leftTopBlack[0];
    int y = leftTopBlack[1];
    int width = image.getWidth();
    while (x < width && image.get(x, y)) {
      x++;
    }
    if (x == width) {
      throw NotFoundException.getNotFoundInstance();
    }

    int moduleSize = (x - leftTopBlack[0]) >>> 3; // We've crossed left first bar, which is 8x
    if (moduleSize == 0) {
      throw NotFoundException.getNotFoundInstance();
    }

    return moduleSize;
  }

  private static int findPatternStart(int x, int y, BitMatrix image) throws NotFoundException {
    int width = image.getWidth();
    int start = x;
    // start should be on black
    int transitions = 0;
    boolean black = true;
    while (start < width - 1 && transitions < 8) {
      start++;
      boolean newBlack = image.get(start, y);
      if (black != newBlack) {
        transitions++;
      }
      black = newBlack;
    }
    if (start == width - 1) {
      throw NotFoundException.getNotFoundInstance();
    }
    return start;
  }

  private static int findPatternEnd(int x, int y, BitMatrix image) throws NotFoundException {
    int width = image.getWidth();
    int end = width - 1;
    // end should be on black
    while (end > x && !image.get(end, y)) {
      end--;
    }
    int transitions = 0;
    boolean black = true;
    while (end > x && transitions < 9) {
      end--;
      boolean newBlack = image.get(end, y);
      if (black != newBlack) {
        transitions++;
      }
      black = newBlack;
    }
    if (end == x) {
      throw NotFoundException.getNotFoundInstance();
    }
    return end;
  }

}
