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

import cy.ac.ucy.paschalis.common.BitMatrix;
import cy.ac.ucy.paschalis.oned.CodaBarWriter;
import cy.ac.ucy.paschalis.oned.Code128Writer;
import cy.ac.ucy.paschalis.oned.Code39Writer;
import cy.ac.ucy.paschalis.oned.EAN13Writer;
import cy.ac.ucy.paschalis.oned.EAN8Writer;
import cy.ac.ucy.paschalis.oned.ITFWriter;
import cy.ac.ucy.paschalis.oned.UPCAWriter;
import cy.ac.ucy.paschalis.pdf417.encoder.PDF417Writer;
import cy.ac.ucy.paschalis.qrcode.QRCodeWriter;

/**
 * This is a factory class which finds the appropriate Writer subclass for the BarcodeFormat
 * requested and encodes the barcode with the supplied contents.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class MultiFormatWriter implements Writer {

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width,
                          int height) throws WriterException {
    return encode(contents, format, width, height, null);
  }

  @Override
  public BitMatrix encode(String contents,
                          BarcodeFormat format,
                          int width, int height,
                          Map<EncodeHintType,?> hints) throws WriterException {

    Writer writer;
    switch (format) {
      case EAN_8:
        writer = new EAN8Writer();
        break;
      case EAN_13:
        writer = new EAN13Writer();
        break;
      case UPC_A:
        writer = new UPCAWriter();
        break;
      case QR_CODE:
        writer = new QRCodeWriter();
        break;
      case CODE_39:
        writer = new Code39Writer();
        break;
      case CODE_128:
        writer = new Code128Writer();
        break;
      case ITF:
        writer = new ITFWriter();
        break;
      case PDF_417:
        writer = new PDF417Writer();
        break;
      case CODABAR:
        writer = new CodaBarWriter();
        break;
      default:
        throw new IllegalArgumentException("No encoder available for format " + format);
    }
    return writer.encode(contents, format, width, height, hints);
  }

}
