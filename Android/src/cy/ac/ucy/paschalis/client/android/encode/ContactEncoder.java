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

package cy.ac.ucy.paschalis.client.android.encode;

import java.util.Collection;
import java.util.HashSet;

/**
 * Implementations encode according to some scheme for encoding contact information, like VCard or
 * MECARD.
 *
 * @author Sean Owen
 */
abstract class ContactEncoder {

  /**
   * @return first, the best effort encoding of all data in the appropriate format; second, a
   *   display-appropriate version of the contact information
   */
  abstract String[] encode(Iterable<String> names,
                           String organization,
                           Iterable<String> addresses,
                           Iterable<String> phones,
                           Iterable<String> emails,
                           String url,
                           String note);

  /**
   * @return null if s is null or empty, or result of s.trim() otherwise
   */
  static String trim(String s) {
    if (s == null) {
      return null;
    }
    String result = s.trim();
    return result.length() == 0 ? null : result;
  }

  static void doAppend(StringBuilder newContents,
                             StringBuilder newDisplayContents,
                             String prefix,
                             String value,
                             Formatter fieldFormatter,
                             char terminator) {
    String trimmed = trim(value);
    if (trimmed != null) {
      newContents.append(prefix).append(':').append(fieldFormatter.format(trimmed)).append(terminator);
      newDisplayContents.append(trimmed).append('\n');
    }
  }

  static void doAppendUpToUnique(StringBuilder newContents,
                                 StringBuilder newDisplayContents,
                                 String prefix,
                                 Iterable<String> values,
                                 int max,
                                 Formatter formatter,
                                 Formatter fieldFormatter,
                                 char terminator) {
    if (values == null) {
      return;
    }
    int count = 0;
    Collection<String> uniques = new HashSet<String>(2);
    for (String value : values) {
      String trimmed = trim(value);
      if (trimmed != null && !uniques.contains(trimmed)) {
        newContents.append(prefix).append(':').append(fieldFormatter.format(trimmed)).append(terminator);
        String display = formatter == null ? trimmed : formatter.format(trimmed);
        newDisplayContents.append(display).append('\n');
        if (++count == max) {
          break;
        }
        uniques.add(trimmed);
      }
    }
  }

}
