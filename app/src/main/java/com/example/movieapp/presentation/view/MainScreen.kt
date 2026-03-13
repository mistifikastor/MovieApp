package com.example.movieapp.presentation.view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun MainScreen(
    state: MainState,
    onIntent: (MainIntent) -> Unit
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои фильмы") },
                actions = {
                    IconButton(
                        onClick = {
                            if (state.selectedCount > 0) {
                                onIntent(MainIntent.ShowDeleteDialog)
                            }
                        },
                        enabled = state.selectedCount > 0
                    ) {
                        BadgedBox(
                            badge = {
                                if (state.selectedCount > 0) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.error
                                    ) {
                                        Text(state.selectedCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Удалить выбранные",
                                tint = if (state.selectedCount > 0)
                                    MaterialTheme.colorScheme.error
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.3f)
                            )
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { onIntent(MainIntent.NavigateToAdd) }
            ) {
                Icon(Icons.Default.Add, contentDescription = "Добавить фильм")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (state.movies.isEmpty()) {
                EmptyMoviesContent()
            } else {
                MoviesList(
                    movies = state.movies,
                    onMovieToggle = { movie ->
                        onIntent(MainIntent.ToggleMovieSelection(movie))
                    }
                )
            }
        }
    }

    if (state.isDeleteDialogVisible) {
        DeleteConfirmationDialog(
            selectedCount = state.selectedCount,
            onConfirm = { onIntent(MainIntent.ConfirmDelete) },
            onDismiss = { onIntent(MainIntent.DismissDeleteDialog) }
        )
    }
}

@Composable
fun EmptyMoviesContent() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "🎬",
            fontSize = 80.sp
        )
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "У вас нет выбранных фильмов",
            fontSize = 20.sp,
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "Нажмите кнопку + чтобы добавить фильмы",
            fontSize = 16.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun MoviesList(
    movies: List<Movie>,
    onMovieToggle: (Movie) -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(
            start = 16.dp,
            top = 16.dp,
            end = 16.dp,
            bottom = 80.dp
        ),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(
            items = movies,
            key = { movie -> movie.id }
        ) { movie ->
            MovieItem(
                movie = movie,
                onSelectionChange = { onMovieToggle(movie) }
            )
        }
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onSelectionChange: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (movie.isSelected)
                MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f)
            else
                MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = if (movie.isSelected) 4.dp else 2.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = movie.isSelected,
                onCheckedChange = { onSelectionChange() },
                modifier = Modifier.padding(end = 8.dp)
            )

            MoviePoster(posterUrl = movie.posterUrl, title = movie.title)

            // ИСПРАВЛЕНО: Убрали .weight(1f) и передаем модификатор правильно
            MovieInfo(
                movie = movie,
                modifier = Modifier.weight(1f)  // Теперь правильно используем weight
            )

            if (movie.isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Выбран",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(24.dp)
                )
            }
        }
    }
}

@Composable
fun MoviePoster(posterUrl: String, title: String) {
    Box(
        modifier = Modifier
            .size(70.dp, 100.dp)
            .padding(end = 12.dp)
            .clip(MaterialTheme.shapes.small)
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
                contentDescription = "Постер $title",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        } else {
            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(text = "🎬", fontSize = 24.sp)
                        Text(text = "Нет постера", fontSize = 10.sp)
                    }
                }
            }
        }
    }
}

// ИСПРАВЛЕНО: Добавили параметр modifier
@Composable
fun MovieInfo(
    movie: Movie,
    modifier: Modifier = Modifier  // Добавляем параметр modifier
) {
    Column(
        modifier = modifier  // Используем переданный modifier
            .padding(end = 8.dp)
    ) {
        Text(
            text = movie.title,
            fontSize = 18.sp,
            fontWeight = FontWeight.Medium,
            maxLines = 2
        )

        Spacer(modifier = Modifier.height(4.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                Icons.Default.Info,
                contentDescription = null,
                modifier = Modifier.size(14.dp)
            )
            Spacer(modifier = Modifier.width(4.dp))
            Text(
                text = movie.year,
                fontSize = 14.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        movie.genre?.let { genre ->
            Spacer(modifier = Modifier.height(4.dp))
            Surface(
                shape = MaterialTheme.shapes.small,
                color = MaterialTheme.colorScheme.secondaryContainer
            ) {
                Text(
                    text = genre,
                    fontSize = 12.sp,
                    modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                )
            }
        }
    }
}

@Composable
fun DeleteConfirmationDialog(
    selectedCount: Int,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Подтверждение удаления") },
        text = { Text("Вы действительно хотите удалить $selectedCount выбранных фильмов?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Удалить", color = MaterialTheme.colorScheme.error)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Отмена")
            }
        }
    )
}