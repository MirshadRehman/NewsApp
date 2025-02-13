package com.example.newsapp.api



import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.utils.Constants.Companion.API_KEY
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApiService {
    @GET("v2/top-headlines")
    suspend fun getTopHeadlines(
        @Query("country") country: String,
        @Query("apiKey") apiKey: String = API_KEY
    ): Response<NewsResponse>
}