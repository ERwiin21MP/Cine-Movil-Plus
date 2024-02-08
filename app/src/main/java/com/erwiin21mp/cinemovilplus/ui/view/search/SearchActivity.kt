package com.erwiin21mp.cinemovilplus.ui.view.search

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
    }
}