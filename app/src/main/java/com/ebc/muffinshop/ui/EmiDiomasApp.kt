package com.ebc.muffinshop.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Style
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.platform.LocalContext
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ebc.muffinshop.ui.screens.PantallaInicio
import com.ebc.muffinshop.ui.screens.PantallaLectura
import com.ebc.muffinshop.ui.screens.PantallaProgreso
import com.ebc.muffinshop.ui.screens.PantallaRepaso
import com.ebc.muffinshop.ui.screens.PantallaTarjetas
import com.ebc.muffinshop.viewmodel.LeccionViewModel

// pantallas y navegación
@Composable
fun EmiDiomasApp(viewModel: LeccionViewModel = viewModel()) {
    val navController = rememberNavController()
    val backEntry by navController.currentBackStackEntryAsState()
    val actual = backEntry?.destination?.route
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current
    LaunchedEffect(Unit) { viewModel.cargarLecciones(context) }
    LaunchedEffect(state.irATarjetas) {
        if (state.irATarjetas) {
            navController.navigate("tarjetas") { popUpTo("inicio"); launchSingleTop = true }
            viewModel.navegacionAtendida()
        }
    }
    val rutas = listOf("inicio", "tarjetas", "repaso", "progreso")
    val etiquetas = listOf("Inicio", "Lección", "Repaso", "Progreso")
    val iconos = listOf(Icons.Default.Home, Icons.Default.Style, Icons.Default.Refresh, Icons.Default.BarChart)

    // menú de abajo
    Scaffold(bottomBar = {
        NavigationBar {
            rutas.forEachIndexed { indice, ruta ->
                NavigationBarItem(
                    selected = actual == ruta || (ruta == "tarjetas" && actual == "lectura"),
                    onClick = {
                        if (ruta == "tarjetas" && state.enRepaso) {
                            viewModel.salirDeRepaso()
                        }
                        navController.navigate(ruta) {
                            popUpTo("inicio")
                            launchSingleTop = true
                        }
                    },
                    icon = { Icon(iconos[indice], contentDescription = null) },
                    label = { Text(etiquetas[indice]) }
                )
            }
        }
    }) { espacio ->
        NavHost(navController, startDestination = "inicio", modifier = Modifier.padding(espacio)) {
            composable("inicio") {
                PantallaInicio(state, viewModel::iniciarLeccion, viewModel::abrirGuardada)
            }
            composable("tarjetas") {
                PantallaTarjetas(state, viewModel::mostrarRespuesta, viewModel::calificar,
                    viewModel::anterior,
                    onLectura = { navController.navigate("lectura") },
                    onRepaso = { navController.navigate("repaso") })
            }
            composable("lectura") {
                PantallaLectura(state, onTarjetas = { navController.navigate("inicio") { popUpTo("inicio") { inclusive = true } } })
            }
            composable("repaso") {
                PantallaRepaso(state, onEmpezar = {
                    viewModel.iniciarRepaso()
                    navController.navigate("tarjetas") { launchSingleTop = true }
                })
            }
            composable("progreso") { PantallaProgreso(state) }
        }
    }
}
