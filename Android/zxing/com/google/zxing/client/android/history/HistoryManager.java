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

package com.google.zxing.client.android.history;


import android.app.Activity;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import mp.paschalis.R;
import mp.paschalis.App;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.google.zxing.client.android.result.ResultHandler;


/**
 * <p>
 * Manages functionality related to scan history.
 * </p>
 *
 * @author Sean Owen
 */
public final class HistoryManager {

	private static final String TAG = HistoryManager.class
			.getSimpleName();

	private static final int MAX_ITEMS = 500;

	private static final String[] COLUMNS = {
			DBHelper.TEXT_COL, DBHelper.DISPLAY_COL, DBHelper.FORMAT_COL,
			DBHelper.TIMESTAMP_COL, DBHelper.EXECUTION_COL,};

	private static final String[] COUNT_COLUMN = {"COUNT(1)"};

	private static final String[] ID_COL_PROJECTION = {DBHelper.ID_COL};

	private static final String[] ID_DETAIL_COL_PROJECTION = {
			DBHelper.ID_COL, DBHelper.EXECUTION_COL};

	private static final DateFormat EXPORT_DATE_TIME_FORMAT = DateFormat
			.getDateTimeInstance(
					DateFormat.MEDIUM,
					DateFormat.MEDIUM);

	private final Activity activity;


	public HistoryManager(Activity activity) {
		this.activity = activity;
	}


	public boolean hasHistoryItems() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.query(DBHelper.TABLE_NAME, COUNT_COLUMN, null, null,
					null, null, null);
			cursor.moveToFirst();
			return cursor.getInt(0) > 0;
		} finally {
			close(cursor, db);
		}
	}


	public List<HistoryItem> buildHistoryItems() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		List<HistoryItem> items = new ArrayList<HistoryItem>();
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.query(DBHelper.TABLE_NAME, COLUMNS, null, null,
					null, null, DBHelper.TIMESTAMP_COL + " DESC");
			while (cursor.moveToNext()) {
				String text = cursor.getString(0);
				//String display = cursor.getString(1);
				//String format = cursor.getString(2);
				long timestamp = cursor.getLong(3);
				String errorCode = cursor.getString(4);

				Result result = new Result(text, null, null,
						BarcodeFormat.valueOf("EAN_13"), timestamp);
				items.add(new HistoryItem(result, "", errorCode));
			}
		} finally {
			close(cursor, db);
		}
		return items;
	}


	public HistoryItem buildHistoryItem(int number) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getReadableDatabase();
			cursor = db.query(DBHelper.TABLE_NAME, COLUMNS, null, null,
					null, null, DBHelper.TIMESTAMP_COL + " DESC");
			cursor.move(number + 1);
			String text = cursor.getString(0);
			String display = cursor.getString(1);
			String format = cursor.getString(2);
			long timestamp = cursor.getLong(3);
			// String details = cursor.getString(5);
			String errorCode = cursor.getString(4);
			Result result = new Result(text, null, null,
					BarcodeFormat.valueOf(format), timestamp);
			return new HistoryItem(result, display, errorCode);
		} finally {
			close(cursor, db);
		}
	}


	public void deleteHistoryItem(int number) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getWritableDatabase();
			cursor = db.query(DBHelper.TABLE_NAME, ID_COL_PROJECTION, null,
					null, null, null, DBHelper.TIMESTAMP_COL + " DESC");
			cursor.move(number + 1);
			db.delete(DBHelper.TABLE_NAME,
					DBHelper.ID_COL + '=' + cursor.getString(0), null);
		} finally {
			close(cursor, db);
		}
	}


	/**
	 * @return the number of items saved on history
	 */
	public long historyItemsNumber() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;

		try {
			db = helper.getWritableDatabase();

			return DatabaseUtils.queryNumEntries(db, DBHelper.TABLE_NAME);
			// Count the number of records in database

		} finally {
			close(null, db);
		}


	}


	/**
	 * @param result  true if item added, false if not
	 * @param handler
	 * @return
	 */
	public BookToDB addHistoryItem(Result result, ResultHandler handler) {
		BookToDB returnResult = new BookToDB();

		// Do not save this item to the history if the preference is turned
		// off, or the contents are
		// considered secure.
		// if
		// (!activity.getIntent().getBooleanExtra(Intents.Scan.SAVE_HISTORY,
		// true)
		// || handler.areContentsSecure()){ return false; }
		// ORIGINAL_CODE
		// SharedPreferences prefs =
		// PreferenceManager.getDefaultSharedPreferences(activity);
		// if (!prefs.getBoolean(PreferencesActivity.KEY_REMEMBER_DUPLICATES,
		// false)) {
		// deletePrevious(result.getText());
		// }

		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;

		try {
			db = helper.getWritableDatabase();


			// If is a new value, put it to database
			if (!hasPrevious(result.getText())) {
				ContentValues values = new ContentValues();
				values.put(DBHelper.TEXT_COL, result.getText());
//				values.put(DBHelper.FORMAT_COL, result.getBarcodeFormat()
//						.toString());
				//values.put(DBHelper.DISPLAY_COL, handler
				//	.getDisplayContents().toString());
				values.put(DBHelper.TIMESTAMP_COL,
						System.currentTimeMillis());


				// Insert the new entry into the DB.
				db.insert(DBHelper.TABLE_NAME, DBHelper.TIMESTAMP_COL,
						values);

				// Book added
				returnResult.added = true;


			}

			returnResult.booksSoFar = DatabaseUtils.queryNumEntries(db,
					DBHelper.TABLE_NAME);
			// Count the number of records in database

		} finally {
			close(null, db);
		}


		// Return number of records currently are in History Database
		return returnResult;
	}


	public void addHistoryItemDetails(String itemID, String itemDetails) {
		// As we're going to do an update only we don't need need to worry
		// about the preferences; if the item wasn't saved it won't be udpated
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getWritableDatabase();
			cursor = db.query(DBHelper.TABLE_NAME, ID_DETAIL_COL_PROJECTION,
					DBHelper.TEXT_COL + "=?", new String[]{itemID},
					null, null, DBHelper.TIMESTAMP_COL + " DESC", "1");
			String oldID = null;
			String oldDetails = null;
			if (cursor.moveToNext()) {
				oldID = cursor.getString(0);
				oldDetails = cursor.getString(1);
			}

			String newDetails = oldDetails == null ? itemDetails
					: oldDetails + " : " + itemDetails;
			ContentValues values = new ContentValues();
			// values.put(DBHelper.DETAILS_COL, newDetails);

			db.update(DBHelper.TABLE_NAME, values, DBHelper.ID_COL + "=?",
					new String[]{oldID});

		} finally {
			close(cursor, db);
		}
	}


	/**
	 * @param text
	 * @return true if a record already exists
	 */
	public String[] getScanHistory() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();


			Cursor cursor = db.query(DBHelper.TABLE_NAME,
					new String[]{DBHelper.TEXT_COL}, null, null, null,
					null, null);


			// rawQuery("select 1 from " + DBHelper.TABLE_NAME +
			// " where " + DBHelper.TEXT_COL + "=" + text,
			// new String[] { text });

			ArrayList<String> arrayListResults = new ArrayList<String>();

			cursor.moveToFirst();
			while (!cursor.isAfterLast()) {
				arrayListResults.add(cursor.getString(cursor
						.getColumnIndex(DBHelper.TEXT_COL)));
				cursor.moveToNext();
			}


			return arrayListResults.toArray(new String[arrayListResults
					.size()]);


		} finally {
			close(null, db);
		}


	}


	/**
	 * @param text
	 * @return true if a record already exists
	 */
	private boolean hasPrevious(String text) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();


			Cursor cursor = db.query(DBHelper.TABLE_NAME,
					new String[]{DBHelper.ID_COL}, DBHelper.TEXT_COL
					+ "=" + text, null, null, null, null);


			// rawQuery("select 1 from " + DBHelper.TABLE_NAME +
			// " where " + DBHelper.TEXT_COL + "=" + text,
			// new String[] { text });

			boolean exists = (cursor.getCount() > 0);
			cursor.close();

			// If ISBN Exists, leave only one entry
			if (exists) {
				return true;

			} else {
				return false;

			}


		} finally {
			close(null, db);
		}


	}


	private void deletePrevious(String text) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();


			Cursor cursor = db.query(DBHelper.TABLE_NAME,
					new String[]{DBHelper.ID_COL}, DBHelper.TEXT_COL
					+ "=" + text, null, null, null, null);


			// rawQuery("select 1 from " + DBHelper.TABLE_NAME +
			// " where " + DBHelper.TEXT_COL + "=" + text,
			// new String[] { text });

			boolean exists = (cursor.getCount() > 0);
			cursor.close();

			// If ISBN Exists, leave only one entry
			if (exists) {

				db.delete(DBHelper.TABLE_NAME, DBHelper.TEXT_COL + "=?",
						new String[]{text});

			}


		} finally {
			close(null, db);
		}
	}


	public void trimHistory() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getWritableDatabase();
			cursor = db.query(DBHelper.TABLE_NAME, ID_COL_PROJECTION, null,
					null, null, null, DBHelper.TIMESTAMP_COL + " DESC");
			cursor.move(MAX_ITEMS);
			while (cursor.moveToNext()) {
				db.delete(DBHelper.TABLE_NAME, DBHelper.ID_COL + '='
						+ cursor.getString(0), null);

			}
		} catch (SQLiteException sqle) {
			// We're seeing an error here when called in
			// CaptureActivity.onCreate() in rare cases
			// and don't understand it. First theory is that it's transient
			// so can be safely ignored.
			// TODO revisit this after live in a future version to see if it
			// 'worked'
			Log.w(TAG, sqle);
			// continue
		} finally {
			close(cursor, db);
		}
	}


	/**
	 * <p>
	 * Builds a text representation of the scanning history. Each scan is
	 * encoded on one line, terminated by a line break (\r\n). The values in
	 * each line are comma-separated, and double-quoted. Double-quotes within
	 * values are escaped with a sequence of two double-quotes. The fields
	 * output are:
	 * </p>
	 * <p/>
	 * <ul>
	 * <li>Raw text</li>
	 * <li>Display text</li>
	 * <li>Format (e.g. QR_CODE)</li>
	 * <li>Timestamp</li>
	 * <li>Formatted version of timestamp</li>
	 * </ul>
	 */
	CharSequence buildHistory() {
		StringBuilder historyText = new StringBuilder(1000);
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		Cursor cursor = null;
		try {
			db = helper.getWritableDatabase();
			cursor = db.query(DBHelper.TABLE_NAME, COLUMNS, null, null,
					null, null, DBHelper.TIMESTAMP_COL + " DESC");

			while (cursor.moveToNext()) {

				historyText.append('"')
						.append(massageHistoryField(cursor.getString(0)))
						.append("\",");
				historyText.append('"')
						.append(massageHistoryField(cursor.getString(1)))
						.append("\",");
				historyText.append('"')
						.append(massageHistoryField(cursor.getString(2)))
						.append("\",");
				historyText.append('"')
						.append(massageHistoryField(cursor.getString(3)))
						.append("\",");

				// Add timestamp again, formatted
				long timestamp = cursor.getLong(3);
				historyText
						.append('"')
						.append(massageHistoryField(EXPORT_DATE_TIME_FORMAT
								.format(new Date(timestamp))))
						.append("\",");

				// Above we're preserving the old ordering of columns which
				// had formatted data in position 5

				historyText.append('"')
						.append(massageHistoryField(cursor.getString(4)))
						.append("\"\r\n");
			}
			return historyText;
		} finally {
			close(cursor, db);
		}
	}


	/**
	 * Deletes the history
	 */
	void clearHistory() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.delete(DBHelper.TABLE_NAME, null, null);
		} finally {
			close(null, db);
		}
	}


	static Uri saveHistory(String history) {
		File bsRoot = new File(Environment.getExternalStorageDirectory(),
				"BarcodeScanner");
		File historyRoot = new File(bsRoot, "History");
		if (!historyRoot.exists() && !historyRoot.mkdirs()) {
			Log.w(TAG, "Couldn't make dir " + historyRoot);
			return null;
		}
		File historyFile = new File(historyRoot, "history-"
				+ System.currentTimeMillis() + ".csv");
		OutputStreamWriter out = null;
		try {
			out = new OutputStreamWriter(new FileOutputStream(historyFile),
					Charset.forName("UTF-8"));
			out.write(history);
			return Uri.parse("file://" + historyFile.getAbsolutePath());
		} catch (IOException ioe) {
			Log.w(TAG, "Couldn't access file " + historyFile + " due to "
					+ ioe);
			return null;
		} finally {
			if (out != null) {
				try {
					out.close();
				} catch (IOException ioe) {
					// do nothing
				}
			}
		}
	}


	private static String massageHistoryField(String value) {
		return value == null ? "" : value.replace("\"", "\"\"");
	}


	private static void close(Cursor cursor, SQLiteDatabase database) {
		if (cursor != null) {
			cursor.close();
		}
		if (database != null) {
			database.close();
		}
	}

	/**
	 * Encapsulates information about how many ISBN Books are into the History
	 * so far, and if the last scan was successfull or not
	 *
	 * @author paschalis
	 */
	public class BookToDB {

		public long booksSoFar = -1;

		public boolean added = false;


	}


	public void clearSucessfullyAddedBooks(String[] isbnsAdded,
	                                       ArrayList<Integer> executionResults) {

		int max = isbnsAdded.length > executionResults.size() ? isbnsAdded.length
				: executionResults.size();

		// Clear all ISBN's from history if they Sucessfully Added to db
		for (int i = 0; i < max; i++) {
			// If ISBN Successfully added
			if (executionResults.get(i) == 1) {
				// Remove it from Scan History
				removeISBNBook(isbnsAdded[i]);
			} else {
				updateBookWithErrorCode(isbnsAdded[i],
						executionResults.get(i));
			}
		}


	}


	/**
	 * @param isbn
	 * @param errorCode
	 */
	private void updateBookWithErrorCode(String isbn, int errorCode) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();

			ContentValues args = new ContentValues();

			args.put(DBHelper.EXECUTION_COL, getErrorMsgFromCode(errorCode));
			args.put(DBHelper.EXECUTION_CODE, errorCode);

			db.update(DBHelper.TABLE_NAME, args, DBHelper.TEXT_COL + "="
					+ isbn, null);

		} finally {
			close(null, db);
		}


	}


	private String getErrorMsgFromCode(int pErroCode) {


		if (pErroCode == App.INSERT_BOOK_ALREADY_EXISTED) return activity
				.getString(R.string.msgInsertBookAlreadyExists);

		else if (pErroCode == App.INSERT_BOOK_DATABASE_INTERNAL_ERROR) return activity
				.getString(R.string.msgInsertBookDatabaseInternalError);
		else if (pErroCode == App.INSERT_BOOK_DIDNT_EXIST_IN_GOOGLE_API) return activity
				.getString(R.string.msgInsertBookDontExistsInGAPI);
		else if (pErroCode == App.INSERT_BOOK_NO_INTERNET) return activity
				.getString(R.string.msgNoInternetConnectionTitle);
		else if (pErroCode == App.INSERT_BOOK_PARSING_FAILED) return activity
				.getString(R.string.msgInsertBookParseError);
		else if (pErroCode == App.INSERT_BOOK_WEIRD_ERROR)
			return activity.getString(R.string.msgInsertBookParseError);

		// everything is fine!
		return "";


	}


	/**
	 * Deletes a history item that has been successfully added to DB
	 *
	 * @param isbn Books ISBN
	 */
	private void removeISBNBook(String isbn) {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.delete(DBHelper.TABLE_NAME, DBHelper.TEXT_COL + "=" + isbn,
					null);
		} finally {
			close(null, db);
		}

	}


	/**
	 * Deletes a history item that has previously added to DB
	 *
	 * @param text Books ISBN
	 */
	void removeAlreadyAddedBooks() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.delete(DBHelper.TABLE_NAME, DBHelper.EXECUTION_CODE + "=" +
					App.INSERT_BOOK_ALREADY_EXISTED,
					null);
		} finally {
			close(null, db);
		}

	}


	/**
	 * Deletes a history item that has encountered erros during commiting
	 *
	 * @param text Books ISBN
	 */
	void removeErrorBooks() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;
		try {
			db = helper.getWritableDatabase();
			db.delete(DBHelper.TABLE_NAME, DBHelper.EXECUTION_CODE + "<" +
					App.INSERT_BOOK_ALREADY_EXISTED,
					null);
		} finally {
			close(null, db);
		}

	}


	/**
	 * @return true, if in Scan History, has books that were already saved to Books of User
	 */
	public boolean hasAlreadyAddedBooks() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;

		try {
			db = helper.getWritableDatabase();


			Cursor cursor = db.query(DBHelper.TABLE_NAME,
					new String[]{DBHelper.ID_COL}, DBHelper.EXECUTION_CODE
					+ "=" + App.INSERT_BOOK_ALREADY_EXISTED, null, null, null, null);


			// rawQuery("select 1 from " + DBHelper.TABLE_NAME +
			// " where " + DBHelper.TEXT_COL + "=" + text,
			// new String[] { text });

			boolean exists = (cursor.getCount() > 0);
			cursor.close();

			// If ISBN Exists, leave only one entry
			if (exists) {
				return true;

			} else {
				return false;

			}


		} finally {
			close(null, db);
		}

	}


	/**
	 * @return true, if in Scan History, has books that have errors
	 */
	public boolean hasErrorBooks() {
		SQLiteOpenHelper helper = new DBHelper(activity);
		SQLiteDatabase db = null;

		try {
			db = helper.getWritableDatabase();


			Cursor cursor = db.query(DBHelper.TABLE_NAME,
					new String[]{DBHelper.ID_COL}, DBHelper.EXECUTION_CODE
					+ "<" + App.INSERT_BOOK_ALREADY_EXISTED, null, null, null, null);


			// rawQuery("select 1 from " + DBHelper.TABLE_NAME +
			// " where " + DBHelper.TEXT_COL + "=" + text,
			// new String[] { text });

			boolean exists = (cursor.getCount() > 0);
			cursor.close();

			// If ISBN Exists, leave only one entry
			if (exists) {
				return true;

			} else {
				return false;

			}


		} finally {
			close(null, db);
		}

	}


}
