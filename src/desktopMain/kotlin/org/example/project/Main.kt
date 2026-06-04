package org.example.project

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePainter
import coil3.compose.setSingletonImageLoaderFactory
import coil3.network.ktor3.KtorNetworkFetcherFactory

fun main() = application {
    setSingletonImageLoaderFactory { context ->
        ImageLoader.Builder(context)
            .components {
                add(KtorNetworkFetcherFactory())
            }
            .build()
    }

    val windowState = rememberWindowState(width = 400.dp, height = 500.dp)

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Information about the movie"
    ) {
        App()
    }
}

@Composable
fun App() {
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Avengers - EndGame",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(20.dp))

            AsyncImage(
                model = "https://upload.wikimedia.org/wikipedia/pt/9/9b/Avengers_Endgame.jpg",
                contentDescription = "Logo Avengers EndGame",
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(250.dp),
                onState = { state ->
                    when (state) {
                        is AsyncImagePainter.State.Loading -> {
                            println("Coil: Loading the image...")
                        }
                        is AsyncImagePainter.State.Success -> {
                            println("Coil: Image loaded com success!")
                        }
                        is AsyncImagePainter.State.Error -> {
                            println("Coil: Error trying to load the image!")
                            state.result.throwable.printStackTrace()
                        }
                        else -> {}
                    }
                }
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Rate: 8.4  |  Year: 2019",
                fontSize = 16.sp,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}