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

package cy.ac.ucy.pmpeis01.client.result;

import cy.ac.ucy.pmpeis01.BarcodeFormat;
import cy.ac.ucy.pmpeis01.Result;
import cy.ac.ucy.pmpeis01.oned.UPCEReader;

/**
 * Parses strings of digits that represent a UPC code.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ProductResultParser extends ResultParser {

  // Treat all UPC and EAN variants as UPCs, in the sense that they are all product barcodes.
  @Override
  public ProductParsedResult parse(Result result) {
    BarcodeFormat format = result.getBarcodeFormat();
    if (!(format == BarcodeFormat.UPC_A || format == BarcodeFormat.UPC_E ||
          format == BarcodeFormat.EAN_8 || format == BarcodeFormat.EAN_13)) {
      return null;
    }
    String rawText = getMassagedText(result);
    int length = rawText.length();
    for (int x = 0; x < length; x++) {
      char c = rawText.charAt(x);
      if (c < '0' || c > '9') {
        return null;
      }
    }
    // Not actually checking the checksum again here    

    String normalizedProductID;
    // Expand UPC-E for purposes of searching
    if (format == BarcodeFormat.UPC_E) {
      normalizedProductID = UPCEReader.convertUPCEtoUPCA(rawText);
    } else {
      normalizedProductID = rawText;
    }

    return new ProductParsedResult(rawText, normalizedProductID);
  }

}