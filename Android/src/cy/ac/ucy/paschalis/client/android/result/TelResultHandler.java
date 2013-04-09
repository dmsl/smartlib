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
import cy.ac.ucy.paschalis.client.result.TelParsedResult;

/**
 * Offers relevant actions for telephone numbers.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class TelResultHandler extends ResultHandler {
  private static final int[] buttons = {
      R.string.button_dial,
      R.string.button_add_contact
  };

  public TelResultHandler(Activity activity, ParsedResult result) {
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
    TelParsedResult telResult = (TelParsedResult) getResult();
    switch (index) {
      case 0:
        dialPhoneFromUri(telResult.getTelURI());
        // When dialer comes up, it allows underlying display activity to continue or something,
        // but app can't get camera in this state. Avoid issues by just quitting, only in the
        // case of a phone number
        getActivity().finish();
        break;
      case 1:
        String[] numbers = new String[1];
        numbers[0] = telResult.getNumber();
        addPhoneOnlyContact(numbers, null);
        break;
    }
  }

  // Overriden so we can take advantage of Android's phone number hyphenation routines.
  @Override
  public CharSequence getDisplayContents() {
    String contents = getResult().getDisplayResult();
    contents = contents.replace("\r", "");
    return PhoneNumberUtils.formatNumber(contents);
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_tel;
  }
}
