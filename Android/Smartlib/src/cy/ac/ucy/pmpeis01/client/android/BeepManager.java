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

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;





/**
 * Manages beeps and vibrations for {@link CaptureActivity}.
 */
final class BeepManager {

	private static final String	TAG				= BeepManager.class
													.getSimpleName();

	private static final float	BEEP_VOLUME		= 0.10f;

	private static final long	VIBRATE_DURATION	= 200L;

	private final Activity		activity;

	private MediaPlayer			mediaPlayerBook;
	
	private MediaPlayer			mediaPlayerOther;

	private boolean			playBeep;

	private boolean			vibrate;
	
	
	ScanType scanType = ScanType.NotSpecified;
//	
//	
	private enum ScanType {
		Book, Other, NotSpecified
	}





	BeepManager(Activity activity) {
		this.activity = activity;
		this.mediaPlayerBook = null;
		this.mediaPlayerOther = null;
		updatePrefs();
	}





	void updatePrefs() {
		SharedPreferences prefs = PreferenceManager
				.getDefaultSharedPreferences(activity);
		playBeep = shouldBeep(prefs, activity);
		vibrate = prefs.getBoolean(PreferencesActivity.KEY_VIBRATE, false);
		if (playBeep && mediaPlayerBook == null && mediaPlayerOther == null){
			// The volume on STREAM_SYSTEM is not adjustable, and users found
			// it too loud,
			// so we now play on the music stream.
			activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayerBook = buildMediaPlayer(activity,ScanType.Book);
			mediaPlayerOther = buildMediaPlayer(activity,ScanType.Other);
		}
	}





	/**Play sound when found a book
	 * 
	 */
	void playSuccessBookSoundAndVibrate() {

		
		if (playBeep && mediaPlayerBook != null){
			mediaPlayerBook.start();
		}
		if (vibrate){
			Vibrator vibrator = (Vibrator) activity
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}


	
	/**Play sound when found a Non-Book
	 * 
	 */
	void playNotBookSoundAndVibrate() {
		scanType=ScanType.Other;
		if (playBeep && mediaPlayerOther != null){
			mediaPlayerOther.start();
		}
		if (vibrate){
			Vibrator vibrator = (Vibrator) activity
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}





	private static boolean shouldBeep(SharedPreferences prefs, Context activity) {
		boolean shouldPlayBeep = prefs.getBoolean(
				PreferencesActivity.KEY_PLAY_BEEP, true);
		if (shouldPlayBeep){
			// See if sound settings overrides this
			AudioManager audioService = (AudioManager) activity
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL){
				shouldPlayBeep = false;
			}
		}
		return shouldPlayBeep;
	}





	private static MediaPlayer buildMediaPlayer(Context activity, ScanType pScanType) {
		MediaPlayer mediaPlayer = new MediaPlayer();
		mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
		// When the beep has finished playing, rewind to queue up another one.
		mediaPlayer
				.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {

					@Override
					public void onCompletion(MediaPlayer player) {
						player.seekTo(0);
					}
				});
		
		AssetFileDescriptor file;
		
		if(pScanType.equals(ScanType.Book)){
			file = activity.getResources().openRawResourceFd(
						R.raw.scanned_book);
		}
		else{	
			file = activity.getResources().openRawResourceFd(
					R.raw.scan_failed);
		}

		try{
			mediaPlayer.setDataSource(file.getFileDescriptor(),
					file.getStartOffset(), file.getLength());
			file.close();
			mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
			mediaPlayer.prepare();
		}
		catch (IOException ioe){
			Log.w(TAG, ioe);
			mediaPlayer = null;
		}
		return mediaPlayer;
	}

}
