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

/**
 * @author Sean Owen
 */
public final class EmailAddressParsedResult extends ParsedResult {

  private final String emailAddress;
  private final String subject;
  private final String body;
  private final String mailtoURI;

  EmailAddressParsedResult(String emailAddress,
                           String subject,
                           String body,
                           String mailtoURI) {
    super(ParsedResultType.EMAIL_ADDRESS);
    this.emailAddress = emailAddress;
    this.subject = subject;
    this.body = body;
    this.mailtoURI = mailtoURI;
  }

  public String getEmailAddress() {
    return emailAddress;
  }

  public String getSubject() {
    return subject;
  }

  public String getBody() {
    return body;
  }

  public String getMailtoURI() {
    return mailtoURI;
  }

  @Override
  public String getDisplayResult() {
    StringBuilder result = new StringBuilder(30);
    maybeAppend(emailAddress, result);
    maybeAppend(subject, result);
    maybeAppend(body, result);
    return result.toString();
  }

}