package com.mathisonian.android.whisprabbit;

import com.urbanairship.UAirship;
import com.urbanairship.push.PushManager;

import android.app.Application;

public class WhisprabbitApplication extends Application {
	@Override
    public void onCreate() {
        UAirship.takeOff(this);
        //PushManager.enablePush();
        PushManager.shared().setIntentReceiver(IntentReceiver.class);
    }
}
