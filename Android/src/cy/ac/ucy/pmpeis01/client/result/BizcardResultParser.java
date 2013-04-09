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

import java.util.ArrayList;
import java.util.List;

import cy.ac.ucy.pmpeis01.Result;

/**
 * Implements the "BIZCARD" address book entry format, though this has been
 * largely reverse-engineered from examples observed in the wild -- still
 * looking for a definitive reference.
 *
 * @author Sean Owen
 */
public final class BizcardResultParser extends AbstractDoCoMoResultParser {

  // Yes, we extend AbstractDoCoMoResultParser since the format is very much
  // like the DoCoMo MECARD format, but this is not technically one of 
  // DoCoMo's proposed formats

  @Override
  public AddressBookParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!rawText.startsWith("BIZCARD:")) {
      return null;
    }
    String firstName = matchSingleDoCoMoPrefixedField("N:", rawText, true);
    String lastName = matchSingleDoCoMoPrefixedField("X:", rawText, true);
    String fullName = buildName(firstName, lastName);
    String title = matchSingleDoCoMoPrefixedField("T:", rawText, true);
    String org = matchSingleDoCoMoPrefixedField("C:", rawText, true);
    String[] addresses = matchDoCoMoPrefixedField("A:", rawText, true);
    String phoneNumber1 = matchSingleDoCoMoPrefixedField("B:", rawText, true);
    String phoneNumber2 = matchSingleDoCoMoPrefixedField("M:", rawText, true);
    String phoneNumber3 = matchSingleDoCoMoPrefixedField("F:", rawText, true);
    String email = matchSingleDoCoMoPrefixedField("E:", rawText, true);

    return new AddressBookParsedResult(maybeWrap(fullName),
                                       null,
                                       buildPhoneNumbers(phoneNumber1, phoneNumber2, phoneNumber3),
                                       null,
                                       maybeWrap(email),
                                       null,
                                       null,
                                       null,
                                       addresses,
                                       null,
                                       org,
                                       null,
                                       title,
                                       null);
  }

  private static String[] buildPhoneNumbers(String number1,
                                            String number2,
                                            String number3) {
    List<String> numbers = new ArrayList<String>(3);
    if (number1 != null) {
      numbers.add(number1);
    }
    if (number2 != null) {
      numbers.add(number2);
    }
    if (number3 != null) {
      numbers.add(number3);
    }
    int size = numbers.size();
    if (size == 0) {
      return null;
    }
    return numbers.toArray(new String[size]);
  }

  private static String buildName(String firstName, String lastName) {
    if (firstName == null) {
      return lastName;
    } else {
      return lastName == null ? firstName : firstName + ' ' + lastName;
    }
  }

}
