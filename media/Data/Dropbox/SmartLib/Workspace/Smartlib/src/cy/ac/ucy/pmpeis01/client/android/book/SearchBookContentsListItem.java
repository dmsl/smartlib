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

import java.util.Locale;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.util.AttributeSet;
import android.widget.LinearLayout;
import android.widget.TextView;
import cy.ac.ucy.pmpeis01.client.android.R;

/**
 * A list item which displays the page number and snippet of this search result.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class SearchBookContentsListItem extends LinearLayout {
  private TextView pageNumberView;
  private TextView snippetView;

  SearchBookContentsListItem(Context context) {
    super(context);
  }

  public SearchBookContentsListItem(Context context, AttributeSet attrs) {
    super(context, attrs);
  }

  @Override
  protected void onFinishInflate() {
    super.onFinishInflate();
    pageNumberView = (TextView) findViewById(R.id.page_number_view);
    snippetView = (TextView) findViewById(R.id.snippet_view);
  }

  public void set(SearchBookContentsResult result) {
    pageNumberView.setText(result.getPageNumber());
    String snippet = result.getSnippet();
    if (snippet.length() > 0) {
      if (result.getValidSnippet()) {
        String lowerQuery = SearchBookContentsResult.getQuery().toLowerCase(Locale.getDefault());
        String lowerSnippet = snippet.toLowerCase(Locale.getDefault());
        Spannable styledSnippet = new SpannableString(snippet);
        StyleSpan boldSpan = new StyleSpan(Typeface.BOLD);
        int queryLength = lowerQuery.length();
        int offset = 0;
        while (true) {
          int pos = lowerSnippet.indexOf(lowerQuery, offset);
          if (pos < 0) {
            break;
          }
          styledSnippet.setSpan(boldSpan, pos, pos + queryLength, 0);
          offset = pos + queryLength;
        }
        snippetView.setText(styledSnippet);
      } else {
        // This may be an error message, so don't try to bold the query terms within it
        snippetView.setText(snippet);
      }
    } else {
      snippetView.setText("");
    }
  }
}
