package com.sri.androidmentorchat

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
import androidx.compose.material.icons.filled.Delete
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.sri.androidmentorchat.ui.theme.AndroidMentorChatTheme
import dev.jeziellago.compose.markdowntext.MarkdownText

@Composable
fun AndroidMentorChatScreen(
    viewModel: MainViewModel = viewModel(factory = MainViewModel.Factory)
) {
    val chatSession by viewModel.chatSession.collectAsStateWithLifecycle()
    val isChatClearable by viewModel.isChatClearable.collectAsStateWithLifecycle()
    val messages by viewModel.messages.collectAsStateWithLifecycle()
    val selectedAgent by viewModel.selectedAgent.collectAsStateWithLifecycle()
    val difficultyLevel by viewModel.difficultyLevel.collectAsStateWithLifecycle()

    AndroidMentorChatScreenUi(
        chatSession = chatSession,
        isChatClearable = isChatClearable,
        messages = messages,
        selectedAgent = selectedAgent,
        difficultyLevel = difficultyLevel,
        onClearChat = viewModel::clearChatSession,
        onSendMessage = viewModel::sendMessage,
        onSelectAgent = viewModel::selectAgent,
        onSelectDifficulty = viewModel::selectDifficulty,
        onUpdateChatSession = viewModel::updateChatSession
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AndroidMentorChatScreenUi(
    chatSession: ChatSession,
    isChatClearable: Boolean,
    messages: List<MessageEntity>,
    selectedAgent: Agent,
    difficultyLevel: DifficultyLevel?,
    onClearChat: () -> Unit,
    onSendMessage: (String) -> Unit,
    onSelectAgent: (AgentType) -> Unit,
    onSelectDifficulty: (DifficultyLevel) -> Unit,
    onUpdateChatSession: (List<ChatHistory>) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier.fillMaxSize(),
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.app_name)) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                ),
                actions = {
                    IconButton(onClick = onClearChat) {
                        if (isChatClearable) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Clear Chat",
                                tint = Color.Red
                            )
                        }
                    }
                }
            )
        }) { innerPadding ->
        AndroidMentorChatScreenContent(
            modifier = Modifier.padding(innerPadding),
            chatSession = chatSession,
            messages = messages,
            selectedAgent = selectedAgent,
            difficultyLevel = difficultyLevel,
            onSendMessage = onSendMessage,
            onSelectAgent = onSelectAgent,
            onSelectDifficulty = onSelectDifficulty,
            onUpdateChatSession = onUpdateChatSession
        )
    }
}

@Composable
fun AndroidMentorChatScreenContent(
    modifier: Modifier = Modifier,
    chatSession: ChatSession,
    messages: List<MessageEntity>,
    selectedAgent: Agent,
    difficultyLevel: DifficultyLevel?,
    onSendMessage: (String) -> Unit,
    onSelectAgent: (AgentType) -> Unit,
    onSelectDifficulty: (DifficultyLevel) -> Unit,
    onUpdateChatSession: (List<ChatHistory>) -> Unit
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        var chatHistoryList = chatSession.messages
        val listState = rememberLazyListState()

        LaunchedEffect(chatHistoryList.size) {
            if (chatHistoryList.isNotEmpty()) {
                listState.animateScrollToItem(chatHistoryList.lastIndex)
            }
        }
        var agentType by remember { mutableStateOf<AgentType>(AgentType.ANDROID_MENTOR) }
        AgentSelector(
            selectedAgent = selectedAgent,
            onAgentSelected = { type ->
                agentType = type
                onSelectAgent(type)
            }
        )
        if (selectedAgent.supportsDifficulty) {
            DifficultySelector(
                difficultyLevel = difficultyLevel,
                onSelectDifficulty = onSelectDifficulty
            )
        }
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxSize()
        ) {
            if (agentType == AgentType.ANDROID_MENTOR && chatHistoryList.isEmpty()) {
                chatHistoryList = messages.map { message ->
                    ChatHistory(
                        senderType = SenderType.valueOf(message.sender),
                        message = message.message
                    )
                }
                listState.requestScrollToItem(
                    index = chatHistoryList.size,
                    scrollOffset = Int.MAX_VALUE
                )
                onUpdateChatSession(chatHistoryList)
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
                    onSendMessage(promptState.text.toString())
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
fun AgentSelector(
    selectedAgent: Agent,
    onAgentSelected: (agentType: AgentType) -> Unit
) {
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
            value = selectedAgent.name,
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
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                    enabled = selectedAgent != agentType.agent
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DifficultySelector(
    difficultyLevel: DifficultyLevel?,
    onSelectDifficulty: (DifficultyLevel) -> Unit
) {
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
            value = difficultyLevel?.name ?: "",
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
            DifficultyLevel.entries.forEach { level ->
                DropdownMenuItem(
                    text = { Text(level.name) },
                    onClick = {
                        onSelectDifficulty(level)
                        isExpanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun Preview_Chat() {
    AndroidMentorChatTheme {
        AndroidMentorChatScreenUi(
            chatSession = ChatSession("preview", listOf(
                ChatHistory(SenderType.USER, "Hello"),
                ChatHistory(SenderType.GEMINI, "Hi! I am your Android Mentor. How can I help you today?")
            )),
            isChatClearable = true,
            messages = emptyList(),
            selectedAgent = AgentType.ANDROID_MENTOR.agent,
            difficultyLevel = DifficultyLevel.BEGINNER,
            onClearChat = {},
            onSendMessage = {},
            onSelectAgent = {},
            onSelectDifficulty = {},
            onUpdateChatSession = {}
        )
    }
}
