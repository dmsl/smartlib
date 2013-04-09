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

package cy.ac.ucy.paschalis.client.android.camera;



import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Point;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Build;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;
import cy.ac.ucy.paschalis.client.android.PreferencesActivity;
import cy.ac.ucy.paschalis.client.android.SmartLib.App;





/**
 * A class which deals with reading, parsing, and setting the camera parameters
 * which are used to configure the camera hardware.
 */
final class CameraConfigurationManager {

	private static final String	TAG				= "CameraConfiguration";

	// This is bigger than the size of a small screen, which is still
	// supported. The routine
	// below will still select the default (presumably 320x240) size for these.
	// This prevents
	// accidental selection of very low resolution on some devices.
	private static final int		MIN_PREVIEW_PIXELS	= 470 * 320;			// normal
																// screen

	private static final int		MAX_PREVIEW_PIXELS	= 960 * 720;			// uses
																// full
																// screen
																// resolution
																// on
																// all
																// 'large'
																// screens

	private final Context		context;

	private Point				screenResolution;

	private Point				cameraResolution;

	private static Camera		camera;





	CameraConfigurationManager(Context context, Camera c) {
		this.context = context;
		camera = c;
	}





	/**
	 * Reads, one time, values from the camera that are needed by the app.
	 */
	void initFromCameraParameters(Camera camera) {
		Camera.Parameters parameters = camera.getParameters();
		WindowManager manager = (WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = manager.getDefaultDisplay();
		int width = display.getWidth();
		int height = display.getHeight();
		// We're landscape-only, and have apparently seen issues with display
		// thinking it's portrait
		// when waking from sleep. If it's not landscape, assume it's mistaken
		// and reverse them:
		if (width < height){
			Log.i(TAG,
					"Display reports portrait orientation; assuming this is incorrect");
			int temp = width;
			width = height;
			height = temp;
		}
		screenResolution = new Point(width, height);
		Log.i(TAG, "Screen resolution: " + screenResolution);
		cameraResolution = findBestPreviewSizeValue(parameters,
				screenResolution);
		Log.i(TAG, "Camera resolution: " + cameraResolution);
	}





	void setDesiredCameraParameters(Camera camera) {
		Camera.Parameters parameters = camera.getParameters();

		if (parameters == null){
			Log.w(TAG,
					"Device error: no camera parameters are available. Proceeding without configuration.");
			return;
		}

		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(context);

		initializeTorch(parameters, prefs);
		String focusMode = null;
		if (prefs.getBoolean(PreferencesActivity.KEY_AUTO_FOCUS, true)){
			focusMode = findSettableValue(
					parameters.getSupportedFocusModes(),
					"continuous-picture", // Camera.Paramters.FOCUS_MODE_CONTINUOUS_PICTURE
										// in 4.0+
					Camera.Parameters.FOCUS_MODE_AUTO);
		}
		// Maybe selected auto-focus but not available, so fall through here:
		if (focusMode == null){
			focusMode = findSettableValue(
					parameters.getSupportedFocusModes(),
					Camera.Parameters.FOCUS_MODE_MACRO, "edof"); // Camera.Parameters.FOCUS_MODE_EDOF
														// in 2.2+
		}
		if (focusMode != null){
			parameters.setFocusMode(focusMode);
		}

		parameters.setPreviewSize(cameraResolution.x, cameraResolution.y);
		camera.setParameters(parameters);
	}





	Point getCameraResolution() {
		return cameraResolution;
	}





	Point getScreenResolution() {
		return screenResolution;
	}





	void setTorch(Camera camera, boolean newSetting) {
		Camera.Parameters parameters = camera.getParameters();
		doSetTorch(parameters, newSetting);
		if (Build.VERSION.SDK_INT > 7) camera.setParameters(parameters);


	}





	private static void initializeTorch(Camera.Parameters parameters,
			SharedPreferences prefs) {
		// Restore stage to in App previous setting
		boolean currentSetting = App.torchState;
		doSetTorch(parameters, currentSetting);
	}





	private static void doSetTorch(Camera.Parameters parameters,
			boolean newSetting) {
		String flashMode = null;
		
		
		
		
		if (newSetting){

			if (Build.VERSION.SDK_INT <= 7){
				parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);
			}
			else{
				// Camera.Parameters.FLASH_MODE_ON
				flashMode = findSettableValue(
						parameters.getSupportedFlashModes(),
						Camera.Parameters.FLASH_MODE_TORCH,
						Camera.Parameters.FLASH_MODE_ON);
			}


		}
		else{
			if (Build.VERSION.SDK_INT <= 7){
				parameters.setFlashMode(Parameters.FLASH_MODE_OFF);
			}
			else{
				flashMode = findSettableValue(
						parameters.getSupportedFlashModes(),
						Camera.Parameters.FLASH_MODE_OFF);
			}
		}
		if (flashMode != null){
			if (Build.VERSION.SDK_INT <= 7){
				 camera.setParameters(parameters);
			}
			else{
				parameters.setFlashMode(flashMode);
			}
		}
	}





	private Point findBestPreviewSizeValue(Camera.Parameters parameters,
			Point screenResolution) {

		// Sort by size, descending
		List<Camera.Size> supportedPreviewSizes = new ArrayList<Camera.Size>(
				parameters.getSupportedPreviewSizes());
		Collections.sort(supportedPreviewSizes,
				new Comparator<Camera.Size>() {

					@Override
					public int compare(Camera.Size a, Camera.Size b) {
						int aPixels = a.height * a.width;
						int bPixels = b.height * b.width;
						if (bPixels < aPixels){ return -1; }
						if (bPixels > aPixels){ return 1; }
						return 0;
					}
				});

		if (Log.isLoggable(TAG, Log.INFO)){
			StringBuilder previewSizesString = new StringBuilder();
			for (Camera.Size supportedPreviewSize : supportedPreviewSizes){
				previewSizesString.append(supportedPreviewSize.width)
						.append('x').append(supportedPreviewSize.height)
						.append(' ');
			}
			Log.i(TAG, "Supported preview sizes: " + previewSizesString);
		}

		Point bestSize = null;
		float screenAspectRatio = (float) screenResolution.x
				/ (float) screenResolution.y;

		float diff = Float.POSITIVE_INFINITY;
		for (Camera.Size supportedPreviewSize : supportedPreviewSizes){
			int realWidth = supportedPreviewSize.width;
			int realHeight = supportedPreviewSize.height;
			int pixels = realWidth * realHeight;
			if (pixels < MIN_PREVIEW_PIXELS || pixels > MAX_PREVIEW_PIXELS){
				continue;
			}
			boolean isCandidatePortrait = realWidth < realHeight;
			int maybeFlippedWidth = isCandidatePortrait ? realHeight
					: realWidth;
			int maybeFlippedHeight = isCandidatePortrait ? realWidth
					: realHeight;
			if (maybeFlippedWidth == screenResolution.x
					&& maybeFlippedHeight == screenResolution.y){
				Point exactPoint = new Point(realWidth, realHeight);
				Log.i(TAG,
						"Found preview size exactly matching screen size: "
								+ exactPoint);
				return exactPoint;
			}
			float aspectRatio = (float) maybeFlippedWidth
					/ (float) maybeFlippedHeight;
			float newDiff = Math.abs(aspectRatio - screenAspectRatio);
			if (newDiff < diff){
				bestSize = new Point(realWidth, realHeight);
				diff = newDiff;
			}
		}

		if (bestSize == null){
			Camera.Size defaultSize = parameters.getPreviewSize();
			bestSize = new Point(defaultSize.width, defaultSize.height);
			Log.i(TAG, "No suitable preview sizes, using default: "
					+ bestSize);
		}

		Log.i(TAG, "Found best approximate preview size: " + bestSize);
		return bestSize;
	}





	private static String findSettableValue(
			Collection<String> supportedValues, String... desiredValues) {
		Log.i(TAG, "Supported values: " + supportedValues);
		String result = null;
		if (supportedValues != null){
			for (String desiredValue : desiredValues){
				if (supportedValues.contains(desiredValue)){
					result = desiredValue;
					break;
				}
			}
		}
		Log.i(TAG, "Settable value: " + result);
		return result;
	}

}
