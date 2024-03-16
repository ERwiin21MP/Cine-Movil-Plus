package com.erwiin21mp.cinemovilplus.ui.view.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
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
import com.erwiin21mp.cinemovilplus.ui.view.home.collection.CollectionAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.content.ContentAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.gender.GenderAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.platforms.PlatformsAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.viewPager2.ViewPagerAdapter
import com.facebook.shimmer.ShimmerFrameLayout
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
    private val adapterCollection = CollectionAdapter { }

    companion object {
        const val TIME_VIEW_PAGER_CHANGE_ITEM = 3000
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initHandler()
        initRunnable()
        initViewPager2()
        initObservers()
        initUIRecyclerViews()
    }

    private fun initUIRecyclerViews() {
        binding.apply {
            for ((recyclerView, adapter) in listOf(
                homeContainerPlatforms.rvPlatforms to adapterPlatform,
                homeContainerAllContent.rvAllContent to adapterAllContent,
                homeContainerCurrentYear.rvCurrentYear to adapterCurrentYear,
                homeContainerGenders.rvGenders to adapterGender,
                homeContainerCineMovilPlusNews.rvCineMovilPlusNews to adapterCineMovilPlusNews,
                homeContainerCollection.rvCollection to adapterCollection
            )) {
                initRecyclerView(recyclerView, adapter)
            }

        }
    }

    private fun initRecyclerView(rv: RecyclerView, adapter: RecyclerView.Adapter<*>) {
        rv.apply {
            this.adapter = adapter
            setDecorationAndLayoutManagerToRecyclerView(this)
        }
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(STARTED) {
                homeViewModel.apply {
                    listOfFeaturedContent.observe(viewLifecycleOwner) { contentList ->
                        if (contentList.isNotEmpty()) {
                            binding.homeContainerViewPager2.apply {
                                loadingViewPager2.visibility = View.GONE
                                contentViewPager2.visibility = View.VISIBLE
                            }
                            sizeOfListContentFeatured = contentList.size
                            adapterViewPager.updateList(contentList)
                            binding.homeContainerViewPager2.ci3.setViewPager(binding.homeContainerViewPager2.vp2FeaturedContent)
                        }
                    }
                    listOfPlatforms.observe(viewLifecycleOwner) { platformList ->
                        if (platformList.isNotEmpty()) {
                            binding.homeContainerPlatforms.apply {
                                showContainer(loadingPlatforms, tvLabelPlatforms, rvPlatforms)
                            }
                            adapterPlatform.updateList(platformList)
                        }
                    }
                    listOfGenders.observe(viewLifecycleOwner) { genderList ->
                        if (genderList.isNotEmpty()) {
                            binding.homeContainerGenders.apply {
                                showContainer(loadingGenders, tvLabelGenders, rvGenders)
                            }
                            adapterGender.updateList(genderList)
                        }
                    }
                    listAllContent.observe(viewLifecycleOwner) { allContentList ->
                        if (allContentList.isNotEmpty()) {
                            binding.homeContainerAllContent.apply {
                                showContainer(loadingAllContent, tvLabelAllContent, rvAllContent)
                            }
                            adapterAllContent.updateList(allContentList)
                        }
                    }
                    listCurrentYear.observe(viewLifecycleOwner) { currentYearList ->
                        if (currentYearList.isNotEmpty()) {
                            binding.homeContainerCurrentYear.apply {
                                showContainer(loadingCurrentYear, tvLabelCurrentYear, rvCurrentYear)
                                tvLabelCurrentYear.text =
                                    "${tvLabelCurrentYear.context.getString(R.string.moviesAndSeries)} ${currentYearList.first().releaseDate}"
                            }
                            adapterCurrentYear.updateList(currentYearList)
                        }
                    }
                    listCineMovilPlusNews.observe(viewLifecycleOwner) { cineMovilPlusList ->
                        if (cineMovilPlusList.isNotEmpty()) {
                            binding.homeContainerCineMovilPlusNews.apply {
                                showContainer(
                                    loadingCineMovilPlusNews,
                                    tvLabelCineMovilPlusNews,
                                    rvCineMovilPlusNews
                                )
                            }
                            adapterCineMovilPlusNews.updateList(cineMovilPlusList)
                        }
                    }
                    listOfCollections.observe(viewLifecycleOwner) { collectionsList ->
                        if (collectionsList.isNotEmpty()) {
                            binding.homeContainerCollection.apply {
                                showContainer(loadingCollection, tvLabelCollection, rvCollection)
                            }
                            adapterCollection.updateList(collectionsList)
                        }
                    }
                }
            }
        }
    }

    private fun showContainer(load: ShimmerFrameLayout, tv: TextView, rv: RecyclerView) {
        load.visibility = View.GONE
        tv.visibility = View.VISIBLE
        rv.visibility = View.VISIBLE
    }

    private fun setDecorationAndLayoutManagerToRecyclerView(recyclerView: RecyclerView) {
        recyclerView.apply {
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_recycler_view)))
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
            binding.homeContainerViewPager2.ci3.setViewPager(this)
        }
    }

    private fun getTransformer(): ViewPager2.PageTransformer {
        return CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                page.scaleY = 0.85f + (1 - abs(position)) * 0.14f
            }
        }
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