package com.erwiin21mp.cinemovilplus.ui.view.contentView

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.databinding.ActivityContentViewBinding
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ContentViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentViewBinding
    private val contentViewViewModel: ContentViewViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        val id = intent.getIntExtra(ID, 0)
        binding.tvId.text = id.toString()
    }
}