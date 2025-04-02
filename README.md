# Book Social Network

A modern social network platform built for book enthusiasts to connect, share, and discuss their favorite books. This application allows users to create profiles, share book reviews, track their reading history, and interact with other book lovers.

## Features

- 🔐 **Authentication & Authorization**
  - Secure user authentication using JWT tokens
  - Role-based access control
  - OAuth2 resource server integration

- 👥 **User Management**
  - User profiles
  - Role management
  - Email notifications

- 📚 **Book Management**
  - Book catalog
  - Book reviews and ratings
  - Reading history tracking

- 💬 **Social Features**
  - User feedback and comments
  - Book recommendations
  - Social interactions

- 📧 **Email Integration**
  - Email notifications
  - Development mail server for testing

## Tech Stack

- **Backend Framework**: Spring Boot 3.4.4
- **Java Version**: 23
- **Database**: PostgreSQL (with H2 for development)
- **Security**: Spring Security with JWT
- **API Documentation**: SpringDoc OpenAPI
- **Template Engine**: Thymeleaf
- **Build Tool**: Maven
- **Containerization**: Docker & Docker Compose

## Prerequisites

- Java 23
- Maven
- Docker and Docker Compose
- PostgreSQL (if running without Docker)

## Getting Started

1. **Clone the repository**
   ```bash
   git clone https://github.com/Dancan254/book-social-network.git
   cd book-social-network
   ```

2. **Start the required services using Docker Compose**
   ```bash
   docker-compose up -d
   ```
   This will start:
   - PostgreSQL database
   - MailDev server for email testing

3. **Build the application**
   ```bash
   ./mvnw clean install
   ```

4. **Run the application**
   ```bash
   ./mvnw spring-boot:run
   ```

The application will be available at `http://localhost:8088`

## API Documentation

Once the application is running, you can access the API documentation at:
- Swagger UI: `http://localhost:8088/swagger-ui.html`
- OpenAPI JSON: `http://localhost:8088/v3/api-docs`

## Development

### Database Configuration
The application uses PostgreSQL in production and H2 in development. Database configuration can be found in `application.properties` or `application.yml`.

### Email Testing
The application includes MailDev for email testing in development:
- Web Interface: `http://localhost:1080`
- SMTP Server: `localhost:1025`

### Security
- JWT-based authentication
- Role-based authorization
- OAuth2 resource server integration
- Secure password handling

## Project Structure

```
src/main/java/com/mongs/book_social_network/
├── auth/          # Authentication related components
├── book/          # Book management
├── common/        # Common utilities and shared components
├── config/        # Application configuration
├── email/         # Email service
├── exceptions/    # Custom exceptions
├── feedback/      # User feedback and comments
├── file/          # File handling
├── history/       # Reading history
├── repository/    # Data access layer
├── role/          # Role management
├── security/      # Security configuration
└── user/          # User management
```

## Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Acknowledgments

- Spring Boot team for the excellent framework
- All contributors who have helped shape this project 