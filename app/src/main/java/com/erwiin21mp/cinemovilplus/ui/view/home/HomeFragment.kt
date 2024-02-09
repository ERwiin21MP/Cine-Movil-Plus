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
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.erwiin21mp.cinemovilplus.core.isNull
import com.erwiin21mp.cinemovilplus.core.logData
import com.erwiin21mp.cinemovilplus.databinding.FragmentHomeBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.erwiin21mp.cinemovilplus.ui.view.home.viewmodel.HomeViewModel
import com.erwiin21mp.cinemovilplus.ui.view.home.viewpager2.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null
    private lateinit var runnable: Runnable
    private var listOfInitContent: List<ContentInitModel> = emptyList()
    private var listOfPlatforms: List<String> = emptyList()
    private lateinit var handler: Handler
    private lateinit var adapterViewPager: ViewPagerAdapter

    private val binding get() = _binding!!

    companion object {
        const val TIME_VIEW_PAGER_CHANGE_ITEM = 3000
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initUIState()
        initHandler()
        initRunnable()
        initViewPager2()
    }

    private fun initViewPager2() {
        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                page.scaleY = 0.85f + (1 - kotlin.math.abs(position)) * 0.14f
            }
        }
        adapterViewPager = ViewPagerAdapter(listOfInitContent) { navigateToContent(it) }
        binding.vp2FeaturedContent.apply {
            adapter = adapterViewPager
            offscreenPageLimit = 3
            setPageTransformer(transformer)
            registerOnPageChangeCallback(object :
                ViewPager2.OnPageChangeCallback() {
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)
                    if (!handler.isNull()) {
                        handler.removeCallbacks(runnable)
                        handler.postDelayed(runnable, TIME_VIEW_PAGER_CHANGE_ITEM.toLong())
                    }
                }
            })
            offscreenPageLimit = 3
            clipToPadding = false
            clipChildren = false
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
        }

        setUpIndicator()
    }

    private fun navigateToContent(id: Int) {

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
            if ((binding.vp2FeaturedContent.currentItem + 1) == listOfInitContent.size)
                lifecycleScope.launch {
                    withContext(Dispatchers.IO) {
                        Thread.sleep(TIME_VIEW_PAGER_CHANGE_ITEM.toLong())
                    }
                    binding.vp2FeaturedContent.currentItem = 0
                }
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(STARTED) {
                homeViewModel.listOfContent.observe(viewLifecycleOwner) {
                    listOfInitContent = it
                    adapterViewPager.updateList(listOfInitContent)
                    setUpIndicator()
                    listOfPlatforms = getPlatforms(listOfInitContent)
                }
                homeViewModel.listOfPlatforms.observe(viewLifecycleOwner) {
                    logData(it.toString())
                }
            }
        }
    }

    private fun getPlatforms(content: List<ContentInitModel>): List<String> {
        val list: ArrayList<String> = arrayListOf()
        content.forEach {
            it.platformsList.forEach { it2 ->
                list.add(it2)
            }
        }
        list.sort()
        val list2 = list.distinct().toMutableList()
        logData(list2.toString())
        return list2
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
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