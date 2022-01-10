package com.example.benefit.ui.viewgroups

import android.widget.EditText
import com.example.benefit.R
import com.example.benefit.remote.models.ServiceField
import com.example.benefit.util.AppPrefs
import com.example.benefit.util.Constants.UZ
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_paynet_service_field_string.view.*

class ItemPaynetServiceFieldString(val obj: ServiceField) : Item() {

    var input: EditText? = null

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.apply {

        }

    }

    override fun getLayout() = R.layout.item_paynet_service_field_string


    override fun unbind(viewHolder: GroupieViewHolder) {
        super.unbind(viewHolder)
        input = null
    }
}
