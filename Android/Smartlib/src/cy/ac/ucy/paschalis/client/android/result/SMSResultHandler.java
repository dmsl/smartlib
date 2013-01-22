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

package cy.ac.ucy.paschalis.client.android.result;

import android.app.Activity;
import android.telephony.PhoneNumberUtils;
import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.result.ParsedResult;
import cy.ac.ucy.paschalis.client.result.SMSParsedResult;

/**
 * Handles SMS addresses, offering a choice of composing a new SMS or MMS message.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class SMSResultHandler extends ResultHandler {
  private static final int[] buttons = {
      R.string.button_sms,
      R.string.button_mms
  };

  public SMSResultHandler(Activity activity, ParsedResult result) {
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
    SMSParsedResult smsResult = (SMSParsedResult) getResult();
    switch (index) {
      case 0:
        // Don't know of a way yet to express a SENDTO intent with multiple recipients
        sendSMS(smsResult.getNumbers()[0], smsResult.getBody());
        break;
      case 1:
        sendMMS(smsResult.getNumbers()[0], smsResult.getSubject(), smsResult.getBody());
        break;
    }
  }

  @Override
  public CharSequence getDisplayContents() {
    SMSParsedResult smsResult = (SMSParsedResult) getResult();
    StringBuilder contents = new StringBuilder(50);
    String[] rawNumbers = smsResult.getNumbers();
    String[] formattedNumbers = new String[rawNumbers.length];
    for (int i = 0; i < rawNumbers.length; i++) {
      formattedNumbers[i] = PhoneNumberUtils.formatNumber(rawNumbers[i]);
    }
    ParsedResult.maybeAppend(formattedNumbers, contents);
    ParsedResult.maybeAppend(smsResult.getSubject(), contents);
    ParsedResult.maybeAppend(smsResult.getBody(), contents);
    return contents.toString();
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_sms;
  }
}
