package br.ufrn.imd.com.alphanoticias

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class User(val uid: String, val name: String? = null, val category: String? = null) {
    constructor() : this("", "", "")
}