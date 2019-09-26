package itsfcp.isc.edu.Hourhand;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

public class HelperManage {

    public static void addFragment(Activity activity, android.app.Fragment fragment, int frameLayoutId) {
        android.app.FragmentManager fragmentManager = activity.getFragmentManager();
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.add(frameLayoutId, fragment);
        transaction.commit();
    }

    public static void configDefaultActionBarBack(AppCompatActivity appCompatActivity) {
        if (appCompatActivity.getSupportActionBar() != null) {
            appCompatActivity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            appCompatActivity.getSupportActionBar().setDisplayShowHomeEnabled(true);
            appCompatActivity.getSupportActionBar().setElevation(0);

        }
    }

    public static void launchNewActivity(Activity activityBase, Class<?> classNameActivity, boolean finishOldActivity) {
        Intent intent = new Intent().setClass(activityBase.getBaseContext(), classNameActivity);
        activityBase.startActivity(intent);
        if (finishOldActivity)
            activityBase.finish();
    }
}
