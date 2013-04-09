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

import java.util.Map;

import cy.ac.ucy.pmpeis01.Result;

/**
 * Represents a result that encodes an e-mail address, either as a plain address
 * like "joe@example.org" or a mailto: URL like "mailto:joe@example.org".
 *
 * @author Sean Owen
 */
public final class EmailAddressResultParser extends ResultParser {

  @Override
  public EmailAddressParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    String emailAddress;
    if (rawText.startsWith("mailto:") || rawText.startsWith("MAILTO:")) {
      // If it starts with mailto:, assume it is definitely trying to be an email address
      emailAddress = rawText.substring(7);
      int queryStart = emailAddress.indexOf('?');
      if (queryStart >= 0) {
        emailAddress = emailAddress.substring(0, queryStart);
      }
      Map<String,String> nameValues = parseNameValuePairs(rawText);
      String subject = null;
      String body = null;
      if (nameValues != null) {
        if (emailAddress.length() == 0) {
          emailAddress = nameValues.get("to");
        }
        subject = nameValues.get("subject");
        body = nameValues.get("body");
      }
      return new EmailAddressParsedResult(emailAddress, subject, body, rawText);
    } else {
      if (!EmailDoCoMoResultParser.isBasicallyValidEmailAddress(rawText)) {
        return null;
      }
      emailAddress = rawText;
      return new EmailAddressParsedResult(emailAddress, null, null, "mailto:" + emailAddress);
    }
  }

}