package com.geora.util;

import android.app.Activity;
import android.content.pm.PackageManager;
import androidx.core.content.ContextCompat;

/**
 * Created by Navjot Singh
 * on 21/5/18.
 */

public class PermissionUtils {

    /**
     * method to check permissions at run time
     *
     * @return true if has permission else false
     */
    public static boolean checkMultiplePermissions(Activity activity, String permissions[]) {
        for (String s : permissions) {
            if (ContextCompat.checkSelfPermission(activity, s) != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public static boolean checkMultiplePermissionsDenied(Activity activity, String permissions[]) {
        for (String s : permissions) {
            if (ContextCompat.checkSelfPermission(activity, s) != PackageManager.PERMISSION_DENIED) {
                return false;
            }
        }
        return true;
    }


    /**
     * method to check permissions grantResult
     *
     * @return true if granted else false
     */
    public static boolean isPermissionGranted(int[] grantResults) {
        for (int i : grantResults) {
            if (i != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

}
