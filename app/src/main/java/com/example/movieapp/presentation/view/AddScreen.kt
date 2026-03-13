package com.example.movieapp.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.text.font.FontWeight
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import coil.size.Scale
import com.example.movieapp.domain.model.Movie
import com.example.movieapp.presentation.model.MainIntent
import com.example.movieapp.presentation.model.MainState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    state: MainState,
    onIntent: (MainIntent) -> Unit
) {
    val selectedMovie = state.selectedMovieForEdit

    var title by remember(selectedMovie) { mutableStateOf(selectedMovie?.title ?: "") }
    var year by remember(selectedMovie) { mutableStateOf(selectedMovie?.year ?: "") }
    var posterUrl by remember(selectedMovie) { mutableStateOf(selectedMovie?.posterUrl ?: "") }

    val isFormValid = title.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        if (selectedMovie == null) "Добавить фильм"
                        else "Редактировать фильм"
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { onIntent(MainIntent.NavigateBack) }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    IconButton(onClick = { onIntent(MainIntent.NavigateToSearch) }) {
                        Icon(Icons.Default.Search, contentDescription = "Поиск фильмов")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            MoviePosterLarge(posterUrl = posterUrl, title = title)

            Spacer(modifier = Modifier.height(24.dp))

            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название фильма") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true,
                isError = title.isBlank(),
                supportingText = {
                    if (title.isBlank()) {
                        Text("Обязательное поле", color = MaterialTheme.colorScheme.error)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Год выпуска") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (isFormValid) {
                        val movieToAdd = createMovie(
                            selectedMovie = selectedMovie,
                            title = title,
                            year = year,
                            posterUrl = posterUrl
                        )
                        onIntent(MainIntent.AddMovie(movieToAdd))
                    }
                },
                enabled = isFormValid,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
            ) {
                Text(
                    text = if (selectedMovie == null) "ДОБАВИТЬ ФИЛЬМ" else "СОХРАНИТЬ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            if (selectedMovie == null) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Или нажмите на иконку поиска 🔍 чтобы найти фильм",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun MoviePosterLarge(
    posterUrl: String,
    title: String
) {
    Card(
        modifier = Modifier
            .size(200.dp, 250.dp)
            .padding(bottom = 24.dp),
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        if (posterUrl.isNotBlank() && posterUrl != "N/A") {
            Image(
                painter = rememberAsyncImagePainter(
                    ImageRequest.Builder(LocalContext.current)
                        .data(posterUrl)
                        .crossfade(true)
                        .scale(Scale.FILL)
                        .build()
                ),
                contentDescription = "Постер фильма",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(text = "🎬", fontSize = 64.sp)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Нет постера",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun createMovie(
    selectedMovie: Movie?,
    title: String,
    year: String,
    posterUrl: String
): Movie {
    return selectedMovie?.copy(
        title = title,
        year = year,
        posterUrl = posterUrl
    ) ?: Movie(
        title = title,
        year = year,
        posterUrl = posterUrl,
        imdbID = "",
        isSelected = false
    )
}