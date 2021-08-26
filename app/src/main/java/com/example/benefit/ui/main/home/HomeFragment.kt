package com.example.benefit.ui.main.home

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.viewpager.widget.PagerAdapter
import com.example.benefit.R
import com.example.benefit.remote.models.CardDTO
import com.example.benefit.remote.models.EPaymentType
import com.example.benefit.remote.models.PaynetCategory
import com.example.benefit.stories.data.StoryUser
import com.example.benefit.stories.screen.EXTRA_STORIES
import com.example.benefit.stories.screen.StoryActivity
import com.example.benefit.ui.auth.AuthActivity
import com.example.benefit.ui.base.BaseFragment
import com.example.benefit.ui.branches_atms.BranchesAtmsActivity
import com.example.benefit.ui.expenses_by_categories.ExpensesByCategoriesActivity
import com.example.benefit.ui.loans.LoanActivity
import com.example.benefit.ui.main.fill_card.FillCardBSD
import com.example.benefit.ui.main.fill_card.FillCardFragment.Companion.ARG_CARD
import com.example.benefit.ui.main.fill_card.FillCardFragment.Companion.ARG_CARDS
import com.example.benefit.ui.main.home.bsd_add_card.AddCardBSD
import com.example.benefit.ui.main.home.card_options.CardOptionsBSD
import com.example.benefit.ui.main.transfer_to_card.TransferToCardBSD
import com.example.benefit.ui.transactions_history.TransactionsHistoryActivity
import com.example.benefit.ui.viewgroups.ItemLoading
import com.example.benefit.ui.viewgroups.ItemPaynetCatg
import com.example.benefit.ui.viewgroups.ItemStory
import com.example.benefit.util.AppPrefs
import com.example.benefit.util.ResultError
import com.example.benefit.util.ResultSuccess
import com.example.benefit.util.loadImageUrl
import com.rd.utils.DensityUtils.dpToPx
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.kotlinandroidextensions.GroupieViewHolder
import kotlinx.android.synthetic.main.fragment_home.*
import kotlinx.android.synthetic.main.item_card.view.*
import splitties.fragments.start
import splitties.preferences.edit
import java.text.DecimalFormat


class HomeFragment : BaseFragment(R.layout.fragment_home) {

    private val paynetCatgAdapter = GroupAdapter<GroupieViewHolder>()
    private val newsAdapter = GroupAdapter<GroupieViewHolder>()
    val viewModel: HomeViewModel by viewModels()


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        setupViews()
        attachListeners()
        subscribeObservers()
        viewModel.getPaynetCategories()
        viewModel.getStories()
        viewModel.getMyCards()
    }

    private fun subscribeObservers() {

        viewModel.isLoadingPaynetCategories.observe(viewLifecycleOwner, {
            when (it ?: return@observe) {
                true -> paynetCatgAdapter.add(ItemLoading())
                else -> paynetCatgAdapter.clear()
            }
        })
        viewModel.signInRequired.observe(viewLifecycleOwner, {
            when (it ?: return@observe) {
                true -> {
                    AppPrefs.edit {
                        token = null
                    }
                    start<AuthActivity>()
                    requireActivity().finish()
                }
                else -> {
                }
            }
        })
        viewModel.isLoadingStories.observe(viewLifecycleOwner, {
            when (it ?: return@observe) {
                true -> newsAdapter.add(ItemLoading())
                else -> newsAdapter.clear()
            }
        })
        viewModel.isLoadingCards.observe(viewLifecycleOwner, {
            when (it ?: return@observe) {
                true -> {
                    cardsProgress.visibility = View.VISIBLE
                    cardsPager.visibility = View.INVISIBLE
                }
                else -> {
                    cardsProgress.visibility = View.INVISIBLE
                    cardsPager.visibility = View.VISIBLE
                }
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
//            lblYoullReceiveCode.text = it ?: return@observe
//            lblYoullReceiveCode.setTextColor(Color.RED)
        })

        viewModel.paynetCatgResp.observe(viewLifecycleOwner, {
            it ?: return@observe
            paynetCatgAdapter.add(
                ItemPaynetCatg(
                    PaynetCategory(
                        paymentTypeEnum = EPaymentType.CARD_TRANSFER,
                        imageResource = R.drawable.ic_payment_card,
                        titleRu = getString(R.string.transfer_to_card)
                    )
                ) {
                    TransferToCardBSD().show(childFragmentManager, "")
                }
            )
//            paynetCatgAdapter.add(
//                ItemPaynetCatg(
//                    PaynetCategory(
//                        paymentTypeEnum = EPaymentType.FRIEND_TRANSFER,
//                        imageResource = R.drawable.ic_payment_friend,
//                        titleRu = getString(R.string.transfer_to_friend)
//                    )
//                ) {
//
//                }
//            )
            it.forEach { paynetCategory ->
                paynetCatgAdapter.add(ItemPaynetCatg(paynetCategory) {})
            }

        })

        viewModel.storiesResp.observe(viewLifecycleOwner, {
            val resp = it ?: return@observe

            when (resp) {
                is ResultError -> {
                }
                is ResultSuccess -> {
                    val storyUsers =
                        ArrayList(resp.value.groupBy { it.partnerId }.values.map { stories ->
                            StoryUser(
                                stories[0].partnerTitle!!,
                                stories[0].partnerIconImage!!,
                                ArrayList(stories)
                            )
                        })
                    storyUsers.forEachIndexed { index, storyUser ->
                        val storiesToShow = arrayListOf<StoryUser>()
                        for (i in index until storyUsers.size) {
                            storiesToShow.add(storyUsers[i])
                        }
                        newsAdapter.add(
                            ItemStory(storyUser) {
                                startActivity(
                                    Intent(requireActivity(), StoryActivity::class.java).apply {
                                        putParcelableArrayListExtra(EXTRA_STORIES, storiesToShow)
                                    })
                            })
                    }
                }
            }

        })
        viewModel.cardsResp.observe(viewLifecycleOwner, {
            it ?: return@observe
            setupCardsPager(it)
        })

    }

    private fun attachListeners() {

        page_one.setOnClickListener {
            val dialog = DialogCashBack()
            dialog.show(childFragmentManager, "")

        }


        page_two.setOnClickListener {
            startActivity(Intent(requireActivity(), LoanActivity::class.java))
        }
        cardBranches.setOnClickListener {
            startActivity(Intent(requireActivity(), BranchesAtmsActivity::class.java))
        }


        cardExpenses.setOnClickListener {
            viewModel.cardsResp.value?.let { cards ->
                startActivity(
                    Intent(
                        requireActivity(),
                        ExpensesByCategoriesActivity::class.java
                    ).apply {
                        putParcelableArrayListExtra(ARG_CARDS, ArrayList(cards))
                    })
            } ?: run {

            }


        }

        cardPayments.setOnClickListener {
            viewModel.cardsResp.value?.let { cards ->
                startActivity(
                    Intent(
                        requireActivity(),
                        TransactionsHistoryActivity::class.java
                    ).apply {
                        putParcelableArrayListExtra(ARG_CARDS, ArrayList(cards))
                    })
            } ?: run {
                val dialog = DialogPleaseAddCard()
                childFragmentManager.setFragmentResultListener(
                    KEY_ADD_CARD,
                    viewLifecycleOwner,
                    { requestKey, result ->
                        AddCardBSD().show(childFragmentManager, "")
                    })
                dialog.show(childFragmentManager, "")
            }
        }


    }

    private fun setupViews() {

        rvPayments.adapter = paynetCatgAdapter
        rvStories.adapter = newsAdapter
        cardOval.setBackgroundResource(R.drawable.shape_oval)
        cardOvalExpenses.setBackgroundResource(R.drawable.shape_oval)
        cardOvalBranches.setBackgroundResource(R.drawable.shape_oval)
        cardOvalLimits.setBackgroundResource(R.drawable.shape_oval)
        cardOvalMessage.setBackgroundResource(R.drawable.shape_oval_yellow)
        cardOvalLoans.setBackgroundResource(R.drawable.shape_oval_yellow)
        cardOvalCashback.setBackgroundResource(R.drawable.shape_oval_yellow)

        setupServicesPager()


    }

    private fun setupCardsPager(cardsDTO: List<CardDTO>) {

//        transactionHistoryPanel.isVisible = cardsDTO.isNotEmpty()
//        categoryExpensesPanel.isVisible = cardsDTO.isNotEmpty()


        val cardViews = arrayListOf<View>()
        cardsDTO.forEach {
            val cardView = makeCardView(it)
            cardsPager.addView(cardView)
            cardViews.add(cardView)
        }

        val cardView2 = layoutInflater.inflate(R.layout.item_add_card, cardsPager, false)
        cardView2.cardParent.setOnClickListener { AddCardBSD().show(childFragmentManager, "") }
        cardsPager.addView(cardView2)
        cardViews.add(cardView2)

        cardsPager.adapter = WizardPagerAdapter(cardViews)
        cardsPager.offscreenPageLimit = cardViews.size
        cardsPager.clipToPadding = false
        cardsPager.setPadding(dpToPx(26), 0, dpToPx(26), 0)
        cardsPager.pageMargin = dpToPx(15)

    }

    private fun makeCardView(cardDTO: CardDTO): View {
        val view = layoutInflater.inflate(R.layout.item_card, cardsPager, false)
        view.tvCardOwner.text = cardDTO.fullName
        view.tvCardNumber.text = cardDTO.pan
        view.tvCardName.text = cardDTO.card_title
        if (cardDTO.background_link != null) view.cardBg.loadImageUrl(cardDTO.background_link)
        view.tvBalance.text =
            "${DecimalFormat("#,###").format(cardDTO.balance!!.dropLast(2).toInt())} UZS"
        view.icPlus.setOnClickListener {
            val dialog = FillCardBSD()
            dialog.arguments = Bundle().apply {
                putParcelable(ARG_CARD, cardDTO)
                putParcelableArrayList(ARG_CARDS, ArrayList(viewModel.cardsResp.value!!))
            }
            dialog.show(childFragmentManager, "")
        }
        view.setOnClickListener {
            val dialog = CardOptionsBSD()
            dialog.arguments = Bundle().apply {
                putParcelable(ARG_CARD, cardDTO)
                putParcelableArrayList(ARG_CARDS, ArrayList(viewModel.cardsResp.value!!))
            }
            dialog.show(childFragmentManager, "")
        }
        return view
    }

    private fun setupServicesPager() {
        servicesPager.adapter = WizardPagerAdapter(listOf(page_one, page_two, page_three))
        servicesPager.offscreenPageLimit = 10
        servicesPager.clipToPadding = false
        servicesPager.setPadding(dpToPx(26), 0, dpToPx(26), 0)
        servicesPager.pageMargin = dpToPx(15)


    }

    class WizardPagerAdapter(val views: List<View>) : PagerAdapter() {
        override fun instantiateItem(collection: ViewGroup, position: Int): Any {
            return views[position]
        }

        override fun getCount(): Int {
            return views.size
        }

        override fun isViewFromObject(arg0: View, arg1: Any): Boolean {
            return arg0 === arg1
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        cardsPager?.adapter = null
    }
}