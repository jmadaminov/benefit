package com.example.benefit.ui.transactions_history.transaction_bsd

import android.content.Context
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.benefit.R
import com.example.benefit.remote.models.FriendDTO
import com.example.benefit.remote.models.FriendDTOs
import com.example.benefit.remote.models.TransactionDTO
import com.example.benefit.ui.main.home.HomeFragment
import com.example.benefit.ui.viewgroups.FriendItem
import com.example.benefit.ui.viewgroups.ItemTransactionTxtOnly
import com.example.benefit.util.SizeUtils
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_transaction_share_payment.*
import javax.inject.Inject
import kotlin.random.Random


/**
 * Created by jahon on 03-Sep-20
 */
@AndroidEntryPoint
class TransactionSharePaymentFragment @Inject constructor() :
    Fragment(R.layout.fragment_transaction_share_payment) {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    val args: TransactionSharePaymentFragmentArgs by navArgs()
    private val viewModel: TransactionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViews()

        attachListeners()
        subscribeObservers()
    }

    private fun setupViews() {

        setupContacts()

    }

    private fun setupContacts() {

        rvContacts.adapter = adapter

        val data = listOf(
            FriendDTO("Алексей", "Иванов", "", ""),
            FriendDTO("Константин", "Игнатьев", "", ""),
            FriendDTO("Марина", "Авазова", "", ""),
            FriendDTO("Екатерина", "Петрова", "", ""),
            FriendDTO("Марат", "Измайлов", "", ""),
            FriendDTO("Валентин", "Автухов", "", ""),
        )

        data.forEach {
            adapter.add(FriendItem(it) {
            })
        }


    }

    private fun subscribeObservers() {


    }

    private fun attachListeners() {
        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }
        ivLocation.setOnClickListener {
            findNavController().navigate(
                TransactionSharePaymentFragmentDirections.actionTransactionSharePaymentFragmentToTransactionSearchFriendNearbyFragment()
            )
        }

        tvSelect.setOnClickListener {
            val contacts = FriendDTOs()

            for (i in 0 until adapter.itemCount) {
                contacts.add((adapter.getItem(i) as FriendItem).friend)
            }

            findNavController().navigate(
                TransactionSharePaymentFragmentDirections.actionTransactionSharePaymentFragmentToTransactionSharePaymentSelectedFragment(
                    contacts
                )
            )
        }
    }


}
