package com.brezze.library_common.binding.viewadapter.toggleButton;

import android.widget.CompoundButton;
import android.widget.ToggleButton;

import androidx.databinding.BindingAdapter;

import com.brezze.library_common.binding.command.BindingCommand;

/**
 * File: ViewAdapter
 * author: zhangjiabiao Create on 2019/11/25 11:31
 * Change (from 2019/11/25)
 * --------------------------------
 * decription:
 * ----------------------------
 */
public class ViewAdapter {

    /**
     * 设置开关状态
     *
     * @param toggleButton
     * @param isChecked
     */
    @BindingAdapter("switchState")
    public static void setSwitchState(ToggleButton toggleButton, boolean isChecked) {
        toggleButton.setChecked(isChecked);
    }


    /**
     * ToggleButton的状态改变监听
     * @param toggleButton
     * @param changeListener
     */
    @BindingAdapter("onCheckedChangeCommand")
    public static void onCheckedChangeCommand(final ToggleButton toggleButton, final BindingCommand<Boolean> changeListener) {
        if (changeListener != null) {
            toggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    changeListener.execute(isChecked);
                }
            });
        }
    }
}
