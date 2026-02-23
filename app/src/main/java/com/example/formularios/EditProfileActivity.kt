package com.example.formularios

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.FileProvider
import java.io.File

class EditProfileActivity : AppCompatActivity() {

    private val FOTO_SACADA = 200
    private val PETICION_CAMARA = 100
    private lateinit var imgPerfil: ImageView
    private var uriPerfil: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtDescripcion = findViewById<EditText>(R.id.edtDescripcion)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnActualizarFoto = findViewById<Button>(R.id.btnActualizarFoto)
        val spinner = findViewById<Spinner>(R.id.spinnerPais)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val cbPerfilPublico = findViewById<CheckBox>(R.id.cbPerfilPublico)

        imgPerfil = findViewById(R.id.imagenPerfil)
        
        if (DataHolder.nombre != ""){
            edtNombre.setText(DataHolder.nombre);
        }
        if (DataHolder.descripcion != ""){
            edtDescripcion.setText(DataHolder.descripcion);
        }
        if(DataHolder.pais.isNotEmpty()){
            val index = (spinner.adapter as ArrayAdapter<String>).getPosition(DataHolder.pais)
            spinner.setSelection(index)
        }
        if(DataHolder.genero != null){
            if(DataHolder.genero == "Hombre"){
                val rbHombre = radioGroup.getChildAt(0) as RadioButton
                rbHombre.isChecked = true;
            }else if(DataHolder.genero == "Mujer"){
                val rbMujer = radioGroup.getChildAt(1) as RadioButton
                rbMujer.isChecked = true;
            }else if (DataHolder.genero == "Otro"){
                val rbOtro = radioGroup.getChildAt(2) as RadioButton
                rbOtro.isChecked = true;
            }else if (DataHolder.genero == "Prefiero no decirlo"){
                val rbNoDecirlo = radioGroup.getChildAt(3) as RadioButton
                rbNoDecirlo.isChecked = true;
            }
        }
        cbPerfilPublico.isChecked = DataHolder.perfilPublico
        
        if (DataHolder.fotoPerfil != null) {
            imgPerfil.setImageURI(DataHolder.fotoPerfil)
            uriPerfil = DataHolder.fotoPerfil
        }
        
        btnActualizarFoto.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), PETICION_CAMARA)

            } else {
                abrirCamara()
            }
        }


        btnGuardar.setOnClickListener {

            DataHolder.nombre = edtNombre.text.toString()
            DataHolder.descripcion = edtDescripcion.text.toString()
            
            val spinner = findViewById<Spinner>(R.id.spinnerPais)
            DataHolder.pais = spinner.selectedItem.toString()


            val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val radioButton = findViewById<RadioButton>(selectedId)
                DataHolder.genero = radioButton.text.toString()
                var valor:String = radioButton.text.toString()
                var valor2:String = radioButton.text.toString()
            }
            
            val cbPerfilPublico = findViewById<CheckBox>(R.id.cbPerfilPublico)
            DataHolder.perfilPublico = cbPerfilPublico.isChecked
            
            DataHolder.fotoPerfil = uriPerfil

            finish()
        }
    }
    
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == FOTO_SACADA) {

                val bitmap = data.extras?.get("data") as? Bitmap

                if (bitmap != null) {

                    imgPerfil.setImageBitmap(bitmap)

                    val uri = saveBitmapToCache(bitmap)

                    uriPerfil = uri
                    DataHolder.fotoPerfil = uri
                }
            }
        }
    }
    
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if (requestCode == PETICION_CAMARA && grantResults.isNotEmpty()
            && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

            abrirCamara()
        }
    }

    private fun saveBitmapToCache(bitmap: Bitmap): Uri {
        val file = File(cacheDir, "foto_perfil.jpg")
        file.outputStream().use {
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, it)
        }
        return FileProvider.getUriForFile(
            this,
            "${packageName}.provider",
            file
        )
    }
    private fun abrirCamara() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, FOTO_SACADA)
    }
}