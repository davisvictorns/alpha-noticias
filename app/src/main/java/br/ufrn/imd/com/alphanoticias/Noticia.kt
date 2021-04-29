package br.ufrn.imd.com.alphanoticias

class Noticia(val id: String, val titulo: String, val descricao: String) {
    constructor() : this("", "", "") {
        
    }
}