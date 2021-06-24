package com.geora.ui.imagepreview;

import android.app.Activity;
import androidx.viewpager.widget.ViewPager;
import android.os.Bundle;

import com.geora.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class ImagePreviewActivity extends Activity {
    @BindView(R.id.vp)
    ViewPager vp;

    private Unbinder unbinder;
    private String allImages;
    private String[] allImagesList;
    private ImageViewPager mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        unbinder = ButterKnife.bind(this);
        setContentView(R.layout.activity_image_preview);

//        getWindow().getSharedElementEnterTransition().setDuration(700);
//        getWindow().getSharedElementReturnTransition().setDuration(700)
//                .setInterpolator(new DecelerateInterpolator());
//        allImages = getIntent().getStringExtra(AppConstantClass.DATA);
//        allImages = allImages.replace("[", "");
//        allImages = allImages.replace("]", "");
//        allImagesList = allImages.split(",");
//        if (allImagesList.length > 0)
            setupAdapter();

    }

    private void setupAdapter() {
//        mAdapter = new ImageViewPager(this);
//        vp.setAdapter(mAdapter);
    }

//    @Override
//    protected int getResourceId() {
//        return R.layout.activity_image_preview;
//    }
}
