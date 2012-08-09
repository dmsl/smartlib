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

package cy.ac.ucy.pmpeis01.client.android.result.supplement;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.Handler;
import android.widget.TextView;
import cy.ac.ucy.pmpeis01.client.android.HttpHelper;
import cy.ac.ucy.pmpeis01.client.android.LocaleManager;
import cy.ac.ucy.pmpeis01.client.android.R;
import cy.ac.ucy.pmpeis01.client.android.history.HistoryManager;

/**
 * @author Kamil Kaczmarczyk
 * @author Sean Owen
 */
final class BookResultInfoRetriever extends SupplementalInfoRetriever {

  private final String isbn;
  private final String source;
  private final Context context;
  
  BookResultInfoRetriever(TextView textView,
                          String isbn,
                          Handler handler,
                          HistoryManager historyManager,
                          Context context) {
    super(textView, handler, historyManager);
    this.isbn = isbn;
    this.source = context.getString(R.string.msg_google_books);
    this.context = context;
  }

  @Override
  void retrieveSupplementalInfo() throws IOException, InterruptedException {

    String contents = HttpHelper.downloadViaHttp("https://www.googleapis.com/books/v1/volumes?q=isbn:" + isbn,
                                                 HttpHelper.ContentType.JSON);

    if (contents.length() == 0) {
      return;
    }

    String title;
    String pages;
    Collection<String> authors = null;

    try {

      JSONObject topLevel = (JSONObject) new JSONTokener(contents).nextValue();
      JSONArray items = topLevel.optJSONArray("items");
      if (items == null || items.isNull(0)) {
        return;
      }

      JSONObject volumeInfo = ((JSONObject) items.get(0)).getJSONObject("volumeInfo");
      if (volumeInfo == null) {
        return;
      }

      title = volumeInfo.optString("title");
      pages = volumeInfo.optString("pageCount");

      JSONArray authorsArray = volumeInfo.optJSONArray("authors");
      if (authorsArray != null && !authorsArray.isNull(0)) {
        authors = new ArrayList<String>();
        for (int i = 0; i < authorsArray.length(); i++) {
          authors.add(authorsArray.getString(i));
        }
      }

    } catch (JSONException e) {
      throw new IOException(e.toString());
    }

    Collection<String> newTexts = new ArrayList<String>();

    if (title != null && title.length() > 0) {
      newTexts.add(title);
    }

    if (authors != null && !authors.isEmpty()) {
      boolean first = true;
      StringBuilder authorsText = new StringBuilder();
      for (String author : authors) {
        if (first) {
          first = false;
        } else {
          authorsText.append(", ");
        }
        authorsText.append(author);
      }
      newTexts.add(authorsText.toString());
    }

    if (pages != null && pages.length() > 0) {
      newTexts.add(pages + "pp.");
    }

    
    String baseBookUri = "http://www.google." + LocaleManager.getBookSearchCountryTLD(context)
        + "/search?tbm=bks&source=zxing&q=";

    append(isbn, source, newTexts.toArray(new String[newTexts.size()]), baseBookUri + isbn);
  }

}
