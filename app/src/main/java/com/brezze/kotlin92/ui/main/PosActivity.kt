package com.brezze.kotlin92.ui.main

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.brezze.kotlin92.BR
import com.brezze.kotlin92.R
import com.brezze.kotlin92.databinding.ActivityPosBinding
import com.brezze.library_common.base.BaseActivity

class PosActivity : BaseActivity<ActivityPosBinding,PosViewModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int = R.layout.activity_pos

    override fun initVariableId(): Int = BR.viewModel
}
