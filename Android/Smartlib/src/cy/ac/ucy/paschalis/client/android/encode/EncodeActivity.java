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


package cy.ac.ucy.paschalis.client.android.encode;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;
import cy.ac.ucy.paschalis.WriterException;
import cy.ac.ucy.paschalis.client.android.FinishListener;
import cy.ac.ucy.paschalis.client.android.Intents;
import cy.ac.ucy.paschalis.client.android.R;

/**
 * This class encodes data from an Intent into a QR code, and then displays it full screen so that
 * another person can scan it with their device.
 *
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class EncodeActivity extends Activity {

  private static final String TAG = EncodeActivity.class.getSimpleName();

  private static final int SHARE_MENU = Menu.FIRST;
  private static final int ENCODE_FORMAT_MENU = Menu.FIRST + 1;
  private static final int MAX_BARCODE_FILENAME_LENGTH = 24;
  private static final Pattern NOT_ALPHANUMERIC = Pattern.compile("[^A-Za-z0-9]");
  private static final String USE_VCARD_KEY = "USE_VCARD";

  private QRCodeEncoder qrCodeEncoder;

  @Override
  public void onCreate(Bundle icicle) {
    super.onCreate(icicle);
    Intent intent = getIntent();
    if (intent == null) {
      finish();
    } else {
      String action = intent.getAction();
      if (Intents.Encode.ACTION.equals(action) || Intent.ACTION_SEND.equals(action)) {
        setContentView(R.layout.encode);
      } else {
        finish();
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    super.onCreateOptionsMenu(menu);
    menu.add(Menu.NONE, SHARE_MENU, Menu.NONE, R.string.menu_share).setIcon(android.R.drawable.ic_menu_share);
    boolean useVcard = qrCodeEncoder != null && qrCodeEncoder.isUseVCard();
    int encodeNameResource = useVcard ? R.string.menu_encode_mecard : R.string.menu_encode_vcard;
    menu.add(Menu.NONE, ENCODE_FORMAT_MENU, Menu.NONE, encodeNameResource)
        .setIcon(android.R.drawable.ic_menu_sort_alphabetically);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case SHARE_MENU:
        share();
        return true;
      case ENCODE_FORMAT_MENU:
        Intent intent = getIntent();
        intent.putExtra(USE_VCARD_KEY, !qrCodeEncoder.isUseVCard());
        startActivity(getIntent());
        finish();
        return true;
      default:
        return false;
    }
  }
  
  private void share() {
    QRCodeEncoder encoder = qrCodeEncoder;
    if (encoder == null) { // Odd
      Log.w(TAG, "No existing barcode to send?");
      return;
    }

    String contents = encoder.getContents();
    if (contents == null) {
      Log.w(TAG, "No existing barcode to send?");
      return;
    }

    Bitmap bitmap;
    try {
      bitmap = encoder.encodeAsBitmap();
    } catch (WriterException we) {
      Log.w(TAG, we);
      return;
    }
    if (bitmap == null) {
      return;
    }

    File bsRoot = new File(Environment.getExternalStorageDirectory(), "BarcodeScanner");
    File barcodesRoot = new File(bsRoot, "Barcodes");
    if (!barcodesRoot.exists() && !barcodesRoot.mkdirs()) {
      Log.w(TAG, "Couldn't make dir " + barcodesRoot);
      showErrorMessage(R.string.msg_unmount_usb);
      return;
    }
    File barcodeFile = new File(barcodesRoot, makeBarcodeFileName(contents) + ".png");
    barcodeFile.delete();
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(barcodeFile);
      bitmap.compress(Bitmap.CompressFormat.PNG, 0, fos);
    } catch (FileNotFoundException fnfe) {
      Log.w(TAG, "Couldn't access file " + barcodeFile + " due to " + fnfe);
      showErrorMessage(R.string.msg_unmount_usb);
      return;
    } finally {
      if (fos != null) {
        try {
          fos.close();
        } catch (IOException ioe) {
          // do nothing
        }
      }
    }

    Intent intent = new Intent(Intent.ACTION_SEND, Uri.parse("mailto:"));
    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name) + " - " + encoder.getTitle());
    intent.putExtra(Intent.EXTRA_TEXT, contents);
    intent.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + barcodeFile.getAbsolutePath()));
    intent.setType("image/png");
    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
    startActivity(Intent.createChooser(intent, null));
  }

  private static CharSequence makeBarcodeFileName(CharSequence contents) {
    String fileName = NOT_ALPHANUMERIC.matcher(contents).replaceAll("_");
    if (fileName.length() > MAX_BARCODE_FILENAME_LENGTH) {
      fileName = fileName.substring(0, MAX_BARCODE_FILENAME_LENGTH);
    }
    return fileName;
  }

  @Override
  protected void onResume() {
    super.onResume();
    // This assumes the view is full screen, which is a good assumption
    WindowManager manager = (WindowManager) getSystemService(WINDOW_SERVICE);
    Display display = manager.getDefaultDisplay();
    int width = display.getWidth();
    int height = display.getHeight();
    int smallerDimension = width < height ? width : height;
    smallerDimension = smallerDimension * 7 / 8;

    Intent intent = getIntent();
    if (intent == null) {
      return;
    }

    try {
      boolean useVCard = intent.getBooleanExtra(USE_VCARD_KEY, false);
      qrCodeEncoder = new QRCodeEncoder(this, intent, smallerDimension, useVCard);
      Bitmap bitmap = qrCodeEncoder.encodeAsBitmap();
      if (bitmap == null) {
        Log.w(TAG, "Could not encode barcode");
        showErrorMessage(R.string.msg_encode_contents_failed);
        qrCodeEncoder = null;
        return;
      }

      ImageView view = (ImageView) findViewById(R.id.image_view);
      view.setImageBitmap(bitmap);

      TextView contents = (TextView) findViewById(R.id.contents_text_view);
      if (intent.getBooleanExtra(Intents.Encode.SHOW_CONTENTS, true)) {
        contents.setText(qrCodeEncoder.getDisplayContents());
        setTitle(getString(R.string.app_name) + " - " + qrCodeEncoder.getTitle());
      } else {
        contents.setText("");
        setTitle(getString(R.string.app_name));
      }
    } catch (WriterException e) {
      Log.w(TAG, "Could not encode barcode", e);
      showErrorMessage(R.string.msg_encode_contents_failed);
      qrCodeEncoder = null;
    }
  }

  private void showErrorMessage(int message) {
    AlertDialog.Builder builder = new AlertDialog.Builder(this);
    builder.setMessage(message);
    builder.setPositiveButton(R.string.button_ok, new FinishListener(this));
    builder.setOnCancelListener(new FinishListener(this));
    builder.show();
  }
}
