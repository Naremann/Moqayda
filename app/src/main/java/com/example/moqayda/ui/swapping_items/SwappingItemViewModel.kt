package com.example.moqayda.ui.swapping_items

import com.example.moqayda.base.BaseViewModel

class SwappingItemViewModel:BaseViewModel<Navigator>() {
    var itemName:String?=null
    var navigator:Navigator?=null
    fun navigateToAddPrivateProductFragment(){
        navigator?.navigateToAddPrivateProductFragment()
    }
}