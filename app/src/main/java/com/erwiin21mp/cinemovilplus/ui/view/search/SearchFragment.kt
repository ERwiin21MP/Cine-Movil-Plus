package com.erwiin21mp.cinemovilplus.ui.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo.IME_ACTION_SEARCH
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.logData
import com.erwiin21mp.cinemovilplus.core.ext.logList
import com.erwiin21mp.cinemovilplus.core.ext.loseFocusAfterAction
import com.erwiin21mp.cinemovilplus.core.ext.onTextChanged
import com.erwiin21mp.cinemovilplus.core.ext.removeAccents
import com.erwiin21mp.cinemovilplus.databinding.FragmentSearchBinding
import com.erwiin21mp.cinemovilplus.domain.model.ContentHomeModel
import com.erwiin21mp.cinemovilplus.domain.model.ContentSearchModel
import com.erwiin21mp.cinemovilplus.ui.utils.SpacingItemDecoration
import com.erwiin21mp.cinemovilplus.ui.view.home.content.ContentAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private var _binding: FragmentSearchBinding? = null
    private val adapterContent = ContentAdapter {}
    private var listOfContent = mutableListOf<ContentSearchModel>()

    private fun initUI() {
        initContentRecyclerView()
        initUIState()
        initListeners()
    }

    private fun initListeners() {
        binding.etSearch.onTextChanged { search(it) }
        binding.etSearch.loseFocusAfterAction(IME_ACTION_SEARCH)
    }

    private fun search(search: String) {
        val list = if (search.isEmpty()) getListHomeModel(listOfContent)
        else getListHomeModel(
            listOfContent.filter { content ->
                with(content) {
                    listOf(
                        id,
                        keywords,
                        genres,
                        type.toString(),
                        uploadDate.toString(),
                        title,
                        originalTitle,
                        synopsis,
                        productionCompanies,
                        productionCountries,
                        releaseDate,
                        verticalImageURL,
                        createdBy,
                        tagline,
                        country,
                        platforms
                    )
                }.any {
                    it.orEmpty().lowercase().removeAccents()
                        .contains(search.lowercase().removeAccents())
                }
            })
        adapterContent.updateList(list)
    }

    private fun initContentRecyclerView() {
        binding.rvContentSearch.apply {
            adapter = adapterContent
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_recycler_view)))
            layoutManager = GridLayoutManager(context, 3)
        }
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                searchViewModel.listOfContent.observe(viewLifecycleOwner) { contentList ->
                    if (contentList.isNotEmpty()) {
                        binding.apply {
                            loadingSearch.visibility = GONE
                            tilSearch.visibility = VISIBLE
                            rvContentSearch.visibility = VISIBLE
                            bannerAdSearch.visibility = VISIBLE
                            listOfContent = contentList.toMutableList()
                            adapterContent.updateList(getListHomeModel(listOfContent))
                            logData("FRAGMENT----------------------------------------------")
                            logList(contentList.sortedBy { it.title })
                        }
                    }
                }
            }
        }
    }

    private fun getListHomeModel(list: List<ContentSearchModel>) = list.map {
        ContentHomeModel(
            id = it.id,
            verticalImageURL = it.verticalImageURL,
            isCameraQuality = it.isCameraQuality,
            title = it.title,
            type = it.type
        )
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}