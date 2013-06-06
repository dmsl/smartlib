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

package mp.paschalis;


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ExpandableListView.OnGroupClickListener;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// import mp.paschalis.R;
import com.google.zxing.client.android.PreferencesActivity;


public class ActivitiesActivity extends SherlockActivity {

	private static final String TAG = ActivitiesActivity.class
			.getSimpleName();

	TextView textViewActivitiesTitle;

	ExpandableListView expandableListViewActivities;

	ListView listViewOutgoingRequests;

	ListView listViewBooksIGave;

	ListView listViewBooksITook;


	ExpandableAdapterActivityInfo expandableAdapterActivityInfo;

	ExpandableAdapterActivityInfo expandableAdapterActivityInfoAdapter;

	App app;


	Boolean isItemChecked = false;

	ArrayList<ArrayList<MainActivity.DataClassActivities>> arrayListAllData;

	// Activity Arrays
	ArrayList<MainActivity.DataClassActivities> arrayListIncomingRequests;

	ArrayList<MainActivity.DataClassActivities> arrayListOutgoingRequests;

	ArrayList<MainActivity.DataClassActivities> arrayListBooksIGave;

	ArrayList<MainActivity.DataClassActivities> arrayListBooksITook;

	ArrayList<String> arrayListgroupCategories;

	MainActivity.DataClassActivities selectedActivity;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_activities);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		isItemChecked = false;


		app = (App) getApplication();

		textViewActivitiesTitle = (TextView) findViewById(R.id.textViewActivitiesUserTitle);

		expandableListViewActivities = (ExpandableListView) findViewById(R.id.expandableListViewIncomingRequests);


		expandableListViewActivities
				.setOnGroupClickListener(new OnGroupClickListener() {

					@Override
					public boolean onGroupClick(ExpandableListView parent,
					                            View v, int groupPosition, long id) {
						isItemChecked = false;
						// REcreate the menu
						invalidateOptionsMenu();

						return false;
					}
				});

		expandableListViewActivities
				.setOnChildClickListener(new OnChildClickListener() {

					@Override
					public boolean onChildClick(ExpandableListView parent,
					                            View v, int groupPosition,
					                            int childPosition, long id) {

						selectedActivity = arrayListAllData.get(
								groupPosition).get(childPosition);

						isItemChecked = true;

						Book book = new Book();
						book.title = selectedActivity.book.title;
						book.isbn = selectedActivity.book.isbn;
						book.authors = selectedActivity.book.authors;
						app.selectedBook = book;

						// REcreate the menu
						invalidateOptionsMenu();

						return false;
					}
				});


		textViewActivitiesTitle.setText(app.user.name + " "
				+ getString(R.string.activities));


	}


	@Override
	protected void onResume() {
		//Set library's logo as ActionBar Icon
		App.imageLoader.DisplayActionBarIcon(app.library.getImageURL(),
				getApplicationContext(), getSupportActionBar());

		if (App.refreshLang) {
			refresh();
		}
		super.onResume();


		isItemChecked = false;

		arrayListgroupCategories = new ArrayList<String>();

		arrayListgroupCategories.add(getString(R.string.incomingRequests));
		arrayListgroupCategories.add(getString(R.string.outgoingRequests));
		arrayListgroupCategories.add(getString(R.string.booksIAmHolding));
		arrayListgroupCategories.add(getString(R.string.booksIAmGiving));

		arrayListIncomingRequests = new ArrayList<MainActivity.DataClassActivities>();
		arrayListOutgoingRequests = new ArrayList<MainActivity.DataClassActivities>();
		arrayListBooksITook = new ArrayList<MainActivity.DataClassActivities>();
		arrayListBooksIGave = new ArrayList<MainActivity.DataClassActivities>();


		arrayListAllData = new ArrayList<ArrayList<MainActivity.DataClassActivities>>();

		arrayListAllData.add(arrayListIncomingRequests);
		arrayListAllData.add(arrayListOutgoingRequests);
		arrayListAllData.add(arrayListBooksITook);
		arrayListAllData.add(arrayListBooksIGave);

		new AsyncTaskGetIncomingRequests().execute();
		new AsyncTaskGetOutgoingRequests().execute();
		new AsyncTaskGetBooksITook().execute();
		new AsyncTaskGetBooksIGave().execute();

		expandableAdapterActivityInfoAdapter = new ExpandableAdapterActivityInfo(
				ActivitiesActivity.this, arrayListgroupCategories,
				arrayListAllData);

		expandableListViewActivities
				.setAdapter(expandableAdapterActivityInfoAdapter);
		// Recreate the menu
		invalidateOptionsMenu();
	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, App.MENU_MY_BOOKS_BOOK_SELECTED, Menu.FIRST,
				R.string.editBook)
				.setVisible(false)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


		menu.add(Menu.NONE, App.MENU_LIBRARY_SETTINGS, Menu.NONE,
				app.library.name)
				.setIcon(R.drawable.ic_menu_account_list)
				.setShowAsActionFlags(
						MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}


	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {


		// If library is selected, show register option
		if (isItemChecked) {
			menu.findItem(App.MENU_MY_BOOKS_BOOK_SELECTED)
					.setVisible(true)
					.setTitle(getString(R.string.reply) + ": "
							+ selectedActivity.book.title);
		} else {
			menu.findItem(App.MENU_MY_BOOKS_BOOK_SELECTED).setVisible(false);

		}


		return true;
	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case App.MENU_GLOBAL_SETTINGS: {
				Intent myIntent = new Intent(ActivitiesActivity.this,
						PreferencesActivity.class);
				ActivitiesActivity.this.startActivity(myIntent);

			}
			return true;
			case App.MENU_LIBRARY_SETTINGS: {
				Intent myIntent = new Intent(ActivitiesActivity.this,
						LibPreferences.class);
				ActivitiesActivity.this.startActivity(myIntent);

			}
			return true;
			case App.MENU_MY_BOOKS_BOOK_SELECTED: {
				Intent myIntent = new Intent(ActivitiesActivity.this,
						RequestActivity.class);


				myIntent.putExtra(
						App.ExtrasForRequestBookActivityFromActivitiesActivity,
						selectedActivity);

				ActivitiesActivity.this.startActivity(myIntent);

			}

			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * Get IncomingRequests
	 *
	 * @author paschalis
	 */
	private class AsyncTaskGetIncomingRequests extends
			AsyncTask<Void, Integer, JSONArray> {

		@Override
		protected JSONArray doInBackground(Void... v) {

			JSONArray result = null;

			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			// Set Username
			parameters.add(new BasicNameValuePair("username", app
					.getUsername()));

			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_getIncomingRequests_URL(), parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null) {
				try {
					// Create JSON Obj based on the result!
					result = new JSONArray(resultStr);

				} catch (JSONException e) {
					Log.e(TAG, "Error parsing data " + e.toString());


				}


			}


			return result;

		}


		@Override
		protected void onPostExecute(JSONArray result) {


			int returnFromJson = App.GENERAL_NO_INTERNET;

			try {
				returnFromJson = result.getJSONObject(0).getInt("result");

			} catch (Exception e1) {

				returnFromJson = App.BOOKS_OF_USER_NO_BOOKS;
			}

			switch (returnFromJson) {
				case App.GENERAL_SUCCESSFULL:

					// Save all books to array
					for (int i = 1; i < result.length(); i++) {

						try {

							JSONObject row;

							MainActivity.DataClassActivities data = new MainActivity.DataClassActivities();

							row = result.getJSONObject(i);
							// Who made this request
							String username = row.getString("username");
							String isbn = row.getString("isbn");
							String title = row.getString("title");
							String authors = row.getString("authors");

							String date = row.getString("date");

							int acknowledge = row.getInt("acknowledge");

							//Dont show already answered negative requests
							if (acknowledge == App.REQUESTS_ANSWER_NEGATIVE)
								continue;

							String imgURL = row.getString("imgURL");

							data.type = MainActivity.DataClassActivities.ActivityType.IncomingRequest;

							data.username = username;
							data.book.isbn = isbn;
							data.book.title = title;
							data.book.authors = authors;
							data.book.dateOfInsert = date;
							data.acknowledge = acknowledge;
							data.book.imgURL = imgURL;


							// Insert book to array
							arrayListIncomingRequests.add(data);


						} catch (JSONException e) {
						}


					}


					break;

				case App.GENERAL_WEIRD_ERROR:
					Toast.makeText(ActivitiesActivity.this,
							R.string.msgWeirdError, Toast.LENGTH_LONG)
							.show();
					break;
				case App.GENERAL_DATABASE_ERROR:
					Toast.makeText(ActivitiesActivity.this,
							R.string.msgDatabaseError, Toast.LENGTH_LONG)
							.show();
					break;
				default:
					break;
			}

			expandableAdapterActivityInfoAdapter.notifyDataSetChanged();


		}
	}


	/**
	 * Get IncomingRequests
	 *
	 * @author paschalis
	 */
	private class AsyncTaskGetOutgoingRequests extends
			AsyncTask<Void, Integer, JSONArray> {


		@Override
		protected JSONArray doInBackground(Void... v) {

			JSONArray result = null;

			// int returnResult = App.GENERAL_NO_INTERNET;


			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			// Set Username
			parameters.add(new BasicNameValuePair("username", app
					.getUsername()));

			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_getOutgoingRequests_URL(), parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null) {
				try {
					// Create JSON Obj based on the result!
					result = new JSONArray(resultStr);

				} catch (JSONException e) {
					Log.e(TAG, "Error parsing data " + e.toString());


				}


			}


			return result;

		}


		@Override
		protected void onPostExecute(JSONArray result) {


			int returnFromJson = App.GENERAL_NO_INTERNET;

			try {
				returnFromJson = result.getJSONObject(0).getInt("result");

			} catch (Exception e1) {

				returnFromJson = App.BOOKS_OF_USER_NO_BOOKS;
			}

			switch (returnFromJson) {
				case App.GENERAL_SUCCESSFULL:

					// Save all books to array
					for (int i = 1; i < result.length(); i++) {

						try {

							JSONObject row;

							MainActivity.DataClassActivities data = new MainActivity.DataClassActivities();

							row = result.getJSONObject(i);
							// Who made this request
							String username = row.getString("username");
							String isbn = row.getString("isbn");
							String title = row.getString("title");
							String authors = row.getString("authors");

							String date = row.getString("date");

							int acknowledge = row.getInt("acknowledge");

							String imgURL = row.getString("imgURL");

							data.type = MainActivity.DataClassActivities.ActivityType.OutgoingRequest;

							data.username = username;
							data.book.isbn = isbn;
							data.book.title = title;
							data.book.authors = authors;
							data.book.dateOfInsert = date;
							data.acknowledge = acknowledge;
							data.book.imgURL = imgURL;


							// Insert book to array
							arrayListOutgoingRequests.add(data);


						} catch (JSONException e) {
						}


					}


					break;
				case App.GENERAL_WEIRD_ERROR:
					Toast.makeText(ActivitiesActivity.this,
							R.string.msgWeirdError, Toast.LENGTH_LONG)
							.show();
					break;
				case App.GENERAL_DATABASE_ERROR:
					Toast.makeText(ActivitiesActivity.this,
							R.string.msgDatabaseError, Toast.LENGTH_LONG)
							.show();
					break;
				default:
					break;
			}

			expandableAdapterActivityInfoAdapter.notifyDataSetChanged();


		}
	}


	/**
	 * Get IncomingRequests
	 *
	 * @author paschalis
	 */
	private class AsyncTaskGetBooksITook extends
			AsyncTask<Void, Integer, JSONArray> {

		@Override
		protected JSONArray doInBackground(Void... v) {

			JSONArray result = null;


			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			// Set Username
			parameters.add(new BasicNameValuePair("username", app
					.getUsername()));

			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_getBooksITook(), parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null) {
				try {
					// Create JSON Obj based on the result!
					result = new JSONArray(resultStr);

				} catch (JSONException e) {
					Log.e(TAG, "Error parsing data " + e.toString());


				}


			}


			return result;

		}


		@Override
		protected void onPostExecute(JSONArray result) {


			int returnFromJson = App.GENERAL_NO_INTERNET;

			try {
				returnFromJson = result.getJSONObject(0).getInt("result");

			} catch (Exception e1) {

				returnFromJson = App.BOOKS_OF_USER_NO_BOOKS;
			}


			switch (returnFromJson) {
				case App.GENERAL_SUCCESSFULL:

					// Save all books to array
					for (int i = 1; i < result.length(); i++) {

						try {

							JSONObject row;

							MainActivity.DataClassActivities data = new MainActivity.DataClassActivities();

							row = result.getJSONObject(i);
							// Who made this request
							String username = row.getString("username");
							String isbn = row.getString("isbn");
							String title = row.getString("title");
							String authors = row.getString("authors");

							String date = row.getString("date");


							String imgURL = row.getString("imgURL");

							data.type = MainActivity.DataClassActivities.ActivityType.BooksITook;

							data.username = username;
							data.book.isbn = isbn;
							data.book.title = title;
							data.book.authors = authors;
							data.book.dateOfInsert = date;
							data.book.imgURL = imgURL;

							// Insert book to array
							arrayListBooksITook.add(data);


						} catch (JSONException e) {
						}


					}


					break;
				case App.GENERAL_WEIRD_ERROR:
					Toast.makeText(ActivitiesActivity.this,
							R.string.msgWeirdError, Toast.LENGTH_LONG)
							.show();
					break;
				case App.GENERAL_DATABASE_ERROR:
					Toast.makeText(ActivitiesActivity.this,
							R.string.msgDatabaseError, Toast.LENGTH_LONG)
							.show();
					break;
				default:
					break;
			}

			expandableAdapterActivityInfoAdapter.notifyDataSetChanged();


		}
	}


	/**
	 * Get IncomingRequests
	 *
	 * @author paschalis
	 */
	private class AsyncTaskGetBooksIGave extends
			AsyncTask<Void, Integer, JSONArray> {


		@Override
		protected JSONArray doInBackground(Void... v) {

			JSONArray result = null;


			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			// Set Username
			parameters.add(new BasicNameValuePair("username", app
					.getUsername()));

			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_getBooksIGave_URL(), parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null) {
				try {
					// Create JSON Obj based on the result!
					result = new JSONArray(resultStr);

				} catch (JSONException e) {
					Log.e(TAG, "Error parsing data " + e.toString());


				}


			}


			return result;

		}


		@Override
		protected void onPostExecute(JSONArray result) {


			int returnFromJson = App.GENERAL_NO_INTERNET;

			try {
				returnFromJson = result.getJSONObject(0).getInt("result");

			} catch (Exception e1) {

				returnFromJson = App.BOOKS_OF_USER_NO_BOOKS;
			}


			switch (returnFromJson) {
				case App.GENERAL_SUCCESSFULL:

					// Save all books to array
					for (int i = 1; i < result.length(); i++) {

						try {

							JSONObject row;

							MainActivity.DataClassActivities data = new MainActivity.DataClassActivities();

							row = result.getJSONObject(i);
							// Who made this request
							String username = row.getString("username");
							String isbn = row.getString("isbn");
							String title = row.getString("title");
							String authors = row.getString("authors");

							String date = row.getString("date");


							String imgURL = row.getString("imgURL");

							data.type = MainActivity.DataClassActivities.ActivityType.BooksIGave;

							data.username = username;
							data.book.isbn = isbn;
							data.book.title = title;
							data.book.authors = authors;
							data.book.dateOfInsert = date;
							data.book.imgURL = imgURL;
							// Insert book to array
							arrayListBooksIGave.add(data);


						} catch (JSONException e) {
						}


					}
					break;

//				case App.GENERAL_NO_INTERNET:
//					break;
//
//				case App.BOOKS_OF_USER_NO_BOOKS:
//					break;

				case App.GENERAL_WEIRD_ERROR:
					Toast.makeText(ActivitiesActivity.this,
							R.string.msgWeirdError, Toast.LENGTH_LONG)
							.show();
					break;
				case App.GENERAL_DATABASE_ERROR:
					Toast.makeText(ActivitiesActivity.this,
							R.string.msgDatabaseError, Toast.LENGTH_LONG)
							.show();
					break;
				default:
					break;
			}

			expandableAdapterActivityInfoAdapter.notifyDataSetChanged();


		}
	}


	/**
	 * Refresh activity's language
	 */
	private void refresh() {
		App.refreshLang = false;
		finish();
		Intent myIntent = new Intent(ActivitiesActivity.this, ActivitiesActivity.class);
		startActivity(myIntent);
	}


}
