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
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import cy.ac.ucy.pmpeis01.client.android.PreferencesActivity;
import cy.ac.ucy.pmpeis01.client.android.R;





public class MyBooksActivity extends SherlockActivity {

	private static final String	TAG			= MyBooksActivity.class
												.getSimpleName();

	App						app;

	ArrayList<Book>			arrayListUserBooks;

	ListView					listViewMyBooks;

	TextView					textViewMyBooksTitle;

	AdapterBookInfo			bookInfoAdapter;

	LinearLayout				linearLayoutSelectedBooks;

//	TextView					textViewMyBookSelected;

	Boolean					isItemChecked	= false;

	String					isbnParam		= null;

	private ProgressBar			progressBarFetchMyBooks;





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (App) getApplication();
		setContentView(R.layout.activity_my_books);

		app.selectedBook = null;

		// Get arguments, to find out if isbns where send as parameters
		final Bundle extras = getIntent().getExtras();

		try{
			isbnParam = extras.getString(App.ExtrasForMyBooksActivityISBN);
		}
		catch (Exception e){
			isbnParam = null;
		}

		listViewMyBooks = (ListView) findViewById(R.id.listViewBooksOfUser);

		textViewMyBooksTitle = (TextView) findViewById(R.id.textViewMyBooksTitle);

		linearLayoutSelectedBooks = (LinearLayout) findViewById(R.id.linearLayoutSelectedBooks);
		progressBarFetchMyBooks = (ProgressBar) findViewById(R.id.progressBarFetchMyBooks);


		if (App.lang.equals("en")){
			textViewMyBooksTitle.setText(app.user.name + " "
					+ getString(R.string.books));
		}
		else if (App.lang.equals("el")){
			textViewMyBooksTitle.setText(getString(R.string.books) + " "
					+ app.user.name);
		}

		arrayListUserBooks = new ArrayList<Book>();

//		textViewMyBookSelected = (TextView) findViewById(R.id.textViewMyBooksSelectedBook);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Get user books
		new AsyncTaskGetMyBooks().execute();

		listViewMyBooks.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				app.selectedBook = (Book) (listViewMyBooks
						.getItemAtPosition(pos));

				listViewMyBooks
						.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				listViewMyBooks.setItemChecked(pos, true);

				isItemChecked = true;

				// Recreate the menu
				invalidateOptionsMenu();

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

		linearLayoutSelectedBooks.setVisibility(View.GONE);

		isItemChecked = false;

		app.selectedBook = null;

		// Recreate the menu
		invalidateOptionsMenu();
	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		menu.add(Menu.NONE, App.MENU_MY_BOOKS_BOOK_SELECTED, Menu.FIRST,
				R.string.editBook)
				.setVisible(false)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(Menu.NONE, App.MENU_LIBRARY_SETTINGS, Menu.NONE,
				app.library.name).setIcon(R.drawable.ic_menu_account_list)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT);
		return true;
	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (isbnParam != null){
					onBackPressed();
				}
				else{
					NavUtils.navigateUpFromSameTask(this);
				}
				return true;
			case App.MENU_LIBRARY_SETTINGS:{
				Intent myIntent = new Intent(MyBooksActivity.this,
						LibPreferences.class);
				MyBooksActivity.this.startActivity(myIntent);

			}
				return true;
			case App.MENU_GLOBAL_SETTINGS:{
				Intent myIntent = new Intent(MyBooksActivity.this,
						PreferencesActivity.class);
				MyBooksActivity.this.startActivity(myIntent);

			}
				return true;

			case App.MENU_MY_BOOKS_BOOK_SELECTED:{
				Intent intent = new Intent(MyBooksActivity.this,
						EditBookActivity.class);

				startActivity(intent);

			}
				return true;

		}
		return super.onOptionsItemSelected(item);
	}





	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		// If library is selected, show register option
		if (isItemChecked){
			menu.findItem(App.MENU_MY_BOOKS_BOOK_SELECTED)
					.setVisible(true)
					.setTitle(getString(R.string.edit) + ": "
							+ app.selectedBook.title);
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
	private class AsyncTaskGetMyBooks extends AsyncTask<Void, Integer, String> {

		String	cachedMyBooksURI;

		File		cachedMyBooks;

		TextView	textViewCachedCopyMsg;





		@Override
		protected void onPreExecute() {
			super.onPreExecute();


			progressBarFetchMyBooks.setVisibility(View.VISIBLE);
			// Open previous cache
			cachedMyBooksURI = App.imageLoader.getCacheDirectory() + "/mb" + app.user.username.hashCode();
			cachedMyBooks = new File(cachedMyBooksURI);

			// Show cached data until new data is fetched
			if (cachedMyBooks.exists()){
				textViewCachedCopyMsg = (TextView) findViewById(R.id.textViewShowingCachedCopy);
				textViewCachedCopyMsg.setVisibility(View.VISIBLE);

				// Make progress bar smaller
				progressBarFetchMyBooks.setScaleX(0.5f);
				progressBarFetchMyBooks.setScaleY(0.5f);

				String cachedData = "";
				// Read data from file
				try{
					BufferedReader in = new BufferedReader(new FileReader(
							cachedMyBooks));
					String str;
					while ((str = in.readLine()) != null)
						cachedData += str;
					in.close();
				}
				catch (IOException e){
					Log.e(TAG, e.toString());
				}

				// Parse and show cached data
				parseAndShowData(cachedData, false);

			}
			else{
				// Wait for new data
				try{
					// Create the file
					cachedMyBooks.createNewFile();
				}
				catch (IOException e){
					Log.e(TAG, e.toString());
				}

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
					app.getLibrary_getUserBooks_URL(), parameters);

			// Save result to cache
			if (resultStr != null){
				try{


					FileWriter fw;

					fw = new FileWriter(cachedMyBooks.getAbsoluteFile());
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
					arrayListUserBooks.clear();
				}

				// Parse and Show Data
				parseAndShowData(resultStr, true);

				// Hide progress bar
				progressBarFetchMyBooks.setVisibility(View.GONE);
			}
			else{
				Toast.makeText(getApplicationContext(),
						R.string.msgFailedDownloadData,
						Toast.LENGTH_SHORT).show();
				progressBarFetchMyBooks.setVisibility(View.GONE);
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

				}



				int returnFromJson = App.GENERAL_NO_INTERNET;

				try{
					returnFromJson = result.getJSONObject(0).getInt(
							"result");

				}
				catch (Exception e1){
					returnFromJson = App.BOOKS_OF_USER_NO_BOOKS;
				}

				switch (returnFromJson) {
					case App.GENERAL_SUCCESSFULL:
						// Save all books to array

						try{
							app.user.totalBooks = result
									.getJSONObject(0).getInt(
											"booksNum");



							if (App.lang.equals("en")){
								textViewMyBooksTitle
										.setText(app.user.name
												+ " "
												+ getString(R.string.books)
												+ " ("
												+ app.user.totalBooks
												+ ")");
							}
							else{
								textViewMyBooksTitle
										.setText(getString(R.string.books)
												+ " "
												+ app.user.name
												+ " ("
												+ app.user.totalBooks
												+ ")");
							}

						}
						catch (Exception e){

						}

						for (int i = 1; i < result.length(); i++){

							try{

								JSONObject row;

								Book book = new Book();

								row = result.getJSONObject(i);

								String isbn = row.getString("isbn");

								// If isbn passed as parameter, drop all
								// other books
								if (isbnParam != null){
									if (!isbn.equals(isbnParam))
										continue;
								}

								String title = row.getString("title");
								String authors = row
										.getString("authors");
								int publishedYear = row
										.getInt("publishedYear");
								int pageCount = row.getInt("pageCount");

								String dateOfInsert = row
										.getString("dateOfInsert");
								String imgURL = row.getString("imgURL");
								String lang = row.getString("lang");
								int bookStatus = row.getInt("status");

								book.isbn = isbn;
								book.title = title;
								book.authors = authors;
								book.publishedYear = publishedYear;
								book.pageCount = pageCount;
								book.dateOfInsert = dateOfInsert;
								book.imgURL = imgURL;
								book.lang = lang;
								book.status = bookStatus;

								// Insert book to array
								arrayListUserBooks.add(book);
							}
							catch (JSONException e){
							}

						}
						
						bookInfoAdapter = new AdapterBookInfo(MyBooksActivity.this,
								R.layout.book_item,
								arrayListUserBooks, false);

						// Show list
						listViewMyBooks.setAdapter(bookInfoAdapter);

						break;
					case App.GENERAL_NO_INTERNET:
						if (liveData){
							// TODO refresh button, and call again
							// asynctask
							// from
							// refresh button
						}
						else{
							Toast.makeText(MyBooksActivity.this,
									R.string.failedToLoadCachedData,
									Toast.LENGTH_LONG).show();
						}

						break;
					case App.BOOKS_OF_USER_NO_BOOKS:

						if (liveData){
							Toast.makeText(MyBooksActivity.this,
									R.string.msgYouHaveNoBooksSoFar,
									Toast.LENGTH_LONG).show();

							Handler handler = new Handler();
							handler.postDelayed(new Runnable() {

								@Override
								public void run() {
									MyBooksActivity.this.finish();
								}
							}, App.DELAY_TWO_SEC);
						}
						else{
							Toast.makeText(MyBooksActivity.this,
									R.string.failedToLoadCachedData,
									Toast.LENGTH_LONG).show();
						}


						break;

					case App.GENERAL_WEIRD_ERROR:
						if (liveData){
							Toast.makeText(MyBooksActivity.this,
									R.string.reportThisToDev,
									Toast.LENGTH_LONG).show();
							MyBooksActivity.this.finish();
						}
						else{
							Toast.makeText(MyBooksActivity.this,
									R.string.failedToLoadCachedData,
									Toast.LENGTH_LONG).show();
						}

						break;
					case App.GENERAL_DATABASE_ERROR:
						if (liveData){
							Toast.makeText(MyBooksActivity.this,
									R.string.reportThisToDev,
									Toast.LENGTH_LONG).show();
							MyBooksActivity.this.finish();

						}
						else{
							Toast.makeText(MyBooksActivity.this,
									R.string.failedToLoadCachedData,
									Toast.LENGTH_LONG).show();
						}

						break;
					default:
						break;
				}
			}


		}
	}





	/**
	 * Refresh activity's language
	 * 
	 */
	private void refresh() {
		App.refreshLang = false;
		finish();
		Intent myIntent = new Intent(MyBooksActivity.this,
				MyBooksActivity.class);
		startActivity(myIntent);
	}



}
