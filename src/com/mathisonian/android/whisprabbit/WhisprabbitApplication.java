package com.mathisonian.android.whisprabbit;

import com.urbanairship.Logger;
import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;
import com.urbanairship.push.PushPreferences;

import android.app.Application;

public class WhisprabbitApplication extends Application {
	@Override
    public void onCreate() {
        UAirship.takeOff(this);
        PushManager.enablePush();
        PushPreferences prefs = PushManager.shared().getPreferences();
        Logger.info("My Application onCreate - App APID: " + prefs.getPushId());
    }
}
