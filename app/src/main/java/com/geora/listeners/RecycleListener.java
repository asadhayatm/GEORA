package com.geora.listeners;

import android.view.View;


public interface RecycleListener {
    void onItemClicked(View view, String s, int pos);
    void onItemLongClicked(String s, int pos);
}
