package com.example.translator

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.text.style.TextAlign
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
    val feelings = remember {
        mutableStateMapOf(
            "Happy" to 0f,
            "Angry" to 0f,
            "Calm" to 0f,
            "Sad" to 0f,
        )
    }

    val submittedEmojis = remember { mutableStateListOf<String>() }

    val feelingEmojis = mapOf(
        "Happy" to "😊",
        "Angry" to "😠",
        "Calm" to "😌",
        "Sad" to "😢"
    )

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
            feelings.forEach { (feeling, value) ->
                FeelingButton(
                    feeling = feeling,
                    emoji = feelingEmojis[feeling] ?: "",
                    progress = value,
                    onValueChange = { newValue ->
                        feelings[feeling] = newValue
                    }
                )
            }

            Button(
                onClick = {
                    val comboEmoji = getComboEmoji(feelings)
                    submittedEmojis.add(comboEmoji)

                    feelings.keys.forEach { feelings[it] = 0f }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
            ) {
                Text("Enter")
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Show last 3-4 emojis horizontally, with horizontal scroll if more
            val toShow = if (submittedEmojis.size > 4) submittedEmojis.takeLast(4) else submittedEmojis
            EmojiList(toShow)
        }
    }
}

// Generate an emoji string based on combination of feelings values
fun getComboEmoji(feelings: Map<String, Float>): String {
    val happy = feelings["Happy"] ?: 0f
    val angry = feelings["Angry"] ?: 0f
    val calm = feelings["Calm"] ?: 0f
    val sad = feelings["Sad"] ?: 0f

    return when {
        happy >= 3f && calm >= 3f -> "😊😌"
        angry >= 3f && sad >= 3f -> "😠😢"
        happy >= 3f && angry >= 3f -> "😠😊"
        calm >= 3f && sad >= 3f -> "😌😢"
        happy >= 3f -> "😊"
        angry >= 3f -> "😠"
        calm >= 3f -> "😌"
        sad >= 3f -> "😢"
        else -> "🤔"
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FeelingButton(
    feeling: String,
    emoji: String,
    progress: Float,
    onValueChange: (Float) -> Unit,
) {
    var isPressed by remember { mutableStateOf(false) }
    var startTime by remember { mutableStateOf(0L) }

    LaunchedEffect(isPressed) {
        if (isPressed) {
            startTime = SystemClock.elapsedRealtime()
            while (isPressed) {
                val elapsed = (SystemClock.elapsedRealtime() - startTime).coerceAtMost(5000L)
                val newProgress = (elapsed / 1000f).coerceAtMost(5f)
                onValueChange(newProgress)
                kotlinx.coroutines.delay(50L)
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp)
            .pointerInteropFilter {
                when (it.action) {
                    android.view.MotionEvent.ACTION_DOWN -> {
                        isPressed = true
                        true
                    }
                    android.view.MotionEvent.ACTION_UP,
                    android.view.MotionEvent.ACTION_CANCEL -> {
                        isPressed = false
                        true
                    }
                    else -> false
                }
            }
    ) {
        Button(
            onClick = { /* no-op */ },
            modifier = Modifier.fillMaxSize(),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = when (feeling) {
                    "Happy" -> Color(0xFFFBBF24)
                    "Angry" -> Color(0xFFEF4444)
                    "Calm" -> Color(0xFF3B82F6)
                    "Sad" -> Color(0xFF6B7280)
                    else -> Color.Gray
                }
            )
        ) {
            Text("$emoji  $feeling: ${"%.1f".format(progress.coerceAtMost(5f))}", color = Color.White)
        }
    }

    LinearProgressIndicator(
    progress = { progress.coerceAtMost(5f) / 5f },
    modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(top = 4.dp),
    color = Color.White.copy(alpha = 0.8f),
    trackColor = Color.White.copy(alpha = 0.3f),
    )
}

@Composable
fun EmojiList(emojis: List<String>) {
    val scrollState = rememberScrollState()

    Row(
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .horizontalScroll(scrollState),
        verticalAlignment = Alignment.CenterVertically
    ) {
        emojis.forEach { emoji ->
            Text(
                text = emoji,
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(64.dp)
            )
        }
    }
}

// Previews

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        MainScreen()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        MainScreen()
    }
}