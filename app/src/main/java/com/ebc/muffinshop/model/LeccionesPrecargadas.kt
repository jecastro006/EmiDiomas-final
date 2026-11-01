package com.ebc.muffinshop.model

import android.content.Context
import com.ebc.muffinshop.R
import org.json.JSONObject

// lecciones incluidas en la aplicación
fun leccionesPrecargadas(context: Context): List<Leccion> {
    val texto = context.resources.openRawResource(R.raw.leccion_italiano)
        .bufferedReader(Charsets.UTF_8).use { it.readText() }
    val datos = JSONObject(texto)
    val leccion = leerLeccion(datos, datos.getString("videoId"), "Español", "misterio")
    return listOf(leccionEjemplo("Español"), leccion)
}

// leer las tarjetas del archivo local
fun leerLeccion(datos: JSONObject, videoId: String, idioma: String, id: String): Leccion {
    fun texto(objeto: JSONObject, campo: String, limite: Int = 2500): String {
        val valor = objeto.getString(campo).trim()
        require(valor.isNotEmpty() && valor.length <= limite) { "Falta un dato en la lección precargada." }
        return valor
    }
    val lista = datos.getJSONArray("tarjetas")
    require(lista.length() in 1..80) { "No se recibieron tarjetas válidas." }
    val tarjetas = (0 until lista.length()).map { i ->
        val tarjeta = lista.getJSONObject(i)
        Tarjeta(texto(tarjeta, "palabra"), texto(tarjeta, "ejemplo"),
            mapOf(idioma to Traduccion(texto(tarjeta, "significado"),
                texto(tarjeta, "ejemploTraducido"), texto(tarjeta, "gramatica"))))
    }
    return Leccion(id, videoId, idioma, texto(datos, "titulo", 200),
        texto(datos, "idiomaOriginal", 100), tarjetas,
        texto(datos, "lectura", 20_000), texto(datos, "lecturaTraducida", 20_000))
}
