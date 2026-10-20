package linhpham.bit230239_foodmanager.model

import com.google.gson.annotations.SerializedName

data class DishCategory(
    @SerializedName("BIT230239_id") val id: Int?,
    @SerializedName("BIT230239_name") val name: String,
    @SerializedName("BIT230239_description") val description: String
)