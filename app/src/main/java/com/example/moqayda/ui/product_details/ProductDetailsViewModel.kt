package com.example.moqayda.ui.product_details

import androidx.lifecycle.MutableLiveData
import com.example.moqayda.base.BaseViewModel

class ProductDetailsViewModel:BaseViewModel<Navigator>(){
    lateinit var description:String
    lateinit var name:String
    lateinit var productToSwapWithName:String
}