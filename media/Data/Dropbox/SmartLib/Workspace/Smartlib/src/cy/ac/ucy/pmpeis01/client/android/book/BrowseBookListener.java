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

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.AdapterView;
import cy.ac.ucy.pmpeis01.client.android.LocaleManager;

import java.util.List;

final class BrowseBookListener implements AdapterView.OnItemClickListener {

  private final SearchBookContentsActivity activity;
  private final List<SearchBookContentsResult> items;

  BrowseBookListener(SearchBookContentsActivity activity, List<SearchBookContentsResult> items) {
    this.activity = activity;
    this.items = items;
  }

  @Override
  public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
    if (position < 1) {
      // Clicked header, ignore it
      return;
    }
    int itemOffset = position - 1;
    if (itemOffset >= items.size()) {
      return;
    }
    String pageId = items.get(itemOffset).getPageId();
    String query = SearchBookContentsResult.getQuery();
    if (LocaleManager.isBookSearchUrl(activity.getISBN()) && pageId.length() > 0) {
      String uri = activity.getISBN();
      int equals = uri.indexOf('=');
      String volumeId = uri.substring(equals + 1);
      String readBookURI = "http://books.google." +
          LocaleManager.getBookSearchCountryTLD(activity) +
          "/books?id=" + volumeId + "&pg=" + pageId + "&vq=" + query;
      Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(readBookURI));
      intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);                    
      activity.startActivity(intent);
    }
  }
}
