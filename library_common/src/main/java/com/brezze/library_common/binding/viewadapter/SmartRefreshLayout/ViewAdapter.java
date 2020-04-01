package com.brezze.library_common.binding.viewadapter.SmartRefreshLayout;

import android.annotation.SuppressLint;

import androidx.databinding.BindingAdapter;

import com.brezze.library_common.binding.command.BindingCommand;
import com.brezze.library_common.utils.RxUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshLoadMoreListener;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;

/**
 * Create by zhangjiabiao on 2019/11/18
 */
public class ViewAdapter {

    @BindingAdapter(value = {"onRefreshCommand", "onLoadMoreCommand"}, requireAll = false)
    public static void onRefreshAndLoadMoreCommand(SmartRefreshLayout layout, final BindingCommand onRefreshCommand, final BindingCommand onLoadMoreCommand) {

        layout.setOnRefreshLoadMoreListener(new OnRefreshLoadMoreListener() {
            @SuppressLint("CheckResult")
            @Override
            public void onLoadMore(RefreshLayout refreshLayout) {
                if (onLoadMoreCommand != null) {
                    Observable.timer(1, TimeUnit.SECONDS)
                            .compose(RxUtils.Companion.getIntance().io2Main())
                            .subscribe(aLong -> {
                                layout.finishLoadMore();
                                onLoadMoreCommand.execute();
                            }, e->{});

                }
            }

            @SuppressLint("CheckResult")
            @Override
            public void onRefresh(RefreshLayout refreshLayout) {
                if (onRefreshCommand != null) {
                    Observable.timer(1, TimeUnit.SECONDS)
                            .compose(RxUtils.Companion.getIntance().io2Main())
                            .subscribe(aLong -> {
                                layout.finishRefresh();
                                onRefreshCommand.execute();
                            }, e->{});

                }
            }
        });


    }
}
