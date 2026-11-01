package com.ebc.muffinshop.model

// significado y ejemplos traducidos
data class Traduccion(
    val significado: String,
    val ejemploTraducido: String,
    val gramatica: String
)

// datos de las tarjetas
data class Tarjeta(
    val palabra: String,
    val ejemplo: String,
    val traducciones: Map<String, Traduccion>
)

// opciones para clasificar una palabra
enum class Comprension(val nombre: String) {
    REPETIR("Repetir"), DIFICIL("Difícil"), FACIL("Fácil"), APRENDIDA("Aprendida")
}

// estado de la lección en pantalla
data class LeccionUiState(
    val leccion: Leccion = leccionEjemplo("Español"),
    val lecciones: List<Leccion> = emptyList(),
    val orden: List<Int> = leccion.tarjetas.indices.toList(),
    val posicion: Int = 0,
    val mostrarRespuesta: Boolean = false,
    val enRepaso: Boolean = false,
    val irATarjetas: Boolean = false
) {
    val idioma: String get() = leccion.idioma
    val valoraciones: Map<Int, Comprension> get() = leccion.valoraciones
}

// datos de una lección completa
data class Leccion(
    val id: String,
    val videoId: String,
    val idioma: String,
    val titulo: String,
    val idiomaOriginal: String,
    val tarjetas: List<Tarjeta>,
    val lectura: String,
    val lecturaTraducida: String,
    val esEjemplo: Boolean = false,
    val valoraciones: Map<Int, Comprension> = emptyMap()
)

// lección con palabras precargadas
fun leccionEjemplo(idioma: String) = Leccion(
    id = "ejemplo", videoId = "", idioma = idioma, titulo = "Un viaje a Roma",
    idiomaOriginal = "Italiano", tarjetas = tarjetasItaliano,
    lectura = lecturaItaliano, lecturaTraducida = lecturaTraducida.getValue(idioma), esEjemplo = true
)
