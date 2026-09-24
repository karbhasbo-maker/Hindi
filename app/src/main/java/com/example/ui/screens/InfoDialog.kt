package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.AmberGold
import com.example.ui.theme.SaffronPrimary

@Composable
fun InfoDialog(
    onDismiss: () -> Unit
) {
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            Button(
                onClick = onDismiss,
                colors = ButtonDefaults.buttonColors(containerColor = SaffronPrimary),
                shape = RoundedCornerShape(12.dp)
            ) {
                Text("Got It", fontWeight = FontWeight.Bold)
            }
        },
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(CircleShape)
                        .background(SaffronPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = "About",
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "Hindi Barahkhadi Guide",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "हिंदी बारहखड़ी - Devanagari Phonetics",
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
            ) {
                InfoSection(
                    title = "What is Barahkhadi? (बारहखड़ी)",
                    content = "\"Barah\" means twelve (12) and \"Khadi\" means column/formation. It refers to the systematic matrix of consonants combined with 12 distinct vowel sounds (मात्राएँ) in the Devanagari script."
                )

                Spacer(modifier = Modifier.height(10.dp))

                InfoSection(
                    title = "Mathematical Structure",
                    content = "36 Core Consonants (क to ज्ञ) × 12 Vowels (अ to अः) = 432 Syllabic combinations. Once you grasp these 12 vowel transformations, you can pronounce and read any word in the Hindi language!"
                )

                Spacer(modifier = Modifier.height(10.dp))

                InfoSection(
                    title = "English Transliteration",
                    content = "Each syllable card provides phonetic approximations (e.g. \"Ka\", \"Kaa\", \"Ki\", \"Kee\"). The app uses high-accuracy Android Hindi Speech Synthesis for authentic pronunciation."
                )

                Spacer(modifier = Modifier.height(10.dp))

                InfoSection(
                    title = "App Features",
                    content = "• Explorer: 36 Consonants with sequenced audio play.\n• Master Chart: Full 36×12 matrix.\n• Swar & Matra: In-depth vowel guide.\n• Practice & Quiz: 3D flip flashcards & speed test.\n• Writing Pad: Trace strokes on canvas with guide letters."
                )
            }
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = MaterialTheme.colorScheme.surface
    )
}

@Composable
fun InfoSection(
    title: String,
    content: String
) {
    Surface(
        shape = RoundedCornerShape(12.dp),
        color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            Text(
                text = title,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = SaffronPrimary
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = content,
                fontSize = 12.sp,
                lineHeight = 17.sp,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
