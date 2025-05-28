package dev.sandroisu.composetutorial

import android.content.res.Configuration
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import dev.sandroisu.composetutorial.ui.theme.ComposeTutorialTheme
import java.time.Clock
import java.util.Date
import java.util.TimeZone

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeTutorialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    LoginScreen()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(name = "Light Mode")
@Composable
fun LoginScreen() {
    var login by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var log by remember { mutableStateOf("") }
    var isFocused by remember { mutableStateOf(false) }
    var rememberMe by rememberSaveable { mutableStateOf(false) }
    val alphaAnimation = remember {
        Animatable(0f)
    }
    LaunchedEffect(Unit) {
        alphaAnimation.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1000, easing = LinearEasing)
        )
    }
    val context = LocalContext.current

    LaunchedEffect(log) {
        Log.e("COMPOSE_TUTORIAL", log)
    }

    Column(modifier = Modifier.padding(20.dp)) {

        Text(modifier = Modifier.graphicsLayer {
            alpha = alphaAnimation.value
        }, text = "This is login screen")

        TextField(
            modifier = Modifier.onFocusChanged {
                isFocused = it.hasFocus
            },
            value = login,
            onValueChange = { newValue -> login = newValue },
            placeholder = { Text(text = "e-mail") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email)
        )
        TextField(
            value = password,
            onValueChange = { newValue -> password = newValue },
            placeholder = { Text(text = "password") }
        )
        Button(
            colors = if (isFocused) {
                ButtonColors(
                    containerColor = Color.Red,
                    contentColor = Color.White,
                    disabledContainerColor = Color.Gray,
                    disabledContentColor = Color.Black,
                )
            } else {
                ButtonDefaults.buttonColors()
            },
            onClick = {
                if (login.contains("@")) {
                    Toast.makeText(context, "Works!", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(context, "“Invalid email!", Toast.LENGTH_SHORT).show()
                }
                log = "Login cliked ${Date().toInstant().atZone(TimeZone.getDefault().toZoneId())}"
            },
            enabled = login.isNotEmpty() && password.isNotEmpty()
        ) { Text(text = "Login") }

        Spacer(modifier = Modifier.width(8.dp))

        Row {
            Checkbox(checked = rememberMe, onCheckedChange = { rememberMe = it })
            Text(text = "Remember Me")
        }

    }
}


@Composable
fun MessageCard(message: Message) {
    Row(modifier = Modifier.padding(all = 8.dp)) {
        Image(
            painter = painterResource(R.drawable.profile_picture),
            contentDescription = "Nice lady",
            modifier = Modifier
                .size(40.dp)
                .clip(CircleShape)
                .border(1.5.dp, MaterialTheme.colorScheme.primary, CircleShape),
        )
        Spacer(modifier = Modifier.width(8.dp))

        Column {
            Text(
                text = message.author,
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall,
            )
            Spacer(modifier = Modifier.width(4.dp))
            Surface(shape = MaterialTheme.shapes.medium, shadowElevation = 1.dp) {
                Text(
                    text = message.body,
                    modifier = Modifier.padding(all = 4.dp),
                    style = MaterialTheme.typography.bodyMedium,
                )
            }
        }
    }
}

@Preview(name = "Light Mode")
@Preview(
    uiMode = Configuration.UI_MODE_NIGHT_YES,
    showBackground = true,
    name = "Dark Mode"
)
@Composable
fun PreviewMessageCard() {
    ComposeTutorialTheme {
        Surface {
            MessageCard(
                message = Message("Lexi", "Take a look at Jetpack Compose, it's great!")
            )
        }
    }
}

data class Message(
    val author: String,
    val body: String,
)