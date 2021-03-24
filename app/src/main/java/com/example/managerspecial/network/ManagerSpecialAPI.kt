package com.example.managerspecial.network

import retrofit2.http.GET

interface ManagerSpecialAPI {
    @GET("/Swiftly-Systems/code-exercise-android/master/backup")
    suspend fun getManagerSpecials(): ManagerSpecials

    //https://raw.githubusercontent.com/Swiftly-Systems/code-exercise-android/master/backup
}