package com.geora.ui.test;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;

import com.geora.R;
import com.geora.base.BaseActivity;
import com.geora.data.constants.AppConstantClass;
import com.geora.ui.imagepreview.ImageViewPager;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class TestActivity extends BaseActivity {
    @BindView(R.id.vp)
    ViewPager vp;
    @BindView(R.id.iv_back)
    ImageView ivBack;

    private Unbinder unbinder;
    private String allImages;
    private String[] allImagesList;
    private ImageViewPager mAdapter;
    int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);

        getWindow().getSharedElementEnterTransition().setDuration(700);
        getWindow().getSharedElementReturnTransition().setDuration(700)
                .setInterpolator(new DecelerateInterpolator());

        allImages = getIntent().getStringExtra(AppConstantClass.DATA);
        allImages = allImages.replace("[", "");
        allImages = allImages.replace("]", "");
        allImagesList = allImages.split(",");
        if (allImagesList.length > 0)
            setupAdapter();

        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finishAfterTransition();
            }
        });

        if (getIntent() != null && getIntent().getExtras() != null) {
            position = getIntent().getExtras().getInt(AppConstantClass.POSITION, 0);
        }
        vp.setCurrentItem(position);

    }

    private void setupAdapter() {
        mAdapter = new ImageViewPager(this, allImagesList);
        vp.setAdapter(mAdapter);
    }

    @Override
    protected int getResourceId() {
        return R.layout.activity_test;
    }
}
