package com.erwiin21mp.cinemovilplus.ui.view.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.databinding.FragmentSearchBinding
import com.erwiin21mp.cinemovilplus.ui.utils.SpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class SearchFragment : Fragment() {

    private val binding get() = _binding!!
    private val searchViewModel: SearchViewModel by viewModels()
    private var _binding: FragmentSearchBinding? = null

    private fun initUI() {

    }

//    private fun search(search: String) {
//        val list = if (search.isEmpty()) listOfContent
//        else listOfContent.filter { content ->
//            with(content) {
//                listOf(
//                    id.toString(),
//                    title,
//                    synopsis,
//                    genres.toString(),
//                    platformsList.toString(),
//                    producersList.toString(),
//                    type,
//                    keywords
//                )
//            }.any { it.lowercase().removeAccents().contains(search.lowercase().removeAccents()) }
//        }
//
//        adapterContent.updateList(list)
//    }

    private fun initContentRecyclerView() {
        binding.rvContentSearch.apply {
//            adapter = adapterContent
            addItemDecoration(SpacingItemDecoration(resources.getDimensionPixelSize(R.dimen.spacing_8)))
            layoutManager = GridLayoutManager(context, 3)
        }
    }

//    private fun initUIState() {
//        lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
//                searchViewModel.listOfContent.observe(viewLifecycleOwner) {
//                    listOfContent = it
//                    adapterContent.updateList(listOfContent)
//                    if (listOfContent.isNotEmpty()) {
//                        binding.apply {
//                            llLoadingSearchFragment.visibility = View.GONE
//                            clContainerSearch.visibility = View.VISIBLE
//                        }
//                    }
//                }
//            }
//        }
//    }

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