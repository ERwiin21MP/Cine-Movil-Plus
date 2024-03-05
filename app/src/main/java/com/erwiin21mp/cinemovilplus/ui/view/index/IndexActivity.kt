package com.erwiin21mp.cinemovilplus.ui.view.index

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.databinding.ActivityIndexBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class IndexActivity : AppCompatActivity() {

    private lateinit var binding: ActivityIndexBinding
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIndexBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initNavigation()
    }

    private fun initNavigation() {
        val navHost = supportFragmentManager.findFragmentById(R.id.fcvContainer) as NavHostFragment
        navController = navHost.navController
        binding.bnvMenu.setupWithNavController(navController)
    }
}