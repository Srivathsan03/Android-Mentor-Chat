# Android Mentor Chat

Android Mentor Chat is an Android application that demonstrates how to integrate Gemini AI to create an Android-focused learning and mentoring experience.

## Features

- **Real-time Chat**: Interactive chat interface with user and AI messages.
- **Android Mentor Agent**: Uses custom instructions to provide Android-focused guidance, learning support, and interview preparation.
- **Gemini AI Integration**: Uses Google's Gemini models to provide AI-powered responses.
- **Streaming AI Responses**: Responses are rendered in real-time as chunks are received from Gemini.
- **Markdown Support**: Renders AI responses with full Markdown formatting for better readability.
- **Modern Android Architecture**: Built using MVVM architecture, Kotlin Coroutines, and StateFlow.
- **Jetpack Compose UI**: A fully declarative and reactive UI.

## Architecture

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

## Tech Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Concurrency**: Kotlin Coroutines & Flow
- **AI SDK**: Google Generative AI SDK (`com.google.genai:google-genai`)
- **Markdown Rendering**: `compose-markdown` library
- **Dependency Management**: Gradle Version Catalog (libs.versions.toml)

## Setup

1. **Get a Gemini API Key**: Obtain an API key from [Google AI Studio](https://aistudio.google.com/).
2. **Configure API Key**: Add your API key to a `local.properties` file in the root directory:
   ```properties
   GEMINI_API_KEY=your_api_key_here
   ```
3. **Build and Run**: Open the project in Android Studio and run the app on an emulator or physical device.

## Project Structure

- `MainActivity`: The main entry point and UI container.
- `MainViewModel`: Manages UI state and business logic.
- `MainRepository`: Handles communication with the Gemini AI service.
- `ChatSession`: Maintains conversation history and session context.
- `ChatHistory`: Represents individual chat messages.
- `ChatRunner`: Orchestrates the chat lifecycle and LLM interactions.

## AI Concepts Explored

- Agent Instructions
- Context Management
- Session Concepts
- Streaming LLM Responses
- Agent Lifecycle

## Future Enhancements

- Memory Support
- Tool Calling
- Multiple AI Personas
- Multi-Session Conversations
- On-device LLM Integration

## License

This project is for educational purposes.
