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
 * Represents some type of metadata about the result of the decoding that the decoder
 * wishes to communicate back to the caller.
 *
 * @author Sean Owen
 */
public enum ResultMetadataType {

  /**
   * Unspecified, application-specific metadata. Maps to an unspecified {@link Object}.
   */
  OTHER,

  /**
   * Denotes the likely approximate orientation of the barcode in the image. This value
   * is given as degrees rotated clockwise from the normal, upright orientation.
   * For example a 1D barcode which was found by reading top-to-bottom would be
   * said to have orientation "90". This key maps to an {@link Integer} whose
   * value is in the range [0,360).
   */
  ORIENTATION,

  /**
   * <p>2D barcode formats typically encode text, but allow for a sort of 'byte mode'
   * which is sometimes used to encode binary data. While {@link Result} makes available
   * the complete raw bytes in the barcode for these formats, it does not offer the bytes
   * from the byte segments alone.</p>
   *
   * <p>This maps to a {@link java.util.List} of byte arrays corresponding to the
   * raw bytes in the byte segments in the barcode, in order.</p>
   */
  BYTE_SEGMENTS,

  /**
   * Error correction level used, if applicable. The value type depends on the
   * format, but is typically a String.
   */
  ERROR_CORRECTION_LEVEL,

  /**
   * For some periodicals, indicates the issue number as an {@link Integer}.
   */
  ISSUE_NUMBER,

  /**
   * For some products, indicates the suggested retail price in the barcode as a
   * formatted {@link String}.
   */
  SUGGESTED_PRICE ,

  /**
   * For some products, the possible country of manufacture as a {@link String} denoting the
   * ISO country code. Some map to multiple possible countries, like "US/CA".
   */
  POSSIBLE_COUNTRY,

  /**
   * For some products, the extension text
   */
  UPC_EAN_EXTENSION,

}
