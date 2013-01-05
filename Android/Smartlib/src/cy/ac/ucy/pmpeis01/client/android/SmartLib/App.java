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

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Locale;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.preference.PreferenceManager;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.util.TypedValue;
import android.widget.TextView;

import com.actionbarsherlock.view.Menu;

import cy.ac.ucy.pmpeis01.client.android.R;
import cy.ac.ucy.pmpeis01.client.android.Cache.ImageLoader;

public class App extends Application {

	static final String TAG = App.class.getSimpleName();

	// ////////////////////// DEFINES
	public static final String ExtrasForPreferencesActivity = "extras_for_preferencesActivity";
	public static final String ExtrasForCaptureActivity = "extras_for_editCaptureActivity";
	public static final String ExtrasForEditBookActivity = "extras_for_editBookActivity";

	public static final String ExtrasForEditBookActivityFromBookSearch = "extras_for_editBookActivityFromBookSearc";

	public static final String ExtrasForSendMessage_DestinationUser = "extras_For_Send_Message_DestinationUser";

	public static final String ExtrasForRequestBookActivityFromActivitiesActivity = "extras_for_RequestBookActivityFromActivitiesActivity";

	public static final String ExtrasForWatchBookActivityFromBookSearch = "extras_for_watchBookActivityFromBookSearc";

	public static final String ExtrasForLentBookActivityISBN = "extras_for_lent_book_activityISBN";

	public static final String ExtrasForMyBooksActivityISBN = "extras_for_my_books_activityISBN";

	public static final int INSERT_BOOK_SUCCESSFULL = 1;

	public static final int INSERT_BOOK_ALREADY_EXISTED = 0;

	public static final int INSERT_BOOK_DATABASE_INTERNAL_ERROR = -11;

	public static final int INSERT_BOOK_DIDNT_EXIST_IN_GOOGLE_API = -2;

	public static final int INSERT_BOOK_WEIRD_ERROR = -12;

	public static final int INSERT_BOOK_NO_INTERNET = -4;

	public static final int INSERT_BOOK_PARSING_FAILED = -6;

	public static final int BOOK_STATE_USER_RENTED = 1;

	public static final int BOOK_STATE_USER_AVAILABLE = 0;
	public static final int SEND_MAIL_USER_DONT_ACCEPTS = 0;

	public static final int BOOK_STATE_USER_NO_RENTAL = -1;

	public static final int BOOK_STATE_USER_OTHER = -2;

	public static final int BOOK_STATE_USER_DONT_OWNS = -3;

	public static final int BOOK_STATE_WRONG_STATUS = -3;

	public static final int BOOKS_OF_USER_NO_BOOKS = 0;

	public final static int REGISTER_SUCCESSFULL = 1;

	public final static int REGISTER_NOT_SUCCESSFULL = 0;

	public final static int REGISTER_PARSING_FAILED = -2;

	public final static int REGISTER_NO_INTERNET = -3;

	public final static int BORROW_SUCCESSFULL = 1;

	public final static int BORROW_CANT_LEND_YOURSELF = 0;

	public final static int BORROW_NOT_FOUND_USER_DESTINATION = -1;

	public final static int RETURN_SUCCESSFULL = 1;

	public final static int GENERAL_SUCCESSFULL = 1;

	public final static int GENERAL_WEIRD_ERROR = -12;

	public final static int GENERAL_DATABASE_ERROR = -11;

	public final static int MAKE_REQUESTS_USER_DONT_ACCEPTS = 0;

	public final static int MAKE_REQUESTS_USER_ALREADY_REQUESTED = -1;

	public final static int REQUESTS_ANSWER_POSITIVE = 1;

	public final static int REQUESTS_ANSWER_NEGATIVE = 0;

	public final static int REQUESTS_ANSWER_DINT_ANSWER_YET = -1;

	// private static final int MENU_SHARE_ID = Menu.FIRST;

	public final static int MENU_GLOBAL_SETTINGS = Menu.FIRST;

	public static final int MENU_SCANNED_BOOKS_ID = Menu.FIRST + 1;

	public static final int MENU_ADD_BOOKS_ID = Menu.FIRST + 2;

	public final static int MENU_LIBRARY_SETTINGS = Menu.FIRST + 3;

	public final static int MENU_CLEAR = Menu.FIRST + 4;

	public final static int MENU_REGISTER = Menu.FIRST + 5;

	public final static int MENU_MANUAL_INSERTION = Menu.FIRST + 6;

	public static final int MENU_MY_BOOKS_BOOK_SELECTED = Menu.FIRST + 7;

	public static final int MENU_SEARCH_SEARCH_BOOKS = Menu.FIRST + 9;

	public static final int MENU_START_ACTIVITY_REFRESH = Menu.FIRST + 10;

	public final static int DELAY_ONE_SEC = 1000;

	public final static int DELAY_TWO_SEC = 2000;

	// private static final int MENU_SETTINGS_ID = Menu.FIRST + 2;

	// private static final int MENU_HELP_ID = Menu.FIRST + 3;

	// private static final int MENU_ABOUT_ID = Menu.FIRST + 4;

	public Library library = null;

	public static String lang;

	boolean registerSuccess = false;

	DeviceType deviceType = DeviceType.NotSpecified;

	int librarySelectedOnList = -1;

	User user = null;

	/** User who tries to register to a SmartLib */
	User registerUser = null;

	public static boolean refreshLang = false;

	public static final String DEVICE_ANDROID = "android";

	public static final int GENERAL_NO_INTERNET = -20;

	// public static final int MENU_SEARCH_BOOKS = 0;
	// SUMMER REMOVED
	// Access history within all application
	// public static HistoryManager historyManager;

	public static ImageLoader imageLoader;

	public Book selectedBook;

//	public Bitmap loginLogoBitmap;
	
	public Drawable loginLogoDrawable;

	private static final String MASTER_URL = "http://www.cs.ucy.ac.cy/projects/smartLib";
	private static final String getLibrariesPath = "/MASTER/getLibraries.php";

	public static boolean torchState = false;

	public static String[] entries = new String[2];
	public static String[] strValues = new String[] { "en", "el" };

	// entries[0] = new String(getString(R.string.english));
	// entries[1] = new String(getString(R.string.greek));
	// strValues[0] = new String("en");
	// strValues[1] = new String("el");

	public enum CaptureMode {
		Insert, LentReturn, SmartMode, notSet

	}

	@Override
	public void onCreate() {
		super.onCreate();

		// Save language preferences
		App.lang = Locale.getDefault().getISO3Language().substring(0, 2);

		updateLanguage(this);

		entries[0] = new String(getString(R.string.english));
		entries[1] = new String(getString(R.string.greek));

	}

	public static void updateLanguage(Context ctx) {

		// Change locale
		Locale locale = new Locale(App.lang);
		Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		ctx.getResources().updateConfiguration(config,
		ctx.getResources().getDisplayMetrics());

	}

	@Override
	public void onConfigurationChanged(Configuration newConfig) {

		super.onConfigurationChanged(newConfig);
		updateLanguage(this);
	}

	/**
	 * Find out if we should change locale
	 * 
	 * @param newLang
	 */
	public static boolean shouldChangeLocale(String newLang) {
		// Get default locale
		if (!App.lang.equals(newLang)) {

			refreshLang = true;
		} else {
			refreshLang = false;
		}

		return refreshLang;

	}

	/**
	 * @param link
	 *            of the PHP Script
	 * @param values
	 *            to build the PHP Post (eg. username=abc)
	 * @return JSON Object with the result
	 */
	public static String executePHPScript(String link,
			ArrayList<NameValuePair> values) {

		String result = null;
		InputStream inputStream = null;

		try {
			// HttpClient will execute the Post
			HttpClient httpclient = new DefaultHttpClient();

			// Make new HTTP Post @link
			HttpPost httppost = new HttpPost(link);

			// Put variables on the HTTP Post
			httppost.setEntity(new UrlEncodedFormEntity(values));

			// Execute & get the response
			HttpResponse response = httpclient.execute(httppost);

			// Save response in InputStream
			HttpEntity entity = response.getEntity();
			inputStream = entity.getContent();

			// Convert response
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "iso-8859-1"), 8);

			StringBuilder stringBuilder = new StringBuilder();
			String line = null;

			while ((line = reader.readLine()) != null) {
				stringBuilder.append(line + "\n");
			}
			// Close Stream
			inputStream.close();

			result = stringBuilder.toString();

		} catch (Exception e1) {
			Log.e(TAG, "Error in PHP Execution " + e1.toString());
			return null;
		}
		// Return JSON Obj
		return result;
	}

	public String getUsername() {
		return this.user.username;
	}

	public String getLibrary_InsertBookByISBN_URL() {
		return this.library.getInsertBookByISBN_URL();
	}

	public String getLibrary_stateOfBook_URL() {
		return this.library.getStateOfBook_URL();
	}

	public String getLibrary_lentABook_URL() {
		return this.library.getLentABook_URL();
	}

	public String getLibrary_returnABook_URL() {
		return this.library.getReturnABook_URL();
	}

	public String getLibrary_getUserBooks_URL() {
		return this.library.getUserBooks_URL();
	}

	public String getLibrary_getChangeStatusOfBook_URL() {
		return this.library.getChangeStatusOfBook_URL();
	}

	public String getLibraryEmail() {
		return this.library.email;
	}

	public String getLibrary_getDeleteABook_URL() {
		return this.library.getDeleteABook_URL();
	}

	public String getLibrary_getSearch_URL() {
		return this.library.getSearch_URL();
	}

	public String getLibrary_getBooksIGave_URL() {
		return this.library.getBooksIGave_URL();
	}

	public String getLibrary_getBooksITook() {
		return this.library.getBooksITook_URL();
	}

	public String getLibrary_getIncomingRequests_URL() {
		return this.library.getIncomingRequests_URL();
	}

	public String getLibrary_getOutgoingRequests_URL() {
		return this.library.getOutgoingRequests_URL();
	}

	public String getLibrary_getRequestABook_URL() {
		return this.library.getRequestABook_URL();
	}

	public String getLibrary_getReplyToBookRequest_URL() {
		return this.library.getReplyToBookRequest_URL();
	}

	public String getLibrary_getdeleteABookRequest_URL() {
		return this.library.getdeleteABookRequest_URL();
	}

	public String getLibrary_getPopularBooks_URL() {
		return this.library.getpopularBooks_URL();
	}

	public String getLibrary_getSendMessage_URL() {
		return this.library.getSendMessage_URL();
	}

	String getLibraries() {
		return MASTER_URL + getLibrariesPath;
	}

	public enum DeviceType {
		Regular, Large, XLarge, NotSpecified, NotAvailable
	}

	public static void setStyleErrorDirection(TextView pTextView) {
		pTextView.setTextColor(Color.parseColor("#C2022C"));
		pTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 25);

	}

	public static void setStyleDirection(TextView pTextView) {
		pTextView.setTextColor(Color.WHITE);
		pTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);

	}

	public static void setStyleErrorDirectionColor(TextView pTextView) {
		pTextView.setTextColor(Color.parseColor("#C2022C"));

	}

	public static void setStyleSuccessDirectionColor(TextView pTextView) {
		pTextView.setTextColor(Color.parseColor("#ff33b5e5"));
	}

	/**
	 * @param status
	 */
	public static String getBookStatusString(int status, Context c) {
		switch (status) {
		case App.BOOK_STATE_USER_RENTED:
			return c.getString(R.string.rented);
		case App.BOOK_STATE_USER_AVAILABLE:
			return c.getString(R.string.available);
		case App.BOOK_STATE_USER_NO_RENTAL:
			return c.getString(R.string.dontLent);

		case App.BOOK_STATE_USER_OTHER:
			return c.getString(R.string.otherDontLent);
		case App.BOOK_STATE_USER_DONT_OWNS:
			return c.getString(R.string.dontOwn);
		default:
			return c.getString(R.string.wrongStatusCode);

		}

	}

	public static int getBookStatusCode(String status, Context c) {

		if (status.equals(c.getString(R.string.rented))) {
			return App.BOOK_STATE_USER_RENTED;
		} else if (status.equals(c.getString(R.string.available))) {
			return App.BOOK_STATE_USER_AVAILABLE;
		} else if (status.equals(c.getString(R.string.dontLent))) {
			return App.BOOK_STATE_USER_NO_RENTAL;
		}

		else if (status.equals(c.getString(R.string.otherDontLent))) {
			return App.BOOK_STATE_USER_OTHER;
		}

		else if (status.equals(c.getString(R.string.dontOwn))) {
			return App.BOOK_STATE_USER_DONT_OWNS;
		}

		else {
			return App.BOOK_STATE_WRONG_STATUS;

		}

	}

	/**
	 * 
	 * @param pTimestamp
	 * @return
	 */
	public static String makeTimeStampHumanReadble(Context context,
			String pTimestamp) {

		try {

			SimpleDateFormat datetimeFormatter1 = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			java.util.Date lFromDate1 = datetimeFormatter1.parse(pTimestamp);

			CharSequence humansTime = DateUtils.getRelativeTimeSpanString(
					context, lFromDate1.getTime());

			return humansTime.toString();
		}

		catch (Exception e) {
			return pTimestamp;
		}

	}

}
