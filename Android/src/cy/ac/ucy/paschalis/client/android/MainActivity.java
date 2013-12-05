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
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import java.io.Serializable;

// import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.Cache.ImageLoader;
import cy.ac.ucy.paschalis.client.android.R;

import com.google.zxing.client.android.CaptureActivity;
import com.google.zxing.client.android.PreferencesActivity;
import com.google.zxing.client.android.history.HistoryActivity;

public class MainActivity extends SherlockActivity {

	App app;

	// TextView textViewWelcomeUser;
	// SUMMER CHANGED
	// Button buttonSmartScanner;

	Button buttonInsert;
	Button buttonLentReturn;

	Button buttonBookSearch;

	Button buttonPopularBooks;

	Button buttonMyBooks;

	Button buttonActivities;

	ImageView imageViewLibaryLogo;

	TextView textViewLibraryLocation;

	static String PSW = "ADSF$FG@$%ASF@$#QTAF@@#$TQAFSD";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (App) getApplication();

		setContentView(R.layout.activity_main);

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		showMessageIfHaventSavedCredentials();

		try {
			getSupportActionBar().setTitle(
					getString(R.string.welcome) + " " + app.user.name);

			// textViewWelcomeUser = (TextView)
			// findViewById(R.id.textViewMainWelcomeUser);

			// Safety checks
			if (app.user == null) {
				// End this activity(Logout user)
				this.finish();
			}

			imageViewLibaryLogo = (ImageView) findViewById(R.id.imageViewLibraryLogo);
			textViewLibraryLocation = (TextView) findViewById(R.id.textViewLibraryLocation);

			// Show logo			
			ImageLoader.DataClassDisplayBookCover bk = new ImageLoader.DataClassDisplayBookCover();
			bk.iv = imageViewLibaryLogo;
			App.imageLoader.DisplayImage(app.library.getImageURL(),
					bk);


			textViewLibraryLocation.setText(app.library.location);

			// textViewWelcomeUser.setText();

			// Bind Buttons
			// buttonSmartScanner = (Button)
			// findViewById(R.id.buttonMainSmartScanner);
			buttonInsert = (Button) findViewById(R.id.buttonMainInsertBook);
			buttonLentReturn = (Button) findViewById(R.id.buttonMainBookLentBook);
			buttonBookSearch = (Button) findViewById(R.id.buttonMainBookSearch);
			buttonPopularBooks = (Button) findViewById(R.id.buttonMainPopularBooks);
			buttonMyBooks = (Button) findViewById(R.id.buttonMainMyBooks);
			buttonActivities = (Button) findViewById(R.id.buttonMainActivities);
			// SUMMER
			// buttonSmartScanner.setOnClickListener(new OnClickListener() {
			//
			// @Override
			// public void onClick(View v) {
			// Intent myIntent = new Intent(MainActivity.this,
			// CaptureActivity.class);
			// MainActivity.this.startActivity(myIntent);
			// }
			// });

			buttonInsert.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(MainActivity.this,
							CaptureActivity.class);
					// Open capture in insert mode
					myIntent.putExtra(App.ExtrasForCaptureActivity,
							App.CaptureMode.Insert);
					MainActivity.this.startActivity(myIntent);
				}
			});

			buttonLentReturn.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(MainActivity.this,
							CaptureActivity.class);
					// Open capture in lent/return mode
					myIntent.putExtra(App.ExtrasForCaptureActivity,
							App.CaptureMode.LentReturn);

					MainActivity.this.startActivity(myIntent);
				}
			});

			// Open User Books
			buttonMyBooks.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(MainActivity.this,
							MyBooksActivity.class);
					MainActivity.this.startActivity(myIntent);
				}
			});

			// Open Book Search
			buttonBookSearch.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(MainActivity.this,
							BookSearchActivity.class);
					MainActivity.this.startActivity(myIntent);
				}
			});

			// Open User Activities
			buttonActivities.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(MainActivity.this,
							ActivitiesActivity.class);
					MainActivity.this.startActivity(myIntent);
				}
			});

			// Open Popular Books
			buttonPopularBooks.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View v) {
					Intent myIntent = new Intent(MainActivity.this,
							LatestAdditionsActivity.class);
					MainActivity.this.startActivity(myIntent);
				}
			});

		} catch (Exception e) {
			// noth
			this.finish();
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// SUMMRE REMOVED
		// menu.add(Menu.NONE, App.MENU_SCANNED_BOOKS_ID, Menu.NONE,
		// R.string.menuScannedBooks)
		// .setIcon(R.drawable.ic_menu_recent_history)
		// .setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		menu.add(Menu.NONE, App.MENU_LIBRARY_SETTINGS, Menu.FIRST,
				app.library.name)
				.setIcon(R.drawable.ic_menu_account_list)
				.setShowAsActionFlags(
						MenuItem.SHOW_AS_ACTION_IF_ROOM
								| MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		// SUMMER REMOVED
		// if (App.historyManager.historyItemsNumber() > 0){
		// // menu.findItem(App.MENU_ADD_BOOKS_ID).setVisible(true);
		// menu.findItem(App.MENU_SCANNED_BOOKS_ID).setVisible(true);
		// }
		// else{
		// // menu.findItem(App.MENU_ADD_BOOKS_ID).setVisible(false);
		// menu.findItem(App.MENU_SCANNED_BOOKS_ID).setVisible(false);
		// }

		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	;

	private void showMessageIfHaventSavedCredentials() {
		getApplicationContext();
		SharedPreferences settings = getSharedPreferences(app.library.name,
				Context.MODE_PRIVATE);
		String username = settings.getString(LibPreferences.lib_user_username, "");

		if (username == "") {
			Toast.makeText(getApplicationContext(), getString(R.string.msgSaveCredentials) + " " +
					app.library.name + " " + getString(R.string.msgSaveCredentials_settings), Toast.LENGTH_LONG).show();
		}
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// NavUtils.navigateUpFromSameTask(this);
				onBackPressed();
				return true;
			case App.MENU_GLOBAL_SETTINGS: {
				Intent myIntent = new Intent(MainActivity.this,
						PreferencesActivity.class);
				MainActivity.this.startActivity(myIntent);

			}
			return true;

			case App.MENU_LIBRARY_SETTINGS: {

				Intent myIntent = new Intent(MainActivity.this,
						LibPreferences.class);
				MainActivity.this.startActivity(myIntent);

			}
			return true;
			case App.MENU_SCANNED_BOOKS_ID: {
				Intent myIntent = new Intent(MainActivity.this,
						HistoryActivity.class);
				MainActivity.this.startActivity(myIntent);
			}
			return true;

		}
		return super.onOptionsItemSelected(item);
	}

	//
	public static class DataClassActivities implements Serializable {

		/**
		 *
		 */
		private static final long serialVersionUID = 1L;

		public static int NO_ACKS = -15;

		public enum ActivityType {
			IncomingRequest, OutgoingRequest, BooksITook, BooksIGave, NotSet
		}

		ActivityType type = ActivityType.NotSet;

		Book book = new Book();

		int acknowledge = NO_ACKS;

		String username;
//		String isbn;
//		String title;
//		String authors;
//		String date;
//		String imgURL;

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
	}

	/**
	 * Refresh activity's language
	 */
	private void refresh() {
		App.refreshLang = false;
		finish();
		Intent myIntent = new Intent(MainActivity.this, MainActivity.class);
		startActivity(myIntent);
	}


}
