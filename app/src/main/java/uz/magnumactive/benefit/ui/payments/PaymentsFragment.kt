package uz.magnumactive.benefit.ui.payments

import android.os.Bundle
import android.view.View
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import uz.magnumactive.benefit.R
import uz.magnumactive.benefit.remote.models.PaynetCategory
import uz.magnumactive.benefit.ui.base.BaseFragment
import uz.magnumactive.benefit.ui.viewgroups.ItemPaynet
import uz.magnumactive.benefit.util.SizeUtils
import uz.magnumactive.benefit.util.setLoadingSpinner
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_payments.*
import javax.inject.Inject


/**
 * Created by jahon on 03-Sep-20
 */

class PaymentsFragment @Inject constructor() : BaseFragment(R.layout.fragment_payments) {

    private val adapter = GroupAdapter<GroupieViewHolder>()
    private val viewModel: PaymentsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupViews()
        attachListeners()
        subscribeObservers()

        viewModel.paynetCatgResp.value?.let {
            loadData(it)
        }
    }


    private fun attachListeners() {

        edtSearch.doOnTextChanged { text, _, _, _ ->
            viewModel.paynetCatgResp.value?.let { paynetCategories ->
                if (text!!.isBlank()) {
                    adapter.clear()
                    loadData(paynetCategories)
                } else {
                    val filtered = paynetCategories.filter {
                        it.titleRu?.lowercase()?.contains(text.toString().lowercase()) == true ||
                                it.titleUz?.lowercase()
                                    ?.contains(text.toString().lowercase()) == true
                    }
                    if (filtered.isNotEmpty()) {
                        adapter.clear()
                        loadData(filtered)
                    }
                }
            }
        }
    }

    private fun setupViews() {
        clParent.layoutParams = clParent.layoutParams.apply {
            height = SizeUtils.getScreenHeight(requireActivity()) - SizeUtils.getActionBarHeight(
                requireActivity()
            )

        }

        rvPayments.adapter = adapter


    }

    private fun loadData(data: List<PaynetCategory>) {
        adapter.clear()

        data.forEach { paynetCategory ->
            adapter.add(ItemPaynet(paynetCategory) {
                findNavController().navigate(
                    PaymentsFragmentDirections.actionPaymentsFragmentToSelectMerchantFragment(it)
                )
            })
        }

    }

    private fun subscribeObservers() {

        viewModel.paynetCatgResp.observe(viewLifecycleOwner) {
            loadData(it)
        }

        viewModel.isLoadingPaynetCategories.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) adapter.setLoadingSpinner()
        }
    }


}
