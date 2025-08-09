package com.example.translator

import android.media.SoundPool
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
    private lateinit var soundPool: SoundPool
    private val soundMap = mutableMapOf<String, Int>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        soundPool = SoundPool.Builder().setMaxStreams(4).build()

        soundMap["Happy"] = soundPool.load(this, R.raw.happy, 1)
        soundMap["Angry"] = soundPool.load(this, R.raw.angry, 1)
        soundMap["Calm"] = soundPool.load(this, R.raw.calm, 1)
        soundMap["Sad"] = soundPool.load(this, R.raw.sad, 1)

        enableEdgeToEdge()
        setContent {
            TranslatorTheme {
                MainScreen { feelingName ->
                    playButtonSound(feelingName)
                }
            }
        }
    }

    private fun playButtonSound(feeling: String) {
        soundMap[feeling]?.let { id ->
            soundPool.play(id, 1f, 1f, 0, 0, 1f)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        soundPool.release()
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(onPlaySound: (String) -> Unit) {
    val feelings = remember {
        mutableStateMapOf("Happy" to 0f, "Angry" to 0f, "Calm" to 0f, "Sad" to 0f)
    }
    val submittedEmojis = remember { mutableStateListOf<String>() }

    val feelingEmojis = mapOf(
        "Happy" to "😊",
        "Angry" to "😡",
        "Calm" to "😌",
        "Sad" to "😢"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFFA3B18A),
                    titleContentColor = Color.White
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            feelings.forEach { (feeling, value) ->
                FeelingButton(
                    feeling = feeling,
                    emoji = "",
                    progress = value,
                    onValueChange = { newValue -> feelings[feeling] = newValue },
                    onPlaySound = { onPlaySound(feeling) }
                )
            }

            Button(
                onClick = {
                    val comboEmoji = getOneEmoji(feelings)
                    submittedEmojis.add(comboEmoji)
                    feelings.keys.forEach { feelings[it] = 0f }
                },
                modifier = Modifier.fillMaxWidth().height(50.dp)
            ) {
                Text("➕")
            }

            EmojiList(submittedEmojis)
        }
    }
}

fun getOneEmoji(feelings: Map<String, Float>): String {
    val h = feelings["Happy"] ?: 0f
    val a = feelings["Angry"] ?: 0f
    val c = feelings["Calm"] ?: 0f
    val s = feelings["Sad"] ?: 0f

    val total = h + a + c + s
    if (total == 0f) return "🤔"

    val sorted = listOf(
        "Happy" to h,
        "Angry" to a,
        "Calm" to c,
        "Sad" to s
    ).sortedByDescending { it.second }

    val top1 = sorted[0]
    val top2 = sorted.getOrNull(1)

    if (top2 == null || top1.second - top2.second >= 1f) {
        return when (top1.first) {
            "Happy" -> "😊"
            "Angry" -> "😡"
            "Calm" -> "😌"
            "Sad" -> "😢"
            else -> "🤔"
        }
    }

    return when (setOf(top1.first, top2.first)) {
        setOf("Happy", "Calm") -> "😇"
        setOf("Happy", "Angry") -> "🤪"
        setOf("Happy", "Sad") -> "🥲"
        setOf("Angry", "Calm") -> "😤"
        setOf("Angry", "Sad") -> "😖"
        setOf("Calm", "Sad") -> "😔"
        setOf("Happy", "Sad") -> "😅"
        setOf("Happy", "Calm") -> "😻"
        setOf("Angry", "Happy") -> "😏"
        else -> "🤯"
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun FeelingButton(
    feeling: String,
    emoji: String,
    progress: Float,
    onValueChange: (Float) -> Unit,
    onPlaySound: () -> Unit,
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
                        onPlaySound()
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
            onClick = { },
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
            Text(text = emoji, color = Color.White, style = MaterialTheme.typography.headlineLarge)
        }
    }

    LinearProgressIndicator(
    progress = { progress.coerceAtMost(5f) / 5f },
    modifier = Modifier
                .fillMaxWidth()
                .height(8.dp)
                .padding(top = 4.dp),
    color = Color.White.copy(alpha = 0.8f),
    trackColor = Color.White.copy(alpha = 0.3f)
    )
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenLight() {
    MaterialTheme(colorScheme = lightColorScheme()) {
        MainScreen(onPlaySound = {})
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreenDark() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        MainScreen(onPlaySound = {})
    }
}