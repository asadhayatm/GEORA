package com.geora.ui.imagepreview;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.geora.R;
import com.geora.util.ImageUtils;

import java.util.ArrayList;

class CustomPagerAdapter extends PagerAdapter {

    Context mContext;
    LayoutInflater mLayoutInflater;
    ArrayList<String> list = new ArrayList<>();

    public CustomPagerAdapter(Context context, String[] allImagesList) {
        mContext = context;
        mLayoutInflater = LayoutInflater.from(mContext);
    }

    // Returns the number of pages to be displayed in the ViewPager.
    @Override
    public int getCount() {
        return list.size();
    }

    // Returns true if a particular object (page) is from a particular page
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    // This method should create the page for the given position passed to it as an argument.
    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        // Inflate the layout for the page
        View itemView = mLayoutInflater.inflate(R.layout.item_vp_image, container, false);
        // Find and populate data into the page (i.e set the image)
        ImageView imageView = (ImageView) itemView.findViewById(R.id.image);
        ImageUtils.setImage(mContext, list.get(position), imageView, null);
        // ...
        // Add the page to the container
        container.addView(itemView);
        // Return the page
        return itemView;
    }

    // Removes the page from the container for the given position.
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
}
