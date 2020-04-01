package com.brezze.library_common.binding.viewadapter.checkbox;

import android.widget.CheckBox;
import android.widget.CompoundButton;

import androidx.databinding.BindingAdapter;

import com.brezze.library_common.binding.command.BindingCommand;


/**
 * File: ViewAdapter
 * author: zhangjiabiao Create on 2019/11/25 11:27
 * Change (from 2019/11/25)
 * --------------------------------
 * decription:
 * ----------------------------
 */
public class ViewAdapter {
    /**
     * @param bindingCommand //绑定监听
     */
    @SuppressWarnings("unchecked")
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void setCheckedChanged(final CheckBox checkBox, final BindingCommand<Boolean> bindingCommand) {
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                bindingCommand.execute(b);
            }
        });
    }

    /**
     * CheckBox 选中
     */
    @BindingAdapter({"requestCheck"})
    public static void requestCheck(CheckBox checkBox, final Boolean isCheck) {
       checkBox.setChecked(isCheck);
    }
}
