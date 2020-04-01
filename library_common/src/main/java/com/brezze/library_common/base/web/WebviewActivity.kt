package com.brezze.library_common.base.web

import android.os.Bundle
import com.brezze.library_common.BR
import com.brezze.library_common.R
import com.brezze.library_common.base.BaseActivity
import com.brezze.library_common.databinding.ActivityWebviewBinding

class WebviewActivity : BaseActivity<ActivityWebviewBinding,WebviewViewModel>() {

    override fun initContentView(savedInstanceState: Bundle?): Int = R.layout.activity_webview

    override fun initVariableId(): Int? = BR.viewModel

    override fun initData() {
        super.initData()

    }
}
