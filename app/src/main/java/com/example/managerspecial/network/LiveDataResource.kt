package com.example.managerspecial.network

import retrofit2.HttpException

data class LiveDataResource<out T>(val status: Status, val data: T? = null, val error: HttpException? = null) {
    companion object {
        fun <T> success(data: T?): LiveDataResource<T> {
            return LiveDataResource(Status.SUCCESS, data = data)
        }

        fun <T> error(error: HttpException?): LiveDataResource<T> {
            return LiveDataResource(Status.ERROR, error = error)
        }


        fun <T> loading(): LiveDataResource<T> {
            return LiveDataResource(Status.LOADING)
        }

        /**
         * Sets the state to UNSET, so that we don't ever need to reset the LiveData to null.
         */
        fun <T> unset(): LiveDataResource<T> {
            return LiveDataResource(Status.UNSET, null)
        }
    }

    fun isDataValid() = status == Status.SUCCESS
}

/**
 * @return takes in a LiveDataResource<X> and returns a LiveDataResource<Y>, with the new data of type <Y>.
 **/
fun <X, Y> LiveDataResource<X>.transformData(data: Y? = null): LiveDataResource<Y> {
    return LiveDataResource(status, data, error)
}