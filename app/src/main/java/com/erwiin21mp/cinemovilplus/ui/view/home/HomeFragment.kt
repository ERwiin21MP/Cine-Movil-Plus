package com.erwiin21mp.cinemovilplus.ui.view.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.State.STARTED
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.erwiin21mp.cinemovilplus.databinding.FragmentHomeBinding
import com.erwiin21mp.cinemovilplus.ui.view.home.viewmodel.HomeViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val homeViewModel: HomeViewModel by viewModels()
    private var _binding: FragmentHomeBinding? = null

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        initUIState()
    }

    private fun initUIState() {
        lifecycleScope.launch {
            repeatOnLifecycle(STARTED) {
//                homeViewModel.home.collect {
//                    it.forEach { content ->
//                        binding.tvList.text = (content.title).plus("\n")
//                        Log.e("Erwin", content.toString())
//                    }
//                }
                homeViewModel.listContentInitModel.observe(viewLifecycleOwner, Observer {
                    it.forEach { item ->
                        Log.e("Erwin 3", item.toString())
                        binding.tvList.text = binding.tvList.text.toString().plus(item.title).plus("\n")
                        Toast.makeText(context, it.size.toString(), Toast.LENGTH_LONG).show()
                    }
                })
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
}