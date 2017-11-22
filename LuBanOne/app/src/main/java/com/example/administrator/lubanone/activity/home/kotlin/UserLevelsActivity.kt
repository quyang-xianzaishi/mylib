package com.example.administrator.lubanone.activity.home.kotlin

import android.view.View
import com.example.administrator.lubanone.R
import com.example.administrator.lubanone.activity.BaseActivity
import kotlinx.android.synthetic.main.activity_user_level.*

/**
 * 用户级别
 */
class UserLevelsActivity : BaseActivity() {

    var mSize: String? = null

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv, R.id.iv_back -> finish()
        }
    }

    override fun beforeSetContentView() {
    }

    override fun getContentViewId(): Int {
        return R.layout.activity_user_level
    }

    override fun initView() {
        tv_title.text = getString(R.string.member_level)

        tv_back.setOnClickListener(this)
        iv_back.setOnClickListener(this)



        mSize = intent?.getStringExtra("code_size")
    }

    override fun loadData() {

    }

}
