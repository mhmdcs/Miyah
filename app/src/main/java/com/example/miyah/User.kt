package com.example.miyah

data class User(
    var email: String? = "", var name: String? = "", var phone: String? = "",
    var type: String? = "", var location: String? = "", var espDistance: Int? = 0,
    var requestStatus: String? = ""
)
