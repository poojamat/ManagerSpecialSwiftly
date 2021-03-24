package com.example.managerspecial.network

data class ManagerSpecial(val imageUrl: String,
                          val width: Int,
                          val height: Int,
                          val display_name: String,
                          val original_price: Double,
                          val price: Double)

data class ManagerSpecials(val canvasUnit: Int,
                           val managerSpecials: List<ManagerSpecial> )
