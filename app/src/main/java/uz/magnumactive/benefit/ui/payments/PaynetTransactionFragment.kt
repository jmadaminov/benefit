package uz.magnumactive.benefit.ui.payments


/**
 * Created by jahon on 03-Sep-20
 */
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import kotlinx.android.synthetic.main.fragment_paynet_transfer.*
import kotlinx.android.synthetic.main.item_card_small.view.*
import kotlinx.android.synthetic.main.transaction_loading.*
import kotlinx.android.synthetic.main.transaction_success.*
import uz.magnumactive.benefit.R
import uz.magnumactive.benefit.remote.models.BalanceInfo
import uz.magnumactive.benefit.remote.models.CardDTO
import uz.magnumactive.benefit.ui.base.BaseFragment
import uz.magnumactive.benefit.ui.main.home.HomeFragment
import uz.magnumactive.benefit.util.RequestState
import uz.magnumactive.benefit.util.SizeUtils
import uz.magnumactive.benefit.util.loadImageUrl
import java.text.DecimalFormat

class PaynetTransactionFragment : BaseFragment(R.layout.fragment_paynet_transfer) {

    val args: PaynetTransactionFragmentArgs by navArgs()
    private val viewModel: PaynetTransactionViewModel by viewModels()
    private val sharedViewModel: PaymentsViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.getMyCards()
        setupViews()

        attachListeners()
        subscribeObservers()
    }

    private fun setupViews() {
        layoutCalculator.edtSum = edtSum
        layoutCalculator.footerTextView = tvMinAmount
        layoutCalculator.minAmount = 500
        tvServiceName.text = args.paynetMerchant.titleShort
        tvServiceFullName.text = args.paynetMerchant.title
        tvCategoryName.text = args.paynetMerchant.getLocalizedCatgName()
        ivTargetService.loadImageUrl(args.paynetMerchant.image!!)
        lblSearching.text = getString(R.string.payment)
    }


    private fun subscribeObservers() {

        viewModel.bftAndMyCardsPair.observe(viewLifecycleOwner) { requestState ->
            progressCards.isVisible = false
            setupCardsPager(requestState.first, requestState.second.getProperly())
        }


        viewModel.transactionState.observe(viewLifecycleOwner) { transactionState ->
            when (transactionState) {
                is RequestState.Error -> {
                    clTopUpLoading.isVisible = false
                    Toast.makeText(context, transactionState.message, Toast.LENGTH_SHORT).show()
                }
                RequestState.Loading -> {
                    clTopUpLoading.isVisible = true
                }
                is RequestState.Success -> {
                    clTopUpLoading.isVisible = false
                    clTopUpSuccess.isVisible = true
                    lblTopUpSuccess.text = getString(R.string.payment_succeeded)
                    tvTransferAmount.text =
                        getString(
                            R.string.transfer_amount,
                            transactionState.value.response!!.filter { it.key == SUMMA_SERVICE_FIELD }[0].value
                        )
                    tvCommissions.text = getString(R.string.commissions_amount, "0")
                }
            }
        }

    }


    private fun setupCardsPager(bftInfo: BalanceInfo, cardsDTO: List<CardDTO>) {

        val cards = arrayListOf<View>().apply {
            val cardView = layoutInflater.inflate(R.layout.item_card_small, null)
            cardView.cardName.text = getString(R.string.cashback_points)
            cardView.tvAmount.text = DecimalFormat("#,###").format(bftInfo.summa) + " BFT"
            cardView.tvCardEndNum.text = ""
            cardView.ivCardBg.setImageResource(R.drawable.gradient_orange)
            cardsPagerSmall.addView(cardView)
            add(cardView)
        }

        cardsDTO.forEach {
            val cardView = layoutInflater.inflate(R.layout.item_card_small, null)
            cardView.cardName.text = it.card_title
            cardView.tvAmount.text =
                DecimalFormat("#,###").format(it.balance?.dropLast(2)?.toInt()) + " UZS"
            cardView.tvCardEndNum.text = "*" + it.panHidden!!.substring(it.panHidden!!.length - 4)
            it.setMiniBackgroundInto(cardView.ivCardBg)
            cardsPagerSmall.addView(cardView)
            cards.add(cardView)
        }

        cardsPagerSmall.adapter = HomeFragment.WizardPagerAdapter(cards)
        cardsPagerSmall.offscreenPageLimit = 10
        cardsPagerSmall.clipToPadding = false
        cardsPagerSmall.setPadding(
            SizeUtils.dpToPx(requireContext(), 26).toInt(),
            0,
            SizeUtils.dpToPx(requireContext(), 26).toInt(),
            0
        )
        cardsPagerSmall.pageMargin = SizeUtils.dpToPx(requireContext(), 15).toInt()

    }


    private fun attachListeners() {

        btnClose.setOnClickListener {
            ((parentFragment as NavHostFragment).parentFragment as PaymentsBSD).dismiss()
        }

        ivBack.setOnClickListener {
            findNavController().popBackStack()
        }

        tvFill.setOnClickListener {
            val fields = StringBuilder()
            args.serviceFields.forEach {
                fields.append("\"${it.name}\":")
                fields.append("\"${it.userSelection}\",")
            }
            fields.append("\"${SUMMA_SERVICE_FIELD}\":")/*sharedViewModel.transferAmountKey*/
            fields.append("\"${layoutCalculator.amount}\"")

            if (cardsPagerSmall.currentItem == 0) {
                viewModel.payWithCashback(
                    serviceId = args.paynetMerchant.own_id!!,
                    providerId = args.paynetMerchant.category_id!!,
                    fields = fields.toString(),
                    layoutCalculator.amount,
                )
            } else {
                viewModel.pay(
                    serviceId = args.paynetMerchant.own_id!!,
                    providerId = args.paynetMerchant.category_id!!,
                    fields = fields.toString(),
                    layoutCalculator.amount,
                    viewModel.bftAndMyCardsPair.value!!.second.getProperly()[cardsPagerSmall.currentItem].id!!
                )
            }
        }

        edtSum.doOnTextChanged { text, start, before, count ->
            tvFill.isEnabled = layoutCalculator.amount > 0 &&
                    viewModel.bftAndMyCardsPair.value!!.second.getProperly()[cardsPagerSmall.currentItem].balance?.dropLast(
                        2
                    )!!.toInt() > layoutCalculator.amount
//            tvMinAmount.isVisible =
//                if (text.isNullOrBlank()) false else text.toString().toInt() < 1000
        }

    }

}
