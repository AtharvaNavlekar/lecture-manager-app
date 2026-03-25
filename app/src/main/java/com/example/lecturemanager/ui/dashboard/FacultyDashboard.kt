package com.example.lecturemanager.ui.dashboard

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Warning
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

// Design DNA tokens (from DESIGN.md)
private val Primary = Color(0xFF6750A4)
private val Surface = Color(0xFFFEFEFB)
private val SurfaceVariant = Color(0xFFF3EDF7)
private val Warning = Color(0xFFFFC107)
private val ErrorRed = Color(0xFFBA1A1A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FacultyDashboardScreen(
    viewModel: DashboardViewModel = hiltViewModel(),
    facultyName: String = "Prof. Sarah"
) {
    val timetableResource by viewModel.timetableState.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("LecMan Mobile") },
                actions = {
                    IconButton(onClick = { viewModel.refreshData() }) {
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
        // --- Resource State Handling ---
        when (val state = timetableResource) {
            is Resource.Loading -> {
                if (state.data.isNullOrEmpty()) {
                    // Full-screen shimmer/loading
                    Box(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(color = Primary)
                    }
                } else {
                    // Show cached data with loading indicator
                    DashboardContent(
                        paddingValues = paddingValues,
                        facultyName = facultyName,
                        schedule = state.data,
                        isRefreshing = true
                    )
                }
            }
            is Resource.Success -> {
                DashboardContent(
                    paddingValues = paddingValues,
                    facultyName = facultyName,
                    schedule = state.data ?: emptyList(),
                    isRefreshing = false
                )
            }
            is Resource.Error -> {
                DashboardContent(
                    paddingValues = paddingValues,
                    facultyName = facultyName,
                    schedule = state.data ?: emptyList(),
                    isRefreshing = false,
                    errorMessage = state.message
                )
            }
        }
    }
}

@Composable
private fun DashboardContent(
    paddingValues: PaddingValues,
    facultyName: String,
    schedule: List<TimetableEntity>,
    isRefreshing: Boolean,
    errorMessage: String? = null
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .background(Surface)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        // Error Snackbar
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

        // Loading indicator
        if (isRefreshing) {
            item {
                LinearProgressIndicator(
                    modifier = Modifier.fillMaxWidth(),
                    color = Primary
                )
            }
        }

        // Header
        item {
            Column {
                Text(
                    text = "Good morning, $facultyName",
                    style = MaterialTheme.typography.headlineLarge,
                    color = Primary
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "You have ${schedule.size} lectures today.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.DarkGray
                )
            }
        }

        // Pending Action Items
        item {
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                Text(
                    text = "Pending Action Items",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = Primary
                )
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFFFFF8E1)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = "Urgent",
                            tint = Warning,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Column {
                            Text("Substitute Request", style = MaterialTheme.typography.titleMedium, color = ErrorRed)
                            Text("Dr. Aris • 4:00 PM Slot", style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                        }
                    }
                }
            }
        }

        // Today's Schedule (driven by ViewModel data)
        item {
            Text(
                text = "Today's Quick Schedule",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = Primary
            )
        }

        if (schedule.isEmpty()) {
            item {
                Text(
                    text = "No lectures scheduled for today.",
                    style = MaterialTheme.typography.bodyLarge,
                    color = Color.Gray,
                    modifier = Modifier.padding(vertical = 16.dp)
                )
            }
        } else {
            items(schedule) { entry ->
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = entry.subject,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${entry.startTime} - ${entry.endTime} • ${entry.roomNumber}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.DarkGray
                        )
                        Text(
                            text = entry.teacherName,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.Gray
                        )
                    }
                }
            }
        }
    }
}
