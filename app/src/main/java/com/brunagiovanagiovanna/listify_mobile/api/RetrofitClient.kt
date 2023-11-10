package com.brunagiovanagiovanna.listify_mobile.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitClient {
    private val BASE_URL = "https://listify-api-bg9i.onrender.com/"

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val taskService: ServiceTask = retrofit.create(ServiceTask::class.java)
}