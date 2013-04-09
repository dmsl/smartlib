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
import java.util.Collection;
import java.util.List;
import java.util.Map;

import cy.ac.ucy.pmpeis01.Result;

/**
 * <p>Parses an "sms:" URI result, which specifies a number to SMS.
 * See <a href="http://tools.ietf.org/html/rfc5724"> RFC 5724</a> on this.</p>
 *
 * <p>This class supports "via" syntax for numbers, which is not part of the spec.
 * For example "+12125551212;via=+12124440101" may appear as a number.
 * It also supports a "subject" query parameter, which is not mentioned in the spec.
 * These are included since they were mentioned in earlier IETF drafts and might be
 * used.</p>
 *
 * <p>This actually also parses URIs starting with "mms:" and treats them all the same way,
 * and effectively converts them to an "sms:" URI for purposes of forwarding to the platform.</p>
 *
 * @author Sean Owen
 */
public final class SMSMMSResultParser extends ResultParser {

  @Override
  public SMSParsedResult parse(Result result) {
    String rawText = getMassagedText(result);
    if (!(rawText.startsWith("sms:") || rawText.startsWith("SMS:") ||
          rawText.startsWith("mms:") || rawText.startsWith("MMS:"))) {
      return null;
    }

    // Check up front if this is a URI syntax string with query arguments
    Map<String,String> nameValuePairs = parseNameValuePairs(rawText);
    String subject = null;
    String body = null;
    boolean querySyntax = false;
    if (nameValuePairs != null && !nameValuePairs.isEmpty()) {
      subject = nameValuePairs.get("subject");
      body = nameValuePairs.get("body");
      querySyntax = true;
    }

    // Drop sms, query portion
    int queryStart = rawText.indexOf('?', 4);
    String smsURIWithoutQuery;
    // If it's not query syntax, the question mark is part of the subject or message
    if (queryStart < 0 || !querySyntax) {
      smsURIWithoutQuery = rawText.substring(4);
    } else {
      smsURIWithoutQuery = rawText.substring(4, queryStart);
    }

    int lastComma = -1;
    int comma;
    List<String> numbers = new ArrayList<String>(1);
    List<String> vias = new ArrayList<String>(1);
    while ((comma = smsURIWithoutQuery.indexOf(',', lastComma + 1)) > lastComma) {
      String numberPart = smsURIWithoutQuery.substring(lastComma + 1, comma);
      addNumberVia(numbers, vias, numberPart);
      lastComma = comma;
    }
    addNumberVia(numbers, vias, smsURIWithoutQuery.substring(lastComma + 1));    

    return new SMSParsedResult(numbers.toArray(new String[numbers.size()]),
                               vias.toArray(new String[vias.size()]),
                               subject,
                               body);
  }

  private static void addNumberVia(Collection<String> numbers,
                                   Collection<String> vias,
                                   String numberPart) {
    int numberEnd = numberPart.indexOf(';');
    if (numberEnd < 0) {
      numbers.add(numberPart);
      vias.add(null);
    } else {
      numbers.add(numberPart.substring(0, numberEnd));
      String maybeVia = numberPart.substring(numberEnd + 1);
      String via;
      if (maybeVia.startsWith("via=")) {
        via = maybeVia.substring(4);
      } else {
        via = null;
      }
      vias.add(via);
    }
  }

}