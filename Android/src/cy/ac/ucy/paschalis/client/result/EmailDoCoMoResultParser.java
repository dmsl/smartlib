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

import java.util.regex.Pattern;

import cy.ac.ucy.paschalis.Result;

/**
 * Implements the "MATMSG" email message entry format.
 *
 * Supported keys: TO, SUB, BODY
 *
 * @author Sean Owen
 */
public final class EmailDoCoMoResultParser extends AbstractDoCoMoResultParser {

  private static final Pattern ATEXT_ALPHANUMERIC = Pattern.compile("[a-zA-Z0-9@.!#$%&'*+\\-/=?^_`{|}~]+");

  @Override
  public EmailAddressParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!rawText.startsWith("MATMSG:")) {
      return null;
    }
    String[] rawTo = matchDoCoMoPrefixedField("TO:", rawText, true);
    if (rawTo == null) {
      return null;
    }
    String to = rawTo[0];
    if (!isBasicallyValidEmailAddress(to)) {
      return null;
    }
    String subject = matchSingleDoCoMoPrefixedField("SUB:", rawText, false);
    String body = matchSingleDoCoMoPrefixedField("BODY:", rawText, false);
    return new EmailAddressParsedResult(to, subject, body, "mailto:" + to);
  }

  /**
   * This implements only the most basic checking for an email address's validity -- that it contains
   * an '@' and contains no characters disallowed by RFC 2822. This is an overly lenient definition of
   * validity. We want to generally be lenient here since this class is only intended to encapsulate what's
   * in a barcode, not "judge" it.
   */
  static boolean isBasicallyValidEmailAddress(String email) {
    return email != null && ATEXT_ALPHANUMERIC.matcher(email).matches() && email.indexOf('@') >= 0;
  }

}