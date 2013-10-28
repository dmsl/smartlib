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

package com.google.zxing.client.android;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetFileDescriptor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Vibrator;
import android.preference.PreferenceManager;
import android.util.Log;

import java.io.IOException;

import cy.ac.ucy.paschalis.client.android.R;


/**
 * Manages beeps and vibrations for {@link CaptureActivity}.
 */
final class BeepManager {

	private static final String TAG = BeepManager.class
			.getSimpleName();

	private static final float BEEP_VOLUME = 0.10f;

	private static final long VIBRATE_DURATION = 200L;

	private final Activity activity;

	private MediaPlayer mediaPlayerBook;

	private MediaPlayer mediaPlayerOther;

	private boolean playBeep;

	private boolean vibrate;


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
		if (playBeep && mediaPlayerBook == null && mediaPlayerOther == null) {
			// The volume on STREAM_SYSTEM is not adjustable, and users found
			// it too loud,
			// so we now play on the music stream.
			activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
			mediaPlayerBook = buildMediaPlayer(activity, ScanType.Book);
			mediaPlayerOther = buildMediaPlayer(activity, ScanType.Other);
		}
	}


	/**
	 * Play sound when found a book
	 */
	void playSuccessBookSoundAndVibrate() {


		if (playBeep && mediaPlayerBook != null) {
			mediaPlayerBook.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) activity
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}


	/**
	 * Play sound when found a Non-Book
	 */
	void playNotBookSoundAndVibrate() {
		scanType = ScanType.Other;
		if (playBeep && mediaPlayerOther != null) {
			mediaPlayerOther.start();
		}
		if (vibrate) {
			Vibrator vibrator = (Vibrator) activity
					.getSystemService(Context.VIBRATOR_SERVICE);
			vibrator.vibrate(VIBRATE_DURATION);
		}
	}


	private static boolean shouldBeep(SharedPreferences prefs, Context activity) {
		boolean shouldPlayBeep = prefs.getBoolean(
				PreferencesActivity.KEY_PLAY_BEEP, true);
		if (shouldPlayBeep) {
			// See if sound settings overrides this
			AudioManager audioService = (AudioManager) activity
					.getSystemService(Context.AUDIO_SERVICE);
			if (audioService.getRingerMode() != AudioManager.RINGER_MODE_NORMAL) {
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

		if (pScanType.equals(ScanType.Book)) {
			file = activity.getResources().openRawResourceFd(
					R.raw.scanned_book);
		} else {
			file = activity.getResources().openRawResourceFd(
					R.raw.scan_failed);
		}

		try {
			mediaPlayer.setDataSource(file.getFileDescriptor(),
					file.getStartOffset(), file.getLength());
			file.close();
			mediaPlayer.setVolume(BEEP_VOLUME, BEEP_VOLUME);
			mediaPlayer.prepare();
		} catch (IOException ioe) {
			Log.w(TAG, ioe);
			mediaPlayer = null;
		}
		return mediaPlayer;
	}

}