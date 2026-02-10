package com.example.formularios

import android.net.Uri

object DataHolder {
    var nombre: String = ""
    var descripcion: String = ""
    var genero: String = ""
    var opcionSeleccionada: String = ""
    var perfilPublico: Boolean = false
    var fotoPerfil: Uri? = null
    data class Contacto(
        val nombre: String,
        val telefono: String
    )
    val contactos: ArrayList<Contacto> = arrayListOf()

}
