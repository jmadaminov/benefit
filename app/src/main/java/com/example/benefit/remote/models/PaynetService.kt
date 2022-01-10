package com.example.benefit.remote.models

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PaynetService(
    @SerializedName("maxAmount") val maxAmount: Int? = null,
    @SerializedName("minAmount") val minAmount: Int? = null,
    @SerializedName("own_id") val own_id: Long? = null,
    @SerializedName("own_order") val own_order: Int? = null,
    @SerializedName("provider_name") val provider_name: ProviderName? = null,
    @SerializedName("service_fields") val service_fields: List<ServiceField>? = null,
    @SerializedName("titleRu") val titleRu: String? = null,
    @SerializedName("titleUz") val titleUz: String? = null,
    @SerializedName("updated_at") val updated_at: String? = null
) : Parcelable

@Parcelize
data class ProviderName(
    @SerializedName("image") val image: String? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("titleShort") val titleShort: String? = null
) : Parcelable

@Parcelize
data class ServiceField(
    @SerializedName("fieldControl") val fieldControl: String? = null,
    @SerializedName("fieldType") val fieldType: FieldType? = null,
    @SerializedName("fieldValues") val fieldValues: String? = null,
    @SerializedName("name") val name: String? = null,
    @SerializedName("own_id") val own_id: Long? = null,
    @SerializedName("own_order") val own_order: Long? = null,
    @SerializedName("required") val required: Int? = null,
    @SerializedName("titleRu") val titleRu: String? = null,
    @SerializedName("titleUz") val titleUz: String? = null
) : Parcelable

enum class FieldType {
    STRING,
    MONEY,
    DATEPOPUP,
    PHONE,
    REGEXBOX,
}