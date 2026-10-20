package linhpham.bit230239_foodmanager.api

import linhpham.bit230239_foodmanager.model.Dish
import linhpham.bit230239_foodmanager.model.DishCategory
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {
    @GET("BIT230239_categories")
    suspend fun getCategories(): Response<List<DishCategory>>

    @GET("BIT230239_dishes")
    suspend fun getDishes(): Response<List<Dish>>

    @POST("BIT230239_dishes")
    suspend fun createDish(@Body dish: Dish): Response<Dish>

    @PUT("BIT230239_dishes/{id}")
    suspend fun updateDish(@Path("id") id: String, @Body dish: Dish): Response<Dish>

    @DELETE("BIT230239_dishes/{id}")
    suspend fun deleteDish(@Path("id") id: String): Response<Unit>

    companion object {
        private const val BASE_URL = "https://6ad738e395e98f36aef20be2.mockapi.io/"

        fun create(): ApiService {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}