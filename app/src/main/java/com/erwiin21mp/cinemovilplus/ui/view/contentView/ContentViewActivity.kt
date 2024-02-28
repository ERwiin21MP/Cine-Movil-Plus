package com.erwiin21mp.cinemovilplus.ui.view.contentView

import android.annotation.SuppressLint
import android.graphics.Typeface
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.StyleSpan
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.logData
import com.erwiin21mp.cinemovilplus.core.ext.navigateToContent
import com.erwiin21mp.cinemovilplus.databinding.ActivityContentViewBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentModel
import com.erwiin21mp.cinemovilplus.ui.utils.SpacingItemDecoration
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.ID
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.MOVIE
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.PREFIX_GENDER
import com.erwiin21mp.cinemovilplus.ui.view.home.HomeViewModel.Companion.SERIE
import com.erwiin21mp.cinemovilplus.ui.view.home.content.ContentAdapter
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ContentViewActivity : AppCompatActivity() {

    private lateinit var binding: ActivityContentViewBinding
    private val contentViewViewModel: ContentViewViewModel by viewModels()
    private val adapterContentRecommendations = ContentAdapter { navigateToContent(it.toString()) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityContentViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI()
    }

    private fun initUI() {
        initObservers()
        initRecommendations()
        CoroutineScope(Dispatchers.IO).launch {
            contentViewViewModel.getItemContent(intent.getStringExtra(ID).toString())
            contentViewViewModel.getContentRecommendation()
        }
    }

    private fun initRecommendations() {
        binding.rvRecommendations.apply {
            layoutManager =
                LinearLayoutManager(this@ContentViewActivity, LinearLayoutManager.HORIZONTAL, false)
            adapter = adapterContentRecommendations
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_8)))
        }
    }

    private fun initObservers() {
        contentViewViewModel.itemContent.observe(this) {
            logData(it.toString(), "IT")
            setData(it)
            binding.apply {
                llLoading.visibility = View.GONE
                llContainer.visibility = View.VISIBLE
            }
        }

        contentViewViewModel.listOfContentRecommendation.observe(this) {
            adapterContentRecommendations.updateList(it)
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setData(item: ContentModel) {
        binding.apply {
            Picasso.get().load(item.horizontalImageUrl).into(ivPoster)
            var genres = ""
            item.genres.forEach { gender ->
                if (gender[0].toString() == PREFIX_GENDER) genres = "$genres${gender.drop(1)}, "
            }
            genres = genres.dropLast(2)
            tvYearAndGenres.text = item.releaseDate.drop(6).plus(" · ").plus(genres)
            if (item.type == SERIE) btnPlay.text = getString(R.string.ver_serie)
            else if (item.type == MOVIE) {
                btnPlay.text = getString(R.string.ver_pelicula)
                tvDuration.visibility = View.VISIBLE
                tvDuration.text = "$"
            }

            setTextViews(item)
        }
    }

    private fun setTextViews(item: ContentModel) {
        binding.apply {
            tvTitle.text = item.title
            tvSynopsis.text = item.synopsis
            tvDuration.text =
                getTextFromDetail(R.string.duration, item.duration.toString().plus(" min"))
            tvDirector.text = getTextFromDetail(R.string.director, item.director)
            tvReleaseDate.text = getTextFromDetail(R.string.releaseDate, item.releaseDate)
            tvClassification.text = getTextFromDetail(R.string.classification, item.classification)
            tvProducerList.text = getTextFromDetail(
                R.string.producerList,
                item.producersList.toString().drop(1).dropLast(1)
            )
            tvDistribution.text = getTextFromDetail(R.string.distribution, item.distribution)
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