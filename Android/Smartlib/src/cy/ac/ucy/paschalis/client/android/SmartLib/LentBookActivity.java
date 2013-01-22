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
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import cy.ac.ucy.paschalis.client.android.PreferencesActivity;
import cy.ac.ucy.paschalis.client.android.R;





public class LentBookActivity extends SherlockActivity {

	private static final String	TAG					= LentBookActivity.class
														.getSimpleName();

	EditText					editTextDestinationUsername;

	TextView					textViewLentResult;

	Button					buttonLentBook;

	ProgressBar				progressBarLentButton;

	App						app;

	Boolean					fromEditBookActivity	= false;

	String					gotISBN;





	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		app = (App) getApplication();

		setContentView(R.layout.activity_lent_book);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Get arguments (User,Destination,ISBN)
		final Bundle extras = getIntent().getExtras();

		try{
			final String isbn = extras
					.getString(App.ExtrasForLentBookActivityISBN);

			fromEditBookActivity = true;

			gotISBN = isbn;

		}
		catch (Exception e){
			Toast.makeText(LentBookActivity.this,
					"Something went wrong. Please report this",
					Toast.LENGTH_LONG).show();
			fromEditBookActivity = false;
			LentBookActivity.this.finish();

		}


		progressBarLentButton = (ProgressBar) findViewById(R.id.progressBarLentButton);

		buttonLentBook = (Button) findViewById(R.id.buttonLentBook);

		editTextDestinationUsername = (EditText) findViewById(R.id.editTextDestinationUser);

		textViewLentResult = (TextView) findViewById(R.id.textViewLentResult);

		editTextDestinationUsername.addTextChangedListener(new TextWatcher() {

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
				if (editTextDestinationUsername.getText().length() >= 4){
					buttonLentBook.setEnabled(true);
				}
				else{
					buttonLentBook.setEnabled(false);
				}
			}
		});



		buttonLentBook.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				DataClassLentABook data = new DataClassLentABook();

				data.destinationUser = editTextDestinationUsername
						.getText().toString();

				data.isbn = gotISBN;

				new AsyncTaskLentABook().execute(data);

			}
		});

	}





	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getSupportMenuInflater().inflate(R.menu.activity_lent_book, menu);

		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		menu.add(Menu.NONE, App.MENU_LIBRARY_SETTINGS, Menu.NONE,
				app.library.name).setIcon(R.drawable.ic_menu_account_list)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}





	@Override
	public void onBackPressed() {
		super.onBackPressed();

	};





	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				if (fromEditBookActivity){
					onBackPressed();
				}
				else{
					NavUtils.navigateUpFromSameTask(this);
				}

				return true;
			case App.MENU_LIBRARY_SETTINGS:{
				Intent myIntent = new Intent(LentBookActivity.this,
						LibPreferences.class);
				LentBookActivity.this.startActivity(myIntent);

			}
				return true;
			case R.id.itemClearRegister:
				editTextDestinationUsername.setText("");
				return true;
			case App.MENU_GLOBAL_SETTINGS:{

				Intent myIntent = new Intent(LentBookActivity.this,
						PreferencesActivity.class);
				LentBookActivity.this.startActivity(myIntent);
				return true;
			}

		}
		return super.onOptionsItemSelected(item);
	}

	/**
	 * Lents a book to user, with the user chose with editext
	 * 
	 * @author paschalis
	 * 
	 */
	private class AsyncTaskLentABook extends
			AsyncTask<DataClassLentABook, Integer, Integer> {

		DataClassLentABook	mData;





		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progressBarLentButton.setVisibility(View.VISIBLE);
			buttonLentBook.setVisibility(View.INVISIBLE);
			buttonLentBook.setEnabled(false);

		}





		@Override
		protected Integer doInBackground(DataClassLentABook... data) {

			mData = data[0];

			int returnResult = App.GENERAL_NO_INTERNET;

			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			// Save Username , ISBN, and Destination User
			parameters.add(new BasicNameValuePair("owner", app.getUsername()));
			parameters.add(new BasicNameValuePair("destination",
					data[0].destinationUser));
			parameters.add(new BasicNameValuePair("isbn", data[0].isbn));

			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_lentABook_URL(), parameters);

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

			progressBarLentButton.setVisibility(View.INVISIBLE);
			buttonLentBook.setVisibility(View.VISIBLE);

			if (result == App.BORROW_SUCCESSFULL){

				Toast.makeText(LentBookActivity.this,
						R.string.msgBookSuccessfullyLented,
						Toast.LENGTH_SHORT).show();

				// Workaround
				try{
					app.selectedBook.status = App.BOOK_STATE_USER_RENTED;
				}
				catch (Exception e){
					// Noth
				}
				LentBookActivity.this.finish();
			}
			else if (result == App.BORROW_CANT_LEND_YOURSELF){
				textViewLentResult
						.setText(getString(R.string.msgYouCantLendYourself)
								+ ". "
								+ getString(R.string.msgChooseADifferentUsername)
								+ ".");
				App.setStyleErrorDirection(textViewLentResult);

				buttonLentBook.setEnabled(true);

			}

			else if (result == App.BORROW_NOT_FOUND_USER_DESTINATION){
				textViewLentResult
						.setText(getString(R.string.msgFailedToFindUser)
								+ ": " + mData.destinationUser + ".\n"
								+ getString(R.string.msgPleaseTryAgain));
				App.setStyleErrorDirection(textViewLentResult);

				buttonLentBook.setEnabled(true);

			}

			else{
				textViewLentResult.setText(R.string.msgBookFailedToLent);
				App.setStyleErrorDirection(textViewLentResult);

				buttonLentBook.setEnabled(true);

			}

		}
	}

	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////
	// //////////////// Classes to encapsulate data
	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////
	/**
	 * @author paschalis
	 * 
	 */
	public static class DataClassLentABook {

		public String	isbn;

		public String	destinationUser;

		public String	answer;
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


	}





	/**
	 * Refresh activity's language
	 * 
	 */
	private void refresh() {
		App.refreshLang = false;
		finish();
		Intent myIntent = new Intent(LentBookActivity.this,
				LentBookActivity.class);
		startActivity(myIntent);
	}



}
