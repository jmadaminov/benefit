package uz.magnumactive.benefit.remote.models


import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MarketProductPhotoDTO(
    @SerializedName("id")
    val id: Int?=null,
    @SerializedName("photos")
    val photos: String?=null,
    @SerializedName("shop_item_id")
    val shopItemId: Int?=null
):Parcelable