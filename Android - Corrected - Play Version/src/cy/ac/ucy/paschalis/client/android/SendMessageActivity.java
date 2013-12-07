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


import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

// import cy.ac.ucy.paschalis.client.android.R;
import com.google.zxing.client.android.PreferencesActivity;

import cy.ac.ucy.paschalis.client.android.R;


public class SendMessageActivity extends SherlockActivity {

	private static final String TAG = SendMessageActivity.class
			.getSimpleName();

	App app;

	EditText editTextMessage;

	Button buttonSendMessage;

	ProgressBar progressBarSendMessage;

	String destUser;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_message);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		app = (App) getApplication();

		editTextMessage = (EditText) findViewById(R.id.editTextMessage);

		buttonSendMessage = (Button) findViewById(R.id.buttonSendMessage);
		progressBarSendMessage = (ProgressBar) findViewById(R.id.progressBarSendMessage);


		// Get arguments, to determine who opened this activity
		final Bundle extras = getIntent().getExtras();

		try {
			destUser = extras
					.getString(App.ExtrasForSendMessage_DestinationUser);
		} catch (Exception e) {
			this.finish();
		}


		editTextMessage.addTextChangedListener(new TextWatcher() {

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
				if (editTextMessage.getText().length() >= 3) buttonSendMessage
						.setEnabled(true);
				else buttonSendMessage.setEnabled(false);
			}
		});


		buttonSendMessage.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				new AsyncTaskSendMessage().execute(destUser);

			}
		});

	}


	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		menu.add(Menu.NONE, App.MENU_CLEAR, Menu.FIRST, R.string.clear)
				.setIcon(R.drawable.ic_menu_close_clear_cancel)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

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
	public void onBackPressed() {
		super.onBackPressed();
	}

	;


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				// NavUtils.navigateUpFromSameTask(this);
				onBackPressed();
				return true;
			case App.MENU_LIBRARY_SETTINGS: {
				Intent myIntent = new Intent(SendMessageActivity.this,
						LibPreferences.class);
				SendMessageActivity.this.startActivity(myIntent);

			}
			return true;
			case App.MENU_CLEAR:
				clearForm();
				return true;
			case App.MENU_GLOBAL_SETTINGS: {
				Intent myIntent = new Intent(SendMessageActivity.this,
						PreferencesActivity.class);
				SendMessageActivity.this.startActivity(myIntent);

			}
			return true;
		}
		return super.onOptionsItemSelected(item);
	}


	private void clearForm() {
		editTextMessage.setText("");

	}


	/**
	 * Lents a book to user, in Activities
	 *
	 * @author paschalis
	 */
	private class AsyncTaskSendMessage extends
			AsyncTask<String, Integer, Integer> {


		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			buttonSendMessage.setEnabled(false);
			buttonSendMessage.setVisibility(View.INVISIBLE);
			progressBarSendMessage.setVisibility(View.VISIBLE);

		}


		@Override
		protected Integer doInBackground(String... dest) {

			int returnResult = App.GENERAL_NO_INTERNET;


			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			// Save Username , ISBN, and Destination User
			parameters.add(new BasicNameValuePair("username", app
					.getUsername()));
			parameters.add(new BasicNameValuePair("destination", dest[0]));
			String fullMessage = getString(R.string.bookInfo) + ":\n"
					+ getString(R.string.title) + ": "
					+ app.selectedBook.title + "\n"
					+ getString(R.string.author) + ": "
					+ app.selectedBook.authors + "\n"
					+ getString(R.string.isbn) + ": "
					+ app.selectedBook.isbn + "\n\n" +
					editTextMessage
							.getText().toString();
			
			
			/*
			 

			 
			 * */

			parameters.add(new BasicNameValuePair("message", fullMessage));

			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_getSendMessage_URL(), parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null) {
				try {
					// Create JSON Obj based on the result!
					JSONObject userData = new JSONObject(resultStr);

					returnResult = userData.getInt("result");

				} catch (JSONException e) {
					Log.e(TAG, "Error parsing data " + e.toString());


				}


			}


			return returnResult;

		}


		protected void onPostExecute(Integer result) {

			buttonSendMessage.setVisibility(View.VISIBLE);
			progressBarSendMessage.setVisibility(View.INVISIBLE);


			if (result == App.GENERAL_SUCCESSFULL) {

				Toast.makeText(SendMessageActivity.this,
						R.string.messageSend, Toast.LENGTH_SHORT).show();
				SendMessageActivity.this.finish();

			} else if (result == App.SEND_MAIL_USER_DONT_ACCEPTS) {
				Toast.makeText(SendMessageActivity.this,
						R.string.userDontAcceptMails, Toast.LENGTH_SHORT)
						.show();

			} else if (result == App.SEND_MAIL_USER_DONT_ACCEPTS) {
				Toast.makeText(
						SendMessageActivity.this,
						getString(R.string.failedToSendEmail) + ". "
								+ getString(R.string.msgPleaseContact)
								+ ": " + app.library.email,
						Toast.LENGTH_LONG).show();

				buttonSendMessage.setEnabled(true);

			}

		}
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
		Intent myIntent = new Intent(SendMessageActivity.this, SendMessageActivity.class);
		startActivity(myIntent);
	}


}
