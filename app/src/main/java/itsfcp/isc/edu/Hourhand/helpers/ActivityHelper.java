package itsfcp.isc.edu.Hourhand.helpers;

import android.app.Activity;
import android.content.Intent;

public class ActivityHelper {

    public static void launchNewActivity(Activity activityBase, Class<?> classNameActivity, boolean finishOldActivity) {
        Intent intent = new Intent().setClass(activityBase.getBaseContext(), classNameActivity);
        activityBase.startActivity(intent);
        if (finishOldActivity)
            activityBase.finish();
    }
}
