package com.sri.androidmentorchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.input.clearText
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sri.androidmentorchat.ui.theme.GeminiChatTheme
import dev.jeziellago.compose.markdowntext.MarkdownText
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val viewModel: MainViewModel by viewModels { MainViewModel.Factory }

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
                title = { Text(stringResource(R.string.app_name)) },
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
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        var chatHistoryList = viewModel.chatSession.collectAsStateWithLifecycle().value.messages
        val listState = rememberLazyListState()
        val messages = viewModel.messages.collectAsStateWithLifecycle()

        LaunchedEffect(chatHistoryList.size) {
            if (chatHistoryList.isNotEmpty()) {
                listState.animateScrollToItem(chatHistoryList.lastIndex)
            }
        }
        var agentType by remember { mutableStateOf<AgentType>(AgentType.ANDROID_MENTOR) }
        AgentSelector(viewModel = viewModel) { type ->
            agentType = type
        }
        if (agentType.agent.supportsDifficulty) {
            DifficultySelector(viewModel = viewModel)
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            if (agentType == AgentType.ANDROID_MENTOR && chatHistoryList.isEmpty()) {
                chatHistoryList = messages.value.map { message ->
                    ChatHistory(
                        senderType = SenderType.valueOf(message.sender),
                        message = message.message
                    )
                }
                listState.requestScrollToItem(
                    index = chatHistoryList.size,
                    scrollOffset = Int.MAX_VALUE
                )
                viewModel.updateChatSession(chatHistoryList)
            }
            items(chatHistoryList.size) { index ->
                val chatHistory = chatHistoryList[index]
                val isGemini = chatHistory.senderType == SenderType.GEMINI

                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalAlignment = if (isGemini) Alignment.End else Alignment.Start
                ) {
                    MarkdownText(
                        modifier = Modifier
                            .background(
                                color = if (isGemini) MaterialTheme.colorScheme.secondaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant,
                                shape = MaterialTheme.shapes.medium
                            )
                            .padding(12.dp),
                        markdown = chatHistory.message,
                        style = MaterialTheme.typography.bodyLarge.copy(
                            color = if (isGemini) MaterialTheme.colorScheme.onSecondaryContainer
                            else MaterialTheme.colorScheme.onSurfaceVariant
                        )
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
                state = promptState
            )
            IconButton(
                onClick = {
                    viewModel.sendMessage(promptState.text.toString())
                    promptState.clearText()
                }
            ) {
                Icon(
                    modifier = Modifier
                        .background(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer
                        )
                        .padding(8.dp),
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = "Send",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AgentSelector(viewModel: MainViewModel, onAgentSelected: (agentType: AgentType) -> Unit) {
    var isExpanded by remember { mutableStateOf(false) }
    val selectedAgentState by viewModel.selectedAgent.collectAsStateWithLifecycle()

    ExposedDropdownMenuBox(
        modifier = Modifier.padding(8.dp),
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = selectedAgentState.name,
            onValueChange = {},
            readOnly = true,
            label = { Text("Current Agent") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            AgentType.entries.forEach { agentType ->
                DropdownMenuItem(
                    text = { Text(agentType.agent.name) },
                    onClick = {
                        onAgentSelected(agentType)
                        viewModel.selectAgent(agentType)
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    enabled = selectedAgentState != agentType.agent
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultySelector(viewModel: MainViewModel) {
    var isExpanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        modifier = Modifier.padding(8.dp),
        expanded = isExpanded,
        onExpandedChange = { isExpanded = !isExpanded }
    ) {
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(type = ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            value = viewModel.difficultyLevel.collectAsStateWithLifecycle().value?.name ?: "",
            onValueChange = {},
            readOnly = true,
            label = { Text("Difficulty Level") },
            trailingIcon = {
                ExposedDropdownMenuDefaults.TrailingIcon(expanded = isExpanded)
            },
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )
        ExposedDropdownMenu(
            expanded = isExpanded,
            onDismissRequest = { isExpanded = false }
        ) {
            DifficultyLevel.entries.forEach { difficultyLevel ->
                DropdownMenuItem(
                    text = { Text(difficultyLevel.name) },
                    onClick = {
                        viewModel.selectDifficulty(difficultyLevel)
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview
@Composable
fun GreetingPreview() {
    GeminiChatTheme {
        ChatScreen(
            MainViewModel(
                AiRepository(),
                ChatRepository(object : MessageDao {
                    override suspend fun insertMessage(message: MessageEntity) {}

                    override fun getAllMessages(): Flow<List<MessageEntity>> {
                        return flowOf(emptyList())
                    }

                    override suspend fun deleteAllMessages() {}
                })
            )
        )
    }
}