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
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import cy.ac.ucy.paschalis.client.android.R;

// import cy.ac.ucy.paschalis.client.android.R;


public class LibPreferences extends SherlockPreferenceActivity implements
		SharedPreferences.OnSharedPreferenceChangeListener {


	public static String rememberCredencials = "lib_prefs_remember_credencials";

	public static String lib_userPass = "lib_user_pass";

	public static String lib_user_username = "lib_user_username";

	public static String lib_make_a_call = "lib_make_a_call";

	App app;


	@Override
	public void onCreate(Bundle aSavedState) {


		app = (App) getApplication();

		// Set library's logo as ActionBar Icon
		App.imageLoader.DisplayActionBarIcon(app.library.getImageURL(),
				getApplicationContext(), getSupportActionBar());

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		super.onCreate(aSavedState);

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);
		prefs.registerOnSharedPreferenceChangeListener(this);


		if (app.library.telephone != "null") {
			if (app.library.telephone != "") {
				setPreferenceScreen(createPreferenceCallLib());
			}
		}

		addPreferencesFromResource(R.xml.lib_prefs);

		// Make a call
		Preference myPref = (Preference) findPreference(lib_make_a_call);
		myPref.setOnPreferenceClickListener(new OnPreferenceClickListener() {

			public boolean onPreferenceClick(Preference preference) {

				{
					AlertDialog.Builder alert = new AlertDialog.Builder(
							LibPreferences.this);
					alert.setTitle(getString(R.string.call) + ": "
							+ app.library.name + " ("
							+ app.library.telephone + ")");


					alert.setIcon(R.drawable.ic_menu_call);

					alert.setNegativeButton(
							R.string.no,
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {
									// Noth

								}
							});


					alert.setPositiveButton(
							R.string.yes,
							new android.content.DialogInterface.OnClickListener() {

								@Override
								public void onClick(
										DialogInterface dialog,
										int which) {

									// Call library
									Intent dial = new Intent();
									dial.setAction("android.intent.action.DIAL");
									dial.setData(Uri.parse("tel:"
											+ app.library.telephone));
									startActivity(dial);

								}
							});

					alert.show();

				}


				return true;
			}
		});


	}


	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				LibPreferences.this.finish();

		}
		return true;
	}


	/**
	 * @return the Dynamically Created Preference Screen
	 */

	private PreferenceScreen createPreferenceCallLib() {

		// Create a Dynamic Preference Screen
		PreferenceScreen root = getPreferenceManager()
				.createPreferenceScreen(this);


		Preference preferenceMakeCall = new Preference(LibPreferences.this);


		preferenceMakeCall.setKey(lib_make_a_call);
		preferenceMakeCall.setTitle(getString(R.string.call) + " "
				+ app.library.name);
		root.addPreference(preferenceMakeCall);


		return root;
	}


	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	                                      String key) {

		boolean haveToRemember = sharedPreferences.getBoolean(
				LibPreferences.rememberCredencials, false);

		SharedPreferences settings = getSharedPreferences(app.library.name,
				MODE_PRIVATE);
		SharedPreferences.Editor editor = settings.edit();

		if (haveToRemember) {
			String pass = app.user.password;
			try {
				pass = Crypto.encrypt(MainActivity.PSW, app.user.password);

				editor.putString(LibPreferences.lib_user_username,
						app.user.username);
				editor.putString(LibPreferences.lib_userPass, pass);
				editor.commit();

			} catch (Exception e) {
				// noth
			}

		} else {
			editor.remove(LibPreferences.lib_user_username);
			editor.remove(LibPreferences.lib_userPass);
			editor.commit();
		}
	}


}
