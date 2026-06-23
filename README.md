# Android Mentor Chat

Android Mentor Chat is an Android application that demonstrates how to integrate Gemini AI to create an Android-focused learning and mentoring experience. Built with a focus on modern Android development practices, it features a fluid UI and real-time streaming responses.

## Why This Project?

Android Mentor Chat was built to explore AI-powered learning experiences on Android.

The application demonstrates how Gemini can be used to create specialized AI agents for mentoring, interviewing, and technical learning while applying modern Android architecture principles.

## Screenshots

<h3>Android Mentor Agent</h3>

<p>
<img src="screenshots/Android%20Mentor.png" width="300"/>
</p>

<h3>Android Interviewer Agent</h3>

<p>
<img src="screenshots/Android%20Interviewer%201.png" width="300"/>
<img src="screenshots/Android%20Interviewer%202.png" width="300"/>
</p>

<h3>Architecture Interviewer Agent</h3>

<p>
<img src="screenshots/Android%20Architect%201.png" width="300"/>
<img src="screenshots/Android%20Architect%202.png" width="300"/>
</p>

## Features

- **Real-time Chat**: Fluid interactive chat interface with distinct user and AI message bubbles.
- **Multiple AI Agents**:
  - Android Mentor Agent
  - Android Interviewer Agent
  - Kotlin Interviewer Agent
  - Architecture Interview Agent
- **Agent Switching**: Dynamically switch between AI personas from the UI.
- **Interview Difficulty Levels**: Configure Beginner, Intermediate, and Senior interview modes for interviewer agents.
- **Gemini AI Integration**: Uses Google's Gemini models to provide AI-powered responses.
- **Streaming AI Responses**: Responses are rendered in real-time as chunks are received from Gemini.
- **Markdown Support**: Full Markdown rendering for AI responses (code blocks, bold text, lists, etc.) using `compose-markdown`.
- **Material 3 Design**: Uses the latest Material Design components and dynamic color system.
- **Persistent Chat History (Room)**: Chat conversations are saved locally using Room Database and restored on app restart.
- **Clear Chat Feature**: Users can clear chat history for the current session or reset conversations.

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
Selected Agent
    ↓
Selected Difficulty
    ↓
Agent Instructions
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

## Data Persistence Flow

```text
User Message
    ↓
ViewModel
    ↓
Room DB (Save ChatHistory)
    ↓
ChatRunner
    ↓
Gemini API
    ↓
Streaming Response
    ↓
Room DB (Update ChatHistory)
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
- `ChatRunner`: Coordinates chat sessions, agent selection, and Gemini interactions.
- `Room Database`: Stores chat sessions and messages locally.
- `Agent`: Defines agent personality and instructions.
- `AgentType`: Manages available AI agents.

## Current Learning Focus

The project is actively evolving as part of an AI Engineering learning journey.

Upcoming areas include:

- Tool Calling
- Memory Systems
- Room Persistence
- On-device LLM Integration
- Multi-Session Conversations

## AI Concepts Explored

- **Agent Instructions**
   - Using system instructions to define the behavior and responsibilities of an AI agent.

- **Agent Personalities**
  - Implementing Android Mentor, Android Interviewer, Kotlin Interviewer, and Architecture Interviewer agents with distinct behaviors.

- **Runtime Agent Configuration**
  - Modifying agent behavior dynamically using difficulty levels without changing the underlying agent implementation.

- **Context Management**
   - Building conversation context from chat history and sending it with each request.

- **Session Lifecycle**
   - Creating, maintaining, and resetting chat sessions based on user actions.

- **Agent Switching**
   - Dynamically switching between AI agents from the UI and starting a new session when required.

- **ChatRunner Pattern**
  - Separating conversation management logic from the ViewModel using a dedicated ChatRunner component.

- **Streaming LLM Responses**
   - Handling partial Gemini responses using Kotlin Flow and updating the UI in real time.


## Roadmap

- [x] Android Mentor Agent
- [x] Android Interviewer Agent
- [x] Kotlin Interviewer Agent
- [x] Architecture Interviewer Agent
- [x] Agent Switching
- [x] Difficulty Levels
- [x] Streaming Responses
- [x] Context Management
- [x] Session Lifecycle Management
- [x] Room Persistence (Chat History Storage)
- [x] Clear Chat Feature

### Next Steps

- [ ] Dependency Injection (Hilt)
- [ ] Multi-Session Support
- [ ] Memory Support
- [ ] Tool Calling
- [ ] Room Persistence (sync/export)
- [ ] Unit Testing
- [ ] On-device LLM Integration
