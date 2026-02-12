// app/src/main/java/com/example/movieapp/view/AddScreen.kt
package com.example.movieapp.view

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
import com.example.movieapp.controller.MainController
import com.example.movieapp.model.Movie

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddScreen(
    controller: MainController,
    onBack: () -> Unit,
    onOpenSearch: () -> Unit,
    selectedMovie: Movie? = null
) {
    var title by remember(selectedMovie) { mutableStateOf(selectedMovie?.title ?: "") }
    var year by remember(selectedMovie) { mutableStateOf(selectedMovie?.year ?: "") }
    var posterUrl by remember(selectedMovie) { mutableStateOf(selectedMovie?.posterUrl ?: "") }

    val isFormValid = title.isNotBlank()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(if (selectedMovie == null) "Добавить фильм" else "Редактировать фильм") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Назад")
                    }
                },
                actions = {
                    // Кнопка поиска для открытия SearchScreen
                    IconButton(onClick = onOpenSearch) {
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
            // Постер фильма
            Card(
                modifier = Modifier
                    .size(200.dp, 250.dp)
                    .padding(bottom = 24.dp),
                elevation = CardDefaults.cardElevation(4.dp),
                shape = RoundedCornerShape(8.dp)
            ) {
                if (posterUrl.isNotBlank() && posterUrl != "N/A") {
                    // Загружаем реальный постер
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
                    // Заглушка если нет постера
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = "🎬",
                                fontSize = 64.sp
                            )
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

            // Поле для названия фильма (обязательное)
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                label = { Text("Название фильма") },
                placeholder = { Text("Введите название") },
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

            // Поле для года выпуска (необязательное)
            OutlinedTextField(
                value = year,
                onValueChange = { year = it },
                label = { Text("Год выпуска") },
                placeholder = { Text("Например: 2024") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Кнопка добавления
            Button(
                onClick = {
                    if (isFormValid) {
                        val movieToAdd = selectedMovie?.copy(
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
                        controller.addMovie(movieToAdd)
                        onBack()
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

            Spacer(modifier = Modifier.height(8.dp))

            // Подсказка
            if (selectedMovie == null) {
                Text(
                    text = "Или нажмите на иконку поиска 🔍 чтобы найти фильм",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(top = 16.dp)
                )
            }
        }
    }
}