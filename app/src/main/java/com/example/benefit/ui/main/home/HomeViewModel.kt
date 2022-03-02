package com.example.benefit.ui.main.home


import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.benefit.remote.AuthApiService
import com.example.benefit.remote.models.CardDTO
import com.example.benefit.remote.models.PaynetCategory
import com.example.benefit.remote.repository.UserRemote
import com.example.benefit.stories.data.Story
import com.example.benefit.util.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val userRemote: UserRemote,
    private val apiAuth: AuthApiService
) : ViewModel() {

    val paynetCatgResp = MutableLiveData<List<PaynetCategory>?>()
    val errorMessage = MutableLiveData<String?>()
    val isLoadingPaynetCategories = MutableLiveData<Boolean>()

    fun getPaynetCategories() {
        isLoadingPaynetCategories.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRemote.paymentCategories()
            withContext(Dispatchers.Main) {
                isLoadingPaynetCategories.value = false
                when (response) {
                    is ResultError -> errorMessage.value = response.message
                    is ResultSuccess -> paynetCatgResp.value = response.value
                }.exhaustive
            }
        }
    }

    val storiesResp = MutableLiveData<ResultWrapper<List<Story>>>()
    val isLoadingStories = MutableLiveData<Boolean>()
    fun getStories() {
        isLoadingStories.value = true
        viewModelScope.launch(Dispatchers.IO) {
            storiesResp.postValue(getFormattedResponse(isLoadingStories) { apiAuth.getStories() })
        }
    }

    var supremeCard: CardDTO? = null
    val cardsResp = MutableLiveData<List<CardDTO>>()
    val isLoadingCards = MutableLiveData<Boolean>()
    val signInRequired = MutableLiveData<Boolean>()
    fun getMyCards() {
        isLoadingCards.value = true
        viewModelScope.launch(Dispatchers.IO) {
            val response = userRemote.getMyCards()
            withContext(Dispatchers.Main) {
                isLoadingCards.value = false
                when (response) {
                    is ResultError -> {
                        if (response.code == 403) signInRequired.value = true
                        errorMessage.value = response.message
                    }
                    is ResultSuccess -> {
                        cardsResp.value = response.value.getProperly()
                        supremeCard = cardsResp.value?.find { it.isSupreme() }
                    }
                }.exhaustive
            }
        }
    }


}