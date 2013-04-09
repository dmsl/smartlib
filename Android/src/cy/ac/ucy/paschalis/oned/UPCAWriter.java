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

package cy.ac.ucy.paschalis.oned;

import java.util.Map;

import cy.ac.ucy.paschalis.BarcodeFormat;
import cy.ac.ucy.paschalis.EncodeHintType;
import cy.ac.ucy.paschalis.Writer;
import cy.ac.ucy.paschalis.WriterException;
import cy.ac.ucy.paschalis.common.BitMatrix;

/**
 * This object renders a UPC-A code as a {@link BitMatrix}.
 *
 * @author qwandor@google.com (Andrew Walbran)
 */
public final class UPCAWriter implements Writer {

  private final EAN13Writer subWriter = new EAN13Writer();

  @Override
  public BitMatrix encode(String contents, BarcodeFormat format, int width, int height)
      throws WriterException {
    return encode(contents, format, width, height, null);
  }

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height,
                          Map<EncodeHintType,?> hints) throws WriterException {
    if (format != BarcodeFormat.UPC_A) {
      throw new IllegalArgumentException("Can only encode UPC-A, but got " + format);
    }
    return subWriter.encode(preencode(contents), BarcodeFormat.EAN_13, width, height, hints);
  }

  /**
   * Transform a UPC-A code into the equivalent EAN-13 code, and add a check digit if it is not
   * already present.
   */
  private static String preencode(String contents) {
    int length = contents.length();
    if (length == 11) {
      // No check digit present, calculate it and add it
      int sum = 0;
      for (int i = 0; i < 11; ++i) {
        sum += (contents.charAt(i) - '0') * (i % 2 == 0 ? 3 : 1);
      }
      contents += (1000 - sum) % 10;
    } else if (length != 12) {
      throw new IllegalArgumentException(
          "Requested contents should be 11 or 12 digits long, but got " + contents.length());
    }
    return '0' + contents;
  }
}