package br.ufrn.imd.com.alphanoticias

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView

class NoticiaAdapter(private val mCtx: Context, private val layoutResId: Int, private val noticiaList: List<Noticia>) :
    ArrayAdapter<Noticia>(mCtx, layoutResId, noticiaList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewTitulo = view.findViewById<TextView>(R.id.textViewTitulo)
        val textViewDescricao = view.findViewById<TextView>(R.id.textViewDescricao)

        val noticia = noticiaList[position]

        textViewTitulo.text = noticia.titulo
        textViewDescricao.text = noticia.descricao

        return view

    }

}