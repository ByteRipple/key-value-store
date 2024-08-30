# Transactional Key Value Store

This project implements an interactive transactional key-value store interface in Kotlin. The interface allows users to perform various operations on
key-value pairs and supports transactional operations with the ability to commit and rollback changes. The key-value data is stored in memory for the
duration of the session and is not written to disk.

## Project Structure

The project consists of two main modules:

+ `core-lib`: This module contains the core logic of the key-value storage, including all operations such as setting, getting, deleting key-value
  pairs, counting values, and handling transactions.
+ `terminal-handler`: This module handles the interaction through the console, including command parsing and invoking the storage interface. It
  provides an interactive interface for users to execute commands and see results.

## Running the Project
You can run the project using Gradle or a script that calls a pre-built JAR file.

+ Run the project using Gradle: `./gradlew run`
+ Run the script that executes the pre-built JAR file. To run the script, Java must be installed on your machine: `./run.sh`

### Building the JAR File

To build JAR file, run the following command: `./gradlew build`

## Commands

+ `SET <key> <value>`: Store the value for the key.
+ `GET <key>`: Return the current value for the key.
+ `DELETE <key>`: Remove the entry for the key.
+ `COUNT <value>`: Return the number of keys that have the given value.
+ `BEGIN`: Start a new transaction.
+ `COMMIT`: Complete the current transaction.
+ `ROLLBACK`: Revert to the state prior to the BEGIN call.
+ `EXIT`:  End the interactive session.

## Usage Examples

### Set and Get a Value

```
> SET foo 123
> GET foo
123
```

### Delete a Value

```
> DELETE foo
> GET foo
key not set
```

### Count the Number of Occurrences of a Value

```
> SET foo 123
> SET bar 456
> SET baz 123
> COUNT 123
2
> COUNT 456
1
```

### Commit a Transaction

```
> SET bar 123
> GET bar
123
> BEGIN
> SET foo 456
> GET bar
123
> DELETE bar
> COMMIT
> GET bar
key not set
> ROLLBACK
no transaction
> GET foo
456
```

### Rollback a Transaction

```
> SET foo 123
> SET bar abc
> BEGIN
> SET foo 456
> GET foo
456
> SET bar def
> GET bar
def
> ROLLBACK
> GET foo
123
> GET bar
abc
> COMMIT
no transaction
```

### Nested Transactions

```
> SET foo 123
> SET bar 456
> BEGIN
> SET foo 456
> BEGIN
> COUNT 456
2
> GET foo
456
> SET foo 789
> GET foo
789
> ROLLBACK
> GET foo
456
> DELETE foo
> GET foo
key not set
> ROLLBACK
> GET foo
123
```
