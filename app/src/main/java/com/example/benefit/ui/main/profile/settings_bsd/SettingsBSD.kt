package com.example.benefit.ui.main.profile.settings_bsd

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.benefit.R
import com.example.benefit.ui.main.BenefitBSD

import dagger.hilt.android.AndroidEntryPoint



class SettingsBSD : BenefitBSD() {

    private val viewModel: SettingsBSDViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bsd_profile, null)

        return view
    }


}