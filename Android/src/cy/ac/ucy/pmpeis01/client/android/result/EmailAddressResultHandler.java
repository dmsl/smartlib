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

package cy.ac.ucy.pmpeis01.client.android.result;

import android.app.Activity;
import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.pmpeis01.client.result.EmailAddressParsedResult;
import cy.ac.ucy.pmpeis01.client.result.ParsedResult;

/**
 * Handles email addresses.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class EmailAddressResultHandler extends ResultHandler {
  private static final int[] buttons = {
      R.string.button_email,
      R.string.button_add_contact
  };

  public EmailAddressResultHandler(Activity activity, ParsedResult result) {
    super(activity, result);
  }

  @Override
  public int getButtonCount() {
    return buttons.length;
  }

  @Override
  public int getButtonText(int index) {
    return buttons[index];
  }

  @Override
  public void handleButtonPress(int index) {
    EmailAddressParsedResult emailResult = (EmailAddressParsedResult) getResult();
    switch (index) {
      case 0:
        sendEmailFromUri(emailResult.getMailtoURI(),
                         emailResult.getEmailAddress(),
                         emailResult.getSubject(),
                         emailResult.getBody());
        break;
      case 1:
        String[] addresses = new String[1];
        addresses[0] = emailResult.getEmailAddress();
        addEmailOnlyContact(addresses, null);
        break;
    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_email_address;
  }
}
