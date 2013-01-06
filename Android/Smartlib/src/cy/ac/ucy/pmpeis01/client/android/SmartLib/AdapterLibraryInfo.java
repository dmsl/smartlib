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

package cy.ac.ucy.pmpeis01.client.android.SmartLib;



import java.util.List;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import cy.ac.ucy.pmpeis01.client.android.R;
import cy.ac.ucy.pmpeis01.client.android.Cache.ImageLoader;





/**
 * @author paschalis
 * 
 *         Our customized adapter, to show the data to the opponent's row, as WE
 *         like!
 * 
 *         Binds usergame & timestamp to the appropriate TextView in the
 *         opponent's row
 * 
 */
public class AdapterLibraryInfo extends ArrayAdapter<Library> {

	int		resource;

	String	response;

	Context	context;
	
	App app;





	// Initialize adapter
	public AdapterLibraryInfo(Context context, int resource,
			List<Library> items) {
		super(context, resource, items);
		this.resource = resource;

	}





	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		LinearLayout libraryInfoView;

		// Get the current alert object
		Library libraryInfo = getItem(position);


		// Inflate the view
		if (convertView == null){
			libraryInfoView = new LinearLayout(getContext());
			String inflater = Context.LAYOUT_INFLATER_SERVICE;
			LayoutInflater vi;
			vi = (LayoutInflater) getContext().getSystemService(inflater);
			vi.inflate(resource, libraryInfoView, true);
		}
		else{
			libraryInfoView = (LinearLayout) convertView;
		}

		// Get layout's Data
		TextView libraryName = (TextView) libraryInfoView
				.findViewById(R.id.TextViewLibraryName);
		
		ImageView libraryLogo = (ImageView) libraryInfoView
				.findViewById(R.id.imageViewLibraryLogo);
		TextView libraryLocation = (TextView) libraryInfoView
				.findViewById(R.id.textViewLibraryLocation);

		// show The Image and save it to Library
		
		try{
			App.imageLoader.DisplayImage(libraryInfo.getImageURL(), libraryLogo, null);			
		}
		catch (NullPointerException e){
			// noth
		}
		
		//new DownloadImageTask(libraryLogo,libraryInfo).execute();



		// Assign the appropriate data from our alert object above
		libraryName.setText(libraryInfo.name);
		// libraryLogo
		libraryLocation.setText(libraryInfo.location);


		return libraryInfoView;
	}



}
