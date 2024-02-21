package com.erwiin21mp.cinemovilplus.ui.view.contentView

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.core.ext.logData
import com.erwiin21mp.cinemovilplus.databinding.ActivityContentViewBinding
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID

class ContentViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
        logData("ContentView")
    }

    private fun initUI() {
        val extras = intent.extras
        if (extras != null) {
            val value = extras.getString(ID)
            binding.tvId.text = value
            logData(value.toString(), "ID")
        }
    }
}