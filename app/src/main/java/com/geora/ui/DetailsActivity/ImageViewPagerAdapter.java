package com.geora.ui.DetailsActivity;

import android.content.Context;
import androidx.viewpager.widget.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.geora.R;
import com.geora.listeners.PagerCallBack;
import com.geora.util.ImageUtils;

/**
 * Created by shaifali on 6/3/18.
 */

public class ImageViewPagerAdapter extends PagerAdapter {

    private final PagerCallBack pagerCallBack;
    private Context mContext;
    private LayoutInflater mLayoutInflater;
    private String[] list;

    public ImageViewPagerAdapter(Context context, String[] list, PagerCallBack pagerCallBack) {
        this.mContext = context;
        this.list = list;
        this.pagerCallBack = pagerCallBack;

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
        View viewOverlay = itemView.findViewById(R.id.view_overlay);

        ImageUtils.setImageRectangle(mContext, list[position], imageView, null);
        container.addView(itemView);

        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagerCallBack.onItemClicked();
            }
        });
        viewOverlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pagerCallBack.onItemClicked();
            }
        });

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
