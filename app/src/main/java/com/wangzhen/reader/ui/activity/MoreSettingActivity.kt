package com.wangzhen.reader.ui.activity

import android.os.Bundle
import com.wangzhen.commons.toolbar.impl.Toolbar
import com.wangzhen.reader.base.toolbar.AppCommonToolbar
import com.wangzhen.reader.databinding.ActivityMoreSettingBinding
import com.wangzhen.reader.utils.ReadSettingManager
import com.wangzhen.reader.ui.base.BaseActivity

/**
 * MoreSettingActivity
 * Created by wangzhen on 2023/4/11
 */
class MoreSettingActivity : BaseActivity() {
    private lateinit var binding: ActivityMoreSettingBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMoreSettingBinding.inflate(layoutInflater)
        setContentView(binding.root)

        var isVolumeTurnPage = ReadSettingManager.instance.isVolumeTurnPage
        with(binding) {
            moreSettingScVolume.isChecked = isVolumeTurnPage
            moreSettingRlVolume.setOnClickListener {
                isVolumeTurnPage = !isVolumeTurnPage
                moreSettingScVolume.isChecked = isVolumeTurnPage
                ReadSettingManager.instance.isVolumeTurnPage = isVolumeTurnPage
            }
        }
    }

    override fun createToolbar(): Toolbar? {
        return AppCommonToolbar(this, "阅读设置")
    }
}