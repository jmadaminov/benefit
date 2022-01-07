package com.example.benefit.ui.viewgroups

import com.example.benefit.R
import com.example.benefit.remote.models.NewsDTO
import com.example.benefit.util.loadImageUrl
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_news_and_promos.view.*

class ItemNewsAndPromos(val newsItem: NewsDTO, val onClick: (String) -> Unit) : Item() {
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

        viewHolder.itemView.apply {
            lblTitle.text = newsItem.title
            tvDate.text = newsItem.created
            lblSubtitle.text = newsItem.content
            imgNews.loadImageUrl(newsItem.image)
            clCard.setOnClickListener {
                lblSubtitle.maxLines = if (lblSubtitle.maxLines == 3) 50 else 3
                onClick(newsItem.url_link)
            }
        }
    }

    override fun getLayout() = R.layout.item_news_and_promos

}

