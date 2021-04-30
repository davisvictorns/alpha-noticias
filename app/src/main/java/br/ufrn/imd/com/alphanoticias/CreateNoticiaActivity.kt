package br.ufrn.imd.com.alphanoticias

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import java.net.URI
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class CreateNoticiaActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    lateinit var editTextTitulo: EditText
    lateinit var editTextDescricao: EditText
    lateinit var btnCreateNoticia: Button
    lateinit var btnAddImagem: Button
    lateinit var viewImage: ImageView
    lateinit var database: FirebaseDatabase
    lateinit var ref: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_noticia)

        database = FirebaseDatabase.getInstance()
        ref = database.getReference("noticias")

        auth = FirebaseAuth.getInstance()

        editTextTitulo = findViewById(R.id.createTitulo)
        editTextDescricao = findViewById(R.id.createDescricao)
        btnAddImagem = findViewById(R.id.btnAddImagem)
        btnCreateNoticia = findViewById(R.id.btn_create_noticia)

        btnAddImagem.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

        btnCreateNoticia.setOnClickListener {
            uploadImageToFirebaseStorage()

            finish()
        }
    }

    private var selectImagemUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 0 && resultCode == Activity.RESULT_OK && data != null){
            Log.d("CreateNoticia", "Uma imagem foi selecionada")

            selectImagemUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectImagemUri)

            viewImage = findViewById(R.id.viewImage)
            viewImage.setImageBitmap(bitmap)
        }
    }

    private fun saveNoticia(urlImagem: String? = "") {
        val titulo = editTextTitulo.text.toString()
        val descricao = editTextDescricao.text.toString()

        if (titulo.isEmpty()) {
            editTextTitulo.error = "Digite um titulo"
            return
        }
        if (descricao.isEmpty()) {
            editTextDescricao.error = "Digite uma descricao"
            return
        }

        val localDateTime: LocalDateTime = LocalDateTime.now()
        val formatter: DateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")
        val dataHotaAtual: String = formatter.format(localDateTime)

        val noticiaId = ref.push().key
        val noticia = noticiaId?.let { Noticia(it, titulo, descricao, auth.uid, urlImagem, dataHotaAtual) }

        if (noticiaId != null) {
            ref.child(noticiaId).setValue(noticia).addOnCompleteListener {
                Toast.makeText(
                    applicationContext,
                    "Notícia cadastrada com sucesso",
                    Toast.LENGTH_LONG
                ).show()
            }
        }
    }

    private fun uploadImageToFirebaseStorage(){

        Log.d("CriacaoImagem", "URI $selectImagemUri")
        if(selectImagemUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        ref.putFile(selectImagemUri!!).addOnSuccessListener{ it_put ->
            Log.d("CriacaoImagem", "Upload feito ${it_put.metadata?.path}")
            ref.downloadUrl.addOnSuccessListener {
                Log.d("CriacaoImagem", "Url $it")
                saveNoticia(it.toString())
            }
        }
    }
}