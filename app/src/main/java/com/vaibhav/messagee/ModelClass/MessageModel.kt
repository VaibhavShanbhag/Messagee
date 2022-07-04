package com.vaibhav.messagee.ModelClass

class MessageModel {
    var message: String? = null
    var senderId: String? = null
    var timeStamp: Long? = null

    constructor(){}

    constructor(message: String?, senderId: String?, timeStamp: Long?){
        this.message = message
        this.senderId = senderId
        this.timeStamp = timeStamp
    }
}