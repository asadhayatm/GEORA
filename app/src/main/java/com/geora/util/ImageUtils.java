package com.geora.util;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.provider.MediaStore;
import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;


public class ImageUtils {

    /**
     * set user profile pic in circular
     *
     * @param mContext
     * @param imageUrl
     * @param imageView
     */
    public static void setImage(final Context mContext, Uri imageUrl, final ImageView imageView, Drawable placeHolder) {
        try {
            Glide.with(mContext).load(imageUrl)
                    .asBitmap().centerCrop().placeholder(placeHolder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            circularBitmapDrawable.setCornerRadius(500);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } catch (Exception e) {
            return;
        }

    }

    public static void setImage(final Context mContext, Drawable imageUrl, final ImageView imageView, Drawable placeHolder) {
        try {
            Glide.with(mContext).load(imageUrl)
                    .asBitmap().centerCrop().placeholder(placeHolder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } catch (Exception e) {
            return;
        }

    }

    public static void setImage(final Context mContext, String imageUrl, final ImageView imageView, Drawable placeHolder) {
        try {
            Glide.with(mContext).load(imageUrl)
                    .asBitmap().centerCrop().placeholder(placeHolder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(new BitmapImageViewTarget(imageView) {
                        @Override
                        protected void setResource(Bitmap resource) {
                            RoundedBitmapDrawable circularBitmapDrawable =
                                    RoundedBitmapDrawableFactory.create(mContext.getResources(), resource);
                            circularBitmapDrawable.setCircular(true);
                            circularBitmapDrawable.setCornerRadius(10);
                            imageView.setImageDrawable(circularBitmapDrawable);
                        }
                    });
        } catch (Exception e) {
            return;
        }
    }

    public static void setImageRectangle(final Context mContext, String imageUrl, final ImageView imageView, Drawable placeHolder) {
        try {
            Glide.with(mContext).load(imageUrl)
                    .asBitmap().placeholder(placeHolder)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageView);
        } catch (Exception e) {
            return;
        }

    }

    public static String getPath(Context context, Uri uri) {
        String result = null;
        String[] proj = {MediaStore.Images.Media.DATA};
        Cursor cursor = context.getContentResolver().query(uri, proj, null, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                int column_index = cursor.getColumnIndexOrThrow(proj[0]);
                result = cursor.getString(column_index);
            }
            cursor.close();
        }
        if (result == null) {
            result = "Not found";
        }
        return result;
    }
}
