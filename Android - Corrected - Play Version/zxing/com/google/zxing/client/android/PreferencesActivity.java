/*
 * Copyright (C) 2009 ZXing authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.zxing.client.android;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceScreen;
import android.widget.Toast;

import com.actionbarsherlock.app.SherlockPreferenceActivity;
import com.actionbarsherlock.view.MenuItem;

import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.App;

/**
 * The main settings activity.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class PreferencesActivity extends SherlockPreferenceActivity implements
		OnSharedPreferenceChangeListener {

	// public static final String KEY_DECODE_1D = "preferences_decode_1D";
	// public static final String KEY_DECODE_QR = "preferences_decode_QR";
	// public static final String KEY_DECODE_DATA_MATRIX =
	// "preferences_decode_Data_Matrix";
	// public static final String KEY_CUSTOM_PRODUCT_SEARCH =
	// "preferences_custom_product_search";

	public static final String KEY_PLAY_BEEP = "preferences_play_beep";

	public static final String KEY_VIBRATE = "preferences_vibrate";

	public static final String KEY_COPY_TO_CLIPBOARD = "preferences_copy_to_clipboard";

	public static final String KEY_SCAN_ONLY_MODE = "preferences_scan_only_mode";

	private boolean captureActivity = false;

	// public static final String KEY_FRONT_LIGHT = "preferences_front_light";
	// //public static final String KEY_BULK_MODE = "preferences_bulk_mode";
	// public static final String KEY_REMEMBER_DUPLICATES =
	// "preferences_remember_duplicates";
	// public static final String KEY_SUPPLEMENTAL =
	// "preferences_supplemental";
	public static final String KEY_AUTO_FOCUS = "preferences_auto_focus";

	// public static final String KEY_SEARCH_COUNTRY =
	// "preferences_search_country";

	// public static final String KEY_HELP_VERSION_SHOWN =
	// "preferences_help_version_shown";

	// private CheckBoxPreference decode1D;
	// private CheckBoxPreference decodeQR;
	// private CheckBoxPreference decodeDataMatrix;
	ListPreference languages;
	Preference preferenceClearCache;

	@SuppressLint("NewApi")
	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		addPreferencesFromResource(R.xml.preferences);
//		getSupportActionBar().setHomeButtonEnabled(true);
		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		PreferenceScreen preferences = getPreferenceScreen();
		preferences.getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);

		languages = (ListPreference) preferences
				.findPreference("preferences_language");
		preferenceClearCache = (Preference) preferences
				.findPreference("preferences_clear_cache");

		// Get arguments, to determine who opened this activity
		final Bundle extras = getIntent().getExtras();

		try {
			captureActivity = (Boolean) extras
					.get(App.ExtrasForPreferencesActivity);
		} catch (Exception e) {
			// Noth
		}

		// Hide languages option
		if (captureActivity) {
			languages.setEnabled(false);
		} else {
			languages.setEnabled(true);
		}

		languages.setDialogTitle(R.string.chooseLanguage);
		languages.setTitle(R.string.language);

		languages.setEntries(App.entries);
		languages.setEntryValues(App.strValues);

		if (App.lang.equals("en")) {
			languages.setValueIndex(0);
		} else if (App.lang.equals("el")) {
			languages.setValueIndex(1);
		}

		languages
				.setOnPreferenceChangeListener(new OnPreferenceChangeListener() {

					@Override
					public boolean onPreferenceChange(Preference preference,
					                                  Object newValue) {

						// Change language
						if (App.shouldChangeLocale((String) newValue)) {

							App.lang = (String) newValue;

							// Change locale
							App.updateLanguage(getApplicationContext());

							// Show notification and close activity
							Toast.makeText(getApplicationContext(),
									R.string.changeLanguageSuccessMsg,
									Toast.LENGTH_LONG).show();
							PreferencesActivity.this.finish();

						}
						return false;

					}
				});

		preferenceClearCache
				.setOnPreferenceClickListener(new OnPreferenceClickListener() {

					@Override
					public boolean onPreferenceClick(Preference preference) {

						AlertDialog.Builder alert = new AlertDialog.Builder(
								PreferencesActivity.this);
						alert.setTitle(R.string.clearCacheQuestion);
						alert.setMessage(R.string.clearCacheQuestionInfo);

						alert.setIcon(R.drawable.ic_menu_close_clear_cancel);

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
										//Clear Images cache
										App.imageLoader.clearCache();


										Toast.makeText(getApplicationContext(),
												getString(R.string.clearCacheOkayMsg),
												Toast.LENGTH_SHORT).show();
									}
								});

						alert.show();


						return false;
					}
				});

		// decode1D = (CheckBoxPreference)
		// preferences.findPreference(KEY_DECODE_1D);
		// decodeQR = (CheckBoxPreference)
		// preferences.findPreference(KEY_DECODE_QR);
		// decodeDataMatrix = (CheckBoxPreference)
		// preferences.findPreference(KEY_DECODE_DATA_MATRIX);

		// disableLastCheckedPref();
	}

	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
	                                      String key) {
		disableLastCheckedPref();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
			case android.R.id.home:
				PreferencesActivity.this.finish();

		}
		return true;
	}


	private void disableLastCheckedPref() {
		// Collection<CheckBoxPreference> checked = new
		// ArrayList<CheckBoxPreference>(
		// 3);
		// if (decode1D.isChecked()) {
		// checked.add(decode1D);
		// }
		// if (decodeQR.isChecked()) {
		// checked.add(decodeQR);
		// }
		// if (decodeDataMatrix.isChecked()) {
		// checked.add(decodeDataMatrix);
		// }
		// boolean disable = checked.size() < 2;
		// CheckBoxPreference[] checkBoxPreferences = {decode1D, decodeQR,
		// decodeDataMatrix};
		// for (CheckBoxPreference pref : checkBoxPreferences) {
		// pref.setEnabled(!(disable && checked.contains(pref)));
		// }
	}

}
