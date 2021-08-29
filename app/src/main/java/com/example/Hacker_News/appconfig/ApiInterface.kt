package com.example.Hacker_News.appconfig

import com.example.Hacker_News.model.Top
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiInterface {
    //Get Top Store information get api called
    @GET("v0/topstories.json?print=pretty")
    fun getTopStories(): Call<List<Int>>

    //Get new Store information get api called
    @GET("v0/newstories.json?print=pretty")
    fun getnewStories(): Call<List<Int>>

    //Get new best information get api called
    @GET("v0/beststories.json?print=pretty")
    fun getbestStories(): Call<List<Int>>

    //Get new ask information get api called
    @GET("v0/showstories.json?print=pretty")
    fun getshowStories(): Call<List<Int>>

    //Get job Store information get api called
    @GET("v0/jobstories.json?print=pretty")
    fun getjobStories(): Call<List<Int>>

    //Get News Details api
    @GET("v0/item/{articleid}.json?print=pretty")
    fun getNewsDetails(@Path("articleid") id: Int): Call<Top>

}