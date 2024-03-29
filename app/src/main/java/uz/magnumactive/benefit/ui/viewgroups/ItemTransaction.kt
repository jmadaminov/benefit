package uz.magnumactive.benefit.ui.viewgroups

import android.util.TypedValue
import androidx.core.view.isVisible
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import com.xwray.groupie.kotlinandroidextensions.Item
import kotlinx.android.synthetic.main.item_transaction.view.*
import uz.magnumactive.benefit.R
import uz.magnumactive.benefit.remote.models.TransactionAnalyticsDTO
import java.text.DecimalFormat


class ItemTransaction(val obj: TransactionAnalyticsDTO, val onClick: (() -> Unit)? = null) :
    Item() {

    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.tvAmount.text =
            DecimalFormat("#,###").format(obj.amountWithoutTiyin) + " UZS"
        viewHolder.itemView.tvTransactionType.text = obj.merchantName

        viewHolder.itemView.tvMinus.isVisible = obj.isCredit == false

//        obj.partner?.image?.let {
//            viewHolder.itemView.icBankLogo.loadImageUrl(it)
//        }

        val transactionImage =
            if (obj.merchantName?.contains("P2P") == true || obj.merchantName?.contains("PAYME") == true) {
//                viewHolder.itemView.tvTransactionType.text =
//                    viewHolder.itemView.context.getString(R.string.transfer_from_card)
                R.drawable.ic_transfer
            } else if (obj.isCredit == true) {
//                viewHolder.itemView.tvTransactionType.text =
//                    viewHolder.itemView.context.getString(R.string.income)
                R.drawable.ic_income
            } else {
//                viewHolder.itemView.tvTransactionType.text =
//                    viewHolder.itemView.context.getString(R.string.expense)
                R.drawable.ic_expense
            }
        viewHolder.itemView.icBankLogo.setImageResource(transactionImage)

        onClick?.let { click ->
            viewHolder.itemView.isClickable = true
            viewHolder.itemView.isFocusable = true
            val outValue = TypedValue()
            viewHolder.itemView.context.theme.resolveAttribute(
                android.R.attr.selectableItemBackground,
                outValue,
                true
            )
            viewHolder.itemView.setBackgroundResource(outValue.resourceId)
            viewHolder.itemView.clParent.setOnClickListener {
                click()
            }
        } ?: run {
            viewHolder.itemView.isClickable = false
            viewHolder.itemView.isFocusable = false
        }

    }

    override fun getLayout() = R.layout.item_transaction

}
