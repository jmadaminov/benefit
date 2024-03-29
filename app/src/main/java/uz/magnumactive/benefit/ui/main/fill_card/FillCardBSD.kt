package uz.magnumactive.benefit.ui.main.fill_card

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import uz.magnumactive.benefit.R
import uz.magnumactive.benefit.remote.models.CardDTO
import uz.magnumactive.benefit.ui.main.BenefitFixedHeightBSD
import uz.magnumactive.benefit.ui.main.fill_card.FillCardFragment.Companion.ARG_CARD
import uz.magnumactive.benefit.ui.main.fill_card.FillCardFragment.Companion.ARG_CARDS
import uz.magnumactive.benefit.ui.main.fill_card.FillCardFragment.Companion.ARG_IS_LOAN
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.bsd_fill_card.*

@AndroidEntryPoint
class FillCardBSD : BenefitFixedHeightBSD() {

    private var isLoan: Boolean = false
    private val viewModel: FillCardViewModel by viewModels()
    var cardBeingFilled: CardDTO? = null
    var selectableCards: List<CardDTO>? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        cardBeingFilled = arguments?.getParcelable(ARG_CARD)
        isLoan = arguments?.getBoolean(ARG_IS_LOAN) ?: false
        selectableCards = arguments?.getParcelableArrayList(ARG_CARDS)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = inflater.inflate(R.layout.bsd_fill_card, container)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        if (cardBeingFilled == null || selectableCards == null) {
            subscribeObservers()
            viewModel.getMyCards()
        } else {
            (childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).findNavController()
                .setGraph(R.navigation.fill_card_nav_graph, Bundle().apply {
                    putParcelable(ARG_CARD, cardBeingFilled)
                    putBoolean(ARG_IS_LOAN, isLoan)
                    putParcelableArrayList(ARG_CARDS, ArrayList(selectableCards!!))
                })
        }
    }

    private fun subscribeObservers() {

        viewModel.isLoadingCards.observe(viewLifecycleOwner) {
            progress.isVisible = it
        }

        viewModel.cardsResp.observe(viewLifecycleOwner) {
            (childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).findNavController()
                .setGraph(R.navigation.fill_card_nav_graph, Bundle().apply {
                    putParcelable(ARG_CARD, cardBeingFilled ?: it[0])
                    putBoolean(ARG_IS_LOAN, isLoan)
                    putParcelableArrayList(ARG_CARDS, ArrayList(it))
                })
        }
    }

}