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

package cy.ac.ucy.paschalis.client.android.SmartLib;



import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockListFragment;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;

import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.SmartLib.App.DeviceType;





/**
 * All the libraries, user can choose from
 * 
 * 
 * @author paschalis
 * 
 */
public class ChooseLibraryFragment extends SherlockListFragment {

	private final static String	TAG					= ChooseLibraryFragment.class
														.getSimpleName();

	AdapterLibraryInfo			adapter;


	OnLibrarySelectedListener	mCallback;

	static Library				chosenLib;

	App						app;

	ArrayList<Library>			libraries;

	boolean					failedToFetchLibraries	= false;

	Menu						menu;





	@Override
	public void onResume() {
		super.onResume();

		try{

			if (libraries.size() < 0){
				libraries.clear();
				new AsyncTaskGetLibraries().execute();
			}



			// if we are on large layout device
			if (app.deviceType.equals(DeviceType.Large)){


				if (chosenLib.positionOnList != -1){

					// Notify the parent activity of selected item
					mCallback.onLibrarySelected(chosenLib);

					// getListView().setSelection(app.librarySelectedOnList);
					//
					// //getListView().setItemChecked(app.librarySelectedOnList,
					// true);
				}

			}

		}
		catch (NullPointerException e){
		}

	}





	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		getSherlockActivity().getSupportActionBar().setHomeButtonEnabled(false);

		getSherlockActivity().getSupportActionBar()
		.setDisplayHomeAsUpEnabled(false);
		
		
		return super.onCreateView(inflater, container, savedInstanceState);
	}




	// The container Activity must implement this interface so the frag can
	// deliver messages
	public interface OnLibrarySelectedListener {

		/**
		 * Called by {@link ChooseLibraryFragment} when a library item is
		 * selected
		 */
		public void onLibrarySelected(Library lib);
	}










	
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setHasOptionsMenu(true);

		app = (App) getSherlockActivity().getApplication();

		


		libraries = new ArrayList<Library>();

		adapter = new AdapterLibraryInfo(getSherlockActivity(),
				R.layout.library_item, libraries);
		setListAdapter(adapter);

		new AsyncTaskGetLibraries().execute();

	}



	@Override
	public void onStart() {
		super.onStart();

		// When in two-pane layout, set the listview to highlight the selected
		// list item
		// (We do this during onStart because at the point the listview is
		// available.)
		if (getFragmentManager().findFragmentById(
				R.id.choose_library_fragment) != null){
			getListView().setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		}
	}





	@Override
	public void onDestroy() {
		super.onDestroy();

		// Avoid duplicates on custom ArrayAdapter
		// if(adapter!=null){
		adapter = null;
		// app.librarySelectedOnList = -1;

		// }
	}





	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);

		// This makes sure that the container activity has implemented
		// the callback interface. If not, it throws an exception.
		try{
			mCallback = (OnLibrarySelectedListener) activity;
		}
		catch (ClassCastException e){
			throw new ClassCastException(activity.toString()
					+ " must implement OnLibrarySelectedListener");
		}
	}








	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);

		this.menu = menu;

	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case App.MENU_START_ACTIVITY_REFRESH:{
				libraries.clear();

				new AsyncTaskGetLibraries().execute();
			}

				return true;

		}
		return super.onOptionsItemSelected(item);
	}






	/**
	 * Get the selected Library
	 */
	@Override
	public void onListItemClick(ListView listView, View v, int position,
			long id) {

		// Save the library
		chosenLib = (Library) listView.getItemAtPosition(position);

		app.librarySelectedOnList = position;

		chosenLib.positionOnList = position;

		// Notify the parent activity of selected item
		mCallback.onLibrarySelected(chosenLib);

		// Set the item as checked to be highlighted when in two-pane layout
		listView.setItemChecked(position, true);

		listView.setSmoothScrollbarEnabled(true);
	}










	/**
	 * Get User's Books
	 * 
	 * @author paschalis
	 * 
	 */
	private class AsyncTaskGetLibraries extends
			AsyncTask<Void, Integer, JSONArray> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			try{
				//FIX
				menu.findItem(App.MENU_START_ACTIVITY_REFRESH).setVisible(false);
				getSherlockActivity()
						.setSupportProgressBarIndeterminateVisibility(true);
			}
			catch (Exception e){
			}

			failedToFetchLibraries = false;
			adapter.clear();

		}





		@Override
		protected JSONArray doInBackground(Void... v) {

			JSONArray result = null;

			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));
			
			// Execute PHP Script
			String resultStr = App.executePHPScript(app.getLibraries(),
					parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null){
				try{
					// Create JSON Obj based on the result!
					result = new JSONArray(resultStr);

				}
				catch (JSONException e){

					Log.e(TAG, "Error parsing data " + e.toString());
					failedToFetchLibraries = true;

				}


			}


			return result;

		}





		@Override
		protected void onPostExecute(JSONArray result) {
			try{
			menu.findItem(App.MENU_START_ACTIVITY_REFRESH).setVisible(true);
			getSherlockActivity()
					.setSupportProgressBarIndeterminateVisibility(false);

			}
			catch(NullPointerException e){ //FIXME
				
			}
			
			if (failedToFetchLibraries){
				Toast.makeText(getSherlockActivity(),
						R.string.msgFailedContactWebpage,
						Toast.LENGTH_LONG).show();
			}

			


			int returnFromJson = App.GENERAL_NO_INTERNET;

			try{
				returnFromJson = result.getJSONObject(0).getInt("result");

			}
			catch (Exception e1){
				returnFromJson = App.GENERAL_NO_INTERNET;
			}


			switch (returnFromJson) {
				case App.GENERAL_SUCCESSFULL:



					// Put libraries to array and display them!
					for (int i = 1; i < result.length(); i++){

						try{

							JSONObject row;

							Library library = new Library();

							row = result.getJSONObject(i);

							String name = row.getString("name");
							String url = row.getString("url");
							String email = row.getString("email");
							String telephone = row
									.getString("telephone");
							String town = row.getString("town");

							String country = row.getString("country");

							library.name = name;
							library.URL = url;
							library.email = email;
							library.telephone = telephone;
							library.town = town;
							library.country = country;
							library.location = library.town + ", "
									+ library.country;


							// Insert library
							libraries.add(library);

							adapter.notifyDataSetChanged();

						}
						catch (Exception e){
							Log.e(TAG, e.getMessage());
						}



					}






					break;
				case App.GENERAL_NO_INTERNET:
					// TODO PUT THIS OPTION IN OTHER MENUS TOO!
					App.isNetworkAvailable(ChooseLibraryFragment.this
							.getSherlockActivity());

					break;

				case App.GENERAL_DATABASE_ERROR:
					Toast.makeText(getSherlockActivity(),
							R.string.msgDatabaseError, Toast.LENGTH_LONG)
							.show();
					break;
				default:
					break;
			}










		}
	}



}
