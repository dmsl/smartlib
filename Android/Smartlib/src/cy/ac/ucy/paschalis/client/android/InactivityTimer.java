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

package cy.ac.ucy.paschalis.client.android;

import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.TimeUnit;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

/**
 * Finishes an activity after a period of inactivity if the device is on battery power.
 */
final class InactivityTimer {

  private static final int INACTIVITY_DELAY_SECONDS = 5 * 60;

  private final ScheduledExecutorService inactivityTimer =
      Executors.newSingleThreadScheduledExecutor(new DaemonThreadFactory());
  private final Activity activity;
  private ScheduledFuture<?> inactivityFuture = null;
  private final BroadcastReceiver powerStatusReceiver = new PowerStatusReceiver();

  InactivityTimer(Activity activity) {
    this.activity = activity;
    onActivity();
  }

  void onActivity() {
    cancel();
    if (!inactivityTimer.isShutdown()) {
      try {
        inactivityFuture = inactivityTimer.schedule(new FinishListener(activity),
            INACTIVITY_DELAY_SECONDS,
            TimeUnit.SECONDS);
      } catch (RejectedExecutionException ree) {
        // surprising, but could be normal if for some reason the implementation just doesn't
        // think it can shcedule again. Since this time-out is non-essential, just forget it
      }
    }
  }

  public void onPause() {
    cancel();
    activity.unregisterReceiver(powerStatusReceiver);
  }

  public void onResume(){
    activity.registerReceiver(powerStatusReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    onActivity();
  }

  private void cancel() {
    ScheduledFuture<?> future = inactivityFuture;
    if (future != null) {
      future.cancel(true);
      inactivityFuture = null;
    }
  }

  void shutdown() {
    cancel();
    inactivityTimer.shutdown();
  }

  private static final class DaemonThreadFactory implements ThreadFactory {
    @Override
    public Thread newThread(Runnable runnable) {
      Thread thread = new Thread(runnable);
      thread.setDaemon(true);
      return thread;
    }
  }

  private final class PowerStatusReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent){
      if (Intent.ACTION_BATTERY_CHANGED.equals(intent.getAction())) {
        // 0 indicates that we're on battery
        // In Android 2.0+, use BatteryManager.EXTRA_PLUGGED
        int batteryPlugged = intent.getIntExtra("plugged", -1);
        if (batteryPlugged > 0) {
          InactivityTimer.this.cancel();
        }
      }
    }
  }

}
