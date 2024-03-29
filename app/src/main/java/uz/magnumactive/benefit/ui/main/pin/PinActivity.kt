package uz.magnumactive.benefit.ui.main.pin

import android.content.Intent
import android.os.Bundle
import androidx.core.widget.doOnTextChanged
import uz.magnumactive.benefit.R
import uz.magnumactive.benefit.ui.auth.AuthActivity
import uz.magnumactive.benefit.ui.base.BaseActivity
import uz.magnumactive.benefit.util.AppPrefs
import uz.magnumactive.benefit.util.loadImageUrl
import kotlinx.android.synthetic.main.activity_pin.*
import uz.magnumactive.benefit.util.Constants
import uz.magnumactive.benefit.util.loadCircleImageUrl

class PinActivity : BaseActivity() {


    private var isBiometricSupported = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pin)

        setupViews()
        attachListeners()
        isBiometricSupported = BiometricAuth(this).isBiometricSupported()
        if (isBiometricSupported) {
            showBioPrompt()
        } else {
//            ivTouchId.isVisible = false
        }

    }

    private fun showBioPrompt() {
        BiometricPromptUtils.createBiometricPrompt(this, onSuccess = {
            finish()
        },
            onUsePin = {

            }
        )
    }

    private fun setupViews() {
        AppPrefs.avatar?.let {
            ivProfile.loadCircleImageUrl(it)
        } ?: run {
            ivProfile.setImageResource(R.drawable.ic_avatar_sample)
        }
        tvFullName.text = AppPrefs.firstName + " " + AppPrefs.lastName
    }

    private fun attachListeners() {
//        ivTouchId.setOnClickListener {
//            showBioPrompt()
//        }
        ivBackSpace.setOnClickListener {
            backSpace()
        }

        tvZero.setOnClickListener {
            appendToPin("0")
        }

        tvOne.setOnClickListener {
            appendToPin("1")
        }


        tvTwo.setOnClickListener {
            appendToPin("2")
        }


        tvThree.setOnClickListener {
            appendToPin("3")
        }


        tvFour.setOnClickListener {
            appendToPin("4")
        }


        tvFive.setOnClickListener {
            appendToPin("5")
        }


        tvSix.setOnClickListener {
            appendToPin("6")
        }


        tvSeven.setOnClickListener {
            appendToPin("7")
        }


        tvEight.setOnClickListener {
            appendToPin("8")
        }

        tvNine.setOnClickListener {
            appendToPin("9")
        }

        ivLogout.setOnClickListener {
            AppPrefs.logOut()
            startActivity(Intent(this, AuthActivity::class.java))
            this.finish()
        }

        tvForgotPin.setOnClickListener {
            AppPrefs.logOut()
            startActivity(Intent(this, AuthActivity::class.java))
            this.finish()
        }

        pinView.doOnTextChanged { text, start, before, count ->
            pinView.getText()?.toString()?.let { pinInput ->
                if (pinInput.length == 4) {
                    if (AppPrefs.pin == pinInput) {
                        finish()
                    } else {
                        pinView.setText("")
                    }
                }
            }
        }
    }

    private fun appendToPin(s: String) {
        pinView.append(s)
    }

    private fun backSpace() {
        pinView.setText(pinView.text?.dropLast(1))
    }

    override fun onBackPressed() {

    }
}