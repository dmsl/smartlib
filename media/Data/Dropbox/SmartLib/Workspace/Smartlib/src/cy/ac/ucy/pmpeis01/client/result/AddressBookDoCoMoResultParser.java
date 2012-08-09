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

import cy.ac.ucy.pmpeis01.Result;

/**
 * Implements the "MECARD" address book entry format.
 *
 * Supported keys: N, SOUND, TEL, EMAIL, NOTE, ADR, BDAY, URL, plus ORG
 * Unsupported keys: TEL-AV, NICKNAME
 *
 * Except for TEL, multiple values for keys are also not supported;
 * the first one found takes precedence.
 *
 * Our understanding of the MECARD format is based on this document:
 *
 * http://www.mobicode.org.tw/files/OMIA%20Mobile%20Bar%20Code%20Standard%20v3.2.1.doc 
 *
 * @author Sean Owen
 */
public final class AddressBookDoCoMoResultParser extends AbstractDoCoMoResultParser {

  @Override
  public AddressBookParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!rawText.startsWith("MECARD:")) {
      return null;
    }
    String[] rawName = matchDoCoMoPrefixedField("N:", rawText, true);
    if (rawName == null) {
      return null;
    }
    String name = parseName(rawName[0]);
    String pronunciation = matchSingleDoCoMoPrefixedField("SOUND:", rawText, true);
    String[] phoneNumbers = matchDoCoMoPrefixedField("TEL:", rawText, true);
    String[] emails = matchDoCoMoPrefixedField("EMAIL:", rawText, true);
    String note = matchSingleDoCoMoPrefixedField("NOTE:", rawText, false);
    String[] addresses = matchDoCoMoPrefixedField("ADR:", rawText, true);
    String birthday = matchSingleDoCoMoPrefixedField("BDAY:", rawText, true);
    if (birthday != null && !isStringOfDigits(birthday, 8)) {
      // No reason to throw out the whole card because the birthday is formatted wrong.
      birthday = null;
    }
    String url = matchSingleDoCoMoPrefixedField("URL:", rawText, true);

    // Although ORG may not be strictly legal in MECARD, it does exist in VCARD and we might as well
    // honor it when found in the wild.
    String org = matchSingleDoCoMoPrefixedField("ORG:", rawText, true);

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
                                       org,
                                       birthday,
                                       null,
                                       url);
  }

  private static String parseName(String name) {
    int comma = name.indexOf((int) ',');
    if (comma >= 0) {
      // Format may be last,first; switch it around
      return name.substring(comma + 1) + ' ' + name.substring(0, comma);
    }
    return name;
  }

}
