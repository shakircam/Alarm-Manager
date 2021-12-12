package com.itmedicus.randomuser.model

data class User(
    val info: Info,
    val results: List<Result>
)
   /* data class Result(
        val cell: String,
        val dob: Dob,
        val email: String,
        val gender: String,
        val id: Id,
        val location: Location,
        val login: Login,
        val name: Name,
        val nat: String,
        val phone: String,
        val picture: Picture,
        val registered: Registered
    )*/
