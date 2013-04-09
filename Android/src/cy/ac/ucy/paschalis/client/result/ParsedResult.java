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
 * <p>Abstract class representing the result of decoding a barcode, as more than
 * a String -- as some type of structured data. This might be a subclass which represents
 * a URL, or an e-mail address. {@link ResultParser#parseResult(Result)} will turn a raw
 * decoded string into the most appropriate type of structured representation.</p>
 *
 * <p>Thanks to Jeff Griffin for proposing rewrite of these classes that relies less
 * on exception-based mechanisms during parsing.</p>
 *
 * @author Sean Owen
 */
public abstract class ParsedResult {

  private final ParsedResultType type;

  protected ParsedResult(ParsedResultType type) {
    this.type = type;
  }

  public final ParsedResultType getType() {
    return type;
  }

  public abstract String getDisplayResult();

  @Override
  public final String toString() {
    return getDisplayResult();
  }

  public static void maybeAppend(String value, StringBuilder result) {
    if (value != null && value.length() > 0) {
      // Don't add a newline before the first value
      if (result.length() > 0) {
        result.append('\n');
      }
      result.append(value);
    }
  }

  public static void maybeAppend(String[] value, StringBuilder result) {
    if (value != null) {
      for (String s : value) {
        if (s != null && s.length() > 0) {
          if (result.length() > 0) {
            result.append('\n');
          }
          result.append(s);
        }
      }
    }
  }

}
