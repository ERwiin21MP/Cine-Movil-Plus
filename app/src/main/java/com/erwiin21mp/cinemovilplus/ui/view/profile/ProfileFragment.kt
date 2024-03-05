package com.erwiin21mp.cinemovilplus.ui.view.profile

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.erwiin21mp.cinemovilplus.R
import com.erwiin21mp.cinemovilplus.core.ext.navigateToLogin
import com.erwiin21mp.cinemovilplus.core.ext.toast
import com.erwiin21mp.cinemovilplus.data.network.firebase.AuthManager
import com.erwiin21mp.cinemovilplus.data.network.firebase.DataBaseManager
import com.erwiin21mp.cinemovilplus.databinding.FragmentProfileBinding
import com.squareup.picasso.Picasso
import dagger.hilt.android.AndroidEntryPoint
import jp.wasabeef.picasso.transformations.CropCircleTransformation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@AndroidEntryPoint
class ProfileFragment : Fragment() {

    private var _binding: FragmentProfileBinding? = null
    private lateinit var auth: AuthManager
    private val database = DataBaseManager()

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
        loadUIDProfile()
        initListeners()
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

    @SuppressLint("SetTextI18n")
    private fun loadUIDProfile() {
        val user = auth.getCurrentUser()
        binding.apply {
            tvUID.text = "UID: ${user!!.uid}"

            var displayName = user.displayName
            val email = user.email

            if (user.isAnonymous) {
                displayName = context?.getString(R.string.userAnonymous)
                tvUserEmail.visibility = View.GONE
            } else Picasso.get().load(user.photoUrl).error(R.drawable.ic_user)
                .transform(CropCircleTransformation()).into(ivProfilePhoto)
            tvUserName.text = displayName
            tvUserEmail.text = email
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