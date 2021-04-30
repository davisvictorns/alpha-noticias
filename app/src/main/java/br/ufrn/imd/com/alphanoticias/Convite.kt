package br.ufrn.imd.com.alphanoticias

import java.time.LocalDateTime


class Convite(val id: String, val email: String, val tipo: String, var aceito: Boolean? = false, val enviado_por_id: String? = null, val enviado_as: String? = null) {
    constructor() : this("", "", "", false, "", "") {

    }
}