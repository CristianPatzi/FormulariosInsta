package com.example.formularios

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.formularios.DataHolder
import kotlin.jvm.java


class MainActivity : AppCompatActivity() {
    private lateinit var imgPerfil: ImageView
    private lateinit var txtNombre: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var layoutPublicaciones: ConstraintLayout

    private var contadorPublicaciones = 0
    private lateinit var publicaciones: List<ImageView>


    private val PICK_IMAGE = 100
    private val PICK_PROFILE = 200
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        imgPerfil = findViewById(R.id.imgPerfil)
        txtNombre = findViewById(R.id.txtNombre)
        txtDescripcion = findViewById(R.id.txtDescripcion)
        layoutPublicaciones = findViewById(R.id.layoutPublicaciones)

        publicaciones = listOf(
            findViewById(R.id.pub1),
            findViewById(R.id.pub2),
            findViewById(R.id.pub3),
            findViewById(R.id.pub4),
            findViewById(R.id.pub5),
            findViewById(R.id.pub6),
            findViewById(R.id.pub7),
            findViewById(R.id.pub8),
            findViewById(R.id.pub9)
        )

        findViewById<Button>(R.id.btnEditar).setOnClickListener {
            startActivity(Intent(this, EditProfileActivity::class.java))
        }

        findViewById<Button>(R.id.btnSubirFoto).setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_IMAGE)
        }

        imgPerfil.setOnClickListener {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, PICK_PROFILE)
        }
    }

    override fun onResume() {
        super.onResume()

        txtNombre.text = DataHolder.nombre
        txtDescripcion.text = DataHolder.descripcion

        // Foto de perfil
        DataHolder.fotoPerfil?.let {
            imgPerfil.setImageURI(it)
        }

        // Mostrar género
        findViewById<TextView>(R.id.txtGenero).text = "Género: ${DataHolder.genero}"

        // Mostrar radio button elegido
        findViewById<TextView>(R.id.txtOpcion).text = "Opción: ${DataHolder.opcionSeleccionada}"

        // Mostrar checkbox
        findViewById<TextView>(R.id.txtPerfilPublico).text =
            if (DataHolder.perfilPublico) "Perfil Público" else "Perfil Privado"
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data

            when (requestCode) {
                PICK_PROFILE -> imgPerfil.setImageURI(uri)

                PICK_IMAGE -> {
                    if (contadorPublicaciones < publicaciones.size) {
                        publicaciones[contadorPublicaciones].setImageURI(uri)
                        contadorPublicaciones++
                    } else {
                        Toast.makeText(this, "No puedes subir más imágenes", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }
}