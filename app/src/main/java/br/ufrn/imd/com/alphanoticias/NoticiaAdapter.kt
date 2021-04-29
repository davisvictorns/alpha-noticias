package br.ufrn.imd.com.alphanoticias

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class NoticiaAdapter(private val mCtx: Context, private val layoutResId: Int, private val noticiaList: List<Noticia>, private val userCategory: String) :
    ArrayAdapter<Noticia>(mCtx, layoutResId, noticiaList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewTitulo = view.findViewById<TextView>(R.id.textViewTitulo)
        val textViewDescricao = view.findViewById<TextView>(R.id.textViewDescricao)
        val btnEditarNoticia = view.findViewById<TextView>(R.id.btnEditarNoticia)
        val btnExcluirNoticia = view.findViewById<TextView>(R.id.btnExcluirNoticia)

        val noticia = noticiaList[position]

        Log.d("NoticiaAdapter", "Got value: $userCategory")
        if(userCategory != "viewer"){
            btnEditarNoticia.visibility = View.VISIBLE;
            btnExcluirNoticia.visibility = View.VISIBLE;
        }

        textViewTitulo.text = noticia.titulo
        textViewDescricao.text = noticia.descricao

        return view
    }
}