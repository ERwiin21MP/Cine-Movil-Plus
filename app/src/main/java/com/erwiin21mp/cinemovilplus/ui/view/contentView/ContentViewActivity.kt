package com.erwiin21mp.cinemovilplus.ui.view.contentView

import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.databinding.ActivityContentViewBinding
import com.erwiin21mp.cinemovilplus.ui.utils.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

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
        initRecommendations()
        CoroutineScope(Dispatchers.IO).launch {

        }
    }

    private fun initRecommendations() {
        binding.rvRecommendations.apply {
            layoutManager =
                LinearLayoutManager(this@ContentViewActivity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_8)))
        }
    }

    private fun getTextFromDetail(refString: Int, text: String): SpannableString {
        val textRet = SpannableString("${getString(refString)} $text")
        textRet.setSpan(
            StyleSpan(Typeface.BOLD),
            0,
            getString(refString).length + 1,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        return textRet
    }
}