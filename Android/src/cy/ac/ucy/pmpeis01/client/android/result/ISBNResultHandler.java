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
import android.view.View;
import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.pmpeis01.Result;
import cy.ac.ucy.pmpeis01.client.result.ISBNParsedResult;
import cy.ac.ucy.pmpeis01.client.result.ParsedResult;

/**
 * Handles books encoded by their ISBN values.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class ISBNResultHandler extends ResultHandler {
  private static final int[] buttons = {
      R.string.button_product_search,
      R.string.button_book_search,
      R.string.button_search_book_contents,
      R.string.button_custom_product_search
  };

  public ISBNResultHandler(Activity activity, ParsedResult result, Result rawResult) {
    super(activity, result, rawResult);
    showGoogleShopperButton(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        ISBNParsedResult isbnResult = (ISBNParsedResult) getResult();
        openGoogleShopper(isbnResult.getISBN());
      }
    });
  }

  @Override
  public int getButtonCount() {
    return hasCustomProductSearch() ? buttons.length : buttons.length - 1;
  }

  @Override
  public int getButtonText(int index) {
    return buttons[index];
  }

  @Override
  public void handleButtonPress(int index) {
    ISBNParsedResult isbnResult = (ISBNParsedResult) getResult();
    switch (index) {
      case 0:
        openProductSearch(isbnResult.getISBN());
        break;
      case 1:
        openBookSearch(isbnResult.getISBN());
        break;
      case 2:
        searchBookContents(isbnResult.getISBN());
        break;
      case 3:
        openURL(fillInCustomSearchURL(isbnResult.getISBN()));
        break;
    }
  }

  @Override
  public int getDisplayTitle() {
    return R.string.result_isbn;
  }
}
