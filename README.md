# Android Mentor Chat

Android Mentor Chat is a sample Android application that demonstrates how to integrate the Gemini AI model to create an interactive chat experience. It features a modern UI built with Jetpack Compose and uses the Google Generative AI SDK for streaming responses.

## Features

- **Real-time Chat**: Interactive chat interface with user and AI messages.
- **Gemini AI Integration**: Uses the Gemini 1.5 Flash Lite model for fast and intelligent responses.
- **Markdown Support**: Renders AI responses with full Markdown formatting for better readability.
- **Modern Android Architecture**: Built using MVVM architecture, Kotlin Coroutines, and StateFlow.
- **Jetpack Compose UI**: A fully declarative and reactive UI.

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

- `com.sri.geminichat.MainActivity`: The main entry point and UI container.
- `com.sri.geminichat.MainViewModel`: Manages UI state and business logic.
- `com.sri.geminichat.MainRepository`: Handles communication with the Gemini AI service.
- `com.sri.geminichat.ChatSession` & `com.sri.geminichat.ChatHistory`: Data models for the chat session and messages.

## License

This project is for educational purposes.
