package com.erwiin21mp.cinemovilplus.ui.view.home

import android.graphics.Typeface
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
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
import com.erwiin21mp.cinemovilplus.core.isNull
import com.erwiin21mp.cinemovilplus.core.logData
import com.erwiin21mp.cinemovilplus.databinding.FragmentHomeBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.erwiin21mp.cinemovilplus.domain.model.LabelContentModel
import com.erwiin21mp.cinemovilplus.domain.model.PlatformModel
import com.erwiin21mp.cinemovilplus.ui.utils.SpacingItemDecoration
import com.erwiin21mp.cinemovilplus.ui.view.home.content.ContentAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.genders.GendersAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.platforms.PlatformAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.viewmodel.HomeViewModel
import com.erwiin21mp.cinemovilplus.ui.view.home.viewmodel.HomeViewModel.Companion.PREFIX_SAGA
import com.erwiin21mp.cinemovilplus.ui.view.home.viewpager2.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding get() = _binding!!
    private lateinit var adapterViewPager: ViewPagerAdapter
    private lateinit var adapterPlatform: PlatformAdapter
    private lateinit var adapterGender: GendersAdapter
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()
    private var listOfContentFeatured: List<ContentInitModel> = emptyList()
    private var listOfPlatforms: List<PlatformModel> = emptyList()
    private var listOfYears: List<String> = emptyList()
    private var listOfSagas: List<String> = emptyList()

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
        initPlatforms()
        initGenders()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(STARTED) {
                homeViewModel.listOfContent.observe(viewLifecycleOwner) { contentList ->
                    listOfContentFeatured = contentList
                    adapterViewPager.updateList(listOfContentFeatured)
                    setUpIndicator()
                    getGendersList(contentList)
                    listOfYears = getYears(contentList)
                    listOfSagas = getListOfSagas(contentList)
                    setContent(contentList)
                }

                homeViewModel.listOfPlatforms.observe(viewLifecycleOwner) {
                    listOfPlatforms = it
                    adapterPlatform.updateList(listOfPlatforms)
                }
            }
        }
    }

    private fun setContent(contentList: List<ContentInitModel>) {
        var listOfLabelsAndContent: MutableList<LabelContentModel> = mutableListOf()
        listOfLabelsAndContent.add(
            LabelContentModel(
                titleList = getString(R.string.allContent),
                contentList = contentList.sortedBy { it.title })
        )

        setViews(listOfLabelsAndContent)
    }

    private fun setViews(listOfLabels: MutableList<LabelContentModel>) {
        listOfLabels.forEach { item ->
            binding.llContainer.addView(setUpTextViewGender(item.titleList))
            binding.llContainer.addView(setUpRecyclerView(item.contentList))
        }
    }

    private fun setUpRecyclerView(contentList: List<ContentInitModel>): RecyclerView {
        val rvGender = RecyclerView(requireContext())
        rvGender.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.MATCH_PARENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        rvGender.setPadding(0, resources.getDimensionPixelSize(R.dimen.spacing_8), 0, 0)
        setLabelsAdapters(rvGender, contentList)
        return rvGender
    }

    private fun setLabelsAdapters(rv: RecyclerView, contentList: List<ContentInitModel>) {
        rv.addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_8)))
        rv.layoutManager = LinearLayoutManager(rv.context, LinearLayoutManager.HORIZONTAL, false)
        rv.adapter = ContentAdapter(contentList) { navigateToContent(it) }
    }

    private fun setUpTextViewGender(titleList: String): TextView {
        val tvGender = TextView(context)
        tvGender.layoutParams = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            LinearLayout.LayoutParams.WRAP_CONTENT
        )

        if (titleList.length == 4) tvGender.text =
            getString(R.string.moviesAndSeries).plus(" ").plus(titleList)
        else tvGender.text = titleList
        tvGender.textSize = 17f
        tvGender.setTypeface(null, Typeface.BOLD)
        tvGender.setPadding(0, resources.getDimensionPixelSize(R.dimen.spacing_20), 0, 0)
        return tvGender
    }

    private fun getListOfSagas(content: List<ContentInitModel>): List<String> {
        val list = arrayListOf<String>()
        content.forEach { contentModel ->
            contentModel.genres.forEach { gender ->
                if (gender[0].toString() == PREFIX_SAGA) list.add(
                    gender.removeRange(0, 1)
                )
            }
        }
        return list.distinct().toList().sorted()
    }

    private fun getYears(content: List<ContentInitModel>): List<String> {
        val list = arrayListOf<Short>()
        val years = arrayListOf<String>()
        content.forEach { list.add(it.releaseDate.split("/")[2].toShort()) }
        list.sortDescending()
        list.forEach { years.add(it.toString()) }
        return years.distinct().toList()
    }

    private fun getGendersList(list: List<ContentInitModel>) {
        var listOfGenders: MutableList<GenderModel> = mutableListOf()
        list.forEach { content ->
            content.genres.forEach { gender ->
                if (gender[0].toString() != PREFIX_SAGA) {
                    listOfGenders.add(
                        GenderModel(
                            gender = gender.removeRange(0, 1),
                            urlPicture = content.horizontalImageUrl
                        )
                    )
                }
            }
        }
        listOfGenders.sortBy { it.gender }
        listOfGenders = listOfGenders.distinctBy { it.gender }.toMutableList()
        listOfGenders.forEach {
            logData(it.toString())
        }

        adapterGender.updateList(listOfGenders)
    }

    private fun initGenders() {
        adapterGender = GendersAdapter { navigateToGender(it) }
        binding.rvGenders.apply {
            addItemDecoration(
                SpacingItemDecoration(
                    resources.getDimensionPixelSize(
                        R.dimen.spacing_8
                    )
                )
            )
            adapter = adapterGender
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    private fun initPlatforms() {
        adapterPlatform = PlatformAdapter(listOfPlatforms) { navigateToPlatform(it) }

        binding.rvPlatforms.addItemDecoration(
            SpacingItemDecoration(
                resources.getDimensionPixelSize(
                    R.dimen.spacing_8
                )
            )
        )
        binding.rvPlatforms.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvPlatforms.adapter = adapterPlatform
    }

    private fun navigateToGender(gender: String) {

    }

    private fun navigateToPlatform(platform: String) {

    }

    private fun initViewPager2() {
        val transformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
            addTransformer { page, position ->
                page.scaleY = 0.85f + (1 - kotlin.math.abs(position)) * 0.14f
            }
        }
        adapterViewPager = ViewPagerAdapter() { navigateToContent(it) }
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
            if ((binding.vp2FeaturedContent.currentItem + 1) == listOfContentFeatured.size)
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

    override fun onPause() {
        handler.removeCallbacks(runnable)
        super.onPause()
    }

    override fun onResume() {
        handler.postDelayed(runnable, TIME_VIEW_PAGER_CHANGE_ITEM.toLong())
        super.onResume()
    }
}