package com.example.benefit.ui.transactions_history.transaction_bsd

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import com.example.benefit.R
import com.example.benefit.remote.models.TransactionDTO
import com.example.benefit.util.MyBSDialog
import com.example.benefit.util.MyNestedScrollBSDialog
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bsd_transaction.*


@AndroidEntryPoint
class TransactionBSD : MyNestedScrollBSDialog() {

    companion object {
        const val ARG_TRANSACTION_DTO = "TRANSACTION_DTO"
    }

    lateinit var transactionDTO: TransactionDTO
    private val viewModel: TransactionViewModel by viewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)

        transactionDTO = requireArguments().getParcelable(ARG_TRANSACTION_DTO)!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bsd_transaction, null)

        findNavController().setGraph(R.navigation.transaction_nav_graph, Bundle().apply {
            putParcelable(ARG_TRANSACTION_DTO, transactionDTO)
        })

        return view
    }


    fun navigateToCardTransfer() {
//        nav_host_fragment.findNavController()
//            .navigate(R.id.action_fillCardFragment_to_cardMakeDepositFromAnyCardFragment)
    }

}