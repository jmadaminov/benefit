package com.example.benefit.ui.main.profile.settings_bsd

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.benefit.R
import com.example.benefit.ui.auth.login.LoginViewModel
import com.example.benefit.ui.base.BaseFragment
import com.example.benefit.util.AppPrefs
import kotlinx.android.synthetic.main.fragment_settings_main.*

/**
 * Created by jahon on 03-Sep-20
 */
class SettingsMainFragment : BaseFragment(R.layout.fragment_settings_main) {


    private val viewModel: LoginViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        attachListeners()
        subscribeObservers()
    }

    private fun setupViews() {
        edtSurname.setText(AppPrefs.lastName)
        edtName.setText(AppPrefs.firstName)
        lblPhoneNum.setText(AppPrefs.phoneNumber)
        cardPhoto.setBackgroundResource(R.drawable.shape_oval)
        cardPhotoIcon.setBackgroundResource(R.drawable.shape_oval)
    }

    private fun subscribeObservers() {
    }

    private fun attachListeners() {

        llChangePhoneNum.setOnClickListener {
            findNavController().navigate(R.id.action_profileSettingsMainFragment_to_profileSettingsCodeFragment)
        }
        tvChangeCode.setOnClickListener {
            findNavController().navigate(R.id.action_profileSettingsMainFragment_to_profileSettingsChangeCodeFragment)
        }
        tvChangeLang.setOnClickListener {
            findNavController().navigate(R.id.action_profileSettingsMainFragment_to_profileSettingsLangFragment)
        }

    }

}