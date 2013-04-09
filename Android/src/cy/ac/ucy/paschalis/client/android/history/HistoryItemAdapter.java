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

package cy.ac.ucy.paschalis.client.android.history;



import java.util.ArrayList;

import android.app.Activity;
import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;
import cy.ac.ucy.paschalis.Result;
import cy.ac.ucy.paschalis.client.android.R;





final class HistoryItemAdapter extends ArrayAdapter<HistoryItem> {

	private final Activity	activity;





	HistoryItemAdapter(Activity activity) {
		super(activity, R.layout.history_list_item,
				new ArrayList<HistoryItem>());
		this.activity = activity;
	}





	@Override
	public View getView(int position, View view, ViewGroup viewGroup) {
		LinearLayout layout;
		if (view instanceof LinearLayout){
			layout = (LinearLayout) view;
		}
		else{
			LayoutInflater factory = LayoutInflater.from(activity);
			layout = (LinearLayout) factory.inflate(
					R.layout.history_list_item, viewGroup, false);
		}

		HistoryItem item = getItem(position);
		Result result = item.getResult();

		String title;
		String errorMsg;

		if (result != null){
			title = result.getText();
			
			errorMsg=item.getErrorMsg();
		}
		else{
			Resources resources = getContext().getResources();
			title = resources.getString(R.string.history_empty);
			errorMsg = resources.getString(R.string.history_empty_detail);
		}

		((TextView) layout.findViewById(R.id.history_title)).setText(title);
		((TextView) layout.findViewById(R.id.history_detail)).setText(errorMsg);

		return layout;
	}

}
