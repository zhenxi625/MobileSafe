package com.demo.safe.activity;

import android.app.Activity;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ChenXingLing on 2017/1/6.
 */

public class ActivityController {

    public static List<Activity> activities = new ArrayList<>();

    public static void addActivity(Activity activity) {
        activities.add(activity);
    }

    public static void removeActivity(Activity activity) {
        activities.remove(activity);
    }

    public static void finishAll() {
        for (Activity activity : activities) {
            if (!activity.isFinishing()) {
                activity.finish();
            }
        }
    }
}
