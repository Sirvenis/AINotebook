# AINotebook

A modern Android note-taking application built with Jetpack Compose, featuring a clean Material Design interface and powerful note management capabilities.

## Features

### Core Functionality
- ✍️ Create, edit, and delete notes
- 🏷️ Tag-based organization
- 🔍 Full-text search
- 📎 Support for attachments (images, audio, files)
- 🌓 Dark/Light theme with system theme support

### Note Management
- 📝 Rich text notes
- 🎤 Audio recording
- 📸 Photo capture
- 📁 File attachments
- 🏷️ Multiple tags per note

### Organization & Filtering
- 🔍 Search through notes
- 🏷️ Filter by tags
- 📊 Sort by:
  - Creation date
  - Modification date
  - Title
  - Number of tags

## Technology Stack

- **Language**: Kotlin
- **UI Framework**: Jetpack Compose
- **Architecture**: MVVM
- **Database**: Room
- **Dependency Injection**: Hilt (planned)
- **Coroutines & Flow**: For asynchronous operations
- **Material Design 3**: For modern UI components

## Project Structure

```
app/
├── src/
│   ├── main/
│   │   ├── java/com/ainotebook/
│   │   │   ├── data/
│   │   │   │   ├── model/       # Data models
│   │   │   │   ├── repository/  # Data repositories
│   │   │   │   └── dao/        # Database access
│   │   │   ├── ui/
│   │   │   │   ├── components/  # Reusable UI components
│   │   │   │   ├── screens/     # App screens
│   │   │   │   ├── theme/       # Theme configuration
│   │   │   │   └── viewmodel/   # ViewModels
│   │   │   └── util/           # Utility classes
│   │   └── res/                # Resources
│   └── test/                   # Unit tests
└── build.gradle               # App-level build config
```

## Setup & Installation

1. Clone the repository:
```bash
git clone https://github.com/Sirvenis/AINotebook.git
```

2. Open the project in Android Studio

3. Build and run the project

## Contributing

Contributions are welcome! Please feel free to submit a Pull Request.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## Upcoming Features

- [ ] Note Categories/Folders
- [ ] Note Export/Import
- [ ] Note Sharing
- [ ] Rich Text Editing
- [ ] Note Templates
- [ ] Note Pinning
- [ ] Note Statistics
- [ ] Note Archiving
- [ ] Cloud Backup