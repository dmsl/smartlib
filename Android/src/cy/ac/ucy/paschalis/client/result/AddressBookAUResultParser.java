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

import java.util.ArrayList;
import java.util.List;

import cy.ac.ucy.paschalis.Result;

/**
 * Implements KDDI AU's address book format. See
 * <a href="http://www.au.kddi.com/ezfactory/tec/two_dimensions/index.html">
 * http://www.au.kddi.com/ezfactory/tec/two_dimensions/index.html</a>.
 * (Thanks to Yuzo for translating!)
 *
 * @author Sean Owen
 */
public final class AddressBookAUResultParser extends ResultParser {

  @Override
  public AddressBookParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    // MEMORY is mandatory; seems like a decent indicator, as does end-of-record separator CR/LF
    if (!rawText.contains("MEMORY") || !rawText.contains("\r\n")) {
      return null;
    }

    // NAME1 and NAME2 have specific uses, namely written name and pronunciation, respectively.
    // Therefore we treat them specially instead of as an array of names.
    String name = matchSinglePrefixedField("NAME1:", rawText, '\r', true);
    String pronunciation = matchSinglePrefixedField("NAME2:", rawText, '\r', true);

    String[] phoneNumbers = matchMultipleValuePrefix("TEL", 3, rawText, true);
    String[] emails = matchMultipleValuePrefix("MAIL", 3, rawText, true);
    String note = matchSinglePrefixedField("MEMORY:", rawText, '\r', false);
    String address = matchSinglePrefixedField("ADD:", rawText, '\r', true);
    String[] addresses = address == null ? null : new String[] {address};
    return new AddressBookParsedResult(maybeWrap(name),
                                       pronunciation,
                                       phoneNumbers,
                                       null,
                                       emails,
                                       null,
                                       null,
                                       note,
                                       addresses,
                                       null,
                                       null,
                                       null,
                                       null,
                                       null);
  }

  private static String[] matchMultipleValuePrefix(String prefix,
                                                   int max,
                                                   String rawText,
                                                   boolean trim) {
    List<String> values = null;
    for (int i = 1; i <= max; i++) {
      String value = matchSinglePrefixedField(prefix + i + ':', rawText, '\r', trim);
      if (value == null) {
        break;
      }
      if (values == null) {
        values = new ArrayList<String>(max); // lazy init
      }
      values.add(value);
    }
    if (values == null) {
      return null;
    }
    return values.toArray(new String[values.size()]);
  }

}
