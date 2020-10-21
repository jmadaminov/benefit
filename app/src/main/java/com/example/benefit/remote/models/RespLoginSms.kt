package com.example.benefit.remote.models

import com.google.gson.annotations.SerializedName

data class RespLoginSms(
    @SerializedName("avatar") val avatar: String? = null,
    @SerializedName("first_name") val first_name: String? = null,
    @SerializedName("last_name") val last_name: String? = null,
    @SerializedName("result") val result: Boolean? = null,
    @SerializedName("user_id") val user_id: Int? = null,
    @SerializedName("user_token") val user_token: String? = null,
    @SerializedName("msg") val msg: String? = null
)