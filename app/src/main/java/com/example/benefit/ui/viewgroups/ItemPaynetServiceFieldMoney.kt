package com.example.benefit.ui.viewgroups

import android.widget.EditText
import com.example.benefit.R
import com.example.benefit.remote.models.ServiceField
import com.example.benefit.util.AppPrefs
import com.example.benefit.util.Constants.UZ
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_paynet_service_field_string.view.*

class ItemPaynetServiceFieldMoney(val obj: ServiceField) : Item() {

    var input: EditText? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.apply {
            lblTitle.text = if (AppPrefs.language == UZ) obj.titleUz else obj.titleRu

        }

    }

    override fun getLayout() = R.layout.item_paynet_service_field_money


    override fun unbind(viewHolder: GroupieViewHolder) {
        super.unbind(viewHolder)
        input = null
    }
}
