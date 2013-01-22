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



import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;

import cy.ac.ucy.paschalis.client.android.PreferencesActivity;
import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.Cache.ImageLoader.DataClassDisplayBookCover;





public class EditBookActivity extends SherlockActivity {

	private static final String	TAG			= EditBookActivity.class
												.getSimpleName();

	Spinner					spinnerEditBookStatus;

	App						app;

	ProgressBar				progressBarChangeStatusOfBook;

	ProgressBar				progressBarLentReturnButton;

	ProgressBar				progressBarLentDeleteButton;

	Button					buttonLentReturnBook;

	Button					buttonDeleteBook;


	TextView					bookISBN;

	TextView					bookTitle;

	TextView					bookAuthors;

	TextView					bookPublishedYear;

	TextView					bookPageCount;

	TextView					bookDateOfInsert;

	ImageView					bookCoverImage;

	Bitmap					bitmapBookCover;

	TextView					bookLanguage;

	TextView					textViewCheckYourBooks;

	/** Ignore first time, when app choosing default db's value */
	Boolean					booleanFirstInit;

	Boolean					fromBookSearch	= false;


	ArrayAdapter<CharSequence>	adapter;

	File						sharingFile;





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (App) getApplication();

		setContentView(R.layout.activity_edit_book);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Get arguments, to determine who opened this activity
		final Bundle extras = getIntent().getExtras();

		try{
			fromBookSearch = extras
					.getBoolean(App.ExtrasForEditBookActivityFromBookSearch);
		}
		catch (Exception e){
			fromBookSearch = false;
		}





		// Set the layout's data
		// Get layout's Data
		bookISBN = (TextView) findViewById(R.id.textViewBookISBN);

		bookTitle = (TextView) findViewById(R.id.textViewBookTitle);
		bookAuthors = (TextView) findViewById(R.id.textViewBookAuthors);
		bookPublishedYear = (TextView) findViewById(R.id.textViewBookPublishedYear);
		bookPageCount = (TextView) findViewById(R.id.textViewBookPageCount);
		bookDateOfInsert = (TextView) findViewById(R.id.textViewBookDateOfInsert);

		bookCoverImage = (ImageView) findViewById(R.id.imageViewBookCover);

		bookLanguage = (TextView) findViewById(R.id.textViewBookLanguage);

		textViewCheckYourBooks = (TextView) findViewById(R.id.textViewCheckYourBooks);


		TextView tvnocover = (TextView) findViewById(R.id.textViewNoCover);
		ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarLoadCover);

		// show The Image and save it to Library
		DataClassDisplayBookCover bk = new DataClassDisplayBookCover();

		bk.iv = bookCoverImage;
		bk.isCover=true;
		bk.pb=pb;
		bk.tv=tvnocover;
		bk.book=app.selectedBook;

		App.imageLoader.DisplayCover(bk);

		// Assign the appropriate data from our alert object above
		bookISBN.setText(app.selectedBook.isbn);
		bookTitle.setText(app.selectedBook.title);
		bookAuthors.setText(app.selectedBook.authors);
		bookPublishedYear.setText(Integer.valueOf(
				app.selectedBook.publishedYear).toString());
		bookPageCount.setText(Integer.valueOf(app.selectedBook.pageCount)
				.toString());
		bookDateOfInsert.setText(App.makeTimeStampHumanReadble(
				getApplicationContext(), app.selectedBook.dateOfInsert));
		bookLanguage.setText(app.selectedBook.lang);


		progressBarChangeStatusOfBook = (ProgressBar) findViewById(R.id.progressBarChangeBookStatus);
		progressBarLentReturnButton = (ProgressBar) findViewById(R.id.progressBarReturnLentBook);
		progressBarLentDeleteButton = (ProgressBar) findViewById(R.id.progressBarBookDeleteButton);

		buttonDeleteBook = (Button) findViewById(R.id.buttonBookDelete);
		buttonLentReturnBook = (Button) findViewById(R.id.buttonLentReturnBook);

		spinnerEditBookStatus = (Spinner) findViewById(R.id.spinnerBookStatus);

		// Create adapter for the Spinner
		adapter = ArrayAdapter.createFromResource(this,
				R.array.ArrayBookSetStatus,
				android.R.layout.simple_spinner_item);
		// Specify the layout to use when the list of choices appears
		adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		// Apply the adapter to the spinner

		spinnerEditBookStatus.setAdapter(adapter);

		getStatusDefaultValue();

		spinnerEditBookStatus
				.setOnItemSelectedListener(new OnItemSelectedListener() {

					@Override
					public void onItemSelected(AdapterView<?> parentView,
							View selectedItemView, int position, long id) {

						if (booleanFirstInit){
							booleanFirstInit = false;
							return;
						}

						// your code here
						new AsyncTaskChangeStatusOfBook().execute(App
								.getBookStatusCode((String) (adapter
										.getItem(position)),
										EditBookActivity.this));
					}





					@Override
					public void onNothingSelected(AdapterView<?> parentView) {
					}

				});

		// Hybrid button: Lent/Return a book
		buttonLentReturnBook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				{
					// Lent the book
					if (buttonLentReturnBook.getText().toString()
							.equals(getString(R.string.lent))){
						// Send ISBN As Argument
						Intent i = new Intent(getApplicationContext(),
								LentBookActivity.class);
						i.putExtra(App.ExtrasForLentBookActivityISBN,
								bookISBN.getText().toString());
						EditBookActivity.this.startActivity(i);
					}
					// Return the book
					else{

						{
							AlertDialog.Builder alert = new AlertDialog.Builder(
									EditBookActivity.this);
							alert.setTitle(R.string.msgIsItReturnedToYou_);


							alert.setIcon(R.drawable.ic_menu_forward);

							alert.setNegativeButton(
									R.string.no,
									new android.content.DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
										}
									});



							alert.setPositiveButton(
									R.string.yes,
									new android.content.DialogInterface.OnClickListener() {

										@Override
										public void onClick(
												DialogInterface dialog,
												int which) {
											// Return the book
											new AsyncTaskReturnABook()
													.execute(app.selectedBook.isbn);
										}
									});

							alert.show();

						}

					}
				}


			}
		});// End of Lent/Return Button!


		// Delete Button
		buttonDeleteBook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				{
					AlertDialog.Builder alert = new AlertDialog.Builder(
							EditBookActivity.this);
					alert.setTitle(R.string.msgAreYouSureDelBOok_);


					alert.setIcon(R.drawable.ic_dialog_alert_holo_light);

					alert.setNegativeButton(
							R.string.no,
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
								}
							});



					alert.setPositiveButton(
							R.string.yes,
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									// Return the book

									new AsyncTaskDeleteABook()
											.execute(app.selectedBook.isbn);
								}
							});

					alert.show();

				}

			}
		});






	}





	/**
	 * Sets the status value, as it came from DB
	 * 
	 */
	private void getStatusDefaultValue() {
		booleanFirstInit = true; // Avoid callback on selected item change

		String myString = App.getBookStatusString(app.selectedBook.status,
				EditBookActivity.this);

		int spinnerPosition = adapter.getPosition(myString);

		// set the default according to value
		spinnerEditBookStatus.setSelection(spinnerPosition);

		updateButtons();

	}





	/**
	 * Fix buttons according to Book Status
	 * 
	 */
	private void updateButtons() {
		if (app.selectedBook.status == App.BOOK_STATE_USER_RENTED){
			buttonLentReturnBook.setText(R.string.isItReturned_);
			buttonLentReturnBook.setEnabled(true);
			buttonDeleteBook.setEnabled(false);
			spinnerEditBookStatus.setVisibility(View.INVISIBLE);
			textViewCheckYourBooks.setVisibility(View.INVISIBLE);
		}
		else if (app.selectedBook.status == App.BOOK_STATE_USER_AVAILABLE){
			buttonLentReturnBook.setText(R.string.lent);
			buttonLentReturnBook.setEnabled(true);
			buttonDeleteBook.setEnabled(true);
			spinnerEditBookStatus.setEnabled(true);
			spinnerEditBookStatus.setVisibility(View.VISIBLE);
			textViewCheckYourBooks.setVisibility(View.VISIBLE);
		}
		else if (app.selectedBook.status == App.BOOK_STATE_USER_NO_RENTAL
				|| app.selectedBook.status == App.BOOK_STATE_USER_OTHER){
			buttonLentReturnBook.setText(R.string.lent);
			buttonLentReturnBook.setEnabled(false);
			buttonDeleteBook.setEnabled(true);
			spinnerEditBookStatus.setEnabled(true);
			spinnerEditBookStatus.setVisibility(View.VISIBLE);
			textViewCheckYourBooks.setVisibility(View.VISIBLE);
		}
		else{

			buttonLentReturnBook.setText(R.string.lent);
			buttonLentReturnBook.setEnabled(false);
			buttonDeleteBook.setEnabled(false);
			buttonDeleteBook.setText(R.string.deleted);
			spinnerEditBookStatus.setEnabled(false);
			spinnerEditBookStatus.setVisibility(View.INVISIBLE);
			textViewCheckYourBooks.setVisibility(View.INVISIBLE);


			Handler handler = new Handler();
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {

					if (!fromBookSearch){
						NavUtils.navigateUpFromSameTask(EditBookActivity.this);
					}
				}
			}, App.DELAY_TWO_SEC);

		}



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

		getStatusDefaultValue();

		try{
			sharingFile.delete();
		}
		catch (Exception e){
			// Noth -catches exception if null
		}


	}









	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_share, menu);

		MenuItem actionItem = menu
				.findItem(R.id.menu_item_share_action_provider_action_bar);
		ShareActionProvider actionProvider = (ShareActionProvider) actionItem
				.getActionProvider();
		actionProvider
				.setShareHistoryFileName(ShareActionProvider.DEFAULT_SHARE_HISTORY_FILE_NAME);

		actionProvider.setShareIntent(createShareIntent());

		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		menu.add(Menu.NONE, App.MENU_LIBRARY_SETTINGS, Menu.NONE,
				app.library.name).setIcon(R.drawable.ic_menu_account_list)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT);


		return true;
	}





	/**
	 * Creates a sharing {@link Intent}.
	 * 
	 * @return The sharing intent.
	 */
	private Intent createShareIntent() {
		Intent shareIntent = new Intent(Intent.ACTION_SEND);
		shareIntent.setType("text/plain");

		String root = Environment.getExternalStorageDirectory()
				+ ".SmartLib/Images";
		new File(root).mkdirs();


		File file = new File(root, app.selectedBook.isbn);


		try{
			FileOutputStream os = new FileOutputStream(file);
			bitmapBookCover.compress(CompressFormat.PNG, 80, os);
			os.flush();
			os.close();

			Uri uri = Uri.fromFile(file);
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

		}
		catch (Exception e){
			Log.e(TAG, e.getStackTrace().toString());
		}


		String bookInfo = "\n\n\n\nMy "
				+ getString(R.string.bookInfo)
				+ ":\n"
				+ getString(R.string.title)
				+ ": \t\t\t\t"
				+ app.selectedBook.title
				+ "\n"
				+ getString(R.string.author)
				+ ": \t\t\t"
				+ app.selectedBook.authors
				+ "\n"
				+ getString(R.string.isbn)
				+ ": \t\t\t\t"
				+ app.selectedBook.isbn
				+ "\n"
				+ getString(R.string.published_)
				+ " \t"
				+ app.selectedBook.publishedYear
				+ "\n"
				+ getString(R.string.pages_)
				+ " \t\t\t"
				+ app.selectedBook.pageCount
				+ "\n"
				+ getString(R.string.isbn)
				+ ": \t\t\t\t"
				+ app.selectedBook.isbn
				+ "\n"
				+ getString(R.string.status)
				+ ": \t\t\t"
				+ App.getBookStatusString(app.selectedBook.status,
						EditBookActivity.this) + "\n\n"
				+ "http://books.google.com/books?vid=isbn"
				+ app.selectedBook.isbn;

		shareIntent.putExtra(Intent.EXTRA_TEXT, bookInfo);

		return shareIntent;
	}






	/**
	 * Override this, so listview is refreshed to show changes
	 * 
	 * */
	@Override
	public void onBackPressed() {
		super.onBackPressed();

		if (!fromBookSearch) NavUtils.navigateUpFromSameTask(this);
	};





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (fromBookSearch){
					onBackPressed();
				}
				else{
					NavUtils.navigateUpFromSameTask(this);
				}
				return true;
			case App.MENU_LIBRARY_SETTINGS:{
				Intent myIntent = new Intent(EditBookActivity.this,
						LibPreferences.class);
				EditBookActivity.this.startActivity(myIntent);

			}
				return true;
			case App.MENU_GLOBAL_SETTINGS:{
				Intent myIntent = new Intent(EditBookActivity.this,
						PreferencesActivity.class);
				EditBookActivity.this.startActivity(myIntent);

			}
		}
		return super.onOptionsItemSelected(item);
	}







	/**
	 * Lents a book to user
	 * 
	 * @author paschalis
	 * 
	 */
	private class AsyncTaskChangeStatusOfBook extends
			AsyncTask<Integer, Integer, Integer> {

		int	tryStatus;





		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBarChangeStatusOfBook.setVisibility(View.VISIBLE);
			spinnerEditBookStatus.setVisibility(View.INVISIBLE);
			buttonLentReturnBook.setEnabled(false);
			buttonDeleteBook.setEnabled(false);

		}





		@Override
		protected Integer doInBackground(Integer... status) {

			tryStatus = status[0];

			int returnResult = App.GENERAL_NO_INTERNET;


			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			parameters.add(new BasicNameValuePair("isbn", bookISBN.getText()
					.toString()));

			// Save Username , ISBN, and Destination User
			parameters.add(new BasicNameValuePair("username", app
					.getUsername()));
			parameters.add(new BasicNameValuePair("newstatus", status[0]
					.toString()));



			// Execute PHP Script
			String resultStr = App
					.executePHPScript(
							app.getLibrary_getChangeStatusOfBook_URL(),
							parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null){
				try{
					// Create JSON Obj based on the result!
					JSONObject userData = new JSONObject(resultStr);

					returnResult = userData.getInt("result");

				}
				catch (JSONException e){
					Log.e(TAG, "Error parsing data " + e.toString());


				}


			}


			return returnResult;

		}





		protected void onPostExecute(Integer result) {



			progressBarChangeStatusOfBook.setVisibility(View.INVISIBLE);
			spinnerEditBookStatus.setVisibility(View.VISIBLE);
			// buttonDeleteBook.setEnabled(true);


			if (result == App.GENERAL_SUCCESSFULL){
				// Workaround
				app.selectedBook.status = tryStatus;

			}


			else{
				Toast.makeText(EditBookActivity.this,
						R.string.msgFailedToChangeStatusCode,
						Toast.LENGTH_SHORT).show();

			}
			updateButtons();
		}
	}








	/**
	 * Lents a book to user
	 * 
	 * @author paschalis
	 * 
	 */
	private class AsyncTaskDeleteABook extends
			AsyncTask<String, Integer, Integer> {

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			buttonDeleteBook.setVisibility(View.INVISIBLE);
			progressBarLentDeleteButton.setVisibility(View.VISIBLE);
			spinnerEditBookStatus.setEnabled(false);
			buttonLentReturnBook.setEnabled(false);
		}





		@Override
		protected Integer doInBackground(String... isbns) {


			int returnResult = App.GENERAL_NO_INTERNET;


			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			parameters.add(new BasicNameValuePair("isbn", isbns[0]));

			// Save Username , ISBN, and Destination User
			parameters.add(new BasicNameValuePair("username", app
					.getUsername()));



			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_getDeleteABook_URL(), parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null){
				try{
					// Create JSON Obj based on the result!
					JSONObject userData = new JSONObject(resultStr);

					returnResult = userData.getInt("result");

				}
				catch (JSONException e){
					Log.e(TAG, "Error parsing data " + e.toString());


				}


			}


			return returnResult;

		}





		protected void onPostExecute(Integer result) {

			buttonDeleteBook.setVisibility(View.VISIBLE);
			progressBarLentDeleteButton.setVisibility(View.INVISIBLE);

			if (result == App.GENERAL_SUCCESSFULL){
				// Workaround
				app.selectedBook.status = App.BOOK_STATE_USER_DONT_OWNS;

			}


			else{
				Toast.makeText(EditBookActivity.this,
						R.string.msgFailedToDeleteBook,
						Toast.LENGTH_SHORT).show();

			}
			updateButtons();
		}
	}










	/**
	 * Return a book to user
	 * 
	 * @author paschalis
	 * 
	 */
	public class AsyncTaskReturnABook extends
			AsyncTask<String, Integer, Integer> {



		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			buttonLentReturnBook.setVisibility(View.INVISIBLE);
			progressBarLentReturnButton.setVisibility(View.VISIBLE);
			spinnerEditBookStatus.setEnabled(false);
			textViewCheckYourBooks.setEnabled(false);
			buttonDeleteBook.setEnabled(false);

		}





		@Override
		protected Integer doInBackground(String... isbn) {

			int returnResult = App.GENERAL_NO_INTERNET;


			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			// Save Username , ISBN, and Destination User
			parameters.add(new BasicNameValuePair("owner", app.getUsername()));
			parameters.add(new BasicNameValuePair("isbn", isbn[0]));


			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_returnABook_URL(), parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null){
				try{
					// Create JSON Obj based on the result!
					JSONObject userData = new JSONObject(resultStr);

					returnResult = userData.getInt("result");

				}
				catch (JSONException e){
					Log.e(TAG, "Error parsing data " + e.toString());


				}


			}


			return returnResult;

		}





		protected void onPostExecute(Integer result) {

			buttonLentReturnBook.setVisibility(View.VISIBLE);
			progressBarLentReturnButton.setVisibility(View.INVISIBLE);
			spinnerEditBookStatus.setEnabled(true);
			textViewCheckYourBooks.setEnabled(true);
			// buttonDeleteBook.setEnabled(false);


			if (result == App.GENERAL_SUCCESSFULL){
				// Workaround
				app.selectedBook.status = App.BOOK_STATE_USER_AVAILABLE;

			}

			else{

				Toast.makeText(
						EditBookActivity.this,
						getString(R.string.msgBookFailedToReturn) + ". "
								+ getString(R.string.msgPleaseContact)
								+ ": " + app.getLibraryEmail(),
						Toast.LENGTH_LONG).show();

			}

			updateButtons();
			// Return in previous Activity in 2 secs

		}

	}





	/**
	 * Refresh activity's language
	 * 
	 */
	private void refresh() {
		App.refreshLang = false;
		finish();
		Intent myIntent = new Intent(EditBookActivity.this,
				EditBookActivity.class);
		startActivity(myIntent);
	}





}
