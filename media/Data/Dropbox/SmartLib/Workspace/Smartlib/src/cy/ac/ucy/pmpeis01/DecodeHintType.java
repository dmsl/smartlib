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

/**
 * Encapsulates a type of hint that a caller may pass to a barcode reader to help it
 * more quickly or accurately decode it. It is up to implementations to decide what,
 * if anything, to do with the information that is supplied.
 *
 * @author Sean Owen
 * @author dswitkin@google.com (Daniel Switkin)
 * @see Reader#decode(BinaryBitmap,java.util.Map)
 */
public enum DecodeHintType {

  /**
   * Unspecified, application-specific hint. Maps to an unspecified {@link Object}.
   */
 OTHER,

  /**
   * Image is a pure monochrome image of a barcode. Doesn't matter what it maps to;
   * use {@link Boolean#TRUE}.
   */
  PURE_BARCODE,

  /**
   * Image is known to be of one of a few possible formats.
   * Maps to a {@link java.util.List} of {@link BarcodeFormat}s.
   */
  POSSIBLE_FORMATS,

  /**
   * Spend more time to try to find a barcode; optimize for accuracy, not speed.
   * Doesn't matter what it maps to; use {@link Boolean#TRUE}.
   */
  TRY_HARDER,

  /**
   * Specifies what character encoding to use when decoding, where applicable (type String)
   */
  CHARACTER_SET,

  /**
   * Allowed lengths of encoded data -- reject anything else. Maps to an int[].
   */
  ALLOWED_LENGTHS,

  /**
   * Assume Code 39 codes employ a check digit. Maps to {@link Boolean}.
   */
  ASSUME_CODE_39_CHECK_DIGIT,

  /**
   * The caller needs to be notified via callback when a possible {@link ResultPoint}
   * is found. Maps to a {@link ResultPointCallback}.
   */
  NEED_RESULT_POINT_CALLBACK,

}
