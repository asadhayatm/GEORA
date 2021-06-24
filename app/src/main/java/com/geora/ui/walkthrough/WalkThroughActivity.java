package com.geora.ui.walkthrough;

import android.content.Intent;
import android.os.Bundle;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.ui.onboard.OnBoardActivity;

public class WalkThroughActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        openOnBoardActivity();
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_walk_through;
    }

    private void openOnBoardActivity() {
        startActivity(new Intent(this, OnBoardActivity.class));
        finishAfterTransition();
    }
}
