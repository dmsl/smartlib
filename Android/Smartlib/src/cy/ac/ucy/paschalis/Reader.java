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

import java.util.Map;

/**
 * Implementations of this interface can decode an image of a barcode in some format into
 * the String it encodes. For example, {@link cy.ac.ucy.paschalis.qrcode.QRCodeReader} can
 * decode a QR code. The decoder may optionally receive hints from the caller which may help
 * it decode more quickly or accurately.
 *
 * See {@link cy.ac.ucy.paschalis.MultiFormatReader}, which attempts to determine what barcode
 * format is present within the image as well, and then decodes it accordingly.
 *
 * @author Sean Owen
 * @author dswitkin@google.com (Daniel Switkin)
 */
public interface Reader {

  /**
   * Locates and decodes a barcode in some format within an image.
   *
   * @param image image of barcode to decode
   * @return String which the barcode encodes
   * @throws NotFoundException if the barcode cannot be located or decoded for any reason
   */
  Result decode(BinaryBitmap image) throws NotFoundException, ChecksumException, FormatException;

  /**
   * Locates and decodes a barcode in some format within an image. This method also accepts
   * hints, each possibly associated to some data, which may help the implementation decode.
   *
   * @param image image of barcode to decode
   * @param hints passed as a {@link java.util.Map} from {@link cy.ac.ucy.paschalis.DecodeHintType}
   * to arbitrary data. The
   * meaning of the data depends upon the hint type. The implementation may or may not do
   * anything with these hints.
   * @return String which the barcode encodes
   * @throws NotFoundException if the barcode cannot be located or decoded for any reason
   */
  Result decode(BinaryBitmap image, Map<DecodeHintType,?> hints)
      throws NotFoundException, ChecksumException, FormatException;

  /**
   * Resets any internal state the implementation has after a decode, to prepare it
   * for reuse.
   */
  void reset();

}