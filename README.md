# MiniGit Repository System

# Mini-Git Repository System

Mini-Git is a simplified version control system that simulates basic Git functionality. This project provides a command-line interface for managing repositories, making commits, and synchronizing between repositories.

## Features

- **Create repositories**: Easily create new repositories with unique names.
- **Commit changes**: Add commits with custom messages to track changes.
- **View repository history**: See the commit history of a repository.
- **Drop commits**: Remove specific commits from the repository.
- **Synchronize repositories**: Merge the commit history of two repositories.
- **View repository head**: Check the current head commit of a repository.

## Getting Started

To run the Mini-Git system, compile and execute the `Client.java` file. This will start the command-line interface where you can interact with the system.

```bash
javac Client.java Repository.java
java Client
```

## Usage

The system supports the following operations:

- `create <repo_name>`: Create a new repository
- `head <repo_name>`: View the current head commit of a repository
- `history <repo_name>`: View the commit history of a repository
- `commit <repo_name>`: Make a new commit in a repository
- `drop <repo_name>`: Remove a specific commit from a repository
- `synchronize <repo_name>`: Merge two repositories
- `quit`: Exit the program

## Project Structure

The project consists of two main classes:

1. `Repository.java`: Implements the core functionality of the version control system, including commit management and repository operations.

2. `Client.java`: Provides the user interface for interacting with the Mini-Git system, handling user input and displaying repository information.

## Implementation Details

- Commits are stored in a linked list structure, with each commit pointing to its predecessor.
- Each commit has a unique ID, timestamp, and associated message.
- The `synchronize` operation merges two repositories while maintaining chronological order of commits.

## Testing

To thoroughly test the Mini-Git implementation, make sure to:

- Create multiple repositories
- Make commits with various messages
- Test the history functionality with different numbers of commits
- Attempt to drop commits from different positions in the history
- Synchronize repositories with overlapping and non-overlapping histories