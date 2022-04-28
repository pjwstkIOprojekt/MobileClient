package com.example.mobileclient.util

import com.google.gson.Gson
import com.google.gson.GsonBuilder

class Constants {
    companion object{
        const val BASE_URL = "http://172.21.40.111:8080"
        val gson: Gson = GsonBuilder().setLenient().create()
    }
}