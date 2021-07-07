package com.example.benefit.ui.auth.login


/**
 * Created by jahon on 03-Sep-20
 */
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.benefit.remote.models.RespLogin
import com.example.benefit.remote.models.RespLoginCode
import com.example.benefit.remote.models.RespLoginSms
import com.example.benefit.remote.repository.UserRemote
import com.example.benefit.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import splitties.preferences.edit
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val userRemote: UserRemote) :
    ViewModel() {

    val loginResp = SingleLiveEvent<RespLogin>()
    val loginCodeResp = SingleLiveEvent<RespLoginCode>()
    val errorMessage = SingleLiveEvent<String>()
    val isLoading = SingleLiveEvent<Boolean>()

    var phoneNum = ""
    var code: Int? = null

    fun login(phoneNumber: String) {
        this.phoneNum = phoneNumber
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRemote.login(phoneNumber)
            withContext(Dispatchers.Main) {
                isLoading.value = false
                when (response) {
                    is ResultError -> {
                        errorMessage.value = response.message
                    }
                    is ResultSuccess -> {
                        code = response.value.sms_live
                        loginResp.value = response.value
                    }
                }.exhaustive
            }
        }
    }




    fun loginCode(code: String) {
        isLoading.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRemote.loginCode(code)
            withContext(Dispatchers.Main) {
                isLoading.value = false
                when (response) {
                    is ResultError -> {
                        errorMessage.value = response.message
                    }
                    is ResultSuccess -> {
                        loginCodeResp.value = response.value
                    }
                }.exhaustive
            }
        }
    }


}