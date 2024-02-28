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
import androidx.core.content.res.ResourcesCompat
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
import com.erwiin21mp.cinemovilplus.data.homeProviders.ContentFeaturedProvider
import com.erwiin21mp.cinemovilplus.data.homeProviders.GendersListProvider
import com.erwiin21mp.cinemovilplus.data.homeProviders.LabelsListProvider
import com.erwiin21mp.cinemovilplus.data.homeProviders.SagasListProvider
import com.erwiin21mp.cinemovilplus.data.homeProviders.YearsListProvider
import com.erwiin21mp.cinemovilplus.databinding.FragmentHomeBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentInitModel
import com.erwiin21mp.cinemovilplus.domain.model.GenderModel
import com.erwiin21mp.cinemovilplus.domain.model.LabelContentModel
import com.erwiin21mp.cinemovilplus.ui.utils.SpacingItemDecoration
import com.erwiin21mp.cinemovilplus.ui.view.home.content.ContentAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.genders.GendersAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.platforms.PlatformAdapter
import com.erwiin21mp.cinemovilplus.ui.view.home.viewpager2.ViewPagerAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val binding get() = _binding!!
    private val adapterViewPager = ViewPagerAdapter { navigateToContent(it.toString()) }
    private val adapterGender = GendersAdapter { navigateToGenderOrPlatform(it) }
    private val adapterPlatform = PlatformAdapter { navigateToGenderOrPlatform(it) }
    private lateinit var runnable: Runnable
    private lateinit var handler: Handler
    private var _binding: FragmentHomeBinding? = null
    private val homeViewModel: HomeViewModel by viewModels()
    private var sizeOfListContentFeatured = 0

    @Inject
    lateinit var gendersListProvider: GendersListProvider

    @Inject
    lateinit var yearsListProvider: YearsListProvider

    @Inject
    lateinit var sagasListProvider: SagasListProvider

    @Inject
    lateinit var labelsListProvider: LabelsListProvider

    @Inject
    lateinit var contentFeatured: ContentFeaturedProvider

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
                    getData(contentList)
                }

                homeViewModel.listOfPlatforms.observe(viewLifecycleOwner) {
                    adapterPlatform.updateList(it)
                }
            }
        }
    }

    private fun getData(contentList: List<ContentInitModel>) {
        val listOfContentFeatured = contentFeatured.getContentFeatured(contentList)
        val listOfGenders = gendersListProvider.getGendersList(contentList)
        val listOfYears = yearsListProvider.getYearsList(contentList)
        val listOfSagas = sagasListProvider.getListOfSagas(contentList)
        val listOfLabels = labelsListProvider.getListOfLabels(contentList, listOfSagas, listOfYears)
        sizeOfListContentFeatured = listOfContentFeatured.size

        if (contentList.isNotEmpty()) {
            binding.apply {
                llLoading.visibility = View.GONE
                rlContainer.visibility = View.VISIBLE
            }
        }

        setData(listOfContentFeatured, listOfGenders, listOfLabels)
    }

    private fun setData(
        listOfContentFeatured: List<ContentInitModel>,
        listOfGenders: MutableList<GenderModel>,
        listOfLabels: List<LabelContentModel>
    ) {
        adapterViewPager.updateList(listOfContentFeatured)
        setUpIndicator()
        adapterGender.updateList(listOfGenders)
        setUpLabels(listOfLabels)
    }

    private fun setUpLabels(listOfLabels: List<LabelContentModel>) {
        binding.llContainer.apply {
            removeAllViews()

            listOfLabels.forEach { item ->
                addView(setUpTextViewGender(item.titleList))
                addView(setUpRecyclerView(item.contentList))
            }
        }
    }

    private fun setUpRecyclerView(listContent: List<ContentInitModel>): RecyclerView {
        return RecyclerView(requireContext()).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            setPadding(0, resources.getDimensionPixelSize(R.dimen.spacing_8), 0, 0)
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_8)))
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = ContentAdapter(listContent) { navigateToContent(it.toString()) }
        }
    }

    private fun setUpTextViewGender(titleList: String): TextView {
        return TextView(context).apply {
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            text = if (titleList.length == 4) getString(R.string.moviesAndSeries).plus(" ")
                .plus(titleList) else titleList
            textSize = 17f
            setTypeface(ResourcesCompat.getFont(context, R.font.montserrat_bold), Typeface.BOLD)
            setPadding(0, resources.getDimensionPixelSize(R.dimen.spacing_20), 0, 0)
        }
    }

    private fun initGenders() {
        binding.rvGenders.apply {
            setDecorationAndLayoutManagerToRecyclerView(this)
            adapter = adapterGender
        }
    }

    private fun initPlatforms() {
        binding.rvPlatforms.apply {
            setDecorationAndLayoutManagerToRecyclerView(this)
            adapter = adapterPlatform
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