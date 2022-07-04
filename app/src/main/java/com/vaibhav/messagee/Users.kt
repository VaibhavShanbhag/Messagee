package com.vaibhav.messagee

class Users {
    var uid: String? = null
    var name: String? = null
    var email: String? = null
    var imageUri: String? = null

    constructor(){}

    constructor(uid: String?, name: String?, email: String?, imageUri: String){
        this.uid = uid
        this.name = name
        this.email = email
        this.imageUri = imageUri
    }
}