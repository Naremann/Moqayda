package com.example.moqayda.ui.register

import androidx.databinding.ObservableField
import com.example.moqayda.ui.register.Navigator
import com.example.moqayda.base.BaseViewModel

class RegisterViewModel : BaseViewModel<Navigator>() {
    val firstName = ObservableField<String>()
    val lastName = ObservableField<String>()
    val mobile = ObservableField<String>()
    val country = ObservableField<String>()
    val city = ObservableField<String>()
    val address = ObservableField<String>()
    val email = ObservableField<String>()
    val password = ObservableField<String>()

    val firstNameError = ObservableField<String>()
    val lastNameError = ObservableField<String>()
    val mobileError = ObservableField<String>()
    val countryError = ObservableField<String>()
    val cityError = ObservableField<String>()
    val addressError = ObservableField<String>()
    val emailError = ObservableField<String>()
    val passwordError = ObservableField<String>()
    lateinit var navigator : Navigator
    fun createAccount(){
        if(validate()){
            addUser()
        }
    }

    private fun addUser() {

    }

    private fun validate() : Boolean{
        var validate = true
        if(firstName.get().isNullOrBlank()){
            firstNameError.set("Please enter first name")
            validate = false
        }
        else
            firstNameError.set(null)
        if(lastName.get().isNullOrBlank()){
            lastNameError.set("Please enter last name")
            validate = false
        }
        else
            lastNameError.set(null)
        if(mobile.get().isNullOrBlank()){
            mobileError.set("Please enter mobile")
            validate = false
        }
        else
            mobileError.set(null)
        if(country.get().isNullOrBlank()){
            countryError.set("Please enter country")
            validate = false
        }
        else
            countryError.set(null)
        if(city.get().isNullOrBlank()){
            cityError.set("Please enter city")
            validate = false
        }
        else
            cityError.set(null)
        if(address.get().isNullOrBlank()){
            addressError.set("Please enter address")
            validate = false
        }
        else
            addressError.set(null)
        if(email.get().isNullOrBlank()){
            emailError.set("Please enter the email")
            validate = false
        }
        else
            emailError.set(null)
        if(password.get().isNullOrBlank()) {
            passwordError.set("Please enter your password")
            validate = false
        }
        else
            passwordError.set(null)
        return validate

    }
}