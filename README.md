# Android Mentor Chat 🤖

Android Mentor Chat is an Android application that demonstrates how to integrate Gemini AI to create an Android-focused learning and mentoring experience. Built with a focus on modern Android development practices, it features a fluid UI and real-time streaming responses.

## 🌟 Features

- **Real-time Chat**: Fluid interactive chat interface with distinct user and AI message bubbles.
- **Android Mentor Agent**: Uses custom instructions to provide Android-focused guidance, learning support, and interview preparation.
- **Gemini AI Integration**: Uses Google's Gemini models to provide AI-powered responses.
- **Streaming AI Responses**: Responses are rendered in real-time as chunks are received from Gemini.
- **Markdown Support**: Full Markdown rendering for AI responses (code blocks, bold text, lists, etc.) using `compose-markdown`.
- **Material 3 Design**: Uses the latest Material Design components and dynamic color system.

## 🏗 Architecture

```text
Compose UI
    ↓
MainViewModel
    ↓
ChatRunner
    ↓
MainRepository
    ↓
Gemini API
```

## Agent Flow

```text
User Message
    ↓
Android Mentor Instructions
    ↓
Chat Session Context
    ↓
ChatRunner
    ↓
Gemini API
    ↓
Streaming Response
    ↓
Compose UI
```

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Concurrency**: Kotlin Coroutines & Flow (StateFlow)
- **AI SDK**: [Google Generative AI SDK](https://github.com/google-gemini/generative-ai-android)
- **Serialization**: Kotlinx Serialization
- **Markdown**: `compose-markdown` library
- **Dependency Management**: Gradle Version Catalog (`libs.versions.toml`)

## 🚀 Setup & Installation

### Prerequisites
- Android Studio Ladybug (or newer)
- Android SDK 34+
- A Gemini API Key

### Configuration
1. **Get a Gemini API Key**: Obtain one for free from [Google AI Studio](https://aistudio.google.com/).
2. **Local Properties**: Add your key to your `local.properties` file in the root directory:
   ```properties
   GEMINI_API_KEY=your_api_key_here
   ```
3. **Build**: Sync Gradle and run the `:app` module.

## 📂 Project Structure

- `MainActivity`: The main entry point and UI container.
- `MainViewModel`: Manages UI state and business logic.
- `MainRepository`: Handles communication with the Gemini AI service.
- `ChatSession`: Maintains conversation history and session context.
- `ChatHistory`: Represents individual chat messages.
- `ChatRunner`: Orchestrates the chat lifecycle and LLM interactions.

## 🧠 AI Concepts Explored

- **Agent Instructions**: Defining specific personas and boundaries for the AI.
- **Context Management**: Passing historical messages to maintain conversation flow.
- **Session Lifecycle**: Managing unique chat IDs and message lists.
- **Streaming LLM Responses**: Handling partial updates for a more responsive UI.

## 🗺 Roadmap
- [ ] **Dependency Injection**: Implement Hilt for better decoupling.
- [ ] **Multi-Session Support**: Save and switch between different chat sessions.
- [ ] **Unit Testing**: Add tests for the ViewModel and Repository layers.
- [ ] **Offline Cache**: Use Room to store chat history locally.
- [ ] **Tool Calling**: Allow the agent to perform specific Android-related tasks.

## 📄 License
This project is for educational purposes.
