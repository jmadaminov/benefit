package uz.magnumactive.benefit.ui.marketplace

import android.animation.LayoutTransition.CHANGING
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_market.*
import uz.magnumactive.benefit.R
import uz.magnumactive.benefit.remote.models.MyBasketResultDTO
import uz.magnumactive.benefit.ui.base.BaseActivity
import uz.magnumactive.benefit.ui.marketplace.cart.BasketActivity
import uz.magnumactive.benefit.ui.marketplace.favourite.FavouritesFragment
import uz.magnumactive.benefit.util.RequestState
import java.text.DecimalFormat


const val EXTRA_SELECTED_MARKET_CATEGORY = "EXTRA_SELECTED_MARKET_CATEGORY"

@AndroidEntryPoint
class MarketActivity : BaseActivity() {

    val viewModel: MarketActivityViewModel by viewModels()
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_market)
        setSupportActionBar(tool_bar)

        navController = findNavController(R.id.nav_host_fragment).apply {
            setGraph(R.navigation.market_navigation, intent.extras)
        }
        nav_view.setupWithNavController(navController)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.elevation = 0F
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            window.statusBarColor = Color.WHITE
        }

        container.layoutTransition.enableTransitionType(CHANGING)


//        navController.addOnDestinationChangedListener { controller, destination, arguments ->
//            ivCleanFavs.isVisible = destination.id == R.id.nav_favourites
//        }
        attachListeners()
        subscribeObservers()
    }

    private fun attachListeners() {

        cardAddedToCart.setOnClickListener {
            startActivity(Intent(this, BasketActivity::class.java))
        }

        ivCleanFavs.setOnClickListener {
            val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment)
            navHostFragment?.childFragmentManager?.fragments?.get(0)?.let {
                (it as? FavouritesFragment)?.onCleanFavsClick()
            }
        }
    }

    fun setFavCleanVisibility(isVisible: Boolean) {
        ivCleanFavs.isVisible = isVisible
    }

    override fun onResume() {
        super.onResume()
        viewModel.getMyCart()
    }

    private fun subscribeObservers() {

        viewModel.myCart.observe(this) {
            val resp = it ?: return@observe
            when (resp) {
                is RequestState.Error -> {}
                RequestState.Loading -> {}
                is RequestState.Success -> {
                    setupCartCard(resp.value)
                }
            }
        }

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> finish()
        }
        return super.onOptionsItemSelected(item)
    }

    fun setTitle(title: String) {
        tvTitle.text = title
    }


    private fun setupCartCard(value: MyBasketResultDTO) {
        val newConstraints = ConstraintSet()
        newConstraints.clone(container)
        if (value.list == null || value.list.isEmpty()) {
            newConstraints.connect(
                R.id.cardAddedToCart,
                ConstraintSet.TOP,
                R.id.nav_view,
                ConstraintSet.BOTTOM
            )
            newConstraints.clear(
                R.id.cardAddedToCart,
                ConstraintSet.BOTTOM,
            )
        } else {
            newConstraints.connect(
                R.id.cardAddedToCart,
                ConstraintSet.BOTTOM,
                R.id.nav_view,
                ConstraintSet.TOP
            )
            newConstraints.clear(
                R.id.cardAddedToCart,
                ConstraintSet.TOP,
            )
            tvCartCount.text = value.list.size.toString()
            tvCartTotal.text =
                DecimalFormat("#,###").format(value.totalSum) + " UZS"
        }
        container.setConstraintSet(newConstraints)
        cardAddedToCart.requestLayout()
    }

}