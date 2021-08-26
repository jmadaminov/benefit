package com.example.benefit.ui.transactions_history.transaction_bsd

import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.benefit.R
import com.example.benefit.remote.models.FriendDTO
import com.example.benefit.remote.models.FriendsDTOs
import com.example.benefit.ui.base.BaseFragment
import com.example.benefit.ui.viewgroups.FriendItem
import com.example.benefit.ui.viewgroups.FriendItemImgName
import com.example.benefit.util.SizeUtils
import com.google.android.material.snackbar.Snackbar
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_transaction_search_friends_nearby.*
import kotlinx.android.synthetic.main.fragment_transaction_search_friends_nearby.clParent
import kotlinx.android.synthetic.main.fragment_transaction_search_friends_nearby.ivBack
import javax.inject.Inject


/**
 * Created by jahon on 03-Sep-20
 */

class TransactionSearchFriendNearbyFragment @Inject constructor() :
    BaseFragment(R.layout.fragment_transaction_search_friends_nearby) {
    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val horizontalAdapter = GroupAdapter<GroupieViewHolder>()

    //    val args: TransactionSearchFriendNearbyFragmentArgs by navArgs()
    private val viewModel: TransactionViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        setupViews()

        attachListeners()
        subscribeObservers()
    }

    private fun setupViews() {
        clParent.layoutParams = clParent.layoutParams.apply {
            height = SizeUtils.getScreenHeight(requireActivity()) - SizeUtils.getActionBarHeight(
                requireActivity()
            )

        }
        clSearching.visibility = View.VISIBLE
        Handler().postDelayed({
            clSearching.visibility = View.INVISIBLE
        }, 2000)
        setupContacts()

    }

    private fun setupContacts() {

        rvContacts.adapter = adapter
        rvSelected.adapter = horizontalAdapter
        adapter.clear()

        val contacts = listOf(
            FriendDTO("Алексей", "Иванов", "", ""),
            FriendDTO("Константин", "Игнатьев", "", ""),
            FriendDTO("Марина", "Авазова", "", ""),
            FriendDTO("Екатерина", "Петрова", "", ""),
            FriendDTO("Марат", "Измайлов", "", ""),
            FriendDTO("Валентин", "Автухов", "", ""),
        )

        contacts.forEach {
            adapter.add(FriendItem(it) {
                populateSelectedList()
            })
        }
        populateSelectedList()

    }

    private fun populateSelectedList() {
        horizontalAdapter.clear()
        for (i in 0 until adapter.itemCount) {
            if ((adapter.getItem(i) as FriendItem).friend.isChecked) horizontalAdapter.add(
                FriendItemImgName((adapter.getItem(i) as FriendItem).friend)
            )
        }

        if (horizontalAdapter.itemCount == 0) {
            rvSelected.visibility = View.GONE
            tvSelected.visibility = View.GONE
        } else {

            rvSelected.visibility = View.VISIBLE
            tvSelected.visibility = View.VISIBLE
        }

        tvSelected.text = getString(R.string.selected) + " " + horizontalAdapter.itemCount
    }

    private fun subscribeObservers() {


    }

    private fun attachListeners() {
        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }


        tvNext.setOnClickListener {
            if (horizontalAdapter.itemCount == 0) {
                Snackbar.make(
                    clParent,
                    getString(R.string.select_at_least_one_contact),
                    Snackbar.LENGTH_SHORT
                ).show()
            } else {
                val contacts = FriendsDTOs()
                for (i in 0 until horizontalAdapter.itemCount) {
                    contacts.add((horizontalAdapter.getItem(i) as FriendItemImgName).friend)
                }
                findNavController().navigate(
                    TransactionSearchFriendNearbyFragmentDirections.actionTransactionSearchFriendNearbyFragmentToTransactionSharePaymentEndFragment(
                        contacts
                    )
                )
            }


        }


    }

}
