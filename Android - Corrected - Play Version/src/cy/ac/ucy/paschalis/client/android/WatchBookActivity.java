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


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.ShareActionProvider;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.util.ArrayList;

// import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.Cache.ImageLoader;
import cy.ac.ucy.paschalis.client.android.R;

import com.google.zxing.client.android.PreferencesActivity;


public class WatchBookActivity extends SherlockActivity {

	private static final String TAG = WatchBookActivity.class
			.getSimpleName();

	App app;

	ProgressBar progressBarRequestBook;

	ProgressBar progressBarSendMessage;

	Button buttonSendMessage;

	Button buttonRequestBook;

	TextView bookISBN;

	TextView bookTitle;

	TextView bookAuthors;

	TextView bookPublishedYear;

	TextView bookPageCount;

	TextView bookDateOfInsert;

	ImageView bookCoverImage;

	TextView bookLanguage;

	TextView textViewWatchBookAvailable;

	Bitmap bitmapBookCover;

	/**
	 * Ignore first time, when app choosing default db's value
	 */
	Boolean booleanFirstInit;

	Boolean isAvailable = false;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (App) getApplication();

		setContentView(R.layout.activity_watch_book);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Get arguments, to determine who opened this activity
		final Bundle extras = getIntent().getExtras();

		try {
			isAvailable = extras
					.getBoolean(App.ExtrasForWatchBookActivityFromBookSearch);
		} catch (Exception e) {
			isAvailable = false;
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

		textViewWatchBookAvailable = (TextView) findViewById(R.id.textViewWatchBookAvailable);


		buttonRequestBook = (Button) findViewById(R.id.buttonRequestBook);

		buttonSendMessage = (Button) findViewById(R.id.buttonSendMessage);
		progressBarSendMessage = (ProgressBar) findViewById(R.id.progressBarSendMessage);

		buttonSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				final DataClassRequestABook dataClassRequestABook = new DataClassRequestABook();

				dataClassRequestABook.username = app.user.username;
				dataClassRequestABook.isbn = app.selectedBook.isbn;


				ArrayList<String> userWhoLent = new ArrayList<String>();
				// Find all users who lent this book
				for (Book.DataClassUser u : app.selectedBook.owners) {
					if (u.status == 0) {
						userWhoLent.add(u.username);
					}
				}


				final CharSequence[] owners = userWhoLent
						.toArray(new CharSequence[userWhoLent.size()]);


				AlertDialog.Builder builder = new AlertDialog.Builder(
						WatchBookActivity.this);
				builder.setTitle(R.string.msgPickBookOwner_);
				builder.setItems(owners,
						new DialogInterface.OnClickListener() {

							public void onClick(DialogInterface dialog,
							                    int item) {

								dataClassRequestABook.owner = (String) owners[item];

								// Send message to owner
								Intent intent = new Intent(
										WatchBookActivity.this,
										SendMessageActivity.class);

								intent.putExtra(
										App.ExtrasForSendMessage_DestinationUser,
										dataClassRequestABook.owner);

								startActivity(intent);

							}
						});
				AlertDialog alert = builder.create();
				alert.show();
			}

		});

		if (!isAvailable) {
			textViewWatchBookAvailable.setText(R.string.no);
			App.setStyleErrorDirection(textViewWatchBookAvailable);
			buttonRequestBook.setEnabled(false);
		} else {


			textViewWatchBookAvailable.setText(R.string.yes);
			App.setStyleSuccessDirection(textViewWatchBookAvailable);

			// Requests a book
			buttonRequestBook.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					{

						final DataClassRequestABook dataClassRequestABook = new DataClassRequestABook();

						dataClassRequestABook.username = app.user.username;
						dataClassRequestABook.isbn = app.selectedBook.isbn;

						ArrayList<String> userWhoLent = new ArrayList<String>();
						// Find all users who lent this book
						for (Book.DataClassUser u : app.selectedBook.owners) {
							if (u.status == 0) {
								userWhoLent.add(u.username);
							}
						}


						final CharSequence[] owners = userWhoLent
								.toArray(new CharSequence[userWhoLent
										.size()]);


						AlertDialog.Builder builder = new AlertDialog.Builder(
								WatchBookActivity.this);
						builder.setTitle(R.string.msgPickBookOwner_);
						builder.setItems(owners,
								new DialogInterface.OnClickListener() {

									public void onClick(
											DialogInterface dialog,
											int item) {

										dataClassRequestABook.owner = (String) owners[item];

										// Request the book
										new AsyncTaskRequestABook()
												.execute(dataClassRequestABook);

									}
								});
						AlertDialog alert = builder.create();
						alert.show();
					}


				}
			});// End of Req Button!


		}


		TextView tvnc = (TextView) findViewById(R.id.textViewNoCover);
		ProgressBar pb = (ProgressBar) findViewById(R.id.progressBarLoadCover);

		// show The Image and save it to Library
		ImageLoader.DataClassDisplayBookCover bk = new ImageLoader.DataClassDisplayBookCover();
		bk.iv = bookCoverImage;
		bk.isCover = true;
		bk.tv = tvnc;
		bk.pb = pb;
		bk.book = app.selectedBook;
		App.imageLoader.DisplayCover(bk);


		bitmapBookCover = ((BitmapDrawable) bookCoverImage.getDrawable())
				.getBitmap();

		// new DownloadImageTask(bookCoverImage, bookInfo)
		// .execute(bookInfo.imgURL);


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


		progressBarRequestBook = (ProgressBar) findViewById(R.id.progressBarRequestBook);

		buttonRequestBook = (Button) findViewById(R.id.buttonRequestBook);

	}


	/**
	 * Sets the status value, as it came from DB
	 */
	private void getStatusDefaultValue() {
		booleanFirstInit = true; // Avoid callback on selected item change

		String myString = getBookStatusString(app.selectedBook.status);


		// updateButtons();

	}


	/**
	 * Fix buttons according to Book Status
	 */
	private void updateButtons() {


	}


	@Override
	protected void onResume() {
		// Set library's logo as ActionBar Icon
		App.imageLoader.DisplayActionBarIcon(app.library.getImageURL(),
				getApplicationContext(), getSupportActionBar());

		if (App.refreshLang) {
			refresh();
		}

		super.onResume();

		getStatusDefaultValue();


	}


	/**
	 * @param status
	 */
	private String getBookStatusString(int status) {
		switch (status) {
			case App.BOOK_STATE_USER_RENTED:
				return getString(R.string.rented);
			case App.BOOK_STATE_USER_AVAILABLE:
				return getString(R.string.available);
			case App.BOOK_STATE_USER_NO_RENTAL:
				return getString(R.string.dontLent);

			case App.BOOK_STATE_USER_OTHER:
				return getString(R.string.otherDontLent);
			// case App.BOOK_STATE_USER_DONT_OWNS:
			// return getString(R.string.dontOwn);
			default:
				return getString(R.string.wrongStatusCode);

		}

	}


	private int getBookStatusCode(String status) {

		if (status.equals(getString(R.string.rented))) {
			return App.BOOK_STATE_USER_RENTED;
		} else if (status.equals(getString(R.string.available))) {
			return App.BOOK_STATE_USER_AVAILABLE;
		} else if (status.equals(getString(R.string.dontLent))) {
			return App.BOOK_STATE_USER_NO_RENTAL;
		} else if (status.equals(getString(R.string.otherDontLent))) {
			return App.BOOK_STATE_USER_OTHER;
		} else if (status.equals(getString(R.string.dontOwn))) {
			return App.BOOK_STATE_USER_DONT_OWNS;
		} else {
			return App.BOOK_STATE_WRONG_STATUS;

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


		try {
			FileOutputStream os = new FileOutputStream(file);
			bitmapBookCover.compress(CompressFormat.PNG, 80, os);
			os.flush();
			os.close();

			Uri uri = Uri.fromFile(file);
			shareIntent.putExtra(Intent.EXTRA_STREAM, uri);

		} catch (Exception e) {
			Log.e(TAG, e.getStackTrace().toString());
		}


		String bookInfo = "\n\n\n\n "
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
				WatchBookActivity.this) + "\n\n"
				+ "http://books.google.com/books?vid=isbn"
				+ app.selectedBook.isbn;

		shareIntent.putExtra(Intent.EXTRA_TEXT, bookInfo);

		return shareIntent;
	}


	/**
	 * Override this, so listview is refreshed to show changes
	 */
	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	;


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				return true;
			case App.MENU_GLOBAL_SETTINGS: {
				Intent myIntent = new Intent(WatchBookActivity.this,
						PreferencesActivity.class);
				WatchBookActivity.this.startActivity(myIntent);

			}
			return true;
			case App.MENU_LIBRARY_SETTINGS: {
				Intent myIntent = new Intent(WatchBookActivity.this,
						LibPreferences.class);
				WatchBookActivity.this.startActivity(myIntent);

			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	/**
	 * @author paschalis
	 */
	public static class DataClassRequestABook {

		String owner;

		String username;

		String isbn;
	}


	/**
	 * Requests a book from other User
	 *
	 * @author paschalis
	 */
	public class AsyncTaskRequestABook extends
			AsyncTask<DataClassRequestABook, Integer, Integer> {

		DataClassRequestABook dataClassRequestABook;


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// progressBarLentButton.setVisibility(View.VISIBLE);
			// buttonLentBook.setVisibility(View.INVISIBLE);
			// buttonLentBook.setEnabled(false);

		}


		@Override
		protected Integer doInBackground(DataClassRequestABook... data) {

			dataClassRequestABook = data[0];

			int returnResult = App.GENERAL_NO_INTERNET;


			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));


			// Save Username , ISBN, and Destination User
			parameters.add(new BasicNameValuePair("owner", data[0].owner));
			parameters.add(new BasicNameValuePair("username",
					data[0].username));
			parameters.add(new BasicNameValuePair("isbn", data[0].isbn));


			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_getRequestABook_URL(), parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null) {
				try {
					// Create JSON Obj based on the result!
					JSONObject userData = new JSONObject(resultStr);

					returnResult = userData.getInt("result");

				} catch (JSONException e) {

					returnResult = App.GENERAL_WEIRD_ERROR;

					Log.e(TAG, "Error parsing data " + e.toString());


				}


			}


			return returnResult;

		}


		protected void onPostExecute(Integer result) {


			if (result == App.GENERAL_SUCCESSFULL) {
				Toast.makeText(
						WatchBookActivity.this,
						getString(R.string.msgRequestSend) + ": "
								+ dataClassRequestABook.owner,
						Toast.LENGTH_SHORT).show();

				buttonRequestBook.setEnabled(false);
			} else if (result == App.MAKE_REQUESTS_USER_DONT_ACCEPTS) {
				Toast.makeText(
						WatchBookActivity.this,
						dataClassRequestABook.owner
								+ ": "
								+ getString(R.string.msgDontAcceptRequests),
						Toast.LENGTH_LONG).show();
			} else if (result == App.MAKE_REQUESTS_USER_ALREADY_REQUESTED) {
				Toast.makeText(
						WatchBookActivity.this,
						getString(R.string.msgAlreadyRequestedTo) + ": "
								+ dataClassRequestABook.owner,
						Toast.LENGTH_LONG).show();
			} else if (result == App.GENERAL_WEIRD_ERROR) {
				Toast.makeText(
						WatchBookActivity.this,
						getString(R.string.msgSomethingWentWrongPlsContact)
								+ ": " + app.library.email,
						Toast.LENGTH_LONG).show();
				WatchBookActivity.this.finish();
			} else if (result == App.GENERAL_DATABASE_ERROR) {
				Toast.makeText(WatchBookActivity.this,
						R.string.msgDatabaseError, Toast.LENGTH_SHORT)
						.show();
			}


			// progressBarLentButton.setVisibility(View.INVISIBLE);
			// buttonLentBook.setVisibility(View.VISIBLE);


		}


	}


	/**
	 * Refresh activity's language
	 */
	private void refresh() {
		App.refreshLang = false;
		finish();
		Intent myIntent = new Intent(WatchBookActivity.this,
				WatchBookActivity.class);
		startActivity(myIntent);
	}


}
