package com.geora.ui.imagepreview;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.geora.R;
import com.geora.util.ImageUtils;

/**
 * Created by shaifali on 6/3/18.
 */

public class ImageViewPager extends PagerAdapter {

    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] list;

    public ImageViewPager(Context context, String[] list) {
        this.mContext = context;
        this.list = list;

    }

    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, final int position) {
        /*
         * inflating the vieewpager desig n here
         * */
        mLayoutInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View itemView = mLayoutInflater.inflate(R.layout.item_vp_image, container, false);

        ImageView imageView = itemView.findViewById(R.id.image_);

        ImageUtils.setImageRectangle(mContext, list[position], imageView, null);
        container.addView(itemView);

        return itemView;
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    /*
    -
    -
    -
    -
    */
    // to reflect the item which is deleted. otherwise it shows item is deleted but in UI it is displyed


}
