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

package cy.ac.ucy.pmpeis01.client.android.book;

import android.os.Handler;
import android.os.Message;
import android.util.Log;


import cy.ac.ucy.pmpeis01.client.android.HttpHelper;
import cy.ac.ucy.pmpeis01.client.android.LocaleManager;
import cy.ac.ucy.pmpeis01.client.android.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

final class NetworkWorker implements Runnable {

  private static final String TAG = NetworkWorker.class.getSimpleName();

  private final String isbn;
  private final String query;
  private final Handler handler;

  NetworkWorker(String isbn, String query, Handler handler) {
    this.isbn = isbn;
    this.query = query;
    this.handler = handler;
  }

  @Override
  public void run() {
    try {
      // These return a JSON result which describes if and where the query was found. This API may
      // break or disappear at any time in the future. Since this is an API call rather than a
      // website, we don't use LocaleManager to change the TLD.
      String uri;
      if (LocaleManager.isBookSearchUrl(isbn)) {
        int equals = isbn.indexOf('=');
        String volumeId = isbn.substring(equals + 1);
        uri = "http://www.google.com/books?id=" + volumeId + "&jscmd=SearchWithinVolume2&q=" + query;
      } else {
        uri = "http://www.google.com/books?vid=isbn" + isbn + "&jscmd=SearchWithinVolume2&q=" + query;
      }

      try {
        String content = HttpHelper.downloadViaHttp(uri, HttpHelper.ContentType.JSON);
        JSONObject json = new JSONObject(content);
        Message message = Message.obtain(handler, R.id.search_book_contents_succeeded);
        message.obj = json;
        message.sendToTarget();
      } catch (IOException ioe) {
        Message message = Message.obtain(handler, R.id.search_book_contents_failed);
        message.sendToTarget();
      }
    } catch (JSONException je) {
      Log.w(TAG, "Error accessing book search", je);
      Message message = Message.obtain(handler, R.id.search_book_contents_failed);
      message.sendToTarget();
    }
  }

}
