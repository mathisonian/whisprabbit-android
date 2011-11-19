package com.mathisonian.android.whisprabbit;

import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

public class IntentReceiver extends BroadcastReceiver {
	private static final String logTag = "PushSample";

	@Override
	public void onReceive(Context context, Intent intent) {
		String action = intent.getAction();
		Log.i(logTag, "HERE");
		if (action.equals(PushManager.ACTION_NOTIFICATION_OPENED)) {
			Log.i(logTag, "User clicked notification. Message: " + intent.getStringExtra(PushManager.EXTRA_ALERT));

			Intent launch = new Intent(Intent.ACTION_MAIN);
			launch.setClass(UAirship.shared().getApplicationContext(),
					WhisprabbitAndroidActivity.class);
			launch.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

			UAirship.shared().getApplicationContext().startActivity(launch);

		}
	}

}
