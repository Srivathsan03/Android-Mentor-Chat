# Android Mentor Chat

Android Mentor Chat is an Android application that demonstrates how to integrate Gemini AI to create an Android-focused learning and mentoring experience. Built with a focus on modern Android development practices, it features a fluid UI and real-time streaming responses.

## Features

- **Real-time Chat**: Fluid interactive chat interface with distinct user and AI message bubbles.
- **Multiple AI Agents**:
  - Android Mentor Agent
  - Android Interviewer Agent
  - Kotlin Interviewer Agent
- **Agent Switching**: Dynamically switch between AI personas from the UI.
- **Gemini AI Integration**: Uses Google's Gemini models to provide AI-powered responses.
- **Streaming AI Responses**: Responses are rendered in real-time as chunks are received from Gemini.
- **Markdown Support**: Full Markdown rendering for AI responses (code blocks, bold text, lists, etc.) using `compose-markdown`.
- **Material 3 Design**: Uses the latest Material Design components and dynamic color system.

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

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Framework**: [Jetpack Compose](https://developer.android.com/jetpack/compose)
- **Architecture**: MVVM (Model-View-ViewModel)
- **Concurrency**: Kotlin Coroutines & Flow (StateFlow)
- **AI SDK**: [Google Generative AI SDK](https://github.com/google-gemini/generative-ai-android)
- **Serialization**: Kotlinx Serialization
- **Markdown**: `compose-markdown` library
- **Dependency Management**: Gradle Version Catalog (`libs.versions.toml`)

## Setup & Installation

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

## Project Structure

- `MainActivity`: The main entry point and UI container.
- `MainViewModel`: Manages UI state and business logic.
- `MainRepository`: Handles communication with the Gemini AI service.
- `ChatSession`: Maintains conversation history and session context.
- `ChatHistory`: Represents individual chat messages.
- `ChatRunner`: Orchestrates the chat lifecycle and LLM interactions.
- `Agent`: Defines agent personality and instructions.
- `AgentType`: Manages available AI agents.

## AI Concepts Explored

- **Agent Instructions**
   - Using system instructions to define the behavior and responsibilities of an AI agent.

- **Agent Personalities**
   - Implementing Android Mentor, Android Interviewer, and Kotlin Interviewer agents with distinct behaviors.

- **Context Management**
   - Building conversation context from chat history and sending it with each request.

- **Session Lifecycle**
   - Creating, maintaining, and resetting chat sessions based on user actions.

- **Agent Switching**
   - Dynamically switching between AI agents from the UI and starting a new session when required.

- **ChatRunner Pattern**
   - Introducing an orchestration layer between the ViewModel and Repository to manage agent-driven conversations.

- **Streaming LLM Responses**
   - Handling partial Gemini responses using Kotlin Flow and updating the UI in real time.


## Roadmap

- [x] Android Mentor Agent
- [x] Android Interviewer Agent
- [x] Kotlin Interviewer Agent
- [x] Agent Switching

### Next Steps

- [ ] Dependency Injection (Hilt)
- [ ] Multi-Session Support
- [ ] Memory Support
- [ ] Architecture Interview Agent
- [ ] Tool Calling
- [ ] Room Persistence
- [ ] Unit Testing
- [ ] On-device LLM Integration

## License
This project is for educational purposes.
