package br.ufrn.imd.com.alphanoticias

import java.time.LocalDateTime

class Convite(val email: String, val tipo: String, val aceito: Boolean? = false, val enviado_por_id: String? = null, val enviado_as: LocalDateTime? = LocalDateTime.now()) {
    constructor() : this("", "", false, "", LocalDateTime.now()) {

    }
}