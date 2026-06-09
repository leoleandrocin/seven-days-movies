package org.example.project

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import org.example.project.model.Movie
import java.io.ByteArrayOutputStream
import java.net.HttpURLConnection
import java.net.URL
import org.jetbrains.skia.Image as SkiaImage

fun main() = application {
    val windowState = rememberWindowState(width = 400.dp, height = 500.dp)

    val movies = listOf(
        Movie(
            "Avengers - EndGame",
            "https://upload.wikimedia.org/wikipedia/pt/9/9b/Avengers_Endgame.jpg",
            "8.4",
            "2019"
        ),
        Movie(
            "Oppenheimer",
            "https://m.media-amazon.com/images/M/MV5BN2JkMDc5MGQtZjg3YS00NmFiLWIyZmQtZTJmNTM5MjVmYTQ4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg",
            "8.2",
            "2023"
        ),
        Movie(
            "The Drama",
            "https://m.media-amazon.com/images/M/MV5BMTcwOGZhYmItYTg3ZS00NjUwLWJiMmQtMjU5YjEwYWY0NmNkXkEyXkFqcGc@._V1_QL75_UX190_CR0,8,190,281_.jpg",
            "7.4",
            "2026"
        )
    )

    Window(
        onCloseRequest = ::exitApplication,
        state = windowState,
        title = "Seven days - Movies"
    ) {
        App(movies)
    }
}

@Composable
fun App(movies: List<Movie>) {
    load(movies)
}

@Composable
private fun load(movies: List<Movie>) {
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
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                items(movies) { movie ->
                    drawMovieItem(movie = movie)
                }
            }
        }
    }
}

@Composable
private fun drawMovieItem(movie: Movie) {
    val (imageBitmap, isLoading, isError) = setUpImage(movie.image)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MaterialTheme.colorScheme.surface)
            .padding(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(width = 100.dp, height = 140.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF2C2C2C)),
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
                        contentDescription = "Logo ${movie.title}",
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.size(250.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.Center
        ) {
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
                        contentDescription = "Rate trophy",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = movie.rate,
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
                        text = movie.year,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = MaterialTheme.colorScheme.secondary
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = movie.title,
                fontSize = 26.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color.White,
                style = MaterialTheme.typography.headlineMedium
            )
        }
    }
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

private fun String.loadAsComposeBitmap(): ImageBitmap {
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