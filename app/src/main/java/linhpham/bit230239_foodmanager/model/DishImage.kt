package linhpham.bit230239_foodmanager.model

import com.google.gson.annotations.SerializedName

data class DishImage(
    @SerializedName("BIT230239_imageUrl") val imageUrl: String,
    @SerializedName("BIT230239_isThumbnail") val isThumbnail: Boolean
)