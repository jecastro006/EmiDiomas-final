package com.ebc.muffinshop.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ebc.muffinshop.model.LeccionUiState

// pantalla de inicio
@Composable
fun PantallaInicio(
    state: LeccionUiState,
    onVerEjemplo: (String) -> Unit,
    onAbrirGuardada: (String) -> Unit
) {
    var idioma by rememberSaveable { mutableStateOf("Español") }
    var mostrarIdiomas by rememberSaveable { mutableStateOf(false) }
    Column(
        modifier = Modifier.fillMaxSize().verticalScroll(rememberScrollState()).padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Text("EmiDiomas", style = MaterialTheme.typography.headlineLarge)
        Text("Practica italiano con tarjetas y una lectura.")
        Text("Las lecciones ya están cargadas y puedes estudiar sin internet.")
        Text("Lección completa", style = MaterialTheme.typography.titleLarge)
        state.lecciones.filter { !it.esEjemplo }.forEach { leccion ->
            Card(modifier = Modifier.fillMaxWidth()) {
                Column(Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(leccion.titulo, style = MaterialTheme.typography.titleMedium)
                    Text("${leccion.tarjetas.size} tarjetas en italiano con traducción al español")
                    Text("${leccion.valoraciones.size} palabras clasificadas")
                    Text("El texto trata sobre una investigación criminal")
                    Button(onClick = { onAbrirGuardada(leccion.id) }) { Text("Estudiar lección") }
                }
            }
        }
        Text("Práctica corta", style = MaterialTheme.typography.titleLarge)
        Text("Un viaje a Roma tiene seis tarjetas para conocer la app")
        Text("Idioma de traducción de la práctica corta")
        Box {
            OutlinedButton(onClick = { mostrarIdiomas = true }) { Text(idioma) }
            DropdownMenu(expanded = mostrarIdiomas, onDismissRequest = { mostrarIdiomas = false }) {
                listOf("Español", "Inglés", "Francés").forEach { opcion ->
                    DropdownMenuItem(text = { Text(opcion) }, onClick = {
                        idioma = opcion
                        mostrarIdiomas = false
                    })
                }
            }
        }
        OutlinedButton(onClick = { onVerEjemplo(idioma) }) { Text("Empezar práctica corta") }
        Text("Las clasificaciones se guardan en este dispositivo")
    }
}
