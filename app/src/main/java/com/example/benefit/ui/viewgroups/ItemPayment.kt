package com.example.benefit.ui.viewgroups

import com.example.benefit.R
import com.example.benefit.remote.models.PaynetCategory
import com.example.benefit.util.AppPrefs
import com.example.benefit.util.Constants
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_payment.view.*

class ItemPayment(val obj: PaynetCategory, val onClick: () -> Unit) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvPaymentName.text = if (AppPrefs.language == Constants.UZ) obj.titleUz else obj.titleRu
        viewHolder.itemView.clParent.setOnClickListener {
            onClick()
        }
    }

    override fun getLayout() = R.layout.item_payment
}
