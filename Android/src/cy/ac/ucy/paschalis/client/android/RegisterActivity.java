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
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

// import cy.ac.ucy.paschalis.client.android.R;
import com.google.zxing.client.android.PreferencesActivity;

import cy.ac.ucy.paschalis.client.android.R;


public class RegisterActivity extends SherlockActivity {

	App app;

	private static final String TAG = RegisterActivity.class
			.getSimpleName();


	private String registerMessage;

	private RegisterAsyncTask registerAsyncTask;

	EditText registerPassword;

	EditText registerConfirmPassword;

	EditText registerName;

	EditText registerSurname;

	EditText registerEmail;

	EditText registerPhone;

	CheckBox checkBoxNotificationsApplication;

	CheckBox checkBoxNotificationsEmail;

	Button buttonRegister;

	EditText registerUsername;


	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_register);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		app = (App) getApplication();


		buttonRegister = (Button) findViewById(R.id.buttonRegister);

		registerAsyncTask = new RegisterAsyncTask();

		registerUsername = (EditText) findViewById(R.id.editTextRegisterUsername);
		registerPassword = (EditText) findViewById(R.id.editTextRegisterPassword);
		registerConfirmPassword = (EditText) findViewById(R.id.editTextRegisterConfirmPassword);
		registerName = (EditText) findViewById(R.id.editTextRegisterName);
		registerSurname = (EditText) findViewById(R.id.editTextRegisterSurname);
		registerEmail = (EditText) findViewById(R.id.editTextRegisterEmail);
		registerPhone = (EditText) findViewById(R.id.editTextRegisterPhone);
		checkBoxNotificationsApplication = (CheckBox) findViewById(R.id.checkBoxRegisterNotificationsApplication);
		checkBoxNotificationsEmail = (CheckBox) findViewById(R.id.checkBoxRegisterNotificationsEmail);
		// textViewConfirmPassword = (TextView)
		// findViewById(R.id.textViewRegisterConfirmPassword);


		// Watch Field Changes
		registerUsername.addTextChangedListener(new TextWatcher() {

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
				enableRegisterFields(RegisterFields.username);
			}
		});


		registerPassword.addTextChangedListener(new TextWatcher() {

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
				enableRegisterFields(RegisterFields.password);
			}
		});


		registerConfirmPassword.addTextChangedListener(new TextWatcher() {

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

				enableRegisterFields(RegisterFields.confirmPassword);
			}
		});


		registerName.addTextChangedListener(new TextWatcher() {

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
				enableRegisterFields(RegisterFields.name);
			}
		});

		registerSurname.addTextChangedListener(new TextWatcher() {

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
				enableRegisterFields(RegisterFields.surname);
			}
		});

		registerEmail.addTextChangedListener(new TextWatcher() {

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
				enableRegisterFields(RegisterFields.email);
			}
		});

		registerPhone.addTextChangedListener(new TextWatcher() {

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
				enableRegisterFields(RegisterFields.phone);

			}
		});


		buttonRegister.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				// Try to register user to database

				// Hide Progress Bar, and reEnable button
				ProgressBar progressBarRegisterButton = (ProgressBar) findViewById(R.id.progressBarRegisterButton);

				buttonRegister.setClickable(false);
				progressBarRegisterButton.setVisibility(View.VISIBLE);
				buttonRegister.setVisibility(View.INVISIBLE);


				// Progress Bar Can cancel the task
				progressBarRegisterButton
						.setOnClickListener(new OnClickListener() {

							@Override
							public void onClick(View v) {
								if (!registerAsyncTask.isCancelled())
									registerAsyncTask.cancel(true);

							}
						});


				// Login User
				if (!registerAsyncTask.isCancelled()) {
					registerAsyncTask = new RegisterAsyncTask();
				}
				registerAsyncTask.execute(app.library);

			}
		});

	}


	private enum RegisterFields {
		username, password, confirmPassword, name, surname, email, phone
	}


	/**
	 * @param input Enable all fields from input and below, that are correct
	 */
	private void enableRegisterFields(RegisterFields input) {
		switch (input) {
			case username:
				if (registerUsername.getText().length() >= 4) {
					registerPassword.setFocusable(true);
					registerPassword.setFocusableInTouchMode(true);
					registerPassword.setEnabled(true);
				} else {
					disableRegisterFields(RegisterFields.username);
					registerPassword.setText("");
					break;
				}
			case password:
				if (registerPassword.getText().length() > 0) {
					registerConfirmPassword.setEnabled(true);
					registerConfirmPassword.setFocusable(true);
					registerConfirmPassword.setFocusableInTouchMode(true);
				} else {
					disableRegisterFields(RegisterFields.password);
					registerConfirmPassword.setText("");
					break;
				}
			case confirmPassword:
				// If password size >=0
				if (registerPassword.getText().length() > 0) {
					// If passwords match
					if (registerConfirmPassword
							.getText()
							.toString()
							.equals(registerPassword.getText()
									.toString())) {
						registerName.setEnabled(true);
						registerName.setFocusable(true);
						registerName.setFocusableInTouchMode(true);
						registerConfirmPassword.setTextColor(Color.BLUE);
					} else {

						registerConfirmPassword.setTextColor(Color.RED);
						disableRegisterFields(RegisterFields.confirmPassword);
						break;
					}
				} else {
					registerConfirmPassword.setTextColor(Color.BLACK);
					break;

				}
			case name:
				if (registerName.getText().length() > 0) {
					registerSurname.setEnabled(true);
					registerSurname.setFocusable(true);
					registerSurname.setFocusableInTouchMode(true);
				} else {
					disableRegisterFields(RegisterFields.name);
					break;
				}
			case surname:
				if (registerSurname.getText().length() > 0) {
					registerEmail.setEnabled(true);
					registerEmail.setFocusable(true);
					registerEmail.setFocusableInTouchMode(true);
				} else {
					disableRegisterFields(RegisterFields.surname);
					break;
				}
			case email:
				// If email address is correct, enable Phone
				if (isEmailCorrect(registerEmail.getText().toString())) {
					registerPhone.setEnabled(true);
					registerPhone.setFocusable(true);
					registerPhone.setFocusableInTouchMode(true);
					registerEmail.setTextColor(Color.BLUE);
				} else {
					registerEmail.setTextColor(Color.RED);
					disableRegisterFields(RegisterFields.email);
					break;
				}
			case phone:
				if (registerPhone.getText().length() > 0) {
					if (isPhoneNumberCorrect(registerPhone.getText()
							.toString())) {
						buttonRegister.setEnabled(true);
						registerPhone.setTextColor(Color.BLUE);
					} else {
						disableRegisterFields(RegisterFields.phone);
						registerPhone.setTextColor(Color.RED);
						break;
					}
				}
			default:

				break;
		}

	}


	/**
	 * @param input Disables all fields from input and below
	 */
	private void disableRegisterFields(RegisterFields input) {
		switch (input) {
			case username:
				registerPassword.setEnabled(false);
				registerPassword.setFocusable(false);
			case password:
				registerConfirmPassword.setEnabled(false);
				registerConfirmPassword.setFocusable(false);
			case confirmPassword:
				registerName.setEnabled(false);
				registerName.setFocusable(false);
			case name:
				registerSurname.setEnabled(false);
				registerSurname.setFocusable(false);
			case surname:
				registerEmail.setEnabled(false);
				registerEmail.setFocusable(false);
			case email:
				registerPhone.setEnabled(false);
				registerPhone.setFocusable(false);
			case phone:
				buttonRegister.setEnabled(false);
			default:

				break;
		}

	}


	/*
	 * Inflate the Menu
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockActivity#onCreateOptionsMenu(android
	 * .view.Menu)
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {


		getSupportMenuInflater().inflate(R.menu.activity_register, menu);

		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);


		return true;
	}


	@Override
	public void onBackPressed() {
		super.onBackPressed();
	}

	;


	/*
	 * Handle menu interraction
	 * 
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.actionbarsherlock.app.SherlockActivity#onOptionsItemSelected(android
	 * .view.MenuItem)
	 */
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				onBackPressed();
				// NavUtils.navigateUpFromSameTask(this);
				return true;
			case R.id.itemClearRegister:
				clearForm();
				return true;
			case App.MENU_GLOBAL_SETTINGS: {
				Intent myIntent = new Intent(RegisterActivity.this,
						PreferencesActivity.class);
				RegisterActivity.this.startActivity(myIntent);

			}
			return true;

		}
		return super.onOptionsItemSelected(item);
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

		if (app.registerSuccess == true) {
			// Make it false, and go back
			app.registerSuccess = false;
			onBackPressed();

		}
	}


	/**
	 * Clears all forms Data
	 */
	private void clearForm() {

		registerUsername.setText("");
		registerPassword.setText("");
		registerConfirmPassword.setText("");
		registerName.setText("");
		registerSurname.setText("");
		registerEmail.setText("");
		registerPhone.setText("");
		checkBoxNotificationsApplication.setChecked(true);
		checkBoxNotificationsEmail.setChecked(false);

		disableRegisterFields(RegisterFields.username);
	}


	/**
	 * Inner Private Class {@link RegisterAsyncTask}: Executes in Background
	 * PHP Script, to Register to SmartLib
	 *
	 * @author paschalis
	 */

	private class RegisterAsyncTask extends AsyncTask<Library, Void, Integer> {

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);
			switch (result) {
				case App.REGISTER_SUCCESSFULL: {
					Intent myIntent = new Intent(RegisterActivity.this,
							RegisterSuccessActivity.class);
					RegisterActivity.this.startActivity(myIntent);
					app.registerSuccess = true;


				}

				break;
				case App.REGISTER_NOT_SUCCESSFULL: {
					AlertDialog.Builder alert = new AlertDialog.Builder(
							RegisterActivity.this);
					alert.setTitle(R.string.msgFailedToRegisterTitle);
					alert.setMessage(registerMessage);

					alert.setIcon(android.R.drawable.ic_dialog_alert);

					alert.setNeutralButton(R.string.dismiss,
							new DialogInterface.OnClickListener() {

								public void onClick(
										DialogInterface dialog,
										int whichButton) {

								}
							});

					alert.show();
					registerMessage = null;// GC
				}

				break;
				case App.REGISTER_NO_INTERNET: {

					// There is internet access, so webpage is down
					if (isNetworkAvailable()) {
						Toast.makeText(
								RegisterActivity.this,
								getString(R.string.libraryIsDown)
										+ "\n"
										+ getString(R.string.reportThisToDev),
								Toast.LENGTH_LONG).show();


						RegisterActivity.this.finish();
					} else {
						AlertDialog.Builder alert = new AlertDialog.Builder(
								RegisterActivity.this);
						alert.setTitle(R.string.msgNoInternetConnectionTitle);
						alert.setMessage(R.string.msgNoInternetConnection);
						alert.setIcon(android.R.drawable.ic_dialog_alert);

						alert.setNeutralButton(R.string.no,
								new DialogInterface.OnClickListener() {

									public void onClick(
											DialogInterface dialog,
											int whichButton) {

									}
								});

						alert.setPositiveButton(R.string.yes,
								new DialogInterface.OnClickListener() {

									public void onClick(
											DialogInterface dialog,
											int whichButton) {
										startActivity(new Intent(
												android.provider.Settings.ACTION_WIRELESS_SETTINGS));
									}
								});

						alert.show();
					}


				}

				break;
				case App.REGISTER_PARSING_FAILED:
					AlertDialog.Builder alert = new AlertDialog.Builder(
							RegisterActivity.this);
					alert.setTitle(R.string.msgFailedToParseTitle);
					alert.setMessage(R.string.msgFailedToParse);

					alert.setIcon(android.R.drawable.ic_dialog_alert);

					alert.setNeutralButton(R.string.no,
							new DialogInterface.OnClickListener() {

								public void onClick(
										DialogInterface dialog,
										int whichButton) {

								}
							});

					alert.setPositiveButton(R.string.yes,
							new DialogInterface.OnClickListener() {

								public void onClick(
										DialogInterface dialog,
										int whichButton) {
									Toast.makeText(
											RegisterActivity.this,
											"Will work when app published to Google Play",
											Toast.LENGTH_SHORT)
											.show();

								}
							});

					alert.show();
					break;
				default:
					break;

			}


			// Hide Progress Bar, and reEnable button
			ProgressBar progressBarRegisterButton = (ProgressBar) findViewById(R.id.progressBarRegisterButton);
			Button buttonRegister = (Button) findViewById(R.id.buttonRegister);
			buttonRegister.setClickable(true);
			buttonRegister.setVisibility(View.VISIBLE);
			progressBarRegisterButton.setVisibility(View.INVISIBLE);

		}


		@Override
		protected Integer doInBackground(Library... lib) {

			int returnResult = App.REGISTER_NO_INTERNET;

			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();

			// Say that we are mobile (Android Device)
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			app.registerUser = new User();

			app.registerUser.username = registerUsername.getText()
					.toString();
			app.registerUser.name = registerName.getText().toString();
			app.registerUser.email = registerEmail.getText().toString();

			// Save Username And Password
			parameters.add(new BasicNameValuePair("username",
					app.registerUser.username));
			parameters.add(new BasicNameValuePair("password",
					registerPassword.getText().toString()));
			parameters.add(new BasicNameValuePair("confPassword",
					registerConfirmPassword.getText().toString()));
			parameters.add(new BasicNameValuePair("name",
					app.registerUser.name));
			parameters.add(new BasicNameValuePair("surname", registerSurname
					.getText().toString()));
			parameters.add(new BasicNameValuePair("email",
					app.registerUser.email));
			parameters.add(new BasicNameValuePair("telephone", registerPhone
					.getText().toString()));

			String strCheckBoxNotifApp = "";
			String strCheckBoxNotifEmail = "";


			if (checkBoxNotificationsApplication.isChecked() == true) {
				strCheckBoxNotifApp = "on";
			}
			if (checkBoxNotificationsEmail.isChecked() == true) {
				strCheckBoxNotifEmail = "on";
			}


			parameters.add(new BasicNameValuePair("appNotif",
					strCheckBoxNotifApp));
			parameters.add(new BasicNameValuePair("emailNotif",
					strCheckBoxNotifEmail));


			// Execute PHP Script
	String resultStr = App.executePHPScript(lib[0].getRegisterURL(),
			parameters);

			// Parse Result (JSON Obj)
	if (resultStr != null) {
			try {
					// Create JSON Obj based on the result!
			JSONObject userData = new JSONObject(resultStr);

				returnResult = userData.getInt("result");


								// User Credencials match
					if (returnResult != App.REGISTER_SUCCESSFULL) {

				registerMessage = userData.getString("message");

					}


			}
 catch (JSONException e) {
					Log.e(TAG, "Error parsing data " + e.toString());

					returnResult = App.REGISTER_PARSING_FAILED;


				}


	}


		return returnResult;

		}


	}// End of Async Task Inner Class


	/**
	 * Validates if an email address is correct
	 *
	 * @author paschalis
	 */
	private boolean isEmailCorrect(String pEmail) {

		Pattern pattern;
		Matcher matcher;

		final String EMAIL_PATTERN = "^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@"
				+ "[A-Za-z0-9]{2,}(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";


		pattern = Pattern.compile(EMAIL_PATTERN);


		matcher = pattern.matcher(pEmail);
		return matcher.matches();

	}


	/**
	 * @param pPhoneNumber
	 * @return true if the phone number is correct
	 */
	private boolean isPhoneNumberCorrect(String pPhoneNumber) {

		Pattern pattern = Pattern
				.compile("(\\+[1-9]{3,4}|0[1-9]{4}|00[1-9]{3})?\\-?\\d{8,20}");
		Matcher matcher = pattern.matcher(pPhoneNumber);

		if (matcher.matches()) return true;


		return false;
	}


	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) this
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();


		return (activeNetworkInfo != null);


	}


	/**
	 * Refresh activity's language
	 */
	private void refresh() {
		App.refreshLang = false;
		finish();
		Intent myIntent = new Intent(RegisterActivity.this, RegisterActivity.class);
		startActivity(myIntent);
	}


}
