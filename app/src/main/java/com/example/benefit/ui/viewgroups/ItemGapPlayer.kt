package com.example.benefit.ui.viewgroups

import com.example.benefit.R
import com.example.benefit.remote.models.Member
import com.example.benefit.util.Constants
import com.example.benefit.util.loadImageUrl
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_gap_player.view.*

data class ItemGapPlayer(val member: Member) : Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.apply {
            tvName.text = member.userInfo?.fullname
            member.userInfo?.avatarLink?.let { avatar ->
                if (avatar.endsWith(".png") || avatar.endsWith(".jpg")) {
                    ivPlayer.loadImageUrl(Constants.BASE_URL + avatar.removeSuffix("/"))
                } else {
                    ivPlayer.setImageResource(R.drawable.ic_avatar_sample)
                }
            }
            tvStatus.text = member.statusText
        }


    }

    override fun getLayout() = R.layout.item_gap_player
}