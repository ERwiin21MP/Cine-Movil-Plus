package com.erwiin21mp.cinemovilplus.ui.view.home

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
import com.erwiin21mp.cinemovilplus.ui.view.home.genders.GenderAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.platforms.PlatformsAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.viewPager2.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

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
    }

    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(STARTED) {
                homeViewModel.listOfContent.observe(viewLifecycleOwner) { contentList ->
                    if (contentList.isNotEmpty()) {
                        binding.apply {
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
                        binding.apply {
                            containerStreamingPlatforms.visibility = View.GONE
                            tvLabelStreamingPlatforms.visibility = View.VISIBLE
                            rvPlatforms.visibility = View.VISIBLE
                        }
                        adapterPlatform.updateList(platformList)
                    }
                }
                homeViewModel.listOfGenders.observe(viewLifecycleOwner) { genderList ->
                    if (genderList.isNotEmpty()) {
                        binding.apply {
                            loadingGenders.visibility = View.GONE
                            tvLabelGenders.visibility = View.VISIBLE
                            rvGenders.visibility = View.VISIBLE
                        }
                        adapterGender.updateList(genderList)
                    }
                }
            }
        }
    }

    private fun initGenders() {
        binding.rvGenders.apply {
            adapter = adapterGender
            setDecorationAndLayoutManagerToRecyclerView(this)
        }
    }

    private fun initPlatforms() {
        binding.rvPlatforms.apply {
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
        binding.vp2FeaturedContent.apply {
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
                page.scaleY = 0.85f + (1 - kotlin.math.abs(position)) * 0.14f
            }
        }
    }

    private fun setUpIndicator() {
        binding.ci3.setViewPager(binding.vp2FeaturedContent)
    }

    private fun initHandler() {
        handler = Handler(Looper.myLooper()!!)
    }

    private fun initRunnable() {
        runnable = Runnable {
            binding.vp2FeaturedContent.currentItem = binding.vp2FeaturedContent.currentItem + 1
            if ((binding.vp2FeaturedContent.currentItem + 1) == sizeOfListContentFeatured)
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        Thread.sleep(TIME_VIEW_PAGER_CHANGE_ITEM.toLong())
                    }
                    binding.vp2FeaturedContent.currentItem = 0
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