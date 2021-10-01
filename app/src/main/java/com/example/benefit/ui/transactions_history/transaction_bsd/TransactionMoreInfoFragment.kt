package com.example.benefit.ui.transactions_history.transaction_bsd

import android.os.Bundle
import android.view.View
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.benefit.R
import com.example.benefit.ui.base.BaseFragment
import com.example.benefit.util.loadImageUrl
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_transaction_more_info.*
import javax.inject.Inject


/**
 * Created by jahon on 03-Sep-20
 */

class TransactionMoreInfoFragment @Inject constructor() :
    BaseFragment(R.layout.fragment_transaction_more_info) {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    val args: TransactionMoreInfoFragmentArgs by navArgs()
    private val viewModel: TransactionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViews()

        attachListeners()
        subscribeObservers()
    }

    private fun setupViews() {
        tvBrandName.text = args.transactionDTO.merchantName
        if (args.transactionDTO.partner?.image.isNullOrBlank()) {
            ivBrandLogo.isVisible = false
        } else {
            ivBrandLogo.isVisible = true
            ivBrandLogo.loadImageUrl(args.transactionDTO.partner!!.image!!)
        }

        tvDenomination.text = args.transactionDTO.merchant

    }

    private fun subscribeObservers() {


    }

    private fun attachListeners() {
        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
    }


}
