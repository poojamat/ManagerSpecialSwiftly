package com.example.managerspecial

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.ViewModel
import com.example.managerspecial.network.ManagerSpecial
import com.example.managerspecial.network.ManagerSpecials
import com.example.managerspecial.network.Repository
import com.example.managerspecial.network.Status
import org.koin.core.KoinComponent
import org.koin.core.inject

class ManagerSpecialViewModel : ViewModel(), KoinComponent{

    val managerSpecialList: LiveData<ManagerSpecialViewState> =
       Transformations.map(inject<Repository>().value.managerSpecialResource().fetch()) {managerSpecialResource->
           if(managerSpecialResource.status == Status.SUCCESS){
               ManagerSpecialViewState(managerSpecialResource.status, managerSpecialResource.data )
           } else{
               ManagerSpecialViewState(managerSpecialResource.status, null)
           }
       }

    fun wouldFitInDimension(
        canvasUnit: Int,
        totalHeightPixels: Int,
        tileDimension: Int,
        dimenUsed: Int
    ): Boolean {
        val unitPixels = getPixelsPerCanvasUnit(canvasUnit, totalHeightPixels)
        val dimensionPixelsNeeded = tileDimension * unitPixels
        val lastWidthPixels = dimenUsed * unitPixels
        return totalHeightPixels - (dimensionPixelsNeeded + lastWidthPixels) >= 0
    }

    private fun getPixelsPerCanvasUnit(canvasUnit: Int, dimen: Int): Int{
        return dimen/canvasUnit
    }

}

class ManagerSpecialViewState(val status: Status, val data: ManagerSpecials?)