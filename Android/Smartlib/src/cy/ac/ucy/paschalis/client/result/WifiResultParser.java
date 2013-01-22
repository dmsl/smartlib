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
 * Parses a WIFI configuration string.  Strings will be of the form:
 * WIFI:T:WPA;S:mynetwork;P:mypass;;
 *
 * The fields can come in any order, and there should be tests to see
 * if we can parse them all correctly.
 *
 * @author Vikram Aggarwal
 */
public final class WifiResultParser extends ResultParser {

  @Override
  public WifiParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!rawText.startsWith("WIFI:")) {
      return null;
    }
    // Don't remove leading or trailing whitespace
    boolean trim = false;
    String ssid = matchSinglePrefixedField("S:", rawText, ';', trim);
    if (ssid == null || ssid.length() == 0) {
      return null;
    }
    String pass = matchSinglePrefixedField("P:", rawText, ';', trim);
    String type = matchSinglePrefixedField("T:", rawText, ';', trim);
    if (type == null) {
      type = "nopass";
    }

    return new WifiParsedResult(type, ssid, pass);
  }
}