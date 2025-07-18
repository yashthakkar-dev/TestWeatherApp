package com.example.testweatherapp.data

//class DataOrException<T, Boolean, E : Exception>(
//    var data: T? = null,
//    var loading: Boolean? = null,
//    var e: E? = null
//) {}

sealed class Resource<T> (
    val data: T? = null,
    val message: String? = null
){
    class Success<T> (data: T) : Resource<T> (data)
    class Loading<T> (data: T? = null) : Resource<T> (data)
    class Error<T> (message: String, data: T? = null) : Resource<T> (data, message)
}