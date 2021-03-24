package com.example.managerspecial.network

class Repository {

    fun managerSpecialResource(): ManagerSpecialResource{
        val managerSpecialAPI = RetrofitBuilder().apiClient().create(ManagerSpecialAPI::class.java)
        return ManagerSpecialResource(managerSpecialAPI)
    }
}