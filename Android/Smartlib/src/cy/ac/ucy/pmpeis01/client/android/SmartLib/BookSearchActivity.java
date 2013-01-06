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



import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import cy.ac.ucy.pmpeis01.client.android.PreferencesActivity;
import cy.ac.ucy.pmpeis01.client.android.R;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.App.DeviceType;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.Book.DataClassUser;





public class BookSearchActivity extends SherlockActivity {

	private static final String	TAG			= BookSearchActivity.class
												.getSimpleName();



	LinearLayout				linearLayoutSearchLayout;

	EditText					editTextSearchKeyword;

	Spinner					spinnerColumnSelect;

	Button					buttonSearch;

	ProgressBar				progressBarSearchButton;

	ListView					listViewBookResults;

	TextView					textViewSearchResults;

	App						app;

	ArrayList<Book>			arrayListSearchResultBooks;

	DataClassSearch			dataClassSearch;


	Boolean					isItemChecked	= false;


	boolean					openedEditBook	= false;

	boolean					resultsShowing	= false;

	/** Whether user is making search or just watching results */
	boolean					isMakingSearch	= true;

	ArrayAdapter<CharSequence>	adapterSearchColumns;

	class DataClassSearch {

		String	column;

		String	keyword;
	}




	AdapterBookInfo	bookInfoAdapter;





	@Override
	public void onCreate(Bundle savedInstanceState) {



		super.onCreate(savedInstanceState);

		resultsShowing = false;

		app = (App) getApplication();
		setContentView(R.layout.activity_book_search);

		app.selectedBook = null;

		linearLayoutSearchLayout = (LinearLayout) findViewById(R.id.linearLayoutSearchLayout);
		editTextSearchKeyword = (EditText) findViewById(R.id.editTextSearchKeyword);
		spinnerColumnSelect = (Spinner) findViewById(R.id.spinnerSearchSelectColumn);
		buttonSearch = (Button) findViewById(R.id.buttonSearchBookSearch);
		progressBarSearchButton = (ProgressBar) findViewById(R.id.progressBarSearchSearchButton);
		listViewBookResults = (ListView) findViewById(R.id.listViewBookResults);
		textViewSearchResults = (TextView) findViewById(R.id.textViewSearchSearchResults);
		arrayListSearchResultBooks = new ArrayList<Book>();

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);


		// Create adapter for the Spinner
		adapterSearchColumns = ArrayAdapter.createFromResource(this,
				R.array.ArraySearchColumns,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapterSearchColumns
				.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner

		spinnerColumnSelect.setAdapter(adapterSearchColumns);

		isItemChecked = false;

		editTextSearchKeyword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}





			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
			}





			@Override
			public void afterTextChanged(Editable s) {
				if (s.length() == 0){
					buttonSearch.setEnabled(false);
				}
				else{
					buttonSearch.setEnabled(true);
				}

			}
		});

		// When button is pressed
		buttonSearch.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				dataClassSearch = new DataClassSearch();
				dataClassSearch.column = getResources().getStringArray(
						R.array.ArraySearchColumnValues)[spinnerColumnSelect
						.getSelectedItemPosition()];
				dataClassSearch.keyword = editTextSearchKeyword.getText()
						.toString();

				// Re-init results
				arrayListSearchResultBooks = new ArrayList<Book>();

				new AsyncTaskSearchBooks().execute(dataClassSearch);
			}
		});





		listViewBookResults.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int pos,
					long arg3) {
				app.selectedBook = (Book) (listViewBookResults
						.getItemAtPosition(pos));

				listViewBookResults
						.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
				listViewBookResults.setItemChecked(pos, true);

				isItemChecked = true;

				// Recreate the menu
				invalidateOptionsMenu();

			}
		});

		listViewBookResults
				.setOnItemLongClickListener(new OnItemLongClickListener() {

					@Override
					public boolean onItemLongClick(AdapterView<?> arg0,
							View arg1, int pos, long arg3) {

						app.selectedBook = (Book) (listViewBookResults
								.getItemAtPosition(pos));

						// Open Book
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
										BookSearchActivity.this,
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
										BookSearchActivity.this,
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

		isItemChecked = false;

		app.selectedBook = null;

		// Recreate the menu
		invalidateOptionsMenu();
	}





	@Override
	public boolean onKeyUp(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_SEARCH){
			linearLayoutSearchLayout.setVisibility(View.VISIBLE);
			isMakingSearch = true;
			invalidateOptionsMenu();

			editTextSearchKeyword.setFocusable(true);
			editTextSearchKeyword.requestFocus();



			return true;
		}
		else{
			return super.onKeyUp(keyCode, event);
		}
	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		menu.add(Menu.NONE, App.MENU_MY_BOOKS_BOOK_SELECTED, Menu.FIRST,
				R.string.editBook)
				.setVisible(false)
				.setShowAsAction(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(Menu.NONE, App.MENU_SEARCH_SEARCH_BOOKS, Menu.FIRST,
				R.string.search)
				.setIcon(R.drawable.ic_menu_search_holo_light)
				.setVisible(false)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		menu.add(Menu.NONE, App.MENU_LIBRARY_SETTINGS, Menu.NONE,
				app.library.name).setIcon(R.drawable.ic_menu_account_list)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_NEVER);

		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);



		menu.add(Menu.FIRST, App.MENU_CLEAR, Menu.FIRST, R.string.clear)
				.setIcon(R.drawable.ic_menu_close_clear_cancel)
				.setVisible(false)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);










		return true;
	}





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				NavUtils.navigateUpFromSameTask(this);
				return true;
			case App.MENU_LIBRARY_SETTINGS:{
				Intent myIntent = new Intent(BookSearchActivity.this,
						LibPreferences.class);
				BookSearchActivity.this.startActivity(myIntent);

			}
				return true;
			case App.MENU_GLOBAL_SETTINGS:{
				Intent myIntent = new Intent(BookSearchActivity.this,
						PreferencesActivity.class);
				BookSearchActivity.this.startActivity(myIntent);

			}
				return true;

			case App.MENU_SEARCH_SEARCH_BOOKS:{
				linearLayoutSearchLayout.setVisibility(View.VISIBLE);
				isMakingSearch = true;
				isItemChecked = false;
				invalidateOptionsMenu();

				// Show on screen keyboard
				InputMethodManager imm = (InputMethodManager) this
						.getSystemService(Service.INPUT_METHOD_SERVICE);
				editTextSearchKeyword.requestFocus();
				imm.showSoftInput(editTextSearchKeyword, 0);

			}
				return true;

			case App.MENU_CLEAR:{


				// If data was already cleared, hide search panel
				if (editTextSearchKeyword.getText().length() == 0
						&& resultsShowing){
					// Close panel & soft keyboard
					linearLayoutSearchLayout.setVisibility(View.GONE);
					textViewSearchResults.setVisibility(View.VISIBLE);
					isMakingSearch = false;
					invalidateOptionsMenu();
				}
				// else just clear data
				else{
					editTextSearchKeyword.setText("");
				}

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


					Intent intent = new Intent(BookSearchActivity.this,
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



					Intent intent = new Intent(BookSearchActivity.this,
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

		if (isMakingSearch){
			menu.findItem(App.MENU_SEARCH_SEARCH_BOOKS).setVisible(false);
			menu.findItem(App.MENU_CLEAR).setVisible(true);

			// Show on screen keyboard
			InputMethodManager imm = (InputMethodManager) this
					.getSystemService(Service.INPUT_METHOD_SERVICE);
			editTextSearchKeyword.requestFocus();
			imm.showSoftInput(editTextSearchKeyword, 0);
		}
		else{
			menu.findItem(App.MENU_SEARCH_SEARCH_BOOKS).setVisible(true);
			menu.findItem(App.MENU_CLEAR).setVisible(false);
			// Hide on screen keyboard
			InputMethodManager imm = (InputMethodManager) BookSearchActivity.this
					.getSystemService(Service.INPUT_METHOD_SERVICE);
			imm.hideSoftInputFromWindow(
					editTextSearchKeyword.getWindowToken(), 0);
		}

		// If book is selected, show edit/view options
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
	private class AsyncTaskSearchBooks extends
			AsyncTask<DataClassSearch, Integer, JSONArray> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			super.onPreExecute();
			// Disable Search Pane
			linearLayoutSearchLayout.setEnabled(false);
			buttonSearch.setVisibility(View.GONE);
			progressBarSearchButton.setVisibility(View.VISIBLE);

		}





		@Override
		protected JSONArray doInBackground(DataClassSearch... data) {

			JSONArray result = null;
			// If user also selected a column
			try{

				ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
				// Say that we are mobile (Android Device)
				parameters.add(new BasicNameValuePair("device",
						App.DEVICE_ANDROID));

				// Set Username
				parameters.add(new BasicNameValuePair("username", app
						.getUsername()));



				if (data[0].column != ""){
					parameters.add(new BasicNameValuePair("column",
							data[0].column));
				}
				parameters.add(new BasicNameValuePair("keyword",
						data[0].keyword));







				// Execute PHP Script
				String resultStr = App.executePHPScript(
						app.getLibrary_getSearch_URL(), parameters);

				// Parse Result (JSON Obj)
				if (resultStr != null){
					try{
						// Create JSON Obj based on the result!
						result = new JSONArray(resultStr);

					}
					catch (JSONException e){
						Log.e(TAG, "Error parsing data " + e.toString());


					}


				}

			}
			catch (Exception e){
				// TODO: handle exception
			}

			return result;

		}





		@Override
		protected void onPostExecute(JSONArray result) {

			linearLayoutSearchLayout.setEnabled(true);
			buttonSearch.setVisibility(View.VISIBLE);
			progressBarSearchButton.setVisibility(View.GONE);

			int returnFromJson = App.GENERAL_NO_INTERNET;

			try{
				returnFromJson = result.getJSONObject(0).getInt("result");

			}
			catch (Exception e1){
			}

			if (result == null){
				returnFromJson = App.GENERAL_NO_INTERNET;
			}

			switch (returnFromJson) {
				case App.GENERAL_SUCCESSFULL:
					// Hide search pane & show results
					linearLayoutSearchLayout.setVisibility(View.GONE);
					textViewSearchResults.setVisibility(View.VISIBLE);

					// Is viewing results now!
					isMakingSearch = false;
					invalidateOptionsMenu();






					for (int i = 1; i < result.length(); i++){

						try{

							JSONObject row;

							Book book = new Book();

							row = result.getJSONObject(i);

							String isbn = row.getString("isbn");
							String username = row.getString("username");
							int bookStatus = row.getInt("status");

							Book bookExistance = bookExists(isbn);

							// Get the use and its book status info
							DataClassUser dataClassUser = new DataClassUser(
									username, bookStatus);


							// Check if book already exists
							if (bookExistance != null){

								// Add only new owner
								bookExistance.owners.add(dataClassUser);

								// User Owns that book
								if (dataClassUser.username
										.equalsIgnoreCase(app.user.username)){

									bookExistance.status = bookStatus;
								}

							}
							// Add book + his first onwer
							else{

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
								arrayListSearchResultBooks.add(book);

							}



						}
						catch (JSONException e){
						}



					}



					bookInfoAdapter = new AdapterBookInfo(
							BookSearchActivity.this, R.layout.book_item,
							arrayListSearchResultBooks, true);

					resultsShowing = true;

					// Show list
					listViewBookResults.setAdapter(bookInfoAdapter);

					break;
				case App.GENERAL_NO_INTERNET:
					App.isNetworkAvailable(BookSearchActivity.this);
					break;

				case App.BOOKS_OF_USER_NO_BOOKS:

					Toast.makeText(BookSearchActivity.this,
							R.string.msgNoResultsFound,
							Toast.LENGTH_LONG).show();
					break;

				case App.GENERAL_WEIRD_ERROR:
					Toast.makeText(BookSearchActivity.this,
							R.string.msgWeirdError, Toast.LENGTH_LONG)
							.show();
					break;
				case App.GENERAL_DATABASE_ERROR:
					Toast.makeText(BookSearchActivity.this,
							R.string.msgDatabaseError, Toast.LENGTH_LONG)
							.show();
					break;
				default:
					break;
			}










		}




	}





	/**
	 * @param pISBN
	 *             to check if book exists
	 * @return true if book exists, otherwise false
	 */
	Book bookExists(String pISBN) {
		for (Book b : arrayListSearchResultBooks){
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
		Intent myIntent = new Intent(BookSearchActivity.this,
				BookSearchActivity.class);
		startActivity(myIntent);
	}



}
