package org.example.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.toComposeImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import androidx.compose.ui.window.rememberWindowState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import org.jetbrains.skia.Image as SkiaImage

fun main() = application {
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
fun App(imageUrl: String = "https://upload.wikimedia.org/wikipedia/pt/9/9b/Avengers_Endgame.jpg") {
    val (imageBitmap, isLoading, isError) = setUpImage(imageUrl)
    load(isLoading, isError, imageBitmap)
}

@Composable
private fun setUpImage(imageUrl: String): Triple<ImageBitmap?, Boolean, Boolean> {
    var imageBitmap by remember { mutableStateOf<ImageBitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isError by remember { mutableStateOf(false) }

    LaunchedEffect(imageUrl) {
        withContext(Dispatchers.IO) {
            try {
                println("Starting image download manually...")
                val bitmap = imageUrl.loadAsComposeBitmap()
                imageBitmap = bitmap
                isLoading = false
                println("Image loaded and converted with success!")
            } catch (e: Exception) {
                e.printStackTrace()
                isError = true
                isLoading = false
            }
        }
    }
    return Triple(imageBitmap, isLoading, isError)
}

@Composable
private fun load(
    isLoading: Boolean,
    isError: Boolean,
    imageBitmap: ImageBitmap?
) {
    val darkColors = darkColorScheme(
        background = Color(0xFF121212),
        surface = Color(0xFF1E1E1E),
        primary = Color(0xFFFFD700),
        secondary = Color(0xFF64B5F6)
    )

    MaterialTheme(colorScheme = darkColors) {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(280.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface),
                    contentAlignment = Alignment.Center
                ) {
                    when {
                        isLoading -> {
                            CircularProgressIndicator(modifier = Modifier.size(50.dp))
                        }

                        isError || imageBitmap == null -> {
                            Text("Error trying to load the image!", color = MaterialTheme.colorScheme.error)
                        }

                        else -> {
                            Image(
                                bitmap = imageBitmap,
                                contentDescription = "Logo Avengers EndGame",
                                contentScale = ContentScale.Fit,
                                modifier = Modifier.size(250.dp)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 12.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Filled.EmojiEvents,
                            contentDescription = "Troféu de Nota",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "8.4",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(MaterialTheme.colorScheme.surface)
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "2019",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.secondary // Cor Azul em destaque
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = "Avengers - EndGame",
                    fontSize = 26.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = Color.White,
                    style = MaterialTheme.typography.headlineMedium
                )
            }
        }
    }
}

fun String.loadAsComposeBitmap(): ImageBitmap {
    val connection = URL(this).openConnection() as HttpURLConnection
    connection.connectTimeout = 5000
    connection.readTimeout = 5000
    connection.requestMethod = "GET"

    connection.setRequestProperty("User-Agent", "Mozilla/5.0")

    if (connection.responseCode != HttpURLConnection.HTTP_OK) {
        throw Exception("Fail to download the image. HTTP code: ${connection.responseCode}")
    }

    val inputStream = connection.inputStream
    val outputStream = ByteArrayOutputStream()

    inputStream.use { input ->
        outputStream.use { output ->
            val buffer = ByteArraySize(4096)
            var bytesRead: Int
            while (input.read(buffer).also { bytesRead = it } != -1) {
                output.write(buffer, 0, bytesRead)
            }
        }
    }

    val imageBytes = outputStream.toByteArray()
    val skiaImage = SkiaImage.makeFromEncoded(imageBytes)

    return skiaImage.toComposeImageBitmap()
}

private fun ByteArraySize(size: Int) = ByteArray(size)