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

package cy.ac.ucy.pmpeis01.client.android;



import java.util.ArrayList;
import java.util.Collection;

import android.content.SharedPreferences;
import android.content.SharedPreferences.OnSharedPreferenceChangeListener;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceScreen;
import cy.ac.ucy.paschalis.client.android.R;





/**
 * The main settings activity.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class PreferencesActivity extends PreferenceActivity implements
		OnSharedPreferenceChangeListener {

	// public static final String KEY_DECODE_1D = "preferences_decode_1D";
	// public static final String KEY_DECODE_QR = "preferences_decode_QR";
	// public static final String KEY_DECODE_DATA_MATRIX =
	// "preferences_decode_Data_Matrix";
	// public static final String KEY_CUSTOM_PRODUCT_SEARCH =
	// "preferences_custom_product_search";

	public static final String	KEY_PLAY_BEEP			= "preferences_play_beep";

	public static final String	KEY_VIBRATE			= "preferences_vibrate";

	public static final String	KEY_COPY_TO_CLIPBOARD	= "preferences_copy_to_clipboard";

	public static final String	KEY_SCAN_ONLY_MODE		= "preferences_scan_only_mode";

	// public static final String KEY_FRONT_LIGHT = "preferences_front_light";
	// //public static final String KEY_BULK_MODE = "preferences_bulk_mode";
	// public static final String KEY_REMEMBER_DUPLICATES =
	// "preferences_remember_duplicates";
	// public static final String KEY_SUPPLEMENTAL =
	// "preferences_supplemental";
	public static final String	KEY_AUTO_FOCUS			= "preferences_auto_focus";





	// public static final String KEY_SEARCH_COUNTRY =
	// "preferences_search_country";

	// public static final String KEY_HELP_VERSION_SHOWN =
	// "preferences_help_version_shown";

	// private CheckBoxPreference decode1D;
	// private CheckBoxPreference decodeQR;
	// private CheckBoxPreference decodeDataMatrix;

	@Override
	protected void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		addPreferencesFromResource(R.xml.preferences);

		PreferenceScreen preferences = getPreferenceScreen();
		preferences.getSharedPreferences()
				.registerOnSharedPreferenceChangeListener(this);
		// decode1D = (CheckBoxPreference)
		// preferences.findPreference(KEY_DECODE_1D);
		// decodeQR = (CheckBoxPreference)
		// preferences.findPreference(KEY_DECODE_QR);
		// decodeDataMatrix = (CheckBoxPreference)
		// preferences.findPreference(KEY_DECODE_DATA_MATRIX);
		disableLastCheckedPref();
	}





	@Override
	public void onSharedPreferenceChanged(SharedPreferences sharedPreferences,
			String key) {
		disableLastCheckedPref();
	}





	private void disableLastCheckedPref() {
		Collection<CheckBoxPreference> checked = new ArrayList<CheckBoxPreference>(
				3);
		// if (decode1D.isChecked()) {
		// checked.add(decode1D);
		// }
		// if (decodeQR.isChecked()) {
		// checked.add(decodeQR);
		// }
		// if (decodeDataMatrix.isChecked()) {
		// checked.add(decodeDataMatrix);
		// }
		//boolean disable = checked.size() < 2;
		// CheckBoxPreference[] checkBoxPreferences = {decode1D, decodeQR,
		// decodeDataMatrix};
		// for (CheckBoxPreference pref : checkBoxPreferences) {
		// pref.setEnabled(!(disable && checked.contains(pref)));
		// }
	}

}
