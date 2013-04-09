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

package cy.ac.ucy.paschalis.client.android.result;

import cy.ac.ucy.paschalis.Result;
import cy.ac.ucy.paschalis.client.android.CaptureActivity;
import cy.ac.ucy.paschalis.client.result.ParsedResult;
import cy.ac.ucy.paschalis.client.result.ResultParser;

/**
 * Manufactures Android-specific handlers based on the barcode content's type.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ResultHandlerFactory {
  private ResultHandlerFactory() {
  }

  public static ResultHandler makeResultHandler(CaptureActivity activity, Result rawResult) {
    ParsedResult result = parseResult(rawResult);
    switch (result.getType()) {
      case ADDRESSBOOK:
        return new AddressBookResultHandler(activity, result);
      case EMAIL_ADDRESS:
        return new EmailAddressResultHandler(activity, result);
      case PRODUCT:
        return new ProductResultHandler(activity, result, rawResult);
      case URI:
        return new URIResultHandler(activity, result);
      case WIFI:
        return new WifiResultHandler(activity, result);
      case GEO:
        return new GeoResultHandler(activity, result);
      case TEL:
        return new TelResultHandler(activity, result);
      case SMS:
        return new SMSResultHandler(activity, result);
      case CALENDAR:
        return new CalendarResultHandler(activity, result);
      case ISBN:
        return new ISBNResultHandler(activity, result, rawResult);
      default:
        return new TextResultHandler(activity, result, rawResult);
    }
  }

  private static ParsedResult parseResult(Result rawResult) {
    return ResultParser.parseResult(rawResult);
  }
}
