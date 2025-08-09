package com.example.translator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.translator.ui.theme.TranslatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            TranslatorTheme {
                MainScreen()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen() {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("ColorFeel") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFA3B18A),
                    titleContentColor = Color.White
                )
            )
        },
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            ColorProgressBar("Happiness", Color(0xFFFBBF24), 0.7f) // Yellow
            ColorProgressBar("Calm", Color(0xFF3B82F6), 0.5f)       // Blue
            ColorProgressBar("Passion", Color(0xFFEF4444), 0.3f)    // Red
            ColorProgressBar("Balance", Color(0xFF10B981), 0.8f)    // Green

            EmojiList(listOf("😊", "😌", "❤️", "🌱"))
        }
    }
}

@Composable
fun ColorProgressBar(label: String, color: Color, progress: Float) {
    Column {
        Text(text = label, style = MaterialTheme.typography.bodyMedium)
        Modifier
            .fillMaxWidth()
            .height(12.dp)
            .clip(RoundedCornerShape(6.dp))
        color.copy(alpha = 0.2f)
        LinearProgressIndicator(
        progress = { progress },
        color = color
        )
    }
}

@Composable
fun EmojiList(emojis: List<String>) {
    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        emojis.forEach { emoji ->
            Text(text = emoji, style = MaterialTheme.typography.headlineMedium)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenLight() {
    TranslatorTheme(darkTheme = false) {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenDark() {
    TranslatorTheme(darkTheme = true) {
        MainScreen()
    }
}