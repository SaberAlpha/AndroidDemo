package com.brezze.library_common.binding.viewadapter.radiogroup;

import android.widget.RadioGroup;

import androidx.annotation.IdRes;
import androidx.databinding.BindingAdapter;

import com.brezze.library_common.binding.command.BindingCommand;


/**
 * File: ViewAdapter
 * Author: 82149 Create: 2019/11/29 10:05
 * Changes (from 2019/11/29)
 * --------------------------------------------------
 * describe:
 * ---------------------------------------------------
 */
public class ViewAdapter {
    @BindingAdapter(value = {"onCheckedChangedCommand"}, requireAll = false)
    public static void onCheckedChangedCommand(final RadioGroup radioGroup, final BindingCommand<Integer> bindingCommand) {
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, @IdRes int checkedId) {
                bindingCommand.execute(checkedId);
//                RadioButton radioButton = (RadioButton) group.findViewById(checkedId);
//                bindingCommand.execute(radioButton.getText().toString());
            }
        });
    }
}
