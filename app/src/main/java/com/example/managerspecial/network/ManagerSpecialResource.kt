package com.example.managerspecial.network

import androidx.lifecycle.liveData
import kotlinx.coroutines.Dispatchers
import org.koin.core.KoinComponent
import retrofit2.HttpException

class ManagerSpecialResource(private val managerSpecialAPI: ManagerSpecialAPI): KoinComponent {
    fun fetch() =
        liveData<LiveDataResource<ManagerSpecials>>(Dispatchers.IO) {
            emit(LiveDataResource.loading())
            try {
                emit(LiveDataResource.success(data = managerSpecialAPI.getManagerSpecials()))
            } catch (exception: HttpException) {
                emit(LiveDataResource.error(exception))
            }
        }

}