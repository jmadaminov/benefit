package uz.magnumactive.benefit.ui.regular_payment

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.findNavController
import uz.magnumactive.benefit.R
import uz.magnumactive.benefit.remote.models.AutoPaymentDTO
import uz.magnumactive.benefit.ui.main.BenefitFixedHeightBSD


class RegularPaymentBSD : BenefitFixedHeightBSD() {


    companion object {
        const val ARG_REGULAR_PAYMENT_DTO = "autoPaymentDTO"
        const val RESULT_REGULAR_PAYMENT_DELETE = "RESULT_REGULAR_PAYMENT_DELETE"
    }

    lateinit var regularPaymentDTO: AutoPaymentDTO

    override fun onAttach(context: Context) {
        super.onAttach(context)

        regularPaymentDTO = requireArguments().getParcelable(ARG_REGULAR_PAYMENT_DTO)!!

    }

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.bsd_regular_payment, container)

        return view
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        (childFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment).findNavController()
                .setGraph(R.navigation.regular_payment_nav_graph, Bundle().apply {
                    putParcelable(ARG_REGULAR_PAYMENT_DTO, regularPaymentDTO)
                })

    }

}