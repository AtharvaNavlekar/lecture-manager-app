package com.example.lecturemanager.ui.requests

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.lecturemanager.util.Resource

// Design tokens
private val Primary = Color(0xFF6750A4)
private val Surface = Color(0xFFFEFEFB)
private val SurfaceVariant = Color(0xFFF3EDF7)
private val WarningAmber = Color(0xFFFFC107)
private val ErrorRed = Color(0xFFBA1A1A)
private val SuccessGreen = Color(0xFF4CAF50)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubstituteActionScreen(
    viewModel: SubstituteViewModel = hiltViewModel()
) {
    val activeRequest by viewModel.activeRequest.collectAsState()
    val remainingMs by viewModel.remainingTimeMs.collectAsState()
    val actionResult by viewModel.actionResult.collectAsState()

    val request = activeRequest

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Substitute Request") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Primary,
                    titleContentColor = Color.White
                )
            )
        }
    ) { paddingValues ->
        when {
            // Action completed
            actionResult is Resource.Success -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text("✅", fontSize = 48.sp)
                        Spacer(modifier = Modifier.height(16.dp))
                        Text(
                            text = (actionResult as Resource.Success).data ?: "Done",
                            style = MaterialTheme.typography.headlineSmall,
                            color = SuccessGreen
                        )
                    }
                }
            }

            // Action in progress
            actionResult is Resource.Loading -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = Primary)
                }
            }

            // No active request
            request == null -> {
                Box(
                    modifier = Modifier.fillMaxSize().padding(paddingValues),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No active substitute request.", style = MaterialTheme.typography.bodyLarge, color = Color.Gray)
                }
            }

            // Active request — render countdown + details
            else -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .background(Surface)
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Error banner
                    if (actionResult is Resource.Error) {
                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color(0xFFFFEBEE)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text(
                                text = "⚠️ ${(actionResult as Resource.Error).message}",
                                modifier = Modifier.padding(16.dp),
                                color = ErrorRed,
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }

                    // Countdown Timer Card
                    val isUrgent = remainingMs < 5 * 60 * 1000 // < 5 minutes
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isUrgent) Color(0xFFFFEBEE) else Color(0xFFFFF8E1)
                        ),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Timer,
                                contentDescription = "Timer",
                                tint = if (isUrgent) ErrorRed else WarningAmber,
                                modifier = Modifier.size(32.dp)
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            Column {
                                Text(
                                    text = if (isUrgent) "⚠ URGENT" else "Action Required",
                                    style = MaterialTheme.typography.labelLarge,
                                    color = ErrorRed
                                )
                                Text(
                                    text = viewModel.formatTime(remainingMs),
                                    style = MaterialTheme.typography.headlineLarge,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isUrgent) ErrorRed else Color.Black
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Requester info
                    Text(
                        text = request.requesterName,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Color.Black
                    )
                    Text(
                        text = "is requesting a substitute for:",
                        style = MaterialTheme.typography.bodyLarge,
                        color = Color.DarkGray
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    // Course details card
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = SurfaceVariant),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier.padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Text(
                                text = request.courseName,
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Primary,
                                textAlign = TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(request.timeSlot, style = MaterialTheme.typography.bodyLarge, color = Color.Black)
                            Text("${request.room} • ${request.date}", style = MaterialTheme.typography.bodyMedium, color = Color.DarkGray)
                        }
                    }

                    Spacer(modifier = Modifier.weight(1f))

                    // Accept / Decline buttons
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedButton(
                            onClick = { viewModel.declineRequest() },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.outlinedButtonColors(contentColor = ErrorRed),
                            shape = RoundedCornerShape(50),
                            enabled = remainingMs > 0
                        ) {
                            Text("Decline", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }

                        Button(
                            onClick = { viewModel.acceptRequest() },
                            modifier = Modifier.weight(1f).height(56.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SuccessGreen),
                            shape = RoundedCornerShape(50),
                            enabled = remainingMs > 0
                        ) {
                            Text("Accept", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}
