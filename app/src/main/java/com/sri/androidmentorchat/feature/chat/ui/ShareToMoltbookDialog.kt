package com.sri.androidmentorchat.feature.chat.ui

import androidx.compose.material3.AlertDialog
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.sri.androidmentorchat.core.theme.AndroidMentorChatTheme
import kotlinx.coroutines.launch

@Composable
fun ShareToMoltbookDialog(
    viewModel: AndroidMentorChatViewModel,
    snackbarHostState: SnackbarHostState
) {
    val showDialog by viewModel.shareDialogVisible.collectAsStateWithLifecycle()
    val scope = rememberCoroutineScope()

    ShareToMoltbookDialogContent(
        showDialog = showDialog,
        dismissDialog = viewModel::dismissShareDialog
    ) {
        viewModel.shareChatToMoltbook { message ->
            scope.launch {
                snackbarHostState.showSnackbar(message)
            }
        }
    }
}

@Composable
fun ShareToMoltbookDialogContent(
    showDialog: Boolean,
    dismissDialog: () -> Unit,
    shareToMoltbook: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = dismissDialog,
            title = {
                Text(text = "Share to Moltbook")
            },
            text = {
                Text(text = "Are you sure you want to share the summary to Moltbook?")
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        shareToMoltbook()
                        dismissDialog()
                    }
                ) {
                    Text("Share")
                }
            },
            dismissButton = {
                TextButton(
                    onClick = dismissDialog
                ) {
                    Text("Cancel")
                }
            }
        )
    }
}

@Preview
@Composable
private fun Preview_ShareDialog() {
    AndroidMentorChatTheme {
        ShareToMoltbookDialogContent(
            showDialog = true,
            dismissDialog = {},
            shareToMoltbook = {}
        )
    }
}
