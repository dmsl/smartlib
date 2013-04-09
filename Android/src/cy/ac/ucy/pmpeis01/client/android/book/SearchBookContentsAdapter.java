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

import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import cy.ac.ucy.paschalis.client.android.R;

/**
 * Manufactures list items which represent SBC results.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
final class SearchBookContentsAdapter extends ArrayAdapter<SearchBookContentsResult> {

  SearchBookContentsAdapter(Context context, List<SearchBookContentsResult> items) {
    super(context, R.layout.search_book_contents_list_item, 0, items);
  }

  @Override
  public View getView(int position, View view, ViewGroup viewGroup) {
    SearchBookContentsListItem listItem;

    if (view == null) {
      LayoutInflater factory = LayoutInflater.from(getContext());
      listItem = (SearchBookContentsListItem) factory.inflate(
          R.layout.search_book_contents_list_item, viewGroup, false);
    } else {
      if (view instanceof SearchBookContentsListItem) {
        listItem = (SearchBookContentsListItem) view;
      } else {
        return view;
      }
    }

    SearchBookContentsResult result = getItem(position);
    listItem.set(result);
    return listItem;
  }
}
