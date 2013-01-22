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
public final class SMSParsedResult extends ParsedResult {

  private final String[] numbers;
  private final String[] vias;
  private final String subject;
  private final String body;

  public SMSParsedResult(String number,
                         String via,
                         String subject,
                         String body) {
    super(ParsedResultType.SMS);
    this.numbers = new String[] {number};
    this.vias = new String[] {via};
    this.subject = subject;
    this.body = body;
  }

  public SMSParsedResult(String[] numbers,
                         String[] vias,
                         String subject,
                         String body) {
    super(ParsedResultType.SMS);
    this.numbers = numbers;
    this.vias = vias;
    this.subject = subject;
    this.body = body;
  }

  public String getSMSURI() {
    StringBuilder result = new StringBuilder();
    result.append("sms:");
    boolean first = true;
    for (int i = 0; i < numbers.length; i++) {
      if (first) {
        first = false;
      } else {
        result.append(',');
      }
      result.append(numbers[i]);
      if (vias != null && vias[i] != null) {
        result.append(";via=");
        result.append(vias[i]);
      }
    }
    boolean hasBody = body != null;
    boolean hasSubject = subject != null;
    if (hasBody || hasSubject) {
      result.append('?');
      if (hasBody) {
        result.append("body=");
        result.append(body);
      }
      if (hasSubject) {
        if (hasBody) {
          result.append('&');
        }
        result.append("subject=");
        result.append(subject);
      }
    }
    return result.toString();
  }

  public String[] getNumbers() {
    return numbers;
  }

  public String[] getVias() {
    return vias;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  @Override
  public String getDisplayResult() {
    StringBuilder result = new StringBuilder(100);
    maybeAppend(numbers, result);
    maybeAppend(subject, result);
    maybeAppend(body, result);
    return result.toString();
  }

}