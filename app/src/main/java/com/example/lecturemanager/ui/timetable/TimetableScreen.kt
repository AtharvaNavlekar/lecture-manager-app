package com.example.lecturemanager.ui.timetable

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lecturemanager.data.local.entity.TimetableEntity
import com.example.lecturemanager.util.Resource

// Design DNA tokens (consistent with FacultyDashboard)
private val Primary = Color(0xFF6750A4)
private val Surface = Color(0xFFFEFEFB)
private val SurfaceVariant = Color(0xFFF3EDF7)
private val ErrorRed = Color(0xFFBA1A1A)

private val DayLabels = listOf("Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat")

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimetableScreen(
    viewModel: TimetableViewModel = hiltViewModel()
) {
    val timetableResource by viewModel.timetableState.collectAsState()
    val selectedDay by viewModel.selectedDay.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Weekly Timetable") },
                actions = {
                    IconButton(onClick = { viewModel.refreshFromNetwork() }) {
                        Icon(Icons.Default.Refresh, contentDescription = "Refresh", tint = Color.White)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(Surface)
        ) {
            // Day selector chips
            DaySelector(
                selectedDay = selectedDay,
                onDaySelected = { viewModel.selectDay(it) }
            )

            // Timetable content driven by Resource state
            when (val state = timetableResource) {
                is Resource.Loading -> {
                    if (state.data.isNullOrEmpty()) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = Primary)
                        }
                    } else {
                        LectureList(schedule = state.data, isRefreshing = true)
                    }
                }

                is Resource.Success -> {
                    LectureList(
                        schedule = state.data ?: emptyList(),
                        isRefreshing = false
                    )
                }

                is Resource.Error -> {
                    LectureList(
                        schedule = state.data ?: emptyList(),
                        isRefreshing = false,
                        errorMessage = state.message
                    )
                }
            }
        }
    }
}

@Composable
private fun DaySelector(
    selectedDay: Int,
    onDaySelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        // Calendar.SUNDAY == 1, SATURDAY == 7
        DayLabels.forEachIndexed { index, label ->
            val dayValue = index + 1 // 1-based to match Calendar constants
            val isSelected = dayValue == selectedDay

            val containerColor by animateColorAsState(
                targetValue = if (isSelected) Primary else SurfaceVariant,
                label = "chipColor"
            )
            val contentColor = if (isSelected) Color.White else Color.DarkGray

            FilterChip(
                selected = isSelected,
                onClick = { onDaySelected(dayValue) },
                label = {
                    Text(
                        text = label,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                    )
                },
                colors = FilterChipDefaults.filterChipColors(
                    selectedContainerColor = containerColor,
                    selectedLabelColor = contentColor,
                    containerColor = containerColor,
                    labelColor = contentColor
                ),
                modifier = Modifier.padding(horizontal = 2.dp)
            )
        }
    }
}

@Composable
private fun LectureList(
    schedule: List<TimetableEntity>,
    isRefreshing: Boolean,
    errorMessage: String? = null
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
        contentPadding = PaddingValues(vertical = 8.dp)
    ) {
        // Error banner
        if (errorMessage != null) {
            item {
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE))
                ) {
                    Text(
                        text = "⚠️ $errorMessage",
                        modifier = Modifier.padding(16.dp),
                        color = ErrorRed,
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }

        // Refreshing indicator
        if (isRefreshing) {
            item {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Primary
                )
            }
        }

        if (schedule.isEmpty() && !isRefreshing) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 48.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "No lectures scheduled for this day.",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.Gray
                    )
                }
            }
        } else {
            items(schedule) { entry ->
                LectureCard(entry)
            }
        }
    }
}

@Composable
private fun LectureCard(entry: TimetableEntity) {
    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Time column
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = entry.startTime,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Text(
                    text = entry.endTime,
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Divider line
            Box(
                modifier = Modifier
                    .width(2.dp)
                    .height(40.dp)
                    .background(Primary.copy(alpha = 0.3f))
            )

            Spacer(modifier = Modifier.width(16.dp))

            // Details column
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = entry.subject,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "${entry.teacherName} • ${entry.roomNumber}",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.DarkGray
                )
            }
        }
    }
}
