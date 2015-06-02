package net.mcarolan.whenzebus;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

public class WhenZeBusApplication extends Application {

	private static boolean isInForeground;
	
	private static final String TAG = "WhenZeBusApplication";

	private static Context context;
	
	@Override
	public void onCreate() {
		super.onCreate();
		this.registerActivityLifecycleCallbacks(new CallbackListener());
		context = this;
	}
	
	public static boolean isInForeground() {
		return isInForeground;
	}

	public static Context getContext() { return context; }

	public static String getResourceString(int id) { return context.getResources().getString(id); }

	private static class CallbackListener implements Application.ActivityLifecycleCallbacks {

		@Override
		public void onActivityCreated(Activity activity,
				Bundle savedInstanceState) {
		}

		@Override
		public void onActivityStarted(Activity activity) {
		}

		@Override
		public void onActivityResumed(Activity activity) {
			isInForeground = true;
			Log.i(TAG, "app in foreground");
		}

		@Override
		public void onActivityPaused(Activity activity) {
			isInForeground = false;
			Log.i(TAG, "app in background");
		}

		@Override
		public void onActivityStopped(Activity activity) {
		}

		@Override
		public void onActivitySaveInstanceState(Activity activity,
				Bundle outState) {
		}

		@Override
		public void onActivityDestroyed(Activity activity) {
		}
		
	}
	
	

}
