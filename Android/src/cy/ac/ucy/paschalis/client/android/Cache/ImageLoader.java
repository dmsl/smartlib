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

package cy.ac.ucy.paschalis.client.android.Cache;



import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Collections;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;

import cy.ac.ucy.paschalis.client.android.R;
import cy.ac.ucy.paschalis.client.android.Cache.ImageLoader.DataClassDisplayBookCover.DataClassBitmapGot;
import cy.ac.ucy.paschalis.client.android.SmartLib.App;
import cy.ac.ucy.paschalis.client.android.SmartLib.Book;





public class ImageLoader {

	private static String			TAG			= ImageLoader.class
													.getSimpleName();

	MemoryCache					memoryCache	= new MemoryCache();

	FileCache						fileCache;

	private Map<ImageView, String>	imageViews	= Collections
													.synchronizedMap(new WeakHashMap<ImageView, String>());

	ExecutorService				executorService;

	final int						stub_id;





	public ImageLoader(Context context) {
		fileCache = new FileCache(context);
		executorService = Executors.newFixedThreadPool(5);
		stub_id = R.drawable.nocover;

	}





	/**
	 * Display Action bar icon
	 * 
	 * @param url
	 * @param ctx
	 * @param actionBar
	 */
	public void DisplayActionBarIcon(String url, Context ctx,
			ActionBar actionBar) {
		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null) actionBar.setIcon(new BitmapDrawable(ctx
				.getResources(), bitmap));


		else{
			queuePhoto(url, ctx, actionBar);
			actionBar.setIcon(R.drawable.ic_launcher);
		}
	}





	/**
	 * Queue photos for ImageViews
	 * 
	 * @param url
	 * @param ctx
	 * @param actionBar
	 */
	private void queuePhoto(String url, Context ctx, ActionBar actionBar) {
		PhotoToLoadABS pABS = new PhotoToLoadABS(url, actionBar, ctx);
		executorService.submit(new PhotosLoaderABS(pABS));
	}





	/**
	 * Display image (now used only for library logos)
	 * 
	 * @param url
	 * @param bk
	 * @param a
	 */
	public void DisplayImage(String url, DataClassDisplayBookCover img) {


		imageViews.put(img.iv, url);


		Bitmap bitmap = memoryCache.get(url);
		if (bitmap != null){
			// Show image
			img.iv.setImageBitmap(bitmap);
			img.iv.refreshDrawableState();

			// If it is cover
			if (img.isCover){
				// Hide progress bar
				img.pb.setVisibility(View.GONE);
				img.tv.setVisibility(View.GONE);
			}


		}
		else{

			img.iv.setImageResource(stub_id);
			if (img.isCover){
				img.pb.setVisibility(View.VISIBLE);
				img.tv.setVisibility(View.GONE);
			}

			queuePhoto(url, img);
		}
	}





	/**
	 * Display Book Cover
	 * 
	 * @param url
	 * @param bk
	 * @param a
	 */
	public void DisplayCover(DataClassDisplayBookCover bc) {


		imageViews.put(bc.iv, bc.book.imgURL);

		Bitmap bitmap = memoryCache.get(bc.book.imgURL);

		// Image cache found
		if (bitmap != null){
			// Show image
			bc.iv.setImageBitmap(bitmap);
			bc.iv.refreshDrawableState();

			// If it is cover
			if (bc.isCover){
				// Hide progress bar
				bc.pb.setVisibility(View.GONE);
				bc.tv.setVisibility(View.GONE);
			}


		}
		// Image cache not found
		else{
			// Show default image
			bc.iv.setImageResource(stub_id);
			if (bc.isCover){
				// and progress bar
				bc.pb.setVisibility(View.VISIBLE);
				bc.tv.setVisibility(View.GONE);
			}

			queuePhoto(bc);
		}
	}








	/**
	 * Queue photos for ImageViews
	 * 
	 * @param url
	 * @param imageView
	 */
	private void queuePhoto(String url, DataClassDisplayBookCover bk) {
		PhotoToLoad p = new PhotoToLoad(url, bk);
		executorService.submit(new PhotosLoader(p));
	}





	/**
	 * Queue photos for Book Covers
	 * 
	 * @param url
	 * @param imageView
	 */
	private void queuePhoto(DataClassDisplayBookCover bc) {
		PhotoToLoadBC pl = new PhotoToLoadBC(bc);
		executorService.submit(new PhotosLoaderBC(pl));
	}







	private DataClassBitmapGot getBitmap(String url,
			DataClassDisplayBookCover bk) {
		File f = fileCache.getFile(url);

		DataClassBitmapGot result = new DataClassBitmapGot();

		// from SD cache
		result.b = decodeFile(f);
		if (result.b != null) return result;

		// from web
		try{
			// If book doesnt have a cover
			if (url.equals(App.noCoverURL)){
				throw new Exception("No Cover exception");
			}
			// Download cover from web
			else{
				result.b = null;
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
				result.b = decodeFile(f);
			}


			return result;
		}
		catch (Exception ex){
			Log.i(TAG, "Failed to download image: " + ex.getMessage());

			if (bk != null){

				// Change cover image URL to no cover
				bk.book.imgURL = App.noCoverURL;

			}

				result.gotBitmap = false;
				return result;
		}
	}





	// decodes image and scales it to reduce memory consumption
	private Bitmap decodeFile(File f) {
		try{
			// decode image size
			BitmapFactory.Options o = new BitmapFactory.Options();
			o.inJustDecodeBounds = true;
			BitmapFactory.decodeStream(new FileInputStream(f), null, o);

			// Find the correct scale value. It should be the power of 2.
			final int REQUIRED_SIZE = 70;
			int width_tmp = o.outWidth, height_tmp = o.outHeight;
			int scale = 1;
			while (true){
				if (width_tmp / 2 < REQUIRED_SIZE
						|| height_tmp / 2 < REQUIRED_SIZE) break;
				width_tmp /= 2;
				height_tmp /= 2;
				scale *= 2;
			}

			// decode with inSampleSize
			BitmapFactory.Options o2 = new BitmapFactory.Options();
			o2.inSampleSize = scale;
			return BitmapFactory.decodeStream(new FileInputStream(f), null,
					o2);
		}
		catch (FileNotFoundException e){
		}
		return null;
	}


	private class PhotoToLoad {

		public String					url;

		public DataClassDisplayBookCover	bk;





		public PhotoToLoad(String u, DataClassDisplayBookCover bk) {
			url = u;
			this.bk = bk;
		}
	}

	// Task for the queue
	private class PhotoToLoadBC {

		public DataClassDisplayBookCover	bc;





		public PhotoToLoadBC(DataClassDisplayBookCover bc) {
			this.bc = bc;
		}
	}


	private class PhotoToLoadABS {

		public String		url;

		public ActionBar	actionBar;

		public Context		ctx;





		public PhotoToLoadABS(String u, ActionBar a, Context ctx) {
			url = u;
			actionBar = a;
			this.ctx = ctx;
		}
	}


	/**
	 * Used for BookCovers
	 * 
	 * @author paschalis
	 * 
	 */
	class PhotosLoaderBC implements Runnable {

		PhotoToLoadBC	photoToLoadbc;





		PhotosLoaderBC(PhotoToLoadBC photoToLoadbc) {
			this.photoToLoadbc = photoToLoadbc;
		}





		@Override
		public void run() {
			if (imageViewReused(photoToLoadbc)) return;

			DataClassBitmapGot rbmp = getBitmap(
					photoToLoadbc.bc.book.imgURL, photoToLoadbc.bc);
			memoryCache.put(photoToLoadbc.bc.book.imgURL, rbmp.b);
			if (imageViewReused(photoToLoadbc)) return;
			BitmapDisplayer bd = new BitmapDisplayer(rbmp.b, photoToLoadbc);
			Activity a = (Activity) photoToLoadbc.bc.iv.getContext();
			a.runOnUiThread(bd);
		}
	}







	/**
	 * Used for ImageViews
	 * 
	 * @author paschalis
	 * 
	 */
	class PhotosLoader implements Runnable {

		PhotoToLoad	photoToLoad;





		PhotosLoader(PhotoToLoad photoToLoad) {
			this.photoToLoad = photoToLoad;
		}





		@Override
		public void run() {
			if (imageViewReused(photoToLoad)) return;

			DataClassBitmapGot rbmp = getBitmap(photoToLoad.url,
					photoToLoad.bk);
			memoryCache.put(photoToLoad.url, rbmp.b);
			if (imageViewReused(photoToLoad)) return;
			BitmapDisplayer bd = new BitmapDisplayer(rbmp.b, photoToLoad);
			Activity a = (Activity) photoToLoad.bk.iv.getContext();
			a.runOnUiThread(bd);
		}
	}


	/**
	 * Used for ActionBar Icons
	 * 
	 * @author paschalis
	 * 
	 */
	class PhotosLoaderABS implements Runnable {

		PhotoToLoadABS	photoToLoadABS;





		PhotosLoaderABS(PhotoToLoadABS photoToLoadABS) {
			this.photoToLoadABS = photoToLoadABS;
		}





		@Override
		public void run() {
			DataClassBitmapGot rbmp = getBitmap(photoToLoadABS.url, null);
			// if(rbmp.cacheIt){
			memoryCache.put(photoToLoadABS.url, rbmp.b);
			// }

			BitmapDisplayer bd = new BitmapDisplayer(rbmp.b, photoToLoadABS);
			Activity a = (Activity) photoToLoadABS.ctx;
			a.runOnUiThread(bd);
		}
	}





	boolean imageViewReused(PhotoToLoad photoToLoad) {
		String tag = imageViews.get(photoToLoad.bk.iv);
		if (tag == null || !tag.equals(photoToLoad.url)) return true;
		return false;
	}





	boolean imageViewReused(PhotoToLoadBC photoToLoadbc) {
		String tag = imageViews.get(photoToLoadbc.bc.iv);
		if (tag == null || !tag.equals(photoToLoadbc.bc.book.imgURL))
			return true;
		return false;
	}

	// Used to display bitmap in the UI thread
	class BitmapDisplayer implements Runnable {

		Bitmap		bitmap;

		PhotoToLoad	photoToLoad;

		PhotoToLoadABS	photoToLoadABS;


		PhotoToLoadBC	photoToLoadbc;





		public BitmapDisplayer(Bitmap b, PhotoToLoad p) {
			bitmap = b;
			photoToLoad = p;
			this.photoToLoadABS = null;
			this.photoToLoadbc = null;
		}





		public BitmapDisplayer(Bitmap bmp, PhotoToLoadABS photoToLoadABS) {
			bitmap = bmp;
			this.photoToLoadABS = photoToLoadABS;
			this.photoToLoad = null;
			this.photoToLoadbc = null;
		}





		public BitmapDisplayer(Bitmap b, PhotoToLoadBC photoToLoadbc) {
			bitmap = b;
			this.photoToLoadbc = photoToLoadbc;
			this.photoToLoad = null;
			this.photoToLoadABS = null;

		}





		public void run() {
			// Using for regular Image View
			if (this.photoToLoad != null){
				if (imageViewReused(photoToLoad)) return;

				if (bitmap != null){
					photoToLoad.bk.iv.setImageBitmap(bitmap);
					photoToLoad.bk.iv.refreshDrawableState();
					// If is a cover image
					if (photoToLoad.bk.isCover){
						photoToLoad.bk.pb.setVisibility(View.GONE);
						photoToLoad.bk.tv.setVisibility(View.GONE);
					}

				}
				else{

					// Show default image
					photoToLoad.bk.iv.setImageResource(stub_id);
					// Show no cover message
					if (photoToLoad.bk.isCover){
						// CHECK REVERSE IN HERE!
						photoToLoad.bk.pb.setVisibility(View.GONE);
						photoToLoad.bk.tv.setVisibility(View.VISIBLE);
					}

				}
			}
			// Using image of Cover
			else if (this.photoToLoadbc != null){
				if (imageViewReused(photoToLoadbc)) return;

				if (bitmap != null){
					photoToLoadbc.bc.iv.setImageBitmap(bitmap);
					photoToLoadbc.bc.iv.refreshDrawableState();
					photoToLoadbc.bc.pb.setVisibility(View.GONE);
					photoToLoadbc.bc.tv.setVisibility(View.GONE);

				}
				else{

					// Show default image
					photoToLoadbc.bc.iv.setImageResource(stub_id);
					// Show no cover message
					// if (photoToLoadbc.bc.isCover){
					// CHECK REVERSE IN HERE!
					photoToLoadbc.bc.pb.setVisibility(View.GONE);
					photoToLoadbc.bc.tv.setVisibility(View.VISIBLE);
					// }

				}

			}
			// Using image for Action Bar
			else{
				if (bitmap != null) photoToLoadABS.actionBar
						.setIcon(new BitmapDrawable(photoToLoadABS.ctx
								.getResources(), bitmap));
				else{
					photoToLoadABS.actionBar
							.setIcon(R.drawable.ic_launcher);
				}
			}

		}
	}





	/**
	 * Clears all caches
	 */
	public void clearCache() {
		memoryCache.clear();
		fileCache.clear();
	}





	/**
	 * @return caches directory
	 */
	public String getCacheDirectory() {
		return fileCache.cacheDir.toString();
	}

	public static class DataClassDisplayBookCover {

		public ImageView	iv;

		public TextView	tv;

		public ProgressBar	pb;

		public boolean		isCover;

		public Book		book;





		/**
		 * Data help Class
		 * 
		 * @author paschalis
		 * 
		 */
		public DataClassDisplayBookCover() {
			iv = null;
			tv = null;
			pb = null;
			book = null;
			isCover = false;// assume its not cover

		}

		/**
		 * Data help Class
		 * 
		 * @author paschalis
		 * 
		 */
		public static class DataClassBitmapGot {

			public Bitmap	b		= null;

			public boolean	gotBitmap	= true;


		}

	}


}
