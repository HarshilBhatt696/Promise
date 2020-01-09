package com.example.tipster.Model

data class ChatChannel(val userIds: MutableList<String>) {
    constructor() : this(mutableListOf())
}