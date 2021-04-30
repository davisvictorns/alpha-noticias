package br.ufrn.imd.com.alphanoticias

class Noticia(val id: String, var titulo: String, var descricao: String, val autor_id: String? = null, val urlImagem: String? = null, var criada_as: String? = null) {
    constructor() : this("", "", "", "", "", "") {
        
    }
}