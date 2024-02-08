package com.erwiin21mp.cinemovilplus.ui.view.home

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.extensions.navigateToProfile
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.erwiin21mp.cinemovilplus.databinding.ActivityHomeBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation

@AndroidEntryPoint
class HomeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityHomeBinding
    private val auth = AuthManager()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initListeners()
        loadProfileImage()
    }

    private fun loadProfileImage() {
        Picasso.get().load(auth.getCurrentUser()!!.photoUrl).error(R.drawable.ic_user).transform(CropCircleTransformation()).into(binding.btnProfile)
    }

    private fun initListeners() {
        binding.apply {
            btnProfile.setOnClickListener { navigateToProfile() }
        }
    }
}