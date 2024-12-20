package com.example.newsapp.data.repository

import com.example.newsapp.api.NewsApiService
import com.example.newsapp.data.model.NewsResponse
import com.example.newsapp.utils.Constants
import retrofit2.Response

class NewsRepository(private val newsApiService: NewsApiService) {

    suspend fun getTopHeadlines(country: String): Response<NewsResponse> {
        return newsApiService.getTopHeadlines(country, Constants.API_KEY)
    }


}