package br.ufrn.imd.com.alphanoticias

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.squareup.picasso.Picasso
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
        val imageViewThumbnail = view.findViewById<ImageView>(R.id.imageViewThumbnail)
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
        textViewDescricao.text = noticia.descricao.take(50)+"..."

        if(noticia.urlImagem != ""){
            Picasso.get().load(noticia.urlImagem).into(imageViewThumbnail)
        }

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
            deletarNoticia(noticia.id)

            val intent = Intent(mCtx, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            mCtx.startActivity(intent)
        }

        return view
    }

    private fun deletarNoticia(idNoticia: String){
        FirebaseDatabase.getInstance().getReference("noticias").child(idNoticia).removeValue()

        Toast.makeText(
            mCtx,
            "Notícia removida!",
            Toast.LENGTH_LONG
        ).show()
    }
}