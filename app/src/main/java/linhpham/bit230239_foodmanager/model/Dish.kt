package linhpham.bit230239_foodmanager.model

import com.google.gson.annotations.SerializedName

data class Dish(
    @SerializedName("BIT230239_id") val id: Int?,
    @SerializedName("BIT230239_name") val name: String,
    @SerializedName("BIT230239_price") val price: Double,
    @SerializedName("BIT230239_preparationTime") val preparationTime: Int,
    @SerializedName("BIT230239_isAvailable") val isAvailable: Boolean,
    @SerializedName("BIT230239_description") val description: String,
    @SerializedName("BIT230239_dishCategoryId") val dishCategoryId: Int,
    @SerializedName("BIT230239_dishImages") val dishImages: List<DishImage>
)