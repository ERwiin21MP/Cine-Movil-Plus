package com.erwiin21mp.cinemovilplus.ui.view.profile

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.VISIBLE
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle.*
import androidx.lifecycle.Lifecycle.State.*
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.loadImage
import com.erwiin21mp.cinemovilplus.core.ext.navigateToLogin
import com.erwiin21mp.cinemovilplus.core.ext.toast
import com.erwiin21mp.cinemovilplus.data.network.firebase.AuthManager
import com.erwiin21mp.cinemovilplus.data.network.firebase.FirestoreManager
import com.erwiin21mp.cinemovilplus.data.network.firebase.LogDataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.FragmentProfileBinding
import com.erwiin21mp.cinemovilplus.ui.utils.Win
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var auth: AuthManager
    private val database = LogDataBaseManager()
    private val profileViewModel: ProfileViewModel by viewModels()

    @Inject
    lateinit var win: Win

    @Inject
    lateinit var db: FirestoreManager

    private companion object {
        const val BUTTON_CANCEL = "Cancel"
        const val BUTTON_VALE = "Vale"
        const val BUTTON_SIGN_OUT = "SignOut"
    }

    private val binding get() = _binding!!

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        auth = AuthManager(requireContext())
        initUI()
    }

    private fun initUI() {
        initObservers()
        initListeners()
    }

    @SuppressLint("SetTextI18n")
    private fun initObservers() {
        lifecycleScope.launch {
            repeatOnLifecycle(STARTED) {
                profileViewModel.profilePhotoURL.observe(viewLifecycleOwner) { url ->
                    binding.ivProfilePhoto.apply {
                        loadImage(
                            url = url,
                            errorImage = R.drawable.ic_guest,
                            onSuccess = { binding.ivProfilePhoto.visibility = VISIBLE }
                        )
                        visibility = VISIBLE
                    }
                }
                profileViewModel.userName.observe(viewLifecycleOwner) { userName ->
                    binding.tvUserName.apply {
                        text = userName
                        visibility = VISIBLE
                    }
                }
                profileViewModel.email.observe(viewLifecycleOwner) { email ->
                    binding.tvUserEmail.apply {
                        text = email
                        visibility = VISIBLE
                    }
                }
                profileViewModel.uid.observe(viewLifecycleOwner) { uid ->
                    binding.tvUID.apply {
                        text = "UID: $uid"
                        visibility = VISIBLE
                    }
                }
            }
        }
    }

    private fun initListeners() {
        binding.btnSignOut.setOnClickListener {
            database.logButtonClicked(BUTTON_SIGN_OUT, auth.getCurrentUser())
            logOut()
        }
    }

    @SuppressLint("InflateParams")
    private fun logOut() {
        val view = LayoutInflater.from(context).inflate(R.layout.alert_sign_out, null)
        val btnCancel: Button = view.findViewById(R.id.btnCancel)
        val btnVale: Button = view.findViewById(R.id.btnVale)
        var dialog: Dialog

        AlertDialog.Builder(requireContext(), R.style.AlertWithOutBackground).apply {
            setView(view)
            create()
            dialog = show()
        }

        btnCancel.setOnClickListener {
            database.logButtonClicked(BUTTON_CANCEL, auth.getCurrentUser())
            dialog.dismiss()
        }

        btnVale.setOnClickListener {
            database.logButtonClicked(BUTTON_VALE, auth.getCurrentUser())
            lifecycleScope.launch {
                database.logSignOut(auth.getCurrentUser())
                auth.signOut()
                withContext(Dispatchers.Main) {
                    dialog.dismiss()
                    toast(getString(R.string.signOutMessage))
                    navigateToLogin()
                    requireActivity().finish()
                }
            }
        }
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