package com.example.benefit.ui.main.partners_category

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.benefit.R
import com.example.benefit.remote.models.PartnerCategoryDTO
import com.example.benefit.ui.partners_map.PartnersMapActivity
import com.example.benefit.ui.viewgroups.ItemPartnerCategory
import com.example.benefit.ui.viewgroups.ItemProgress
import com.example.benefit.util.ErrorWrapper
import com.example.benefit.util.ResultWrapper
import com.example.benefit.util.exhaustive
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_partners_category.*
import splitties.fragments.start

@AndroidEntryPoint
class PartnersCategoryFragment : Fragment(R.layout.fragment_partners_category) {

    val viewModel: PartnersCategoryViewModel by viewModels()


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getPartners()

        setupViews()
        attachListeners()
        subscribeObservers()
    }

    private fun attachListeners() {
        ivMap.setOnClickListener {
            start<PartnersMapActivity> {
                putParcelableArrayListExtra(
                    PartnersMapActivity.EXTRA_CATEGORIES,
                    ArrayList((viewModel.partnersResp.value!! as ResultWrapper.Success).value)
                )
            }
        }
    }

    private fun setupViews() {
        rvPartners.adapter = adapter
    }

    private fun subscribeObservers() {
        viewModel.partnersResp.observe(viewLifecycleOwner, Observer {
            val response = it ?: return@Observer

            when (response) {
                is ErrorWrapper.ResponseError -> {
                    ivMap.visibility = View.INVISIBLE
                }
                is ErrorWrapper.SystemError -> {
                    ivMap.visibility = View.INVISIBLE
                }
                is ResultWrapper.Success -> {
                    ivMap.visibility = View.VISIBLE
                    loadData(response.value)
                }
                ResultWrapper.InProgress -> {
                    ivMap.visibility = View.INVISIBLE
                    adapter.clear()
                    adapter.add(ItemProgress())
                    adapter.notifyDataSetChanged()
                }
            }.exhaustive


        })
    }

    val adapter = GroupAdapter<GroupieViewHolder>()

    private fun loadData(response: List<PartnerCategoryDTO>) {
        adapter.clear()

        response.forEach { partner ->
            adapter.add(ItemPartnerCategory(partner))
        }

    }
}