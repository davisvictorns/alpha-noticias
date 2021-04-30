package br.ufrn.imd.com.alphanoticias

class Noticia(val id: String, val titulo: String, val descricao: String, val autor_id: String? = null, val urlImagem: String? = null, val criada_as: String? = null) {
    constructor() : this("", "", "", "", "", "") {
        
    }
}