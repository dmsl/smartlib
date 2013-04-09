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

/**
 * @author Sean Owen
 */
public final class TelParsedResult extends ParsedResult {

  private final String number;
  private final String telURI;
  private final String title;

  public TelParsedResult(String number, String telURI, String title) {
    super(ParsedResultType.TEL);
    this.number = number;
    this.telURI = telURI;
    this.title = title;
  }

  public String getNumber() {
    return number;
  }

  public String getTelURI() {
    return telURI;
  }

  public String getTitle() {
    return title;
  }

  @Override
  public String getDisplayResult() {
    StringBuilder result = new StringBuilder(20);
    maybeAppend(number, result);
    maybeAppend(title, result);
    return result.toString();
  }

}