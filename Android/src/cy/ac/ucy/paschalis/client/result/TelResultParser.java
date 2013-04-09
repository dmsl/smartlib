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

package cy.ac.ucy.paschalis.client.result;

import cy.ac.ucy.paschalis.Result;

/**
 * Parses a "tel:" URI result, which specifies a phone number.
 *
 * @author Sean Owen
 */
public final class TelResultParser extends ResultParser {

  @Override
  public TelParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!rawText.startsWith("tel:") && !rawText.startsWith("TEL:")) {
      return null;
    }
    // Normalize "TEL:" to "tel:"
    String telURI = rawText.startsWith("TEL:") ? "tel:" + rawText.substring(4) : rawText;
    // Drop tel, query portion
    int queryStart = rawText.indexOf('?', 4);
    String number = queryStart < 0 ? rawText.substring(4) : rawText.substring(4, queryStart);
    return new TelParsedResult(number, telURI, null);
  }

}