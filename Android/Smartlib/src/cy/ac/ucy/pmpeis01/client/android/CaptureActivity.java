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

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.text.ClipboardManager;
import android.text.InputType;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuItem;

import cy.ac.ucy.pmpeis01.BarcodeFormat;
import cy.ac.ucy.pmpeis01.Result;
import cy.ac.ucy.pmpeis01.ResultMetadataType;
import cy.ac.ucy.pmpeis01.ResultPoint;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.App;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.App.CaptureMode;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.Book;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.LentBookActivity;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.LibPreferences;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.Library;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.MainActivity;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.MyBooksActivity;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.RegisterActivity;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.StartActivity;
import cy.ac.ucy.pmpeis01.client.android.SmartLib.LentBookActivity.DataClassLentABook;
import cy.ac.ucy.pmpeis01.client.android.camera.CameraManager;
import cy.ac.ucy.pmpeis01.client.android.history.HistoryActivity;
import cy.ac.ucy.pmpeis01.client.android.history.HistoryManager;
import cy.ac.ucy.pmpeis01.client.android.history.HistoryManager.BookToDB;
import cy.ac.ucy.pmpeis01.client.android.result.ResultButtonListener;
import cy.ac.ucy.pmpeis01.client.android.result.ResultHandler;
import cy.ac.ucy.pmpeis01.client.android.result.ResultHandlerFactory;

/**
 * This activity opens the camera and does the actual scanning on a background
 * thread. It draws a viewfinder to help the user place the barcode correctly,
 * shows feedback as the image processing is happening, and then overlays the
 * results when a scan is successful.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 * @author Sean Owen
 */
public final class CaptureActivity extends SherlockActivity implements
		SurfaceHolder.Callback {

	private static final String TAG = CaptureActivity.class.getSimpleName();

	private static final long DEFAULT_INTENT_RESULT_DURATION_MS = 1500L;

	private static final long NOT_A_BOOK_DELAY = 500L;

	private static final long BULK_MODE_SCAN_DELAY_MS = 1000L;

	private static final String PACKAGE_NAME = "cy.ac.ucy.pmpeis01.client.android";

	private static final String PRODUCT_SEARCH_URL_PREFIX = "http://www.google";

	private static final String PRODUCT_SEARCH_URL_SUFFIX = "/m/products/scan";

	private static final String[] ZXING_URLS = {
			"http://zxing.appspot.com/scan", "zxing://scan/" };

	private static final String RETURN_CODE_PLACEHOLDER = "{CODE}";

	private static final String RETURN_URL_PARAM = "ret";

	private static final String RAW_PARAM = "raw";

	public static final int HISTORY_REQUEST_CODE = 0x0000bacc;

	private static final Set<ResultMetadataType> DISPLAYABLE_METADATA_TYPES = EnumSet
			.of(ResultMetadataType.ISSUE_NUMBER,
					ResultMetadataType.SUGGESTED_PRICE,
					ResultMetadataType.ERROR_CORRECTION_LEVEL,
					ResultMetadataType.POSSIBLE_COUNTRY);

	private CameraManager cameraManager;

	private CaptureActivityHandler handler;

	private Result savedResultToShow;

	private ViewfinderView viewfinderView;

	// private TextView statusView;

	private View resultView;

	private Result lastResult;

	private boolean hasSurface;

	private boolean copyToClipboard;

	private boolean scanOnlyMode;

	private IntentSource source;

	private String sourceUrl;

	private String returnUrlTemplate;

	private boolean returnRaw;

	private Collection<BarcodeFormat> decodeFormats;

	private String characterSet;

	private String versionName;

	// private HistoryManager historyManager;

	private InactivityTimer inactivityTimer;

	private BeepManager beepManager;

	private ToggleButton toggleButtonCameraLight;

	private TextView textViewScanResults;

	private TextView textViewScannerDirection;

	private boolean firstBookAdded = true;

	App app;

	private CaptureMode captureMode = CaptureMode.notSet;

	private boolean submittedWithError = false;
	// SUMMER REMOVED
	// private boolean allSubmittedSuccesfully = false;

	private final DialogInterface.OnClickListener aboutListener = new DialogInterface.OnClickListener() {

		@Override
		public void onClick(DialogInterface dialogInterface, int i) {
			Intent intent = new Intent(Intent.ACTION_VIEW,
					Uri.parse(getString(R.string.zxing_url)));
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			startActivity(intent);
		}
	};

	ViewfinderView getViewfinderView() {
		return viewfinderView;
	}

	public Handler getHandler() {
		return handler;
	}

	CameraManager getCameraManager() {
		return cameraManager;
	}

	@Override
	public void onCreate(Bundle icicle) {
		super.onCreate(icicle);

		app = (App) getApplication();
		
		// Force change to chosen locale
		Locale locale = new Locale(App.lang); 
        Locale.setDefault(locale);
		Configuration config = new Configuration();
		config.locale = locale;
		getBaseContext().getResources()
				.updateConfiguration(
						config,
						getBaseContext().getResources()
								.getDisplayMetrics());
		

		getSupportActionBar().setDisplayHomeAsUpEnabled(true);

		// Get arguments, to determine who opened this activity
		final Bundle extras = getIntent().getExtras();

		try {
			captureMode = (CaptureMode) extras
					.get(App.ExtrasForCaptureActivity);
		} catch (Exception e) {
			this.finish();
		}

		Window window = getWindow();
		window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.capture);

		hasSurface = false;

		inactivityTimer = new InactivityTimer(this);
		beepManager = new BeepManager(this);

		toggleButtonCameraLight = (ToggleButton) findViewById(R.id.toggleButtonCameraLight);

		// If device have flashlight, show a button
		if (getApplicationContext().getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_CAMERA_FLASH)) {

			toggleButtonCameraLight.setVisibility(View.VISIBLE);

			toggleButtonCameraLight
					.setOnCheckedChangeListener(new OnCheckedChangeListener() {

						@Override
						public void onCheckedChanged(CompoundButton buttonView,
								boolean isChecked) {
							// Open Light
							if (isChecked) {
								cameraManager.setTorch(true);
							}
							// Close Light
							else {
								cameraManager.setTorch(false);
							}

						}
					});
		}

		textViewScanResults = (TextView) findViewById(R.id.textViewScanResults);
		textViewScannerDirection = (TextView) findViewById(R.id.textViewScannerInfo);

		App.setStyleDirection(textViewScannerDirection);

		PreferenceManager.setDefaultValues(this, R.xml.preferences, false);

		invalidateOptionsMenu();// Re-create the menu

		// TODO FUTURE showHelpOnFirstLaunch();
	}
	
	
	
	/**Refresh activity's language
	 * 
	 */
	private void refresh() {
		App.refreshLang=false;
	    finish();
	    Intent myIntent = new Intent(CaptureActivity.this, CaptureActivity.class);
	    startActivity(myIntent);
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

		// long previousScannedBooks = App.historyManager.historyItemsNumber();
		// SUMMER CHANGED
		// if (allSubmittedSuccesfully){
		// textViewScanResults
		// .setText(getString(R.string.allBooksSubmittedSuccessfully));
		// textViewScanResults.setVisibility(View.VISIBLE);
		// allSubmittedSuccesfully = false;
		// }
		// else if (submittedWithError){
		// textViewScanResults
		// .setText(getString(R.string.booksFailToSubmit) + ": "
		// + previousScannedBooks);
		// textViewScanResults.setVisibility(View.VISIBLE);
		// firstBookAdded = false;
		//
		// submittedWithError = false;
		//
		// }

		// We have scanned books from previous session
		// if (previousScannedBooks > 0){
		// textViewScanResults
		// .setText(getString(R.string.previouslyScannedBooks)
		// + ": " + previousScannedBooks);
		// textViewScanResults.setVisibility(View.VISIBLE);
		// firstBookAdded = false;
		//
		// }
		// else{
		// textViewScanResults.setText("");
		// firstBookAdded = true;
		// }

		// CameraManager must be initialized here, not in onCreate(). This is
		// necessary because we don't
		// want to open the camera driver and measure the screen size if we're
		// going to show the help on
		// first launch. That led to bugs where the scanning rectangle was the
		// wrong size and partially
		// off screen.
		cameraManager = new CameraManager(getApplication());

		viewfinderView = (ViewfinderView) findViewById(R.id.viewfinder_view);
		viewfinderView.setCameraManager(cameraManager);

		resultView = findViewById(R.id.result_view);

		handler = null;
		lastResult = null;

		resetStatusView();

		SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
		SurfaceHolder surfaceHolder = surfaceView.getHolder();
		if (hasSurface) {
			// The activity was paused but not stopped, so the surface still
			// exists. Therefore
			// surfaceCreated() won't be called, so init the camera here.
			initCamera(surfaceHolder);
		} else {
			// Install the callback and wait for surfaceCreated() to init the
			// camera.
			surfaceHolder.addCallback(this);
			surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		}

		beepManager.updatePrefs();

		inactivityTimer.onResume();

		Intent intent = getIntent();

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(this);

		copyToClipboard = prefs.getBoolean(
				PreferencesActivity.KEY_COPY_TO_CLIPBOARD, true)
				&& (intent == null || intent.getBooleanExtra(
						Intents.Scan.SAVE_HISTORY, true));

		scanOnlyMode = prefs.getBoolean(PreferencesActivity.KEY_SCAN_ONLY_MODE,
				false);

		source = IntentSource.NONE;
		decodeFormats = null;
		characterSet = null;

		if (intent != null) {

			String action = intent.getAction();
			String dataString = intent.getDataString();

			if (Intents.Scan.ACTION.equals(action)) {

				// Scan the formats the intent requested, and return the
				// result to the calling activity.
				source = IntentSource.NATIVE_APP_INTENT;
				decodeFormats = DecodeFormatManager.parseDecodeFormats(intent);

				if (intent.hasExtra(Intents.Scan.WIDTH)
						&& intent.hasExtra(Intents.Scan.HEIGHT)) {
					int width = intent.getIntExtra(Intents.Scan.WIDTH, 0);
					int height = intent.getIntExtra(Intents.Scan.HEIGHT, 0);
					if (width > 0 && height > 0) {
						cameraManager.setManualFramingRect(width, height);
					}
				}

				// String customPromptMessage = intent
				// .getStringExtra(Intents.Scan.PROMPT_MESSAGE);
				// if (customPromptMessage != null){
				// statusView.setText(customPromptMessage);
				// }

			} else if (dataString != null
					&& dataString.contains(PRODUCT_SEARCH_URL_PREFIX)
					&& dataString.contains(PRODUCT_SEARCH_URL_SUFFIX)) {

				// Scan only products and send the result to mobile Product
				// Search.
				source = IntentSource.PRODUCT_SEARCH_LINK;
				sourceUrl = dataString;
				decodeFormats = DecodeFormatManager.PRODUCT_FORMATS;

			} else if (isZXingURL(dataString)) {

				// Scan formats requested in query string (all formats if
				// none specified).
				// If a return URL is specified, send the results there.
				// Otherwise, handle it ourselves.
				source = IntentSource.ZXING_LINK;
				sourceUrl = dataString;
				Uri inputUri = Uri.parse(sourceUrl);
				returnUrlTemplate = inputUri
						.getQueryParameter(RETURN_URL_PARAM);
				returnRaw = inputUri.getQueryParameter(RAW_PARAM) != null;
				decodeFormats = DecodeFormatManager
						.parseDecodeFormats(inputUri);

			}

			characterSet = intent.getStringExtra(Intents.Scan.CHARACTER_SET);

		}
	}

	private static boolean isZXingURL(String dataString) {
		if (dataString == null) {
			return false;
		}
		for (String url : ZXING_URLS) {
			if (dataString.startsWith(url)) {
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onPause() {
		if (handler != null) {
			handler.quitSynchronously();
			handler = null;
		}
		inactivityTimer.onPause();
		cameraManager.closeDriver();
		if (!hasSurface) {
			SurfaceView surfaceView = (SurfaceView) findViewById(R.id.preview_view);
			SurfaceHolder surfaceHolder = surfaceView.getHolder();
			surfaceHolder.removeCallback(this);
		}

		super.onPause();
	}

	@Override
	protected void onDestroy() {
		inactivityTimer.shutdown();
		super.onDestroy();
	}

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch (keyCode) {

		case KeyEvent.KEYCODE_BACK:
			if (source == IntentSource.NATIVE_APP_INTENT) {
				setResult(RESULT_CANCELED);
				finish();
				return true;
			}
			if ((source == IntentSource.NONE || source == IntentSource.ZXING_LINK)
					&& lastResult != null) {
				restartPreviewAfterDelay(0L);
				return true;
			}
			break;
		case KeyEvent.KEYCODE_FOCUS:
		case KeyEvent.KEYCODE_CAMERA:
			// Handle these events so they don't launch the Camera app
			return true;

		}
		return super.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		// SUMMER REMOVED
		// menu.add(Menu.NONE, App.MENU_ADD_BOOKS_ID, Menu.NONE,
		// R.string.menu_add_books)
		// .setIcon(R.drawable.ic_menu_add)
		// .setShowAsActionFlags(
		// MenuItem.SHOW_AS_ACTION_WITH_TEXT
		// | MenuItem.SHOW_AS_ACTION_IF_ROOM);

		menu.add(Menu.NONE, App.MENU_MANUAL_INSERTION, Menu.NONE,
				R.string.menuManual)
				.setIcon(R.drawable.ic_menu_edit)
				.setShowAsActionFlags(
						MenuItem.SHOW_AS_ACTION_WITH_TEXT
								| MenuItem.SHOW_AS_ACTION_IF_ROOM);
//SUMMER REMOVED
//		menu.add(Menu.NONE, App.MENU_SCANNED_BOOKS_ID, Menu.NONE,
//				R.string.menuScannedBooks)
//				.setIcon(R.drawable.ic_menu_recent_history)
//				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		menu.add(Menu.NONE, App.MENU_GLOBAL_SETTINGS, Menu.NONE,
				R.string.menu_settings)
				.setIcon(R.drawable.ic_menu_settings_holo_light)
				.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		menu.add(Menu.NONE, App.MENU_LIBRARY_SETTINGS, Menu.NONE,
				app.library.name).setIcon(R.drawable.ic_menu_account_list)
				.setShowAsActionFlags(MenuItem.SHOW_AS_ACTION_WITH_TEXT);

		return true;
	}

	// Don't display the share menu item if the result overlay is showing.
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {

		// SUMMER REMOVED
		// if (App.historyManager.historyItemsNumber() > 0){
		// menu.findItem(App.MENU_ADD_BOOKS_ID).setVisible(true);
		// menu.findItem(App.MENU_SCANNED_BOOKS_ID).setVisible(true);
		// }
		// else{
		// menu.findItem(App.MENU_ADD_BOOKS_ID).setVisible(false);
		// menu.findItem(App.MENU_SCANNED_BOOKS_ID).setVisible(false);
		// }
		return true;
	}

	@Override
	public void onBackPressed() {
		super.onBackPressed();
	};

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent = new Intent(Intent.ACTION_VIEW);
		intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		case App.MENU_LIBRARY_SETTINGS: {
			Intent myIntent = new Intent(CaptureActivity.this,
					LibPreferences.class);
			CaptureActivity.this.startActivity(myIntent);

		}
			return true;

			// SUMMER CHANGED
			// case App.MENU_SCANNED_BOOKS_ID:{
			// Intent myIntent = new Intent(CaptureActivity.this,
			// HistoryActivity.class);
			// CaptureActivity.this.startActivity(myIntent);
			// }
			// break;

		case App.MENU_MANUAL_INSERTION: {

			AlertDialog.Builder alert = new AlertDialog.Builder(
					CaptureActivity.this);
			alert.setTitle(R.string.msgPleaseEnterISBNcode);

			final EditText editTextManualISBNCode = new EditText(
					CaptureActivity.this);

			editTextManualISBNCode.setInputType(InputType.TYPE_CLASS_NUMBER);

			alert.setView(editTextManualISBNCode);

			alert.setIcon(R.drawable.ic_menu_forward);

			alert.setNegativeButton(R.string.cancel, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {

				}
			});

			alert.setPositiveButton(R.string.yes, new OnClickListener() {

				@Override
				public void onClick(DialogInterface dialog, int which) {
					// Get data from EditBox
					String input = editTextManualISBNCode.getText().toString();
					// Check ISBN Code
					if (!(input.length() >= 9 && input.length() <= 13)) {
						Toast.makeText(CaptureActivity.this,
								R.string.msgNotValidISBN, Toast.LENGTH_SHORT)
								.show();
					} else {
						if (input.toString().replaceAll("\\d+", "").length() > 0) {
							Toast.makeText(CaptureActivity.this,
									R.string.msgNotValidISBN,
									Toast.LENGTH_SHORT).show();

						} else {
							DataClassFindUserInfo manualData = new DataClassFindUserInfo();

							Result newResult = new Result(input, null, null,
									null);

							manualData.ISBN = input;
							manualData.result = newResult;
							manualData.resultHandler = null;

							// TODO flash to db using PHP. also disable local db
							// here!
							// Save CHECK FOR RM!

							// SUMMER CHANGED
							// Find out if user already
							// owns this
							// book
							new AsyncTaskFindUsersRelationWithBook()
									.execute(manualData);

						}

					}

					restartPreviewAfterDelay(0);

				}
			});

			alert.show();

		}
			break;

		case App.MENU_GLOBAL_SETTINGS: {
			Intent myIntent = new Intent(CaptureActivity.this,
					PreferencesActivity.class);
			
			// Open Options in Capture Mode (Language options disabled)
			myIntent.putExtra(App.ExtrasForPreferencesActivity,
					true);

			CaptureActivity.this.startActivity(myIntent);
		}
			break;


		// SUMMER CHANGE
		// case App.MENU_ADD_BOOKS_ID:
		// //TODO make this AFTER EVERY SCAN!!
		// // Add books to database
		// new AsyncTaskInsertBooks().execute(App.historyManager
		// .getScanHistory());
		// break;
		//

		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	private void decodeOrStoreSavedBitmap(Bitmap bitmap, Result result) {
		// Bitmap isn't used yet -- will be used soon
		if (handler == null) {
			savedResultToShow = result;
		} else {
			if (result != null) {
				savedResultToShow = result;
			}
			if (savedResultToShow != null) {
				Message message = Message.obtain(handler,
						R.id.decode_succeeded, savedResultToShow);
				handler.sendMessage(message);
			}
			savedResultToShow = null;
		}
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if (holder == null) {
			Log.e(TAG,
					"*** WARNING *** surfaceCreated() gave us a null surface!");
		}
		if (!hasSurface) {
			hasSurface = true;
			initCamera(holder);
		}
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		hasSurface = false;
	}

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {

	}

	/**
	 * A valid barcode has been found, so give an indication of success and show
	 * the results.
	 * 
	 * @param rawResult
	 *            The contents of the barcode.
	 * @param barcode
	 *            A greyscale bitmap of the camera data which was decoded.
	 */
	public void handleDecode(Result rawResult, Bitmap barcode) {
		inactivityTimer.onActivity();
		lastResult = rawResult;
		ResultHandler resultHandler = ResultHandlerFactory.makeResultHandler(
				this, rawResult);

		// Find out if its a book
		String searchMe = resultHandler.toString();

		// Scanned ISBN Code
		if (searchMe.contains("ISBN")) {

			beepManager.playSuccessBookSoundAndVibrate();

			DataClassFindUserInfo data = new DataClassFindUserInfo();

			data.ISBN = rawResult.getText();
			data.result = rawResult;
			data.resultHandler = resultHandler;

			// Find out if user already has a relation with this book
			new AsyncTaskFindUsersRelationWithBook().execute(data);

		} else {
			beepManager.playNotBookSoundAndVibrate();

			textViewScannerDirection.setText(R.string.msgNotISBNCode);
			App.setStyleErrorDirection(textViewScannerDirection);

			restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);

		}

	}

	/**
	 * Superimpose a line for 1D or dots for 2D to highlight the key features of
	 * the barcode.
	 * 
	 * @param barcode
	 *            A bitmap of the captured image.
	 * @param rawResult
	 *            The decoded results which contains the points to draw.
	 */
	private void drawResultPoints(Bitmap barcode, Result rawResult) {
		ResultPoint[] points = rawResult.getResultPoints();
		if (points != null && points.length > 0) {
			Canvas canvas = new Canvas(barcode);
			Paint paint = new Paint();
			paint.setColor(getResources().getColor(R.color.result_image_border));
			paint.setStrokeWidth(3.0f);
			paint.setStyle(Paint.Style.STROKE);
			Rect border = new Rect(2, 2, barcode.getWidth() - 2,
					barcode.getHeight() - 2);
			canvas.drawRect(border, paint);

			paint.setColor(getResources().getColor(R.color.result_points));
			if (points.length == 2) {
				paint.setStrokeWidth(4.0f);
				drawLine(canvas, paint, points[0], points[1]);
			} else if (points.length == 4
					&& (rawResult.getBarcodeFormat() == BarcodeFormat.UPC_A || rawResult
							.getBarcodeFormat() == BarcodeFormat.EAN_13)) {
				// Hacky special case -- draw two lines, for the barcode and
				// metadata
				drawLine(canvas, paint, points[0], points[1]);
				drawLine(canvas, paint, points[2], points[3]);
			} else {
				paint.setStrokeWidth(10.0f);
				for (ResultPoint point : points) {
					canvas.drawPoint(point.getX(), point.getY(), paint);
				}
			}
		}
	}

	private static void drawLine(Canvas canvas, Paint paint, ResultPoint a,
			ResultPoint b) {
		canvas.drawLine(a.getX(), a.getY(), b.getX(), b.getY(), paint);
	}

	// Put up our own UI for how to handle the decoded contents.
	private void handleDecodeInternally(Result rawResult,
			ResultHandler resultHandler, Bitmap barcode) {
		// statusView.setVisibility(View.GONE);
		viewfinderView.setVisibility(View.GONE);
		resultView.setVisibility(View.VISIBLE);

		ImageView barcodeImageView = (ImageView) findViewById(R.id.barcode_image_view);
		if (barcode == null) {
			barcodeImageView.setImageBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.ic_launcher));
		} else {
			barcodeImageView.setImageBitmap(barcode);
		}

		TextView formatTextView = (TextView) findViewById(R.id.format_text_view);
		formatTextView.setText(rawResult.getBarcodeFormat().toString());

		TextView typeTextView = (TextView) findViewById(R.id.type_text_view);
		typeTextView.setText(resultHandler.getType().toString());

		DateFormat formatter = DateFormat.getDateTimeInstance(DateFormat.SHORT,
				DateFormat.SHORT);
		String formattedTime = formatter.format(new Date(rawResult
				.getTimestamp()));
		TextView timeTextView = (TextView) findViewById(R.id.time_text_view);
		timeTextView.setText(formattedTime);

		TextView metaTextView = (TextView) findViewById(R.id.meta_text_view);
		View metaTextViewLabel = findViewById(R.id.meta_text_view_label);
		metaTextView.setVisibility(View.GONE);
		metaTextViewLabel.setVisibility(View.GONE);
		Map<ResultMetadataType, Object> metadata = rawResult
				.getResultMetadata();
		if (metadata != null) {
			StringBuilder metadataText = new StringBuilder(20);
			for (Map.Entry<ResultMetadataType, Object> entry : metadata
					.entrySet()) {
				if (DISPLAYABLE_METADATA_TYPES.contains(entry.getKey())) {
					metadataText.append(entry.getValue()).append('\n');
				}
			}
			if (metadataText.length() > 0) {
				metadataText.setLength(metadataText.length() - 1);
				metaTextView.setText(metadataText);
				metaTextView.setVisibility(View.VISIBLE);
				metaTextViewLabel.setVisibility(View.VISIBLE);
			}
		}

		TextView contentsTextView = (TextView) findViewById(R.id.contents_text_view);
		CharSequence displayContents = resultHandler.getDisplayContents();
		contentsTextView.setText(displayContents);
		// Crudely scale betweeen 22 and 32 -- bigger font for shorter text
		int scaledSize = Math.max(22, 32 - displayContents.length() / 4);
		contentsTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, scaledSize);

		TextView supplementTextView = (TextView) findViewById(R.id.contents_supplement_text_view);
		supplementTextView.setText("");
		supplementTextView.setOnClickListener(null);

		int buttonCount = resultHandler.getButtonCount();
		ViewGroup buttonView = (ViewGroup) findViewById(R.id.result_button_view);
		buttonView.requestFocus();
		for (int x = 0; x < ResultHandler.MAX_BUTTON_COUNT; x++) {
			TextView button = (TextView) buttonView.getChildAt(x);
			if (x < buttonCount) {
				button.setVisibility(View.VISIBLE);
				button.setText(resultHandler.getButtonText(x));
				button.setOnClickListener(new ResultButtonListener(
						resultHandler, x));
			} else {
				button.setVisibility(View.GONE);
			}
		}

		if (copyToClipboard && !resultHandler.areContentsSecure()) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			clipboard.setText(displayContents);
		}
	}

	// Briefly show the contents of the barcode, then handle the result outside
	// Barcode Scanner.
	private void handleDecodeExternally(Result rawResult,
			ResultHandler resultHandler, Bitmap barcode) {

		if (barcode != null) {
			viewfinderView.drawResultBitmap(barcode);
		}

		// Since this message will only be shown for a second, just tell the
		// user what kind of
		// barcode was found (e.g. contact info) rather than the full
		// contents, which they won't
		// have time to read.
		// statusView.setText(getString(resultHandler.getDisplayTitle()));

		if (copyToClipboard && !resultHandler.areContentsSecure()) {
			ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);
			CharSequence text = resultHandler.getDisplayContents();
			if (text != null) {
				clipboard.setText(text);
			}
		}

		if (source == IntentSource.NATIVE_APP_INTENT) {

			// Hand back whatever action they requested - this can be changed
			// to Intents.Scan.ACTION when
			// the deprecated intent is retired.
			Intent intent = new Intent(getIntent().getAction());
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.putExtra(Intents.Scan.RESULT, rawResult.toString());
			intent.putExtra(Intents.Scan.RESULT_FORMAT, rawResult
					.getBarcodeFormat().toString());
			byte[] rawBytes = rawResult.getRawBytes();
			if (rawBytes != null && rawBytes.length > 0) {
				intent.putExtra(Intents.Scan.RESULT_BYTES, rawBytes);
			}
			Map<ResultMetadataType, ?> metadata = rawResult.getResultMetadata();
			if (metadata != null) {
				if (metadata.containsKey(ResultMetadataType.UPC_EAN_EXTENSION)) {
					intent.putExtra(Intents.Scan.RESULT_UPC_EAN_EXTENSION,
							metadata.get(ResultMetadataType.UPC_EAN_EXTENSION)
									.toString());
				}
				Integer orientation = (Integer) metadata
						.get(ResultMetadataType.ORIENTATION);
				if (orientation != null) {
					intent.putExtra(Intents.Scan.RESULT_ORIENTATION,
							orientation.intValue());
				}
				String ecLevel = (String) metadata
						.get(ResultMetadataType.ERROR_CORRECTION_LEVEL);
				if (ecLevel != null) {
					intent.putExtra(Intents.Scan.RESULT_ERROR_CORRECTION_LEVEL,
							ecLevel);
				}
				Iterable<byte[]> byteSegments = (Iterable<byte[]>) metadata
						.get(ResultMetadataType.BYTE_SEGMENTS);
				if (byteSegments != null) {
					int i = 0;
					for (byte[] byteSegment : byteSegments) {
						intent.putExtra(
								Intents.Scan.RESULT_BYTE_SEGMENTS_PREFIX + i,
								byteSegment);
						i++;
					}
				}
			}
			sendReplyMessage(R.id.return_scan_result, intent);

		} else if (source == IntentSource.PRODUCT_SEARCH_LINK) {

			// Reformulate the URL which triggered us into a query, so that
			// the request goes to the same
			// TLD as the scan URL.
			int end = sourceUrl.lastIndexOf("/scan");
			String replyURL = sourceUrl.substring(0, end) + "?q="
					+ resultHandler.getDisplayContents() + "&source=zxing";
			sendReplyMessage(R.id.launch_product_query, replyURL);

		} else if (source == IntentSource.ZXING_LINK) {

			// Replace each occurrence of RETURN_CODE_PLACEHOLDER in the
			// returnUrlTemplate
			// with the scanned code. This allows both queries and REST-style
			// URLs to work.
			if (returnUrlTemplate != null) {
				CharSequence codeReplacement = returnRaw ? rawResult.getText()
						: resultHandler.getDisplayContents();
				try {
					codeReplacement = URLEncoder.encode(
							codeReplacement.toString(), "UTF-8");
				} catch (UnsupportedEncodingException e) {
					// can't happen; UTF-8 is always supported. Continue, I
					// guess, without encoding
				}
				String replyURL = returnUrlTemplate.replace(
						RETURN_CODE_PLACEHOLDER, codeReplacement);
				sendReplyMessage(R.id.launch_product_query, replyURL);
			}

		}
	}

	private void sendReplyMessage(int id, Object arg) {
		Message message = Message.obtain(handler, id, arg);
		long resultDurationMS = getIntent().getLongExtra(
				Intents.Scan.RESULT_DISPLAY_DURATION_MS,
				DEFAULT_INTENT_RESULT_DURATION_MS);
		if (resultDurationMS > 0L) {
			handler.sendMessageDelayed(message, resultDurationMS);
		} else {
			handler.sendMessage(message);
		}
	}

	/**
	 * FUTURE TODO
	 * 
	 * We want the help screen to be shown automatically the first time a new
	 * version of the app is run. The easiest way to do this is to check
	 * android:versionCode from the manifest, and compare it to a value stored
	 * as a preference.
	 */
	// private boolean showHelpOnFirstLaunch() {
	// try{
	// PackageInfo info = getPackageManager().getPackageInfo(
	// PACKAGE_NAME, 0);
	// int currentVersion = info.versionCode;
	// // Since we're paying to talk to the PackageManager anyway, it
	// // makes sense to cache the app
	// // version name here for display in the about box later.
	// this.versionName = info.versionName;
	// SharedPreferences prefs = PreferenceManager
	// .getDefaultSharedPreferences(this);
	// int lastVersion = prefs.getInt(
	// PreferencesActivity.KEY_HELP_VERSION_SHOWN, 0);
	// if (currentVersion > lastVersion){
	// prefs.edit()
	// .putInt(PreferencesActivity.KEY_HELP_VERSION_SHOWN,
	// currentVersion).commit();
	// Intent intent = new Intent(this, HelpActivity.class);
	// intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
	// // Show the default page on a clean install, and the what's
	// // new page on an upgrade.
	// String page = lastVersion == 0 ? HelpActivity.DEFAULT_PAGE
	// : HelpActivity.WHATS_NEW_PAGE;
	// intent.putExtra(HelpActivity.REQUESTED_PAGE_KEY, page);
	// startActivity(intent);
	// return true;
	// }
	// }
	// catch (PackageManager.NameNotFoundException e){
	// Log.w(TAG, e);
	// }
	// return false;
	// }
	//

	private void initCamera(SurfaceHolder surfaceHolder) {
		if (surfaceHolder == null) {
			throw new IllegalStateException("No SurfaceHolder provided");
		}
		if (cameraManager.isOpen()) {
			Log.w(TAG,
					"initCamera() while already open -- late SurfaceView callback?");
			return;
		}
		try {
			cameraManager.openDriver(surfaceHolder);
			// Creating the handler starts the preview, which can also throw
			// a RuntimeException.
			if (handler == null) {
				handler = new CaptureActivityHandler(this, decodeFormats,
						characterSet, cameraManager);
			}
			decodeOrStoreSavedBitmap(null, null);
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			displayFrameworkBugMessageAndExit();
		} catch (RuntimeException e) {
			// Barcode Scanner has seen crashes in the wild of this variety:
			// java.?lang.?RuntimeException: Fail to connect to camera
			// service
			Log.w(TAG, "Unexpected error initializing camera", e);
			displayFrameworkBugMessageAndExit();
		}
	}

	private void displayFrameworkBugMessageAndExit() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle(getString(R.string.app_name));
		builder.setMessage(getString(R.string.msg_camera_framework_bug));
		builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
		builder.setOnCancelListener(new FinishListener(this));
		builder.show();
	}

	public void restartPreviewAfterDelay(long delayMS) {
		if (handler != null) {
			handler.sendEmptyMessageDelayed(R.id.restart_preview, delayMS);
			// Restore scanning message
			handler.postDelayed(new Runnable() {

				@Override
				public void run() {
					App.setStyleDirection(textViewScannerDirection);
					textViewScannerDirection
							.setText(R.string.msgScannerDefaultMessage);
				}
			}, delayMS);
		}

		resetStatusView();

	}

	private void resetStatusView() {
		resultView.setVisibility(View.GONE);

		// statusView.setText(R.string.msg_default_status);
		// statusView.setVisibility(View.VISIBLE);
		viewfinderView.setVisibility(View.VISIBLE);
		lastResult = null;
	}

	public void drawViewfinder() {
		viewfinderView.drawViewfinder();
	}

	/**
	 * Check if book is owned by user
	 * 
	 * @author paschalis
	 * 
	 */
	private class AsyncTaskFindUsersRelationWithBook extends
			AsyncTask<DataClassFindUserInfo, Integer, Integer> {

		DataClassFindUserInfo mData;

		@Override
		protected Integer doInBackground(DataClassFindUserInfo... data) {

			mData = data[0];

			int returnResult = App.BOOK_STATE_USER_DONT_OWNS;

			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters
					.add(new BasicNameValuePair("device", App.DEVICE_ANDROID));

			// Save Username And Password
			parameters
					.add(new BasicNameValuePair("username", app.getUsername()));

			parameters.add(new BasicNameValuePair("isbn", data[0].ISBN));

			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_stateOfBook_URL(), parameters);

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
			// SUMMER REMOVED
			// If scan only mode, ignore all cases
			// and assume user down owns the book
			// if (scanOnlyMode){
			// result = App.BOOK_STATE_USER_DONT_OWNS;
			// }

			switch (result) {

			case App.BOOK_STATE_USER_RENTED:

				// If we want to return a book
				if (captureMode.equals(CaptureMode.LentReturn)) {

					AlertDialog.Builder alert = new AlertDialog.Builder(
							CaptureActivity.this);
					alert.setTitle(R.string.msgYouAlreadyOwnThisBook);
					alert.setMessage(R.string.msgAndYouHaveRentedItDoYOu);

					alert.setIcon(R.drawable.ic_menu_back);

					alert.setNegativeButton(R.string.no, new OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// Restart scanner.
							restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
						}
					});

					alert.setPositiveButton(R.string.yes,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// Return book
									new AsyncTaskReturnABook()
											.execute(mData.ISBN);

								}
							});

					alert.show();

				}

				else if (captureMode.equals(CaptureMode.Insert)) {

					beepManager.playNotBookSoundAndVibrate();

					textViewScannerDirection
							.setText(R.string.msgBookOnwedAndRented);
					App.setStyleErrorDirection(textViewScannerDirection);

					restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);

				}

				break;

			case App.BOOK_STATE_USER_AVAILABLE:

				// Lent this book
				if (captureMode.equals(CaptureMode.LentReturn)) {

					AlertDialog.Builder alertOuter = new AlertDialog.Builder(
							CaptureActivity.this);
					alertOuter.setTitle(R.string.msgYouAlreadyOwnThisBook);
					alertOuter.setMessage(R.string.msgAndItIsAvailable);

					alertOuter.setIcon(R.drawable.ic_menu_back);

					alertOuter.setNegativeButton(R.string.no,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {
									// Restart scanner.
									restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
								}
							});

					alertOuter.setPositiveButton(R.string.yes,
							new OnClickListener() {

								@Override
								public void onClick(DialogInterface dialog,
										int which) {

									{
										// Send ISBN As Argument
										Intent i = new Intent(
												getApplicationContext(),
												LentBookActivity.class);
										i.putExtra(
												App.ExtrasForLentBookActivityISBN,
												mData.ISBN);
										CaptureActivity.this.startActivity(i);

										// Restart scanner.
										restartPreviewAfterDelay(0);
									}

								}
							});

					alertOuter.show();

				}

				else if (captureMode.equals(CaptureMode.Insert)) {

					beepManager.playNotBookSoundAndVibrate();

					textViewScannerDirection
							.setText(R.string.msgBookOnwedAndAvailable);
					App.setStyleErrorDirection(textViewScannerDirection);

					restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
				}

				break;

			case App.BOOK_STATE_USER_NO_RENTAL:
				beepManager.playNotBookSoundAndVibrate();

				textViewScannerDirection
						.setText(R.string.msgBookOnwedAndNotAvailable);
				App.setStyleErrorDirection(textViewScannerDirection);

				restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);

				// SUMMER REMOVED
				// {
				// AlertDialog.Builder alert = new AlertDialog.Builder(
				// CaptureActivity.this);
				// alert.setTitle(R.string.msgYouAlreadyOwnThisBook);
				// alert.setMessage(R.string.msgAndYouDontRentIt);
				//
				// alert.setIcon(R.drawable.ic_dialog_alert_holo_light);
				//
				// alert.setNegativeButton(R.string.no,
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(
				// DialogInterface dialog,
				// int which) {
				// // Restart scanner.
				// restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
				// }
				// });
				//
				// alert.setPositiveButton(R.string.yes,
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(
				// DialogInterface dialog,
				// int which) {
				// //Open edit book activity
				// // Send ISBN As Argument
				// Intent i = new Intent(
				// getApplicationContext(),
				// MyBooksActivity.class);
				// i.putExtra(
				// App.ExtrasForMyBooksActivityISBN,
				// mData.ISBN);
				// CaptureActivity.this
				// .startActivity(i);
				//
				// // Restart scanner.
				// restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
				// }
				// });
				//
				// alert.show();
				//
				// }

				break;
			case App.BOOK_STATE_USER_OTHER:
				beepManager.playNotBookSoundAndVibrate();

				textViewScannerDirection
						.setText(R.string.msgBookOnwedAndNotAvailable);
				App.setStyleErrorDirection(textViewScannerDirection);

				restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);

				// SUMMER REMOVED
				// {
				// AlertDialog.Builder alert = new AlertDialog.Builder(
				// CaptureActivity.this);
				// alert.setTitle(R.string.msgYouAlreadyOwnThisBook);
				// alert.setMessage(R.string.msgAndYouDontRentItOther);
				//
				// alert.setIcon(R.drawable.ic_dialog_alert_holo_light);
				//
				// alert.setNegativeButton(R.string.no,
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(
				// DialogInterface dialog,
				// int which) {
				// // Restart scanner.
				// restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
				//
				//
				// }
				// });
				//
				// alert.setPositiveButton(R.string.yes,
				// new OnClickListener() {
				//
				// @Override
				// public void onClick(
				// DialogInterface dialog,
				// int which) {
				// //Open edit book activity
				// // Send ISBN As Argument
				// Intent i = new Intent(
				// getApplicationContext(),
				// MyBooksActivity.class);
				// i.putExtra(
				// App.ExtrasForMyBooksActivityISBN,
				// mData.ISBN);
				// CaptureActivity.this
				// .startActivity(i);
				//
				// // Restart scanner.
				// restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
				// }
				// });
				//
				//
				//
				// alert.show();
				//
				// }

				break;
			case App.BOOK_STATE_USER_DONT_OWNS:

				//
				if (captureMode.equals(CaptureMode.Insert)) {
					// TODO INSERT TO LIBARRY PHP!
					new AsyncTaskInsertBooks().execute(mData.result.getText());

				}

				else if (captureMode.equals(CaptureMode.LentReturn)) {
					beepManager.playNotBookSoundAndVibrate();

					textViewScannerDirection.setText(R.string.msgBookDownOwn);
					App.setStyleErrorDirection(textViewScannerDirection);

					restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
				}

				// TODO find todo comment with inserting to db(add menu button),
				// and
				// run insertin here!!

				// SUMMER REMOVED
				// // Insert book to history, and get result
				// BookToDB dbResult = App.historyManager.addHistoryItem(
				// mData.result, mData.resultHandler);
				//
				// // Book Added
				// if (dbResult.added){
				// textViewScanResults
				// .setText(getString(R.string.scannedBooks)
				// + ": " + dbResult.booksSoFar);
				//
				// textViewScanResults.setVisibility(View.VISIBLE);
				//
				//
				// if (firstBookAdded){
				// invalidateOptionsMenu();
				// firstBookAdded = false;
				//
				// }
				//
				// }
				//
				// else{
				//
				// beepManager.playNotBookSoundAndVibrate();
				//
				//
				//
				// textViewScannerDirection
				// .setText(R.string.msgBookAlreadyScanned);
				// App.setStyleErrorDirection(textViewScannerDirection);
				//
				// }

				// Restart scanner.
				// restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);

				break;

			default:
				break;
			}

			// continue staff..
		}

	}

	/**
	 * Tries to Insert all books from Scan History to Database
	 * 
	 * @author paschalis
	 * 
	 */
	private class AsyncTaskInsertBooks extends
			AsyncTask<String, Integer, Integer> {

		// ProgressDialog progressDialog;

		// Save results for all ISBN Insert Tries
		// SUMMER REMOVED
		// public ArrayList<Integer> executionResults;
		// String[] isbnsAdded;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();

			// TODO PUT TEXT INSERTING BOOK to pers
			textViewScannerDirection
					.setText(getString(R.string.msgBookInsertingBook));

			// SUMMER REMOVED
			// executionResults = new ArrayList<Integer>();
			// progressDialog = new ProgressDialog(CaptureActivity.this);
			// progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
			// progressDialog.setTitle(R.string.insertBooksToDb);
			// progressDialog.setMessage(getString(R.string.pleaseWait));
			//
			// progressDialog.show();
		}

		protected Integer doInBackground(String... isbns) {

			// isbnsAdded = isbns;

			int returnResult = App.INSERT_BOOK_NO_INTERNET;

			// SUMMER REMOVED
			// int count = isbns.length;
			// progressDialog.setMax(count);
			// SUMMER CHANGED
			// for (int i = 0; i < count; i++) {
			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();

			// Say that we are mobile (Android Device)
			parameters
					.add(new BasicNameValuePair("device", App.DEVICE_ANDROID));

			// Save Username And Password, ISBN
			parameters
					.add(new BasicNameValuePair("username", app.getUsername()));

			// SUMMER CHANGED from i to 0
			parameters.add(new BasicNameValuePair("isbn", isbns[0]));

			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_InsertBookByISBN_URL(), parameters);

			// Parse Result (JSON Obj)
			if (resultStr != null) {
				try {
					// Create JSON Obj based on the result!
					JSONObject userData = new JSONObject(resultStr);

					returnResult = userData.getInt("result");

					// User Credencials match
					// SUMMER REMOVED
					// executionResults.add(returnResult);

				} catch (JSONException e) {
					Log.e(TAG, "Error parsing data " + e.toString());

					returnResult = App.INSERT_BOOK_PARSING_FAILED;
					// executionResults.add(returnResult);

				}

			} else {
				returnResult = App.INSERT_BOOK_NO_INTERNET;
				// executionResults.add(App.INSERT_BOOK_NO_INTERNET);
			}

			publishProgress(1);
			// }

			return returnResult;

		}

		// SUMMER REMOVED
		// protected void onProgressUpdate(Integer... progress) {
		// setProgress(progress[0]);
		//
		// // Update Dialog
		// progressDialog.incrementProgressBy(progress[0]);
		//
		// }

		protected void onPostExecute(Integer result) {
			// SUMMER REMOVED
			// Close Progress Dialog
			// progressDialog.dismiss();

			// Set text according to result
			switch (result) {
			case App.INSERT_BOOK_ALREADY_EXISTED:
				// SUMMER CHANGE all textViewScanResults to
				// textViewScannerDirection
				textViewScannerDirection
						.setText(getString(R.string.msgBookExistedInPersonalLibrary));
				App.setStyleErrorDirection(textViewScannerDirection);
				break;
			case App.INSERT_BOOK_DATABASE_INTERNAL_ERROR:
				textViewScannerDirection
						.setText(getString(R.string.msgInsertBookDatabaseInternalError));
				App.setStyleErrorDirection(textViewScannerDirection);
				break;
			case App.INSERT_BOOK_DIDNT_EXIST_IN_GOOGLE_API:
				textViewScannerDirection
						.setText(getString(R.string.msgInsertBookDontExistsInGAPI));
				App.setStyleErrorDirection(textViewScannerDirection);
				break;
			case App.INSERT_BOOK_NO_INTERNET:
				textViewScannerDirection
						.setText(getString(R.string.msgNoInternetConnectionTitle));
				App.setStyleErrorDirection(textViewScannerDirection);
				break;
			case App.INSERT_BOOK_PARSING_FAILED:
				textViewScannerDirection
						.setText(getString(R.string.msgErrorApplicationReport));
				App.setStyleErrorDirection(textViewScannerDirection);
				break;
			case App.INSERT_BOOK_SUCCESSFULL:
				textViewScannerDirection
						.setText(getString(R.string.msgBookInsertedSuccessfully));
				App.setStyleDirection(textViewScannerDirection);
				App.setStyleSuccessDirectionColor(textViewScannerDirection);
				break;
			case App.INSERT_BOOK_WEIRD_ERROR:
				textViewScannerDirection
						.setText(getString(R.string.msgErrorApplicationReport));
				App.setStyleErrorDirection(textViewScannerDirection);
				break;

			default:
				break;
			}

			// Restart Scanner - SUMMER ADDITION
			restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);

			// SUMMER CHANGED
			// // Clear all successfully added books
			// App.historyManager.clearSucessfullyAddedBooks(isbnsAdded,
			// executionResults);
			//
			// long failedBooks = App.historyManager.historyItemsNumber();
			//
			// textViewScanResults.setText("");
			//
			// // Some books failed to submit
			// if (failedBooks > 0) {
			// submittedWithError = true;
			//
			// textViewScanResults
			// .setText(getString(R.string.booksFailToSubmit) + ": "
			// + failedBooks);
			//
			// AlertDialog.Builder alert = new AlertDialog.Builder(
			// CaptureActivity.this);
			// alert.setTitle(R.string.executionFinished);
			// alert.setMessage(R.string.notAllBooksAddedToUserLib);
			//
			// alert.setIcon(R.drawable.ic_dialog_alert_holo_light);
			//
			// alert.setNegativeButton(R.string.no, new OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			//
			// }
			// });
			//
			// alert.setPositiveButton(R.string.yes, new OnClickListener() {
			//
			// @Override
			// public void onClick(DialogInterface dialog, int which) {
			// Intent myIntent = new Intent(CaptureActivity.this,
			// HistoryActivity.class);
			// CaptureActivity.this.startActivity(myIntent);
			//
			// }
			// });
			//
			// alert.show();
			//
			// }
			// // All books submitted okay!
			// else {
			// allSubmittedSuccesfully = true;
			//
			// textViewScanResults
			// .setText(getString(R.string.allBooksSubmittedSuccessfully));
			//
			// }

		}
	}

	/**
	 * Returns a Book
	 * 
	 * @author paschalis
	 * 
	 */
	private class AsyncTaskReturnABook extends
			AsyncTask<String, Integer, Integer> {

		@Override
		protected Integer doInBackground(String... isbn) {

			int returnResult = App.GENERAL_NO_INTERNET;

			ArrayList<NameValuePair> parameters = new ArrayList<NameValuePair>();
			// Say that we are mobile (Android Device)
			parameters
					.add(new BasicNameValuePair("device", App.DEVICE_ANDROID));

			// Save Username , ISBN, and Destination User
			parameters.add(new BasicNameValuePair("owner", app.getUsername()));
			parameters.add(new BasicNameValuePair("isbn", isbn[0]));

			// Execute PHP Script
			String resultStr = App.executePHPScript(
					app.getLibrary_returnABook_URL(), parameters);

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

			if (result == App.GENERAL_SUCCESSFULL) {

				Toast.makeText(CaptureActivity.this,
						R.string.msgBookSuccesfullyReturned, Toast.LENGTH_SHORT)
						.show();

			}

			else {

				Toast.makeText(
						CaptureActivity.this,
						getString(R.string.msgBookFailedToReturn) + ". "
								+ getString(R.string.msgPleaseContact) + ": "
								+ app.getLibraryEmail(), Toast.LENGTH_LONG)
						.show();

			}
			// Restart scanner
			restartPreviewAfterDelay(BULK_MODE_SCAN_DELAY_MS);
		}

	}

	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////
	// //////////////// Classes to encapsulate data
	// ///////////////////////////////////////////////////////////////////////////////
	// ///////////////////////////////////////////////////////////////////////////////

	/**
	 * 
	 * Class to send data to asJustas Bandza T. Sevcenkos 12-27 03223,
	 * Vilniusync task to find user's relation with book
	 * 
	 * @author paschalis
	 * 
	 */
	class DataClassFindUserInfo {

		ResultHandler resultHandler;

		Result result;

		String ISBN;
	}

}
