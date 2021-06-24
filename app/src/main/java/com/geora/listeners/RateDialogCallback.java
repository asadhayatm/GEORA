package com.geora.listeners;

/**
 * Created by pardeep on 14/12/16.
 */

import android.view.View;

/**
 * this interface is used to get the callback from the dialog
 */
public interface RateDialogCallback {
    void onSubmit(View view, String title, String description, double rating);


}
