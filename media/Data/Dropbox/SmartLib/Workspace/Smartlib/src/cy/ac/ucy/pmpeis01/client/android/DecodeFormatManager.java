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

package cy.ac.ucy.pmpeis01.client.android;

import java.util.Arrays;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;
import java.util.regex.Pattern;

import android.content.Intent;
import android.net.Uri;
import cy.ac.ucy.pmpeis01.BarcodeFormat;

final class DecodeFormatManager {

  private static final Pattern COMMA_PATTERN = Pattern.compile(",");

  static final Collection<BarcodeFormat> PRODUCT_FORMATS;
  static final Collection<BarcodeFormat> ONE_D_FORMATS;
  static final Collection<BarcodeFormat> QR_CODE_FORMATS = EnumSet.of(BarcodeFormat.QR_CODE);
  static final Collection<BarcodeFormat> DATA_MATRIX_FORMATS = EnumSet.of(BarcodeFormat.DATA_MATRIX);
  static {
    PRODUCT_FORMATS = EnumSet.of(BarcodeFormat.UPC_A,
                                 BarcodeFormat.UPC_E,
                                 BarcodeFormat.EAN_13,
                                 BarcodeFormat.EAN_8,
                                 BarcodeFormat.RSS_14);
    ONE_D_FORMATS = EnumSet.of(BarcodeFormat.CODE_39,
                               BarcodeFormat.CODE_93,
                               BarcodeFormat.CODE_128,
                               BarcodeFormat.ITF,
                               BarcodeFormat.CODABAR);
    ONE_D_FORMATS.addAll(PRODUCT_FORMATS);
  }

  private DecodeFormatManager() {}

  static Collection<BarcodeFormat> parseDecodeFormats(Intent intent) {
    List<String> scanFormats = null;
    String scanFormatsString = intent.getStringExtra(Intents.Scan.FORMATS);
    if (scanFormatsString != null) {
      scanFormats = Arrays.asList(COMMA_PATTERN.split(scanFormatsString));
    }
    return parseDecodeFormats(scanFormats, intent.getStringExtra(Intents.Scan.MODE));
  }

  static Collection<BarcodeFormat> parseDecodeFormats(Uri inputUri) {
    List<String> formats = inputUri.getQueryParameters(Intents.Scan.FORMATS);
    if (formats != null && formats.size() == 1 && formats.get(0) != null){
      formats = Arrays.asList(COMMA_PATTERN.split(formats.get(0)));
    }
    return parseDecodeFormats(formats, inputUri.getQueryParameter(Intents.Scan.MODE));
  }

  private static Collection<BarcodeFormat> parseDecodeFormats(Iterable<String> scanFormats,
                                                              String decodeMode) {
    if (scanFormats != null) {
      Collection<BarcodeFormat> formats = EnumSet.noneOf(BarcodeFormat.class);
      try {
        for (String format : scanFormats) {
          formats.add(BarcodeFormat.valueOf(format));
        }
        return formats;
      } catch (IllegalArgumentException iae) {
        // ignore it then
      }
    }
    if (decodeMode != null) {
      if (Intents.Scan.PRODUCT_MODE.equals(decodeMode)) {
        return PRODUCT_FORMATS;
      }
      if (Intents.Scan.QR_CODE_MODE.equals(decodeMode)) {
        return QR_CODE_FORMATS;
      }
      if (Intents.Scan.DATA_MATRIX_MODE.equals(decodeMode)) {
        return DATA_MATRIX_FORMATS;
      }
      if (Intents.Scan.ONE_D_MODE.equals(decodeMode)) {
        return ONE_D_FORMATS;
      }
    }
    return null;
  }

}
