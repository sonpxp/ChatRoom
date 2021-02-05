package com.sonmob.chatroom.model

class MessageModel(){
    lateinit var message: String
    lateinit var sender_id: String
    lateinit var reciever_id: String
    lateinit var datetime: String

    constructor(message: String, sender_id: String, reciever_id: String, datetime: String) : this(){
        this.message = message
        this.sender_id = sender_id
        this.reciever_id = reciever_id
        this.datetime = datetime
    }
}