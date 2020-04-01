package com.brezze.library_common.app;

import android.app.Activity;
import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;

import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;

import me.jessyan.autosize.AutoSize;


/**
 * Create by zhangjiabiao on 2019/11/7
 */
public class CommonApp extends Application {

    private static Application context;

    @Override
    public void onCreate() {
        super.onCreate();
        setApplication(this,true);
    }

    public static synchronized void setApplication(@NonNull Application application,@NonNull boolean isDebug) {
        context = application;
        // 屏幕适配
        AutoSize.initCompatMultiProcess(context);
    }

//    static {
//        //设置全局的Header构建器
//        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
//            layout.setPrimaryColorsId(android.R.color.white, android.R.color.black);//全局设置主题颜色
//            return new ClassicsHeader(context);//.setTimeFormat(new DynamicTimeFormat("更新于 %s"));//指定为经典Header，默认是 贝塞尔雷达Header
//        });
//        //设置全局的Footer构建器
//        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> {
//            //指定为经典Footer，默认是 BallPulseFooter
//            return new ClassicsFooter(context).setDrawableSize(20);
//        });
//    }


    //返回
    public static Context getAppContext(){
        return context.getApplicationContext();
    }
}
