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
 * Parses the "URLTO" result format, which is of the form "URLTO:[title]:[url]".
 * This seems to be used sometimes, but I am not able to find documentation
 * on its origin or official format?
 *
 * @author Sean Owen
 */
public final class URLTOResultParser extends ResultParser {

  @Override
  public URIParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!rawText.startsWith("urlto:") && !rawText.startsWith("URLTO:")) {
      return null;
    }
    int titleEnd = rawText.indexOf(':', 6);
    if (titleEnd < 0) {
      return null;
    }
    String title = titleEnd <= 6 ? null : rawText.substring(6, titleEnd);
    String uri = rawText.substring(titleEnd + 1);
    return new URIParsedResult(uri, title);
  }

}