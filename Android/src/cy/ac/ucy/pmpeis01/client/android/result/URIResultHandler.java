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

import java.util.Locale;

import android.app.Activity;
import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.pmpeis01.client.android.LocaleManager;
import cy.ac.ucy.pmpeis01.client.result.ParsedResult;
import cy.ac.ucy.pmpeis01.client.result.URIParsedResult;

/**
 * Offers appropriate actions for URLS.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class URIResultHandler extends ResultHandler {
  // URIs beginning with entries in this array will not be saved to history or copied to the
  // clipboard for security.
  private static final String[] SECURE_PROTOCOLS = {
    "otpauth:"
  };

  private static final int[] buttons = {
      R.string.button_open_browser,
      R.string.button_share_by_email,
      R.string.button_share_by_sms,
      R.string.button_search_book_contents,
  };

  public URIResultHandler(Activity activity, ParsedResult result) {
    super(activity, result);
  }

  @Override
  public int getButtonCount() {
    if (LocaleManager.isBookSearchUrl(((URIParsedResult) getResult()).getURI())) {
      return buttons.length;
    }
    return buttons.length - 1;
  }

  @Override
  public int getButtonText(int index) {
    return buttons[index];
  }

  @Override
  public void handleButtonPress(int index) {
    URIParsedResult uriResult = (URIParsedResult) getResult();
    String uri = uriResult.getURI();
    switch (index) {
      case 0:
        openURL(uri);
        break;
      case 1:
        shareByEmail(uri);
        break;
      case 2:
        shareBySMS(uri);
        break;
      case 3:
        searchBookContents(uri);
        break;
    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_uri;
  }

  @Override
  public boolean areContentsSecure() {
    URIParsedResult uriResult = (URIParsedResult) getResult();
    String uri = uriResult.getURI().toLowerCase(Locale.ENGLISH);
    for (String secure : SECURE_PROTOCOLS) {
      if (uri.startsWith(secure)) {
        return true;
      }
    }
    return false;
  }
}
