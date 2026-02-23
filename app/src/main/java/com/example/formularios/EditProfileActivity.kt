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
import androidx.databinding.DataBindingUtil
import com.example.formularios.databinding.ActivityContactsBinding
import com.example.formularios.databinding.ActivityEditProfileBinding
import java.io.File

lateinit var bindingEditProfile: ActivityEditProfileBinding
class EditProfileActivity : AppCompatActivity() {

    private val PICK_PROFILE = 200
    private val CAMERA_REQUEST = 100
    private lateinit var imgPerfil: ImageView
    private var uriPerfil: Uri? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)
        bindingEditProfile = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)

        imgPerfil = findViewById(R.id.imagenPerfil)

        //rellenar campos
        if (DataHolder.nombre != ""){
            bindingEditProfile.edtNombre.setText(DataHolder.nombre);
        }
        if (DataHolder.descripcion != ""){
            bindingEditProfile.edtDescripcion.setText(DataHolder.descripcion);
        }
        if(DataHolder.genero.isNotEmpty()){
            val index = (bindingEditProfile.spinnerGenero.adapter as ArrayAdapter<String>).getPosition(DataHolder.genero)
            bindingEditProfile.spinnerGenero.setSelection(index)
        }
        if(DataHolder.opcionSeleccionada != null){
            if(DataHolder.opcionSeleccionada == "Hombre"){
                val rbHombre = bindingEditProfile.radioGroup.getChildAt(0) as RadioButton
                rbHombre.isChecked = true;
            }else if(DataHolder.opcionSeleccionada == "Mujer"){
                val rbMujer = bindingEditProfile.radioGroup.getChildAt(1) as RadioButton
                rbMujer.isChecked = true;
            }else if (DataHolder.opcionSeleccionada == "Otro"){
                val rbOtro = bindingEditProfile.radioGroup.getChildAt(2) as RadioButton
                rbOtro.isChecked = true;
            }else if (DataHolder.opcionSeleccionada == "Prefiero no decirlo"){
                val rbNoDecirlo = bindingEditProfile.radioGroup.getChildAt(3) as RadioButton
                rbNoDecirlo.isChecked = true;
            }
        }
        bindingEditProfile.cbPerfilPublico.isChecked = DataHolder.perfilPublico

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

        bindingEditProfile.btnActualizarFoto.setOnClickListener {
            if (checkSelfPermission(android.Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED) {

                requestPermissions(arrayOf(android.Manifest.permission.CAMERA), CAMERA_REQUEST)

            } else {
                abrirCamara()
            }
        }


        bindingEditProfile.btnGuardar.setOnClickListener {

            DataHolder.nombre = bindingEditProfile.edtNombre.text.toString()
            DataHolder.descripcion = bindingEditProfile.edtDescripcion.text.toString()

            // Spinner
            DataHolder.genero = bindingEditProfile.spinnerGenero.selectedItem.toString()

            // RadioGroup
            val selectedId = bindingEditProfile.radioGroup.checkedRadioButtonId
            if (selectedId != -1) {
                val radioButton = findViewById<RadioButton>(selectedId)
                DataHolder.opcionSeleccionada = radioButton.text.toString()
                var valor:String = radioButton.text.toString()
                var valor2:String = radioButton.text.toString()
            }

            // Checkbox
            DataHolder.perfilPublico = bindingEditProfile.cbPerfilPublico.isChecked

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