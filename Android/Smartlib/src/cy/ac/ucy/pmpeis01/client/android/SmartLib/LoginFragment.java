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
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockFragment;

import cy.ac.ucy.pmpeis01.client.android.R;








/**
 * 
 * 
 * @author paschalis
 * 
 */
public class LoginFragment extends SherlockFragment {







	public static final int		LOGIN_CORRECT_CREDENCIALS	= 1;

	public static final int		LOGIN_WRONG_CREDENCIALS		= 0;

	public static final int		LOGIN_FAILED_TO_COMMUNICATE	= -11;

	public static final int		LOGIN_FAILED_PARSE			= -2;

	public static final int		LOGIN_NO_INTERNET_CONNECTION	= -3;


	public static final int		LOGIN_NOT_ACTIVATED			= -4;


	public static final int		LOGIN_VISITOR				= -5;

	public static final int		LOGIN_BANNED				= -6;

	private static final String	TAG						= LoginFragment.class
															.getSimpleName();

	final static String			ARG_POSITION				= "position";

	int						mCurrentPosition			= -1;

	App						app;

	EditText					editTextUsername;

	EditText					editTextPassword;

	TextView					textViewUsernameDirection;

	TextView					textViewPasswordDirection;

	Button					buttonLogin;

	TextView					textViewLibrary;

	TextView					textViewTitle;

	TextView					textViewLibraryLocation;





	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {

		app = ((StartActivity) getActivity()).app;

		// If activity recreated (such as from screen rotate), restore
		// the previous Library selection set by onSaveInstanceState().
		// This is primarily necessary when in the two-pane layout.
		if (savedInstanceState != null){
			mCurrentPosition = savedInstanceState.getInt(ARG_POSITION);
		}




		// In large and in regular layout, its the same Login layout
		return inflater.inflate(R.layout.login, container, false);



	}







	@Override
	public void onStart() {
		super.onStart();

		Bundle args = getArguments();



		if (args != null){
			// Set Login Screen, based on Library choosen
			updateLoginView(ChooseLibraryFragment.chosenLib);

		}
		else if (mCurrentPosition != -1){
			// Set Login Screen, based on Library choosen
			updateLoginView(ChooseLibraryFragment.chosenLib);
		}
	}






	@Override
	public void onStop() {
		super.onStop();

		((StartActivity) getActivity()).isLibrarySelected = false;
		// Invalidate the menu, so a new one will be created
		((StartActivity) getActivity()).invalidateOptionsMenu();


	}





	@Override
	public void onResume() {
		super.onResume();

		enableLoginFields();
	}





	/**
	 * Enable all login fields that have to be enabled
	 */
	private void enableLoginFields() {
		// Check if have to reanble fields
		// Enable password
		try{
			if (editTextUsername.getText().length() >= 4){
				editTextPassword.setEnabled(true);
				textViewPasswordDirection.setEnabled(true);
				// Enable login button
				if (editTextPassword.getText().length() > 0){
					buttonLogin.setEnabled(true);
				}
				else buttonLogin.setEnabled(false);
			}
			// Disable password
			else{
				editTextPassword.setEnabled(false);
				textViewPasswordDirection.setEnabled(false);
				editTextPassword.setText("");

			}
		}
		catch (NullPointerException npe){
			// noth
		}

	}





	/**
	 * Update the login View
	 * 
	 * @param code
	 *             the Library code
	 */
	public void updateLoginView(Library lib) {

		try{
			((StartActivity) getActivity()).isLibrarySelected = true;
			// Invalidate the menu, so a new one will be created
			((StartActivity) getActivity()).invalidateOptionsMenu();


			
			
			TextView loginLibrary = (TextView) getActivity().findViewById(
					R.id.TextViewLoginLibraryName);

			loginLibrary.setText(lib.name);

			textViewLibraryLocation = (TextView) getActivity().findViewById(
					R.id.textViewLoginLibraryLocation);

			textViewLibraryLocation.setText(lib.location);


			ImageView loginLogo = (ImageView) getActivity().findViewById(
					R.id.imageViewLoginLibraryLogo);

			// Show logo
			App.imageLoader.DisplayImage(lib.getImageURL(), loginLogo);

			//Save bitma in app 
			loginLogo.buildDrawingCache();
		      app.bitmap = loginLogo.getDrawingCache();
			
			
			enableLoginForm();
			tryToAutofillWithPrefs();
		}
		catch (NullPointerException npe){
			// noth
		}



	}





	private void tryToAutofillWithPrefs() {
		SharedPreferences settings = getActivity().getSharedPreferences(app.library.name,
				getActivity().getApplicationContext().MODE_PRIVATE);
		String username =	settings.getString(LibPreferences.lib_user_username, "");
		if(username=="") return;
		
		String pass =	settings.getString(LibPreferences.lib_userPass, "");
		String password="";
		try{
			password=  Crypto.decrypt(MainActivity.PSW, pass);
		}
		catch (Exception e){
			e.printStackTrace();
		}
		
		if(password=="") return;
		
		editTextUsername.setText(username);
		editTextPassword.setText(password);
		
		
		
	}







	/**
	 * Enables Login/Register form
	 */
	private void enableLoginForm() {
		textViewLibrary = (TextView) getActivity().findViewById(
				R.id.TextViewLoginLibraryName);
		textViewTitle = (TextView) getActivity().findViewById(
				R.id.TextViewLoginTitle);

		// textViewLibraryLocation = (TextView) getActivity().findViewById(
		// R.id.textViewLibraryLocation);

		buttonLogin = (Button) getActivity().findViewById(R.id.buttonLogin);

		editTextUsername = (EditText) getActivity().findViewById(
				R.id.editTextLoginUsername);

		editTextPassword = (EditText) getActivity().findViewById(
				R.id.editTextLoginPassword);

		textViewUsernameDirection = (TextView) getActivity().findViewById(
				R.id.textViewDirectionLoginUsername);

		textViewPasswordDirection = (TextView) getActivity().findViewById(
				R.id.textViewDirectionLoginPassword);


		final Button buttonLogin = (Button) getActivity().findViewById(
				R.id.buttonLogin);

		textViewLibrary.setEnabled(true);
		textViewTitle.setEnabled(true);
		textViewLibraryLocation.setEnabled(true);
		editTextUsername.setEnabled(true);
		textViewUsernameDirection.setEnabled(true);

		editTextUsername.setText("");
		editTextPassword.setText("");

		editTextUsername.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {


			}





			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				// Clear Login Staff
				clearLoginStaff();

			}





			@Override
			public void afterTextChanged(Editable s) {
				// Enable login button
				if (editTextUsername.getText().length() >= 4){
					editTextPassword.setEnabled(true);
					textViewPasswordDirection.setEnabled(true);
				}
				else{
					editTextPassword.setEnabled(false);
					textViewPasswordDirection.setEnabled(false);
					editTextPassword.setText("");

				}
			}


		});

		editTextPassword.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before,
					int count) {
			}





			@Override
			public void beforeTextChanged(CharSequence s, int start,
					int count, int after) {
				// Clear Login Staff
				clearLoginStaff();
			}





			@Override
			public void afterTextChanged(Editable s) {
				if (editTextPassword.getText().length() > 0){
					buttonLogin.setEnabled(true);
				}
				else buttonLogin.setEnabled(false);
			}
		});



		// Login Button
		buttonLogin.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {

				// Create new User
				app.user = new User();

				app.user.username = editTextUsername.getText().toString();
				app.user.password = editTextPassword.getText().toString();

				// Hide Progress Bar, and reEnable button
				ProgressBar progressBarLoginButton = (ProgressBar) getActivity()
						.findViewById(R.id.progressBarLoginButton);
				Button buttonLogin = (Button) getActivity().findViewById(
						R.id.buttonLogin);
				buttonLogin.setClickable(false);
				buttonLogin.setEnabled(false);
				progressBarLoginButton.setVisibility(View.VISIBLE);


				// Login User
				new LoginAsyncTask().execute(app.library);
			}
		});


		enableLoginFields();

	}





	/**
	 * Clears login message and Progress dialog
	 * 
	 */
	private void clearLoginStaff() {
		ProgressBar progressBarLoginButton = (ProgressBar) getActivity()
				.findViewById(R.id.progressBarLoginButton);

		TextView editTextLoginMsg = (TextView) getActivity().findViewById(
				R.id.textViewLoginMessage);

		progressBarLoginButton.setVisibility(View.INVISIBLE);
		editTextLoginMsg.setText("");
	}





	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);

		// Save the current library selection in case we need to recreate the
		// fragment
		outState.putInt(ARG_POSITION, mCurrentPosition);
	}





	private boolean isNetworkAvailable() {
		ConnectivityManager connectivityManager = (ConnectivityManager) getActivity()
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo activeNetworkInfo = connectivityManager
				.getActiveNetworkInfo();



		return (activeNetworkInfo != null);


	}





	/**
	 * Inner Private Class {@link LoginAsyncTask}: Executes in Background PHP
	 * Script, to Login to SmartLib
	 * 
	 * @author paschalis
	 * 
	 */
	private class LoginAsyncTask extends AsyncTask<Library, Void, Integer> {

		@Override
		protected void onPostExecute(Integer result) {
			super.onPostExecute(result);

			/** TextView to display the message */
			TextView textViewLoginMessage = (TextView) getActivity()
					.findViewById(R.id.textViewLoginMessage);

			switch (result) {
				case LOGIN_FAILED_TO_COMMUNICATE:
					textViewLoginMessage
							.setText(R.string.msgLoginCantReachServer);
					break;
				case LOGIN_WRONG_CREDENCIALS:
					textViewLoginMessage
							.setText(R.string.msgLoginWrongCredencials);
					break;
				case LOGIN_NOT_ACTIVATED:
					textViewLoginMessage.setText(getActivity().getString(
							R.string.msgLoginAccountNotActivated)
							+ " " + app.user.email);
					break;
				case LOGIN_BANNED:
					textViewLoginMessage.setText(getActivity().getString(
							R.string.msgLoginUserBanned)
							+ " " + app.library.email);
					break;
				case LOGIN_NO_INTERNET_CONNECTION:

					// There is internet access, so webpage is down
					if (isNetworkAvailable()){
						textViewLoginMessage
								.setText(getString(R.string.libraryIsDown)
										+ "\n"
										+ getString(R.string.reportThisToDev));
					}
					else{

						textViewLoginMessage
								.setText(R.string.msgLoginNoInternet);
						AlertDialog.Builder alert = new AlertDialog.Builder(
								getActivity());
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

					break;
				case LOGIN_VISITOR:
					textViewLoginMessage.setText(R.string.msgLoginVisitor);
					break;

				case LOGIN_CORRECT_CREDENCIALS:
					textViewLoginMessage.setText("");

					// Switch to Main Activity
					Intent myIntent = new Intent(getActivity(),
							MainActivity.class);
					getActivity().startActivity(myIntent);
					break;





				default:
					textViewLoginMessage.setText("");
					break;
			}


			// Hide Progress Bar, and reEnable button
			ProgressBar progressBarLoginButton = (ProgressBar) getActivity()
					.findViewById(R.id.progressBarLoginButton);
			Button buttonLogin = (Button) getActivity().findViewById(
					R.id.buttonLogin);
			buttonLogin.setClickable(true);
			buttonLogin.setEnabled(true);
			progressBarLoginButton.setVisibility(View.INVISIBLE);

		}





		@Override
		protected Integer doInBackground(Library... lib) {

			int returnResult = LOGIN_NO_INTERNET_CONNECTION;

			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();

			// Say that we are mobile
			parameters.add(new BasicNameValuePair("device",
					App.DEVICE_ANDROID));

			// Save Username And Password
			parameters.add(new BasicNameValuePair("username",
					app.user.username));
			parameters.add(new BasicNameValuePair("password",
					app.user.password));

			// Execute PHP Script
			String resultStr = App.executePHPScript(lib[0].getLoginURL(),
					parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null){
				try{
					// Create JSON Obj based on the result!
					JSONObject userData = new JSONObject(resultStr);

					// int result =
					returnResult = userData.getInt("result");

					// User Credencials match
					if (returnResult == LOGIN_CORRECT_CREDENCIALS){
						// Save all data from JSON Object
						app.user.name = userData.getString("name");
						app.user.surname = userData.getString("surname");
						app.user.email = userData.getString("email");
						app.user.telephone = userData
								.getString("telephone");
						app.user.level = userData.getInt("level");
						app.user.allowRequests = userData
								.getInt("allowRequests");

						//
						if (app.user.level == User.LEVEL_NOT_ACTIVATED){
							returnResult = LOGIN_NOT_ACTIVATED;
						}
						else if (app.user.level == User.LEVEL_BANNED){
							returnResult = LOGIN_BANNED;
						}
						else if (app.user.level == User.LEVEL_VISITOR){
							returnResult = LOGIN_VISITOR;
						}




					}


				}
				catch (JSONException e){

					Toast.makeText(
							LoginFragment.this.getActivity(),
							getString(R.string.msgFailedContactWebpage)
									+ "\n"
									+ getString(R.string.msgReportThisTo_)
									+ app.library.email,
							Toast.LENGTH_LONG).show();

					Log.e(TAG, "Error parsing data " + e.toString());

					returnResult = LOGIN_FAILED_PARSE;


				}


			}

			return returnResult;

		}



	}// End of Async Task Inner Class



}
