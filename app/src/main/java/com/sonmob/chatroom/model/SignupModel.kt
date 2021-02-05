package com.sonmob.chatroom.model

class SignupModel() {
    lateinit var name: String
    lateinit var email: String
    lateinit var password: String
    lateinit var id: String

    constructor(name: String, email: String, password: String, id: String) : this() {
        this.name = name
        this.email = email
        this.password = password
        this.id = id
    }


}