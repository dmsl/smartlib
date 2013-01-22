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

package cy.ac.ucy.paschalis;

/**
 * These are a set of hints that you may pass to Writers to specify their behavior.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public enum EncodeHintType {

  /**
   * Specifies what degree of error correction to use, for example in QR Codes.
   * Type depends on the encoder. For example for QR codes it's type
   * {@link cy.ac.ucy.paschalis.qrcode.decoder.ErrorCorrectionLevel ErrorCorrectionLevel}.
   */
  ERROR_CORRECTION,

  /**
   * Specifies what character encoding to use where applicable (type {@link String})
   */
  CHARACTER_SET,

  /**
   * Specifies whether to use compact mode for PDF417 (type {@link Boolean}).
   */
  PDF417_COMPACT,

  /**
   * Specifies what compaction mode to use for PDF417 (type
   * {@link cy.ac.ucy.paschalis.pdf417.encoder.Compaction Compaction}).
   */
  PDF417_COMPACTION,

  /**
   * Specifies the minimum and maximum number of rows and columns for PDF417 (type
   * {@link cy.ac.ucy.paschalis.pdf417.encoder.Dimensions Dimensions}).
   */
  PDF417_DIMENSIONS,

}
