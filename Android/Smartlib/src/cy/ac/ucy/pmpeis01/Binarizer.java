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

package cy.ac.ucy.pmpeis01;

import cy.ac.ucy.pmpeis01.common.BitArray;
import cy.ac.ucy.pmpeis01.common.BitMatrix;

/**
 * This class hierarchy provides a set of methods to convert luminance data to 1 bit data.
 * It allows the algorithm to vary polymorphically, for example allowing a very expensive
 * thresholding technique for servers and a fast one for mobile. It also permits the implementation
 * to vary, e.g. a JNI version for Android and a Java fallback version for other platforms.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public abstract class Binarizer {

  private final LuminanceSource source;

  protected Binarizer(LuminanceSource source) {
    this.source = source;
  }

  public final LuminanceSource getLuminanceSource() {
    return source;
  }

  /**
   * Converts one row of luminance data to 1 bit data. May actually do the conversion, or return
   * cached data. Callers should assume this method is expensive and call it as seldom as possible.
   * This method is intended for decoding 1D barcodes and may choose to apply sharpening.
   * For callers which only examine one row of pixels at a time, the same BitArray should be reused
   * and passed in with each call for performance. However it is legal to keep more than one row
   * at a time if needed.
   *
   * @param y The row to fetch, 0 <= y < bitmap height.
   * @param row An optional preallocated array. If null or too small, it will be ignored.
   *            If used, the Binarizer will call BitArray.clear(). Always use the returned object.
   * @return The array of bits for this row (true means black).
   */
  public abstract BitArray getBlackRow(int y, BitArray row) throws NotFoundException;

  /**
   * Converts a 2D array of luminance data to 1 bit data. As above, assume this method is expensive
   * and do not call it repeatedly. This method is intended for decoding 2D barcodes and may or
   * may not apply sharpening. Therefore, a row from this matrix may not be identical to one
   * fetched using getBlackRow(), so don't mix and match between them.
   *
   * @return The 2D array of bits for the image (true means black).
   */
  public abstract BitMatrix getBlackMatrix() throws NotFoundException;

  /**
   * Creates a new object with the same type as this Binarizer implementation, but with pristine
   * state. This is needed because Binarizer implementations may be stateful, e.g. keeping a cache
   * of 1 bit data. See Effective Java for why we can't use Java's clone() method.
   *
   * @param source The LuminanceSource this Binarizer will operate on.
   * @return A new concrete Binarizer implementation object.
   */
  public abstract Binarizer createBinarizer(LuminanceSource source);

  public final int getWidth() {
    return source.getWidth();
  }

  public final int getHeight() {
    return source.getHeight();
  }

}
