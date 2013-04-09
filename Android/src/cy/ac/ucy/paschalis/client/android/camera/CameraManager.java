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



import java.io.IOException;

import android.content.Context;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Handler;
import android.util.Log;
import android.view.SurfaceHolder;
import cy.ac.ucy.paschalis.client.android.PlanarYUVLuminanceSource;
import cy.ac.ucy.paschalis.client.android.SmartLib.App;





/**
 * This object wraps the Camera service object and expects to be the only one
 * talking to it. The implementation encapsulates the steps needed to take
 * preview-sized images, which are used for both preview and decoding.
 * 
 * @author dswitkin@google.com (Daniel Switkin)
 */
public final class CameraManager {

	private static final String				TAG				= CameraManager.class
																.getSimpleName();

	private static final int					MIN_FRAME_WIDTH	= 240;

	private static final int					MIN_FRAME_HEIGHT	= 240;

	private static final int					MAX_FRAME_WIDTH	= 600;

	private static final int					MAX_FRAME_HEIGHT	= 400;

	private final Context					context;

	private final CameraConfigurationManager	configManager;

	private static Camera							camera;

	private AutoFocusManager					autoFocusManager;

	private Rect							framingRect;

	private Rect							framingRectInPreview;

	private boolean						initialized;

	private boolean						previewing;

	private int							requestedFramingRectWidth;

	private int							requestedFramingRectHeight;

	/**
	 * Preview frames are delivered here, which we pass on to the registered
	 * handler. Make sure to clear the handler so it will only receive one
	 * message.
	 */
	private final PreviewCallback				previewCallback;





	public CameraManager(Context context) {
		this.context = context;
		this.configManager = new CameraConfigurationManager(context,camera);
		previewCallback = new PreviewCallback(configManager);
	}





	/**
	 * Opens the camera driver and initializes the hardware parameters.
	 * 
	 * @param holder
	 *             The surface object which the camera will draw preview frames
	 *             into.
	 * @throws IOException
	 *              Indicates the camera driver failed to open.
	 */
	public synchronized void openDriver(SurfaceHolder holder)
			throws IOException {
		Camera theCamera = camera;
		if (theCamera == null){
			theCamera = Camera.open();
			if (theCamera == null){ throw new IOException(); }
			camera = theCamera;
		}
		theCamera.setPreviewDisplay(holder);

		if (!initialized){
			initialized = true;
			configManager.initFromCameraParameters(theCamera);
			if (requestedFramingRectWidth > 0
					&& requestedFramingRectHeight > 0){
				setManualFramingRect(requestedFramingRectWidth,
						requestedFramingRectHeight);
				requestedFramingRectWidth = 0;
				requestedFramingRectHeight = 0;
			}
		}
		configManager.setDesiredCameraParameters(theCamera);
	}





	public synchronized boolean isOpen() {
		return camera != null;
	}





	/**
	 * Closes the camera driver if still in use.
	 */
	public synchronized void closeDriver() {
		if (camera != null){
			camera.release();
			camera = null;
			// Make sure to clear these each time we close the camera, so
			// that any scanning rect
			// requested by intent is forgotten.
			framingRect = null;
			framingRectInPreview = null;
		}
	}





	/**
	 * Asks the camera hardware to begin drawing preview frames to the screen.
	 */
	public synchronized void startPreview() {
		Camera theCamera = camera;
		if (theCamera != null && !previewing){
			theCamera.startPreview();
			previewing = true;
			autoFocusManager = new AutoFocusManager(context, camera);
		}
	}





	/**
	 * Tells the camera to stop drawing preview frames.
	 */
	public synchronized void stopPreview() {
		if (autoFocusManager != null){
			autoFocusManager.stop();
			autoFocusManager = null;
		}
		if (camera != null && previewing){
			camera.stopPreview();
			previewCallback.setHandler(null, 0);
			previewing = false;
		}
	}





	/**
	 * Convenience method for
	 * {@link cy.ac.ucy.paschalis.client.android.CaptureActivity}
	 */
	public synchronized void setTorch(boolean newSetting) {

		if (camera != null){
			if (autoFocusManager != null){
				autoFocusManager.stop();
			}
			
			
			 

			 configManager.setTorch(camera, newSetting);


			if (autoFocusManager != null){
				autoFocusManager.start();
			}
		}

		App.torchState = newSetting;
	}
	





	/**
	 * A single preview frame will be returned to the handler supplied. The
	 * data will arrive as byte[] in the message.obj field, with width and
	 * height encoded as message.arg1 and message.arg2, respectively.
	 * 
	 * @param handler
	 *             The handler to send the message to.
	 * @param message
	 *             The what field of the message to be sent.
	 */
	public synchronized void requestPreviewFrame(Handler handler, int message) {
		Camera theCamera = camera;
		if (theCamera != null && previewing){
			previewCallback.setHandler(handler, message);
			theCamera.setOneShotPreviewCallback(previewCallback);
		}
	}





	/**
	 * Calculates the framing rect which the UI should draw to show the user
	 * where to place the barcode. This target helps with alignment as well as
	 * forces the user to hold the device far enough away to ensure the image
	 * will be in focus.
	 * 
	 * @return The rectangle to draw on screen in window coordinates.
	 */
	public synchronized Rect getFramingRect() {
		if (framingRect == null){
			if (camera == null){ return null; }
			Point screenResolution = configManager.getScreenResolution();
			if (screenResolution == null){
				// Called early, before init even finished
				return null;
			}
			int width = screenResolution.x * 3 / 4;
			if (width < MIN_FRAME_WIDTH){
				width = MIN_FRAME_WIDTH;
			}
			else if (width > MAX_FRAME_WIDTH){
				width = MAX_FRAME_WIDTH;
			}
			int height = screenResolution.y * 3 / 4;
			if (height < MIN_FRAME_HEIGHT){
				height = MIN_FRAME_HEIGHT;
			}
			else if (height > MAX_FRAME_HEIGHT){
				height = MAX_FRAME_HEIGHT;
			}
			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) / 2;
			framingRect = new Rect(leftOffset, topOffset,
					leftOffset + width, topOffset + height);
			Log.d(TAG, "Calculated framing rect: " + framingRect);
		}
		return framingRect;
	}





	/**
	 * Like {@link #getFramingRect} but coordinates are in terms of the preview
	 * frame, not UI / screen.
	 */
	public synchronized Rect getFramingRectInPreview() {
		if (framingRectInPreview == null){
			Rect framingRect = getFramingRect();
			if (framingRect == null){ return null; }
			Rect rect = new Rect(framingRect);
			Point cameraResolution = configManager.getCameraResolution();
			Point screenResolution = configManager.getScreenResolution();
			if (cameraResolution == null || screenResolution == null){
				// Called early, before init even finished
				return null;
			}
			rect.left = rect.left * cameraResolution.x / screenResolution.x;
			rect.right = rect.right * cameraResolution.x
					/ screenResolution.x;
			rect.top = rect.top * cameraResolution.y / screenResolution.y;
			rect.bottom = rect.bottom * cameraResolution.y
					/ screenResolution.y;
			framingRectInPreview = rect;
		}
		return framingRectInPreview;
	}





	/**
	 * Allows third party apps to specify the scanning rectangle dimensions,
	 * rather than determine them automatically based on screen resolution.
	 * 
	 * @param width
	 *             The width in pixels to scan.
	 * @param height
	 *             The height in pixels to scan.
	 */
	public synchronized void setManualFramingRect(int width, int height) {
		if (initialized){
			Point screenResolution = configManager.getScreenResolution();
			if (width > screenResolution.x){
				width = screenResolution.x;
			}
			if (height > screenResolution.y){
				height = screenResolution.y;
			}
			int leftOffset = (screenResolution.x - width) / 2;
			int topOffset = (screenResolution.y - height) / 2;
			framingRect = new Rect(leftOffset, topOffset,
					leftOffset + width, topOffset + height);
			Log.d(TAG, "Calculated manual framing rect: " + framingRect);
			framingRectInPreview = null;
		}
		else{
			requestedFramingRectWidth = width;
			requestedFramingRectHeight = height;
		}
	}





	/**
	 * A factory method to build the appropriate LuminanceSource object based
	 * on the format of the preview buffers, as described by Camera.Parameters.
	 * 
	 * @param data
	 *             A preview frame.
	 * @param width
	 *             The width of the image.
	 * @param height
	 *             The height of the image.
	 * @return A PlanarYUVLuminanceSource instance.
	 */
	public PlanarYUVLuminanceSource buildLuminanceSource(byte[] data,
			int width, int height) {
		Rect rect = getFramingRectInPreview();
		if (rect == null){ return null; }
		// Go ahead and assume it's YUV rather than die.
		return new PlanarYUVLuminanceSource(data, width, height, rect.left,
				rect.top, rect.width(), rect.height(), false);
	}

}
