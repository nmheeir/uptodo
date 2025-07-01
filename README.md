# UpTodo - Modern Task Management App

A comprehensive task management application built with Jetpack Compose and modern Android development practices. UpTodo helps you organize your daily tasks, track your productivity, and maintain focus with built-in timer functionality.

## 🚀 Features

### Core Functionality
- **Task Management**: Create, edit, delete, and organize tasks with priorities
- **Categories**: Organize tasks with customizable color-coded categories
- **Calendar Integration**: View tasks by date with an intuitive calendar interface
- **Focus Mode**: Built-in Pomodoro-style timer for focused work sessions
- **Task Completion**: Mark tasks as complete with swipe gestures
- **Sub-tasks**: Break down complex tasks into manageable sub-tasks

### User Experience
- **Modern UI**: Clean, Material Design 3 interface with smooth animations
- **Dark/Light Theme**: Automatic theme switching based on system preferences
- **Responsive Design**: Optimized for different screen sizes
- **Intuitive Navigation**: Bottom navigation with smooth transitions
- **Swipe Actions**: Quick task completion and deletion with swipe gestures

### Advanced Features
- **Task Filtering**: Filter tasks by completion status, priority, and category
- **Focus Session Tracking**: Track your productivity with session history
- **Custom Time Picker**: Wheel-based time selection for deadlines
- **Task Details**: Comprehensive task editing with deadline, priority, and category management

## 🛠 Technologies Used

### Core Framework
- **Kotlin** - Primary programming language
- **Jetpack Compose** - Modern UI toolkit
- **Material Design 3** - Design system and components

### Architecture & Dependencies
- **MVVM Architecture** - Clean separation of concerns
- **Hilt** - Dependency injection
- **Room Database** - Local data persistence
- **Navigation Compose** - Type-safe navigation
- **Coroutines & Flow** - Asynchronous programming

### Additional Libraries
- **Timber** - Logging
- **Calendar Compose** - Calendar implementation
- **Snapper** - Smooth scrolling effects
- **DataStore** - Preferences storage
- **Kotlinx Collections Immutable** - Immutable collections

## 📱 Installation

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- Android SDK API level 27 or higher
- Kotlin 2.0.10 or later

### Setup Instructions

1. **Clone the repository**
   ```bash
   git clone https://github.com/yourusername/uptodo.git
   cd uptodo
   ```

2. **Open in Android Studio**
   - Launch Android Studio
   - Select "Open an existing project"
   - Navigate to the cloned directory and select it

3. **Sync project dependencies**
   ```bash
   ./gradlew build
   ```

4. **Run the application**
   - Connect an Android device or start an emulator
   - Click the "Run" button in Android Studio or use:
   ```bash
   ./gradlew installDebug
   ```

## 🎯 Usage

### Creating Tasks
```kotlin
// Example of creating a new task
val newTask = TaskEntity(
    title = "Complete project documentation",
    description = "Write comprehensive README and API docs",
    priority = Priority.HIGH,
    categoryId = 1L,
    deadline = LocalDateTime.now().plusDays(2),
    isReminder = true
)
```

### Managing Categories
```kotlin
// Creating a custom category
val category = CategoryEntity(
    name = "Work",
    color = "#FF6B6B" // Hex color code
)
```

### Focus Sessions
```kotlin
// Starting a focus session
val focusTime = LocalTime.of(0, 25, 0) // 25 minutes
viewModel.onAction(FocusUiAction.SetFocusTime(focusTime))
viewModel.onAction(FocusUiAction.StartFocus)
```

## 📊 Database Schema

### Task Entity
| Field | Type | Description |
|-------|------|-------------|
| taskId | Long | Primary key (auto-generated) |
| parentTask | Long? | Reference to parent task for sub-tasks |
| categoryId | Long | Foreign key to category |
| title | String | Task title |
| description | String | Task description |
| priority | Priority | LOW, MEDIUM, HIGH |
| createdAt | LocalDateTime | Creation timestamp |
| updatedAt | LocalDateTime? | Last update timestamp |
| isComplete | Boolean | Completion status |
| isReminder | Boolean | Reminder enabled |
| duration | Long | Estimated duration in seconds |
| deadline | LocalDateTime | Task deadline |

### Category Entity
| Field | Type | Description |
|-------|------|-------------|
| categoryId | Long | Primary key (auto-generated) |
| name | String | Category name |
| color | String | Hex color code |

### Focus Session Entity
| Field | Type | Description |
|-------|------|-------------|
| focusSessionId | Int | Primary key (auto-generated) |
| duration | Long | Session duration in seconds |
| createdAt | LocalDateTime | Session timestamp |

## ⚙️ Configuration

### Build Configuration
The app uses Gradle version catalogs for dependency management. Key configurations:

```kotlin
// app/build.gradle.kts
android {
    compileSdk = 35
    defaultConfig {
        minSdk = 27
        targetSdk = 35
    }
}
```

### Database Configuration
```kotlin
// Room database setup
@Database(
    entities = [TaskEntity::class, CategoryEntity::class, FocusSessionEntity::class],
    version = 2,
    exportSchema = true
)
@TypeConverters(Converters::class)
abstract class InternalDatabase : RoomDatabase()
```

## 🎨 UI Components

### Custom Components
- **WheelTimePicker**: Custom time selection wheel
- **TaskItem**: Swipeable task card with actions
- **CalendarDialog**: Date selection with calendar view
- **CircularProgress**: Animated progress indicator for focus sessions

### Theme Configuration
```kotlin
// Material Design 3 theming
@Composable
fun UpTodoTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
)
```

## 📱 App Structure

### Main Screens
- **Index Screen**: Main task list with filtering options
- **Calendar Screen**: Calendar view with task scheduling
- **Focus Screen**: Pomodoro timer with session tracking
- **Task Detail Screen**: Comprehensive task editing interface

### Navigation
The app uses bottom navigation with four main sections:
- 🏠 **Index**: Task overview and management
- 📅 **Calendar**: Date-based task view
- ⏰ **Focus**: Productivity timer
- ⋯ **More**: Additional settings and options

## 🤝 Contributing

We welcome contributions! Please follow these guidelines:

### Development Setup
1. Fork the repository
2. Create a feature branch: `git checkout -b feature/amazing-feature`
3. Follow the existing code style and architecture patterns
4. Write tests for new functionality
5. Commit changes: `git commit -m 'Add amazing feature'`
6. Push to branch: `git push origin feature/amazing-feature`
7. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable and function names
- Add KDoc comments for public APIs
- Maintain consistent formatting with ktlint

### Architecture Guidelines
- Follow MVVM pattern
- Use Hilt for dependency injection
- Implement proper error handling
- Write unit tests for ViewModels and repositories

## 🐛 Known Issues

- Focus timer may not persist across app restarts
- Calendar view performance could be optimized for large datasets
- Some animations may lag on older devices

## 🔮 Roadmap

### Upcoming Features
- [ ] Cloud synchronization
- [ ] Task sharing and collaboration
- [ ] Advanced analytics and reporting
- [ ] Widget support
- [ ] Notification improvements
- [ ] Export/import functionality

### Performance Improvements
- [ ] Database query optimization
- [ ] Memory usage optimization
- [ ] Animation performance enhancements

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

```
MIT License

Copyright (c) 2025 UpTodo

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.
```

## 📞 Support & Contact

### Getting Help
- **Issues**: Report bugs or request features via [GitHub Issues](https://github.com/yourusername/uptodo/issues)
- **Discussions**: Join community discussions in [GitHub Discussions](https://github.com/yourusername/uptodo/discussions)
- **Documentation**: Check the [Wiki](https://github.com/yourusername/uptodo/wiki) for detailed guides

### Maintainers
- **Primary Maintainer**: [Your Name](https://github.com/yourusername)
- **Email**: your.email@example.com

### Community
- **Discord**: [Join our Discord server](https://discord.gg/uptodo)
- **Twitter**: [@UpTodoApp](https://twitter.com/uptodoapp)

---

## 🙏 Acknowledgments

- [Jetpack Compose](https://developer.android.com/jetpack/compose) for the modern UI toolkit
- [Material Design](https://material.io/) for design guidelines
- [Calendar Compose](https://github.com/kizitonwose/CalendarView) for calendar implementation
- [Snapper](https://github.com/chrisbanes/snapper) for smooth scrolling effects
- All contributors who help improve this project

---

**Made with ❤️ using Jetpack Compose**