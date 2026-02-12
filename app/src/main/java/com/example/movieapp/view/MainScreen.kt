// app/src/main/java/com/example/movieapp/view/MainScreen.kt
package com.example.movieapp.view

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.movieapp.controller.MainController
import com.example.movieapp.model.Movie
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(
    controller: MainController,
    onAddClick: () -> Unit
) {
    val movies by controller.movies.collectAsState()
    val selectedCount by controller.selectedCount.collectAsState()

    var showDeleteConfirmation by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Мои фильмы") },
                actions = {
                    // Кнопка удаления отображается всегда в основном меню
                    // Но активна только когда есть выбранные элементы
                    IconButton(
                        onClick = {
                            if (selectedCount > 0) {
                                showDeleteConfirmation = true
                            }
                        },
                        enabled = selectedCount > 0
                    ) {
                        BadgedBox(
                            badge = {
                                if (selectedCount > 0) {
                                    Badge(
                                        containerColor = MaterialTheme.colorScheme.error
                                    ) {
                                        Text(selectedCount.toString())
                                    }
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Удалить выбранные",
                                tint = if (selectedCount > 0)
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
            FloatingActionButton(onClick = onAddClick) {
                Icon(Icons.Default.Add, contentDescription = "Добавить фильм")
            }
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (movies.isEmpty()) {
                // Пустое состояние
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
            } else {
                // Список фильмов
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(
                        start = 16.dp,
                        top = 16.dp,
                        end = 16.dp,
                        bottom = 80.dp // Отступ для FAB
                    ),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(
                        items = movies,
                        key = { movie -> movie.id }
                    ) { movie ->
                        MovieItem(
                            movie = movie,
                            onSelectionChange = { isSelected ->
                                controller.toggleMovieSelection(movie)
                            }
                        )
                    }
                }
            }
        }
    }

    // Диалог подтверждения удаления
    if (showDeleteConfirmation) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmation = false },
            title = { Text("Подтверждение удаления") },
            text = {
                Text("Вы действительно хотите удалить $selectedCount выбранных фильмов?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        controller.deleteSelectedMovies()
                        showDeleteConfirmation = false
                    }
                ) {
                    Text("Удалить", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDeleteConfirmation = false }) {
                    Text("Отмена")
                }
            }
        )
    }
}

@Composable
fun MovieItem(
    movie: Movie,
    onSelectionChange: (Boolean) -> Unit
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
            defaultElevation = if (movie.isSelected) 4.dp else 1.dp
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox (галочка) для выбора фильма
            Checkbox(
                checked = movie.isSelected,
                onCheckedChange = onSelectionChange,
                modifier = Modifier.padding(end = 8.dp),
                colors = CheckboxDefaults.colors(
                    checkedColor = MaterialTheme.colorScheme.primary
                )
            )

            // Постер (заглушка)
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .padding(end = 12.dp)
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = MaterialTheme.shapes.small,
                    color = MaterialTheme.colorScheme.secondaryContainer,
                    tonalElevation = 2.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "🎬",
                            fontSize = 32.sp
                        )
                    }
                }
            }

            // Информация о фильме
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(end = 8.dp)
            ) {
                Text(
                    text = movie.title,
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface,
                    maxLines = 2
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = movie.year,
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Индикатор выбора (дополнительная визуализация)
            if (movie.isSelected) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = "Выбран",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}