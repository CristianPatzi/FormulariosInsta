package com.example.formularios

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.android.gms.common.data.DataHolder
import kotlin.jvm.java


class MainActivity : AppCompatActivity() {
    private lateinit var imgPerfil: ImageView
    private lateinit var txtNombre: TextView
    private lateinit var txtDescripcion: TextView
    private lateinit var layoutPublicaciones: LinearLayout

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
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {
            val uri = data.data

            when (requestCode) {
                PICK_PROFILE -> imgPerfil.setImageURI(uri)

                PICK_IMAGE -> {
                    val imageView = ImageView(this)
                    imageView.setImageURI(uri)
                    imageView.layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        600
                    )
                    imageView.scaleType = ImageView.ScaleType.CENTER_CROP
                    layoutPublicaciones.addView(imageView)
                }
            }
        }
    }
}
