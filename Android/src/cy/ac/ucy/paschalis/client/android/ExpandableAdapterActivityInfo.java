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

package cy.ac.ucy.paschalis.client.android;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;

// import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.Cache.ImageLoader;
import cy.ac.ucy.paschalis.client.android.R;


/**
 * @author paschalis
 *         <p/>
 *         customized Expandable adapter
 */

public class ExpandableAdapterActivityInfo extends BaseExpandableListAdapter {


	int resource;

	String response;

	Context context;

	App app;

	ArrayList<String> groupElements;

	ArrayList<ArrayList<MainActivity.DataClassActivities>> allData;


	public ExpandableAdapterActivityInfo(Context cm, ArrayList<String> gp,
	                                     ArrayList<ArrayList<MainActivity.DataClassActivities>> ce) {
		context = cm;
		groupElements = gp;
		allData = ce;

	}


	@Override
	public Object getChild(int groupPosition, int childPosition) {
		return null;
	}


	@Override
	public long getChildId(int groupPosition, int childPosition) {
		return 0;
	}


	@Override
	public View getChildView(int groupPosition, int childPosition,
	                         boolean isLastChild, View convertView, ViewGroup parent) {


		// Get the current Object
		MainActivity.DataClassActivities activityData = (MainActivity.DataClassActivities)
				allData.get(groupPosition).get(childPosition);


		// Inflate the view
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.activity_item, null);
		}


		// Get layout's Data
		TextView username = (TextView) convertView
				.findViewById(R.id.textViewActivityItemUsername);

		TextView date = (TextView) convertView
				.findViewById(R.id.textViewActivityItemDate);

		TextView acknowledgeTitle = (TextView) convertView
				.findViewById(R.id.textViewActivityItemAnswerTitle);
		TextView acknowledge = (TextView) convertView
				.findViewById(R.id.textViewActivityItemAnswer);
		TextView isbn = (TextView) convertView
				.findViewById(R.id.textViewActivityItemISBN);
		TextView title = (TextView) convertView
				.findViewById(R.id.textViewActivityItemTitle);
		TextView authors = (TextView) convertView
				.findViewById(R.id.textViewActivityItemAuthors);

		ImageView cover = (ImageView) convertView
				.findViewById(R.id.imageViewActivityBookCover);

		username.setText(activityData.username);

		date.setText(App.makeTimeStampHumanReadble(context, activityData.book.dateOfInsert));

		//Set Ack Status strings
		setStatusString(activityData.acknowledge, acknowledge, acknowledgeTitle);


		isbn.setText(activityData.book.isbn);
		title.setText(activityData.book.title);
		authors.setText(activityData.book.authors);

		TextView tvnocover = (TextView) convertView.
				findViewById(R.id.textViewNoCover);
		ProgressBar pb = (ProgressBar) convertView.findViewById(R.id.progressBarLoadCover);

		// show The Image and save it to
		ImageLoader.DataClassDisplayBookCover bk = new ImageLoader.DataClassDisplayBookCover();
		bk.iv = cover;
		bk.isCover = true;
		bk.pb = pb;
		bk.tv = tvnocover;
		App.imageLoader.DisplayImage(activityData.book.imgURL, bk);


		// return activitiesInfoView;

		return convertView;


	}


	@Override
	public View getGroupView(int groupPosition, boolean isExpanded,
	                         View convertView, ViewGroup parent) {


		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) context
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.activity_group_item,
					null);
		}

		TextView textViewGroupName = (TextView) convertView
				.findViewById(R.id.textViewActivityGroupItemTitle);
		TextView textViewGroupNumber = (TextView) convertView
				.findViewById(R.id.textViewActivityGroupItemNumber);
		textViewGroupName.setText(groupElements.get(groupPosition));


		textViewGroupNumber.setText("(" + allData.get(groupPosition).size() + ")");


		return convertView;
	}


	@Override
	public boolean hasStableIds() {
		return false;
	}


	@Override
	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}


	@Override
	public int getChildrenCount(int groupPosition) {
		return allData.get(groupPosition).size();
	}


	@Override
	public ArrayList<MainActivity.DataClassActivities> getGroup(int groupPosition) {
		return allData.get(groupPosition);
	}


	public static void setStatusString(int ack, TextView acknowledge, TextView acknowledgeTitle) {

		//Check if there is acknoledge(only for in/out requests)
		if (ack == MainActivity.DataClassActivities.NO_ACKS) {
			acknowledge.setVisibility(View.GONE);
			acknowledgeTitle.setVisibility(View.GONE);
		}
		//Show custom status code
		else {
			acknowledge.setVisibility(View.VISIBLE);
			acknowledgeTitle.setVisibility(View.VISIBLE);

			switch (ack) {
				case App.REQUESTS_ANSWER_POSITIVE:
					acknowledge.setText(R.string.yes);
					App.setStyleSuccessDirectionColor(acknowledge);
					break;
				case App.REQUESTS_ANSWER_NEGATIVE:
					acknowledge.setText(R.string.no);
					App.setStyleErrorDirectionColor(acknowledge);
					break;
				case App.REQUESTS_ANSWER_DINT_ANSWER_YET:
					acknowledge.setText(R.string.notYet);
					App.setStyleErrorDirectionColor(acknowledge);
					break;

				default:
					break;
			}

		}


	}


	@Override
	public int getGroupCount() {
		return groupElements.size();

	}


	@Override
	public long getGroupId(int groupPosition) {
		return 0;
	}
}



