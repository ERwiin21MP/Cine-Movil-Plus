package com.erwiin21mp.cinemovilplus.ui.view.contentView

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.core.ext.logData
import com.erwiin21mp.cinemovilplus.databinding.ActivityContentViewBinding
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentViewBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra(ID).orEmpty()
        logData(intent.extras.toString(), "Bundle")
        logData(id)
        binding.tvId.text = id
    }
}