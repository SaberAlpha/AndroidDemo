package com.brezze.library_common.binding.viewadapter.image;

import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.databinding.BindingAdapter;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public final class ViewAdapter {

    @BindingAdapter(value = {"url", "placeholderRes","isRect"}, requireAll = false)
    public static void setImageUri(ImageView imageView, String url, int placeholderRes ,boolean isRect) {
        if (imageView.getContext() != null) {
//            //设置图片圆角角度
//            RoundedCorners roundedCorners= new RoundedCorners(6);
//            //通过RequestOptions扩展功能
//            RequestOptions options=RequestOptions.bitmapTransform(new GlideRoundTransform(imageView.getContext(),6));
//            RequestOptions options=new RequestOptions().placeholder(placeholderRes).error(placeholderRes);
            RequestOptions options = RequestOptions.placeholderOf(placeholderRes).error(placeholderRes);
            if (!isRect){
                options = RequestOptions.bitmapTransform(new GlideRoundTransform(imageView.getContext(),6)).placeholder(placeholderRes).error(placeholderRes);
            }

            if (!TextUtils.isEmpty(url)) {
                //使用Glide框架加载图片
                try {
                    Glide.with(imageView.getContext())
                            .load(url)
                            .apply(options)
                            .into(imageView);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                if (placeholderRes != 0)
                    imageView.setImageResource(placeholderRes);
            }
        }
    }
    @BindingAdapter(value = {"backgroundSelect"})
    public static void setBackground(ImageView imageView, Drawable drawable) {
        imageView.setBackground(drawable);
    }
}
