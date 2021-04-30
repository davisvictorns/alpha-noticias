package br.ufrn.imd.com.alphanoticias

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class NoticiaAdapter(
    private val mCtx: Context,
    private val layoutResId: Int,
    private val noticiaList: List<Noticia>,
    private val userCategory: String
) :
    ArrayAdapter<Noticia>(mCtx, layoutResId, noticiaList) {

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        val layoutInflater: LayoutInflater = LayoutInflater.from(mCtx)
        val view: View = layoutInflater.inflate(layoutResId, null)

        val textViewTitulo = view.findViewById<TextView>(R.id.textViewTitulo)
        val textViewDescricao = view.findViewById<TextView>(R.id.textViewDescricao)
        val btnLerNoticia = view.findViewById<Button>(R.id.btnLerNoticia);
        val btnEditarNoticia = view.findViewById<TextView>(R.id.btnEditarNoticia)
        val btnExcluirNoticia = view.findViewById<TextView>(R.id.btnExcluirNoticia)

        val noticia = noticiaList[position]

        Log.d("NoticiaAdapter", "Got value: $userCategory")
        if (userCategory != "viewer") {
            btnEditarNoticia.visibility = View.VISIBLE;
            btnExcluirNoticia.visibility = View.VISIBLE;
        }

        textViewTitulo.text = noticia.titulo
        textViewDescricao.text = noticia.descricao

        btnLerNoticia.setOnClickListener {
            val intent = Intent(mCtx, DetalharNoticiaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("idNoticia", noticia.id)
            mCtx.startActivity(intent)
        }

        btnEditarNoticia.setOnClickListener {
            val intent = Intent(mCtx, EditarNoticiaActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            intent.putExtra("idNoticia", noticia.id)
            mCtx.startActivity(intent)
        }

        btnExcluirNoticia.setOnClickListener {
            // deletar a noticia
            // atualizar listview
            Toast.makeText(
                mCtx,
                "Funcionalidade não implementada",
                Toast.LENGTH_LONG
            ).show()
        }

        return view
    }
}