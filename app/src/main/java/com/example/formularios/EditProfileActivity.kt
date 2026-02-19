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

    private val PICK_PROFILE = 200
    private val CAMERA_REQUEST = 100
    private lateinit var imgPerfil: ImageView
    private var uriPerfil: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        val edtNombre = findViewById<EditText>(R.id.edtNombre)
        val edtDescripcion = findViewById<EditText>(R.id.edtDescripcion)
        val btnGuardar = findViewById<Button>(R.id.btnGuardar)
        val btnActualizarFoto = findViewById<Button>(R.id.btnActualizarFoto)
        val spinner = findViewById<Spinner>(R.id.spinnerGenero)
        val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
        val cbPerfilPublico = findViewById<CheckBox>(R.id.cbPerfilPublico)

        imgPerfil = findViewById(R.id.imagenPerfil)

        //rellenar campos
        if (DataHolder.nombre != ""){
            edtNombre.setText(DataHolder.nombre);
        }
        if (DataHolder.descripcion != ""){
            edtDescripcion.setText(DataHolder.descripcion);
        }
        if(DataHolder.genero.isNotEmpty()){
            val index = (spinner.adapter as ArrayAdapter<String>).getPosition(DataHolder.genero)
            spinner.setSelection(index)
        }
        if(DataHolder.opcionSeleccionada != null){
            if(DataHolder.opcionSeleccionada == "Hombre"){
                val rbHombre = radioGroup.getChildAt(0) as RadioButton
                rbHombre.isChecked = true;
            }else if(DataHolder.opcionSeleccionada == "Mujer"){
                val rbMujer = radioGroup.getChildAt(1) as RadioButton
                rbMujer.isChecked = true;
            }else if (DataHolder.opcionSeleccionada == "Otro"){
                val rbOtro = radioGroup.getChildAt(2) as RadioButton
                rbOtro.isChecked = true;
            }else if (DataHolder.opcionSeleccionada == "Prefiero no decirlo"){
                val rbNoDecirlo = radioGroup.getChildAt(3) as RadioButton
                rbNoDecirlo.isChecked = true;
            }
        }
        cbPerfilPublico.isChecked = DataHolder.perfilPublico

        //  Mostrar foto si ya existe
        if (DataHolder.fotoPerfil != null) {
            imgPerfil.setImageURI(DataHolder.fotoPerfil)
            uriPerfil = DataHolder.fotoPerfil
        }

//        btnActualizarFoto.setOnClickListener {
//            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
//            intent.addCategory(Intent.CATEGORY_OPENABLE)
//            intent.type = "image/*"
//            intent.flags = Intent.FLAG_GRANT_READ_URI_PERMISSION or
//                    Intent.FLAG_GRANT_PERSISTABLE_URI_PERMISSION
//            startActivityForResult(intent, PICK_PROFILE)
//        }

        btnActualizarFoto.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST)

            } else {
                abrirCamara()
            }
        }


        btnGuardar.setOnClickListener {

            DataHolder.nombre = edtNombre.text.toString()
            DataHolder.descripcion = edtDescripcion.text.toString()

            // Spinner
            val spinner = findViewById<Spinner>(R.id.spinnerGenero)
            DataHolder.genero = spinner.selectedItem.toString()

            // RadioGroup
            val radioGroup = findViewById<RadioGroup>(R.id.radioGroup)
            val selectedId = radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val radioButton = findViewById<RadioButton>(selectedId)
                DataHolder.opcionSeleccionada = radioButton.text.toString()
                var valor:String = radioButton.text.toString()
                var valor2:String = radioButton.text.toString()
            }

            // Checkbox
            val cbPerfilPublico = findViewById<CheckBox>(R.id.cbPerfilPublico)
            DataHolder.perfilPublico = cbPerfilPublico.isChecked

            // Foto
            DataHolder.fotoPerfil = uriPerfil

            finish()
        }
    }

//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//
//        if (resultCode == Activity.RESULT_OK && data != null) {
//            val uri = data.data
//
//            if (requestCode == PICK_PROFILE && resultCode == Activity.RESULT_OK) {
//                val bitmap = data?.extras?.get("data") as? Bitmap
//                if (bitmap != null) {
//
//                    imgPerfil.setImageBitmap(bitmap)
//
//                    val uri = saveBitmapToCache(bitmap)
//                    uriPerfil = uri
//                    DataHolder.fotoPerfil = uri
//                }
//            }
//        }
//    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK && data != null) {

            if (requestCode == PICK_PROFILE) {

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

        if (requestCode == CAMERA_REQUEST && grantResults.isNotEmpty()
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
        startActivityForResult(intent, PICK_PROFILE)
    }
}