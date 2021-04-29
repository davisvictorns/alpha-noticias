package br.ufrn.imd.com.alphanoticias

import java.time.LocalDateTime

class Noticia(val id: String, val titulo: String, val descricao: String, val autor_id: String? = null, val urlImagem: String? = null, val criada_as: LocalDateTime? = LocalDateTime.now()) {
    constructor() : this("", "", "", "", "", LocalDateTime.now()) {
        
    }
}