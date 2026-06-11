package com.sri.geminichat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonColors
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sri.geminichat.ui.theme.GeminiChatTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel = ViewModelProvider(this)[MainViewModel::class.java]

        setContent {
            GeminiChatTheme {
                ChatScreen(viewModel)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChatScreen(viewModel: MainViewModel) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text("Gemini Chat") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        }) { innerPadding ->
        ChatScreen(
            modifier = Modifier.padding(innerPadding),
            viewModel = viewModel
        )
    }
}

@Composable
fun ChatScreen(
    modifier: Modifier = Modifier,
    viewModel: MainViewModel
) {
    Column(
        modifier = modifier.fillMaxSize()
    ) {
        val chatHistoryList = viewModel.chatHistory.collectAsStateWithLifecycle()
        LazyColumn(
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            items(chatHistoryList.value.size) { index ->
                val chatHistory = chatHistoryList.value[index]
                val isGemini = chatHistory.sender == Sender.GEMINI

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = if (isGemini) Alignment.End else Alignment.Start
                ) {
                    Text(
                        modifier = Modifier
                            .background(
                                color = if (isGemini) MaterialTheme.colorScheme.secondaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(12.dp),
                        text = chatHistory.message,
                        color = if (isGemini) MaterialTheme.colorScheme.onSecondaryContainer
                        else MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            val promptState = rememberTextFieldState()
            TextField(
                modifier = Modifier
                    .padding(8.dp)
                    .weight(1f),
                state = promptState,
                enabled = viewModel.isEnabled.collectAsStateWithLifecycle().value
            )
            IconButton(
                onClick = {
                    viewModel.sendMessage(promptState.text.toString())
                    promptState.clearText()
                },
                enabled = viewModel.isEnabled.collectAsStateWithLifecycle().value
            ) {
                Icon(
                    modifier = Modifier
                        .background(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.onPrimaryContainer
                        )
                        .padding(8.dp),
                    imageVector = Icons.Default.Send,
                    contentDescription = "Send",
                    tint = Color.White
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    GeminiChatTheme {
        ChatScreen(MainViewModel())
    }
}