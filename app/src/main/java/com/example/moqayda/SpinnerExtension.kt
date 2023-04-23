package com.example.moqayda

import android.content.Context
import android.widget.ArrayAdapter
import android.widget.Spinner

fun Spinner.initSpinner(context:Context,spinner:Spinner,textArrayId:Int){
    ArrayAdapter.createFromResource(context,
        textArrayId,
        android.R.layout.simple_spinner_item
    ).also { adapter ->
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
         spinner.adapter = adapter
    }

}
