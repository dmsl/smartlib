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



import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import cy.ac.ucy.paschalis.client.android.PreferencesActivity;
import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.SmartLib.App.DeviceType;
import cy.ac.ucy.paschalis.client.android.SmartLib.Book.DataClassUser;





public class LatestAdditionsActivity extends SherlockActivity {

	private static final String	TAG				= LatestAdditionsActivity.class
													.getSimpleName();

	App						app;

	ArrayList<Book>			arrayListLatestAdditionsBooks;

	ListView					listViewLatestAdditionsBooks;

	AdapterBookInfo			bookInfoAdapter;

	Boolean					isItemChecked		= false;

	// String isbnParam = null;

	private ProgressBar			progressBarFetchLatestAdditionBooks;

	boolean					openedEditBook		= false;

	boolean					showingLiveData	= false;





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (App) getApplication();
		setContentView(R.layout.activity_latest_additions);

		app.selectedBook = null;

		listViewLatestAdditionsBooks = (ListView) findViewById(R.id.listViewLatestAdditions);
		progressBarFetchLatestAdditionBooks = (ProgressBar) findViewById(R.id.progressBarFetchMyBooks);
		arrayListLatestAdditionsBooks = new ArrayList<Book>();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		listViewLatestAdditionsBooks
				.setOnItemClickListener(new OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> arg0,
							View arg1, int pos, long arg3) {
						app.selectedBook = (Book) (listViewLatestAdditionsBooks
								.getItemAtPosition(pos));

						listViewLatestAdditionsBooks
								.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
						listViewLatestAdditionsBooks.setItemChecked(pos,
								true);

						isItemChecked = true;

						// Recreate the menu
						invalidateOptionsMenu();

					}
				});



		listViewLatestAdditionsBooks
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int pos, long arg3) {

						app.selectedBook = (Book) (listViewLatestAdditionsBooks
								.getItemAtPosition(pos));
						openedEditBook = true;
						{
							// If user owns that book, open edit book
							if (app.selectedBook.status != App.BOOK_STATE_USER_DONT_OWNS){
								// Workaround - Change the status of
								// book, to users
								// specific status
								for (DataClassUser u : app.selectedBook.owners){
									// Find real status for that book
									if (u.username
											.equalsIgnoreCase(app.user.username)){
										app.selectedBook.status = u.status;
										break;
									}
								}


								Intent intent = new Intent(
										LatestAdditionsActivity.this,
										EditBookActivity.class);

								intent.putExtra(
										App.ExtrasForEditBookActivityFromBookSearch,
										true);

								openedEditBook = true;

								startActivity(intent);
							}
							else{
								Boolean isAvail = false;

								// Find all users who lent this book
								for (DataClassUser u : app.selectedBook.owners){
									if (u.status == App.BOOK_STATE_USER_AVAILABLE){
										isAvail = true;
									}
								}



								Intent intent = new Intent(
										LatestAdditionsActivity.this,
										WatchBookActivity.class);

								intent.putExtra(
										App.ExtrasForWatchBookActivityFromBookSearch,
										isAvail);

								startActivity(intent);
							}



						}


						return true;
					}

				});

	}





	@Override
	protected void onResume() {
		// Set library's logo as ActionBar Icon
		App.imageLoader.DisplayActionBarIcon(app.library.getImageURL(),
				getApplicationContext(), getSupportActionBar());
		if (App.refreshLang){
			refresh();
		}
		super.onResume();

		if (openedEditBook){

			openedEditBook = false;// restore this value
		}

		if (!showingLiveData || app.shouldRefresh){
			app.shouldRefresh = false;
			// Get user books
			arrayListLatestAdditionsBooks.clear();
			new AsyncTaskGetLatestAdditionsBooks().execute();
		}



		isItemChecked = false;

		app.selectedBook = null;

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

		menu.add(Menu.NONE, App.MENU_LIBRARY_SETTINGS, Menu.NONE,
				app.library.name).setIcon(R.drawable.ic_menu_account_list)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		return true;
	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;

			case App.MENU_GLOBAL_SETTINGS:{
				Intent myIntent = new Intent(LatestAdditionsActivity.this,
						PreferencesActivity.class);
				LatestAdditionsActivity.this.startActivity(myIntent);

			}
				return true;
			case App.MENU_LIBRARY_SETTINGS:{
				Intent myIntent = new Intent(LatestAdditionsActivity.this,
						LibPreferences.class);
				LatestAdditionsActivity.this.startActivity(myIntent);

			}

				return true;
			case App.MENU_MY_BOOKS_BOOK_SELECTED:{
				// If user owns that book, open edit book
				if (app.selectedBook.status != App.BOOK_STATE_USER_DONT_OWNS){
					// Workaround - Change the status of book, to users
					// specific status
					for (DataClassUser u : app.selectedBook.owners){
						// Find real status for that book
						if (u.username
								.equalsIgnoreCase(app.user.username)){
							app.selectedBook.status = u.status;
							break;
						}
					}


					Intent intent = new Intent(
							LatestAdditionsActivity.this,
							EditBookActivity.class);

					intent.putExtra(
							App.ExtrasForEditBookActivityFromBookSearch,
							true);

					openedEditBook = true;

					startActivity(intent);
				}
				else{
					Boolean isAvail = false;

					// Find all users who lent this book
					for (DataClassUser u : app.selectedBook.owners){
						if (u.status == App.BOOK_STATE_USER_AVAILABLE){
							isAvail = true;
						}
					}



					Intent intent = new Intent(
							LatestAdditionsActivity.this,
							WatchBookActivity.class);

					intent.putExtra(
							App.ExtrasForWatchBookActivityFromBookSearch,
							isAvail);

					startActivity(intent);
				}



			}
				return true;
		}
		return super.onOptionsItemSelected(item);
	}





	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {


		// If library is selected, show register option
		if (isItemChecked){

			String bookTitle;
			try{

				if (app.deviceType.equals(DeviceType.Large)){
					bookTitle = app.selectedBook.title;
				}
				else{
					// If smaller device
					bookTitle = app.selectedBook.title.substring(0, 9)
							+ "..";
				}
			}
			catch (Exception e){
				menu.findItem(App.MENU_MY_BOOKS_BOOK_SELECTED).setVisible(
						false);
				return false;
			}



			if (app.selectedBook.status != App.BOOK_STATE_USER_DONT_OWNS){
				menu.findItem(App.MENU_MY_BOOKS_BOOK_SELECTED)
						.setVisible(true)
						.setTitle(getString(R.string.edit) + ": "
								+ bookTitle);
			}
			else{

				menu.findItem(App.MENU_MY_BOOKS_BOOK_SELECTED)
						.setVisible(true)
						.setTitle(getString(R.string.view) + ": "
								+ bookTitle);

			}


		}
		else{
			menu.findItem(App.MENU_MY_BOOKS_BOOK_SELECTED).setVisible(false);

		}

		return true;
	}



	/**
	 * Get User's Books
	 * 
	 * @author paschalis
	 * 
	 */
	private class AsyncTaskGetLatestAdditionsBooks extends
			AsyncTask<Void, Integer, String> {

		String	cachedLatestAdditionsBooksURI;

		File		cachedLatestAdditionsBooks;

		TextView	textViewCachedCopyMsg;


		File		cachedLatestAdditionsBookstmp;

		String	cachedLatestAdditionsBooksURItmp;





		@Override
		protected void onPreExecute() {
			super.onPreExecute();


			progressBarFetchLatestAdditionBooks.setVisibility(View.VISIBLE);
			// Open previous cache
			cachedLatestAdditionsBooksURI = App.imageLoader
					.getCacheDirectory() + "/la";
			cachedLatestAdditionsBooksURItmp = App.imageLoader
					.getCacheDirectory() + "/la_";
			cachedLatestAdditionsBooks = new File(
					cachedLatestAdditionsBooksURI);

			cachedLatestAdditionsBookstmp = new File(
					cachedLatestAdditionsBooksURItmp);

			// Show cached data until new data is fetched
			if (cachedLatestAdditionsBooks.exists()){
				textViewCachedCopyMsg = (TextView) findViewById(R.id.textViewShowingCachedCopy);
				textViewCachedCopyMsg.setVisibility(View.VISIBLE);

				// Make progress bar smaller
				if (Build.VERSION.SDK_INT >= 11){
					progressBarFetchLatestAdditionBooks.setScaleX(0.5f);
					progressBarFetchLatestAdditionBooks.setScaleY(0.5f);
				}


				String cachedData = "";
				// Read data from file
				try{
					BufferedReader in = new BufferedReader(new FileReader(
							cachedLatestAdditionsBooks));
					String str;
					while ((str = in.readLine()) != null)
						cachedData += str;
					in.close();
				}
				catch (IOException e){
					Log.e(TAG, e.toString());
				}

				showingLiveData = false;

				// Parse and show cached data
				parseAndShowData(cachedData, false);

			}
			else{
				// Wait for new data
				showingLiveData = false;


			}

		}





		@Override
		protected String doInBackground(Void... v) {


			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			// Set Username
			parameters.add(new BasicNameValuePair("username", app
					.getUsername()));

			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_getPopularBooks_URL(), parameters);

			// Save result to cache
			if (resultStr != null){
				try{
					// Create the temporary file
					cachedLatestAdditionsBookstmp.createNewFile();

					FileWriter fw;

					fw = new FileWriter(
							cachedLatestAdditionsBookstmp
									.getAbsoluteFile());
					BufferedWriter bw = new BufferedWriter(fw);

					bw.write(resultStr);
					bw.close();

				}
				catch (IOException e){
					Log.e(TAG, e.toString());
				}

			}

			return resultStr;

		}





		@Override
		protected void onPostExecute(String resultStr) {
			if (resultStr != null){

				// If cached copy was showing, hide the message because
				// content now becomes live
				if (textViewCachedCopyMsg != null){
					textViewCachedCopyMsg.setVisibility(View.GONE);

					// Reset cached books
					arrayListLatestAdditionsBooks.clear();

				}

				showingLiveData = true;

				// Parse and Show Data
				parseAndShowData(resultStr, true);

				// Hide progress bar
				progressBarFetchLatestAdditionBooks
						.setVisibility(View.GONE);
			}
			else{
				Toast.makeText(getApplicationContext(),
						R.string.msgFailedDownloadData,
						Toast.LENGTH_SHORT).show();
				progressBarFetchLatestAdditionBooks
						.setVisibility(View.GONE);

				App.isNetworkAvailable(LatestAdditionsActivity.this);

			}

		}





		private void parseAndShowData(String resultStr, boolean liveData) {
			JSONArray result = null;

			// Parse Result (JSON Obj)
			if (resultStr != null){
				try{
					// Create JSON Obj based on the result!
					result = new JSONArray(resultStr);

				}
				catch (JSONException e){
					Log.e(TAG, "Error parsing data " + e.toString());
					// Delete temporary cache
					cachedLatestAdditionsBookstmp.delete();
				}



				int returnFromJson = App.GENERAL_NO_INTERNET;

				try{
					returnFromJson = result.getJSONObject(0).getInt(
							"result");

				}
				catch (Exception e1){
					returnFromJson = App.BOOKS_OF_USER_NO_BOOKS;
					// Delete temporary cache
					cachedLatestAdditionsBookstmp.delete();
				}

				switch (returnFromJson) {
					case App.GENERAL_SUCCESSFULL:
						// Save all books to array
						for (int i = 1; i < result.length(); i++){

							try{

								JSONObject row;

								Book book = new Book();

								row = result.getJSONObject(i);

								String isbn = row.getString("isbn");
								String username = row
										.getString("username");
								int bookStatus = row.getInt("status");

								Book bookExistance = bookExists(isbn);

								// Get the use and its book status info
								DataClassUser dataClassUser = new DataClassUser(
										username, bookStatus);

								// Check if book already exists
								if (bookExistance != null){

									// Add only new owner
									bookExistance.owners
											.add(dataClassUser);

									// User Owns that book
									if (dataClassUser.username
											.equalsIgnoreCase(app.user.username)){

										bookExistance.status = bookStatus;
									}

								}
								// Add book + his first owner
								else{

									String title = row
											.getString("title");
									String authors = row
											.getString("authors");
									int publishedYear = row
											.getInt("publishedYear");
									int pageCount = row
											.getInt("pageCount");

									String dateOfInsert = row
											.getString("dateOfInsert");
									String imgURL = row
											.getString("imgURL");

									String lang = row
											.getString("lang");

									book.isbn = isbn;
									book.title = title;
									book.authors = authors;
									book.publishedYear = publishedYear;
									book.pageCount = pageCount;
									book.dateOfInsert = dateOfInsert;
									book.imgURL = imgURL;
									book.lang = lang;

									// Add book first owner
									book.owners.add(dataClassUser);

									// Is user's book
									if (dataClassUser.username
											.equalsIgnoreCase(app.user.username)){
										book.status = bookStatus;
									}
									else{
										book.status = App.BOOK_STATE_USER_DONT_OWNS;
									}

									// Insert book to array
									arrayListLatestAdditionsBooks
											.add(book);

								}

							}
							catch (JSONException e){
								Log.e(TAG,
										"JSON ERR: " + e.getMessage());
							}

						}





						bookInfoAdapter = new AdapterBookInfo(
								LatestAdditionsActivity.this,
								R.layout.book_item,
								arrayListLatestAdditionsBooks, true);

						// Show list
						listViewLatestAdditionsBooks
								.setAdapter(bookInfoAdapter);

						// If is live data, save new cache
						if (liveData){
							cachedLatestAdditionsBooks.delete();
							cachedLatestAdditionsBookstmp
									.renameTo(cachedLatestAdditionsBooks
											.getAbsoluteFile());
						}

						break;
					case App.GENERAL_NO_INTERNET:
						if (liveData){
							// Delete newly-cached data
							cachedLatestAdditionsBookstmp.delete();
						}
						else{
							Toast.makeText(LatestAdditionsActivity.this,
									R.string.failedToLoadCachedData,
									Toast.LENGTH_LONG).show();
							// Delete cache data
							cachedLatestAdditionsBookstmp.delete();
							cachedLatestAdditionsBooks.delete();
						}

						break;
					case App.BOOKS_OF_USER_NO_BOOKS:

						if (liveData){
							// Delete newly-cached data
							cachedLatestAdditionsBookstmp.delete();

							Toast.makeText(LatestAdditionsActivity.this,
									R.string.msgNoResultsFound,
									Toast.LENGTH_LONG).show();

							Handler handler = new Handler();
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									LatestAdditionsActivity.this
											.finish();
								}
							}, App.DELAY_TWO_SEC);
						}
						else{
							Toast.makeText(LatestAdditionsActivity.this,
									R.string.failedToLoadCachedData,
									Toast.LENGTH_LONG).show();
							// Delete cache data
							cachedLatestAdditionsBookstmp.delete();
							cachedLatestAdditionsBooks.delete();
						}


						break;

					case App.GENERAL_WEIRD_ERROR:
						if (liveData){
							// Delete newly-cached data
							cachedLatestAdditionsBookstmp.delete();
							Toast.makeText(LatestAdditionsActivity.this,
									R.string.reportThisToDev,
									Toast.LENGTH_LONG).show();
							LatestAdditionsActivity.this.finish();
						}
						else{
							Toast.makeText(LatestAdditionsActivity.this,
									R.string.failedToLoadCachedData,
									Toast.LENGTH_LONG).show();
							// Delete cache data
							cachedLatestAdditionsBookstmp.delete();
							cachedLatestAdditionsBooks.delete();
						}

						break;
					case App.GENERAL_DATABASE_ERROR:
						if (liveData){
							// Delete newly-cached data
							cachedLatestAdditionsBookstmp.delete();
							Toast.makeText(LatestAdditionsActivity.this,
									R.string.reportThisToDev,
									Toast.LENGTH_LONG).show();
							LatestAdditionsActivity.this.finish();

						}
						else{
							Toast.makeText(LatestAdditionsActivity.this,
									R.string.failedToLoadCachedData,
									Toast.LENGTH_LONG).show();
							// Delete cache data
							cachedLatestAdditionsBookstmp.delete();
							cachedLatestAdditionsBooks.delete();
						}

						break;
					default:
						break;
				}
			}


		}
	}





	/**
	 * @param pISBN
	 *             to check if book exists
	 * @return true if book exists, otherwise false
	 */
	Book bookExists(String pISBN) {
		for (Book b : arrayListLatestAdditionsBooks){
			if (b.isbn.equals(pISBN)) return b;
		}
		return null;
	}





	/**
	 * Refresh activity's language
	 * 
	 */
	private void refresh() {
		App.refreshLang = false;
		finish();
		Intent myIntent = new Intent(LatestAdditionsActivity.this,
				LatestAdditionsActivity.class);
		startActivity(myIntent);
	}



}
