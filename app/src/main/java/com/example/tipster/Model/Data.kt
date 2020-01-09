package com.example.tipster.Model

data class User(val name : String , val gender : String , val Bio : String , val profilePicturePath : String? , val Interest : ArrayList<String> , val registrationTokens: MutableList<String>, val FireabseAuthToken : String , val numberOfPeopleDating : ArrayList<String> , val requests : ArrayList<String> ,val declineOrBlocked  : ArrayList<String>) {
    constructor() : this("" , "" , "", null  ,ArrayList<String>() , mutableListOf() , "" , ArrayList<String>() , ArrayList<String>() ,ArrayList<String>() )


}