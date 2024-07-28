# MelodyOpus-Backend

## Project Description

This repository hosts the backend code for MelodyOpus, a modern music player application built with Spring Boot. The backend handles all the core functionalities required for a music streaming service, including user authentication, playlist management, song streaming, and more. It exposes RESTful APIs for the frontend to interact with.

Mobile: [check here](https://github.com/anh-nt24/MelodyOpus-Mobile)

## Features
- **User Authentication and Authorization**
  - User registration and login
  - JWT-based authentication
  - OAuth2 login with Google, Facebook
  - Forgot password functionality

- **Playlist Management**
  - Create, update, delete, and retrieve playlists
  - Add and remove songs from playlists

- **Music Streaming and Management**
  - Stream songs from the server
  - Create, update, delete and retrieve songs

- **User Interaction**
  - Follow and unfollow other users
  - Like and unlike songs

## Technologies Used
- **Spring Boot** - Backend framework
- **Spring Security** - Security and JWT handling
- **Spring Security OAuth2** - OAuth2 login with Google
- **JPA/Hibernate** - ORM for database interactions
- **MySQL** - Relational database
- **Maven** - Dependency management
- **JavaMailSender** - Sending emails

## Getting Started

### Prerequisites

- Java 17 or higher
- Maven 3.6 or higher
- MySQL database


### Installation

1. **Clone the repository**
   ```bash
   git clone https://github.com/your-username/MelodyOpus-Backend.git
   cd MelodyOpus-Backend
   ```

2. **Setup the database**

    *Database will be automatically created after successfully starting up the application.*

3. **Configure email and OAuth2 service**

    *Config in the .env file.*

    For example:
    ```dotenv
    # Google OAuth2 Configuration
    GOOGLE_CLIENT_ID=your-google-client-id
    GOOGLE_CLIENT_SECRET=your-secret-client-id

    # Email Configuration
    GMAIL_ADDRESS=your-email-address
    GMAIL_APP_PASSWORD=yput-app-password-in-the-email
    ```

4. **Build the project**
    
    ```bash
    mvn clean install
    ```

5. **Run the application**

    ```bash
    export $(grep -v '^#' .env | xargs) & mvn spring-boot:run
    ```






