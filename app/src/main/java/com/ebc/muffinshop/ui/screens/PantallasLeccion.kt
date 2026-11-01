package com.ebc.muffinshop.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.ebc.muffinshop.model.Comprension
import com.ebc.muffinshop.model.LeccionUiState

// espacio que comparten las pantallas
@Composable
private fun PantallaBase(titulo: String, state: LeccionUiState, contenido: @Composable ColumnScope.() -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text(titulo, style = MaterialTheme.typography.headlineMedium)
        Text(
            if (state.leccion.esEjemplo) "Lección de ejemplo · ${state.leccion.idiomaOriginal}"
            else "${state.leccion.titulo} · ${state.leccion.idiomaOriginal}",
            color = MaterialTheme.colorScheme.primary
        )
        contenido()
    }
}

// pantalla de tarjetas
@Composable
fun PantallaTarjetas(
    state: LeccionUiState,
    onMostrar: () -> Unit,
    onCalificar: (Comprension) -> Unit,
    onAnterior: () -> Unit,
    onLectura: () -> Unit,
    onRepaso: () -> Unit
) {
    PantallaBase(if (state.enRepaso) "Tarjetas de repaso" else "Tarjetas", state) {
        Text("Traducción: ${state.idioma}")
        val id = state.orden.getOrNull(state.posicion)
        if (id == null) {
            Text("Terminaste este recorrido.", style = MaterialTheme.typography.titleLarge)
            Text("Puedes leer el texto o repasar las palabras que te costaron más.")
            Button(onClick = onRepaso) { Text("Ver repaso") }
            OutlinedButton(onClick = onLectura) { Text("Ver lectura") }
        } else {
            val tarjeta = state.leccion.tarjetas[id]
            val traduccion = tarjeta.traducciones.getValue(state.idioma)
            Text("Tarjeta ${id + 1} de ${state.leccion.tarjetas.size}")
            Text("Pendientes en esta vuelta: ${state.orden.size - state.posicion}")
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(20.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Text(tarjeta.palabra, style = MaterialTheme.typography.headlineLarge)
                    Text(tarjeta.ejemplo)
                    if (state.mostrarRespuesta) {
                        Text(traduccion.significado, style = MaterialTheme.typography.titleLarge)
                        Text(traduccion.ejemploTraducido)
                        Text(traduccion.gramatica)
                    }
                }
            }
            Button(onClick = onMostrar, modifier = Modifier.fillMaxWidth()) {
                Text(if (state.mostrarRespuesta) "Ocultar significado" else "Mostrar significado")
            }
            Text("¿Qué tanto comprendes esta palabra?")
            Text("Repetir: pronto · Difícil: más adelante · Fácil: al final · Aprendida: no vuelve en esta vuelta.")
            Comprension.entries.chunked(2).forEach { opciones ->
                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    opciones.forEach { opcion ->
                        OutlinedButton(
                            onClick = { onCalificar(opcion) },
                            enabled = state.mostrarRespuesta,
                            modifier = Modifier.weight(1f)
                        ) { Text(opcion.nombre) }
                    }
                }
            }
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton(onClick = onAnterior, enabled = state.posicion > 0) { Text("Anterior") }
            }
        }
    }
}

// lectura y vocabulario
@Composable
fun PantallaLectura(state: LeccionUiState, onTarjetas: () -> Unit) {
    PantallaBase("Lectura", state) {
        Text(state.leccion.titulo, style = MaterialTheme.typography.titleLarge)
        Text(state.leccion.lectura, style = MaterialTheme.typography.bodyLarge)
        Text("Traducción: ${state.idioma}", fontWeight = FontWeight.Bold)
        Text(state.leccion.lecturaTraducida)
        Text("Vocabulario de la lectura", style = MaterialTheme.typography.titleMedium)
        state.leccion.tarjetas.forEach { tarjeta ->
            Text("${tarjeta.palabra}: ${tarjeta.traducciones.getValue(state.idioma).significado}")
        }
        OutlinedButton(onClick = onTarjetas) { Text("Volver al inicio") }
    }
}

// palabras para repasar
@Composable
fun PantallaRepaso(state: LeccionUiState, onEmpezar: () -> Unit) {
    val pendientes = state.leccion.tarjetas.indices.filter {
        state.valoraciones[it] == Comprension.REPETIR || state.valoraciones[it] == Comprension.DIFICIL
    }
    PantallaBase("Repaso", state) {
        Text("Aquí aparecen las tarjetas marcadas como Repetir o Difícil.")
        if (pendientes.isEmpty()) {
            Text("No tienes tarjetas pendientes de repaso. Puedes clasificar palabras en Lección.")
        } else {
            Text("${pendientes.size} tarjetas para repasar")
            pendientes.forEach { id ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(Modifier.padding(16.dp)) {
                        Text(state.leccion.tarjetas[id].palabra, style = MaterialTheme.typography.titleMedium)
                        Text(state.valoraciones.getValue(id).nombre)
                    }
                }
            }
            Button(onClick = onEmpezar, modifier = Modifier.fillMaxWidth()) { Text("Empezar repaso") }
        }
    }
}

// progreso de la lección
@Composable
fun PantallaProgreso(state: LeccionUiState) {
    PantallaBase("Progreso", state) {
        val aprendidas = state.valoraciones.values.count { it == Comprension.APRENDIDA }
        Text("Aprendidas: $aprendidas de ${state.leccion.tarjetas.size}", style = MaterialTheme.typography.titleLarge)
        LinearProgressIndicator(
            progress = { aprendidas.toFloat() / state.leccion.tarjetas.size },
            modifier = Modifier.fillMaxWidth()
        )
        Text("Clasificadas: ${state.valoraciones.size} de ${state.leccion.tarjetas.size}")
        Comprension.entries.forEach { opcion ->
            val cantidad = state.valoraciones.values.count { it == opcion }
            Text("${opcion.nombre}: $cantidad")
        }
        Text("Sin clasificar: ${state.leccion.tarjetas.size - state.valoraciones.size}")
        Text("Tus clasificaciones se guardan en este dispositivo. Al abrir una lección empieza una vuelta nueva.")
    }
}
