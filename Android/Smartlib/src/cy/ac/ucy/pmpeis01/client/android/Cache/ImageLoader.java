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

package cy.ac.ucy.pmpeis01.client.android.Cache;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.actionbarsherlock.app.ActionBar;

import cy.ac.ucy.pmpeis01.client.android.R;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.widget.ImageView;

public class ImageLoader {

	MemoryCache memoryCache = new MemoryCache();
	FileCache fileCache;
	private Map<ImageView, String> imageViews = Collections
			.synchronizedMap(new WeakHashMap<ImageView, String>());
	ExecutorService executorService;

	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
	}

	final int stub_id = R.drawable.nocover;
	
	
	
	public void DisplayActionBarIcon(String url, Context ctx,
			ActionBar actionBar) {
//		imageViews.put(actionBar, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			actionBar.setIcon(new BitmapDrawable(
					ctx.getResources(),bitmap));
		
		
		else {
			queuePhoto(url, ctx, actionBar);
			actionBar.setIcon(R.drawable.ic_launcher);
		}
	}

	
	
	/**Queue photos for ImageViews
	 * @param url
	 * @param ctx
	 * @param actionBar
	 */
	private void queuePhoto(String url, Context ctx, ActionBar actionBar) {
			PhotoToLoadABS pABS = new PhotoToLoadABS(url, actionBar,ctx);
			executorService.submit(new PhotosLoaderABS(pABS));
		}



	public void DisplayImage(String url, ImageView imageView) {
		imageViews.put(imageView, url);
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null)
			imageView.setImageBitmap(bitmap);
		else {
			queuePhoto(url, imageView);
			imageView.setImageResource(stub_id);
		}
	}

	/**Queue photos for ImageViews
	 * @param url
	 * @param imageView
	 */
	private void queuePhoto(String url, ImageView imageView) {
		PhotoToLoad p = new PhotoToLoad(url, imageView);
		executorService.submit(new PhotosLoader(p));
	}

	private Bitmap getBitmap(String url) {
		File f = fileCache.getFile(url);

		// from SD cache
		Bitmap b = decodeFile(f);
		if (b != null)
			return b;

		// from web
		try {
			Bitmap bitmap = null;
			URL imageUrl = new URL(url);
			HttpURLConnection conn = (HttpURLConnection) imageUrl
					.openConnection();
			conn.setConnectTimeout(30000);
			conn.setReadTimeout(30000);
			conn.setInstanceFollowRedirects(true);
			InputStream is = conn.getInputStream();
			OutputStream os = new FileOutputStream(f);
			Utils.CopyStream(is, os);
			os.close();
			bitmap = decodeFile(f);
			return bitmap;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try {
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true) {
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE)
					break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null, o2);
		} catch (FileNotFoundException e) {
		}
		return null;
	}

	// Task for the queue
	private class PhotoToLoad {
		public String url;
		public ImageView imageView;

		public PhotoToLoad(String u, ImageView i) {
			url = u;
			imageView = i;
		}
	}
	
	
	private class PhotoToLoadABS {
		public String url;
		public ActionBar actionBar;
		public Context ctx;

		public PhotoToLoadABS(String u, ActionBar a, Context ctx) {
			url = u;
			actionBar = a;
			this.ctx=ctx;
		}
	}
	
	

	/**Used for ImageViews
	 * @author paschalis
	 *
	 */
	class PhotosLoader implements Runnable {
		PhotoToLoad photoToLoad;

		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}

		@Override
		public void run() {
			if (imageViewReused(photoToLoad))
				return;
			Bitmap bmp = getBitmap(photoToLoad.url);
			memoryCache.put(photoToLoad.url, bmp);
			if (imageViewReused(photoToLoad))
				return;
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoad);
			Activity a = (Activity) photoToLoad.imageView.getContext();
			a.runOnUiThread(bd);
		}
	}
	
	
	/**Used for ActionBar Icons
	 * @author paschalis
	 *
	 */
	class PhotosLoaderABS implements Runnable {
		PhotoToLoadABS photoToLoadABS;

		PhotosLoaderABS(PhotoToLoadABS photoToLoadABS) {
			this.photoToLoadABS = photoToLoadABS;
		}

		@Override
		public void run() {
			Bitmap bmp = getBitmap(photoToLoadABS.url);
			memoryCache.put(photoToLoadABS.url, bmp);
			BitmapDisplayer bd = new BitmapDisplayer(bmp, photoToLoadABS);
			Activity a = (Activity) photoToLoadABS.ctx;
			a.runOnUiThread(bd);
		}
	}

	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.imageView);
		if (tag == null || !tag.equals(photoToLoad.url))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {
		Bitmap bitmap;
		PhotoToLoad photoToLoad;
		PhotoToLoadABS photoToLoadABS;

		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
		}

		public BitmapDisplayer(Bitmap bmp, PhotoToLoadABS photoToLoadABS) {
			bitmap=bmp;
			this.photoToLoadABS=photoToLoadABS;
		}

		public void run() {
			//Using image for Action Bar
			if(this.photoToLoad==null){
				if (bitmap != null)
					photoToLoadABS.actionBar.setIcon(new BitmapDrawable(
							photoToLoadABS.ctx.getResources(),bitmap));
				else
					photoToLoadABS.actionBar.setIcon(R.drawable.ic_launcher);
			}
			//Using for regular Image View
			else
			{
				if (imageViewReused(photoToLoad))
					return;
				if (bitmap != null)
					photoToLoad.imageView.setImageBitmap(bitmap);
				else
					photoToLoad.imageView.setImageResource(stub_id);
			}
			
		}
	}

	/**
	 * Clears all caches
	 */
	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
		//TODO CHECK IF text caches are cleared
	}
	
	/**
	 * @return caches directory
	 */
	public String getCacheDirectory(){
		return fileCache.cacheDir.toString();
	}

}
