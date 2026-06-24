package com.sri.androidmentorchat

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.sri.androidmentorchat.core.theme.AndroidMentorChatTheme
import com.sri.androidmentorchat.feature.chat.ui.AndroidMentorChatScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            AndroidMentorChatTheme {
                AndroidMentorChatScreen()
            }
        }
    }
}