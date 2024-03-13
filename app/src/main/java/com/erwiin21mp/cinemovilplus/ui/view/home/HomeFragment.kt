package com.erwiin21mp.cinemovilplus.ui.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.isNull
import com.erwiin21mp.cinemovilplus.core.ext.navigateToContent
import com.erwiin21mp.cinemovilplus.databinding.FragmentHomeBinding
import com.erwiin21mp.cinemovilplus.ui.utils.SpacingItemDecoration
import com.erwiin21mp.cinemovilplus.ui.view.home.content.ContentAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.genders.GenderAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.platforms.PlatformsAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.viewPager2.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.abs

@SuppressLint("SetTextI18n")
@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding get() = _binding!!
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()
    private var sizeOfListContentFeatured = 0
    private val adapterViewPager = ViewPagerAdapter { navigateToContent(it) }
    private val adapterPlatform = PlatformsAdapter { navigateToGenderOrPlatform(it) }
    private val adapterGender = GenderAdapter { navigateToGenderOrPlatform(it.toString()) }
    private val adapterAllContent = ContentAdapter { navigateToContent(it) }
    private val adapterCurrentYear = ContentAdapter { navigateToContent(it) }
    private val adapterCineMovilPlusNews = ContentAdapter { navigateToContent(it) }

    companion object {
        const val TIME_VIEW_PAGER_CHANGE_ITEM = 3000
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initObservers()
        initHandler()
        initRunnable()
        initViewPager2()
        initPlatforms()
        initGenders()
        initAllContent()
        initCurrentYear()
        initCineMovilPlusNews()
    }

    private fun initCineMovilPlusNews() {
        binding.homeContainerCineMovilPlusNews.rvCineMovilPlusNews.apply {
            adapter = adapterCineMovilPlusNews
            setDecorationAndLayoutManagerToRecyclerView(this)
        }
    }

    private fun initCurrentYear() {
        binding.homeContainerCurrentYear.rvCurrentYear.apply {
            adapter = adapterCurrentYear
            setDecorationAndLayoutManagerToRecyclerView(this)
        }
    }

    private fun initAllContent() {
        binding.homeContainerAllContent.rvAllContent.apply {
            adapter = adapterAllContent
            setDecorationAndLayoutManagerToRecyclerView(this)
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(STARTED) {
                homeViewModel.listOfContent.observe(viewLifecycleOwner) { contentList ->
                    if (contentList.isNotEmpty()) {
                        binding.homeContainerViewPager2.apply {
                            loadingViewPager2.visibility = View.GONE
                            containerViewPager2.visibility = View.VISIBLE
                        }
                        sizeOfListContentFeatured = contentList.size
                        adapterViewPager.updateList(contentList)
                        setUpIndicator()
                    }
                }
                homeViewModel.listOfPlatforms.observe(viewLifecycleOwner) { platformList ->
                    if (platformList.isNotEmpty()) {
                        binding.homeContainerPlatforms.apply {
                            containerStreamingPlatforms.visibility = View.GONE
                            tvLabelStreamingPlatforms.visibility = View.VISIBLE
                            rvPlatforms.visibility = View.VISIBLE
                        }
                        adapterPlatform.updateList(platformList)
                    }
                }
                homeViewModel.listOfGenders.observe(viewLifecycleOwner) { genderList ->
                    if (genderList.isNotEmpty()) {
                        binding.homeContainerGenders.apply {
                            loadingGenders.visibility = View.GONE
                            tvLabelGenders.visibility = View.VISIBLE
                            rvGenders.visibility = View.VISIBLE
                        }
                        adapterGender.updateList(genderList)
                    }
                }
                homeViewModel.listAllContent.observe(viewLifecycleOwner) { allContentList ->
                    if (allContentList.isNotEmpty()) {
                        binding.homeContainerAllContent.apply {
                            loadingAllContent.visibility = View.GONE
                            tvLabelAllContent.visibility = View.VISIBLE
                            rvAllContent.visibility = View.VISIBLE
                        }
                        adapterAllContent.updateList(allContentList)
                    }
                }
                homeViewModel.listCurrentYear.observe(viewLifecycleOwner) { currentYearList ->
                    if (currentYearList.isNotEmpty()) {
                        binding.homeContainerCurrentYear.apply {
                            loadingCurrentYear.visibility = View.GONE
                            tvLabelCurrentYear.visibility = View.VISIBLE
                            rvCurrentYear.visibility = View.VISIBLE
                            tvLabelCurrentYear.text =
                                "${tvLabelCurrentYear.context.getString(R.string.moviesAndSeries)} ${currentYearList.first().releaseDate}"
                        }
                        adapterCurrentYear.updateList(currentYearList)
                    }
                }
                homeViewModel.listCineMovilPlusNews.observe(viewLifecycleOwner) { cineMovilPlusList ->
                    if (cineMovilPlusList.isNotEmpty()) {
                        binding.homeContainerCineMovilPlusNews.apply {
                            loadingCineMovilPlusNews.visibility = View.GONE
                            tvLabelCineMovilPlusNews.visibility = View.VISIBLE
                            rvCineMovilPlusNews.visibility = View.VISIBLE
                        }
                        adapterCineMovilPlusNews.updateList(cineMovilPlusList)
                    }
                }
            }
        }
    }

    private fun initGenders() {
        binding.homeContainerGenders.rvGenders.apply {
            adapter = adapterGender
            setDecorationAndLayoutManagerToRecyclerView(this)
        }
    }

    private fun initPlatforms() {
        binding.homeContainerPlatforms.rvPlatforms.apply {
            adapter = adapterPlatform
            setDecorationAndLayoutManagerToRecyclerView(this)
        }
    }

    private fun setDecorationAndLayoutManagerToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_8)))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initViewPager2() {
        binding.homeContainerViewPager2.vp2FeaturedContent.apply {
            adapter = adapterViewPager
            offscreenPageLimit = 3
            setPageTransformer(getTransformer())
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (!handler.isNull()) {
                        handler.removeCallbacks(runnable)
                        handler.postDelayed(runnable, TIME_VIEW_PAGER_CHANGE_ITEM.toLong())
                    }
                }
            })
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }
        setUpIndicator()
    }

    private fun getTransformer(): ViewPager2.PageTransformer {
        return CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                page.scaleY = 0.85f + (1 - abs(position)) * 0.14f
            }
        }
    }

    private fun setUpIndicator() {
        binding.homeContainerViewPager2.apply { ci3.setViewPager(vp2FeaturedContent) }
    }

    private fun initHandler() {
        handler = Handler(Looper.myLooper()!!)
    }

    private fun initRunnable() {
        runnable = Runnable {
            binding.homeContainerViewPager2.apply {
                vp2FeaturedContent.currentItem = vp2FeaturedContent.currentItem + 1
                if ((vp2FeaturedContent.currentItem + 1) == sizeOfListContentFeatured)
                    lifecycleScope.launch {
                        withContext(Dispatchers.IO) {
                            Thread.sleep(TIME_VIEW_PAGER_CHANGE_ITEM.toLong())
                        }
                        vp2FeaturedContent.currentItem = 0
                    }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    private fun navigateToGenderOrPlatform(word: String) {

    }

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    override fun onResume() {
        handler.postDelayed(runnable, TIME_VIEW_PAGER_CHANGE_ITEM.toLong())
        super.onResume()
    }
}