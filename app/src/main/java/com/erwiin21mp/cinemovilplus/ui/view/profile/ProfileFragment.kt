package com.erwiin21mp.cinemovilplus.ui.view.profile

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.data.network.AuthManager
import com.erwiin21mp.cinemovilplus.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private val auth = AuthManager()

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    private fun initUI() {
        loadProfileImage()
        loadUIDProfile()
    }

    @SuppressLint("SetTextI18n")
    private fun loadUIDProfile() {
        binding.tvUID.text = "UID: ${auth.getCurrentUser()!!.uid}"
    }

    private fun loadProfileImage() {
        Picasso.get().load(auth.getCurrentUser()!!.photoUrl).error(R.drawable.ic_user)
            .transform(CropCircleTransformation()).into(binding.ivProfilePhoto)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return binding.root
    }
}