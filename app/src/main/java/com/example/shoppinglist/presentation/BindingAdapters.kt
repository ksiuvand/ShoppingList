package com.example.shoppinglist.presentation

import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

@BindingAdapter("errorInputName")
fun bindErrorInputName(textInputLayout: TextInputLayout, isError: Boolean){
    if (isError){
        textInputLayout.error = "Error"
    }else{
        textInputLayout.error = null
    }
}

@BindingAdapter("errorInputCount")
fun bindErrorInputCount(textInputLayout: TextInputLayout, isError: Boolean){
    if (isError){
        textInputLayout.error = "Error"
    }else{
        textInputLayout.error = null
    }
}