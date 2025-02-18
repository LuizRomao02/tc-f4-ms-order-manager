# Tech Challenger FIAP F4

Welcome to the "Power Programmers Product Batch" project repository. This project aims to implement a batch processing system for product data.

## 🎓 Academic Project

Developed as part of the **Java Architecture and Development** postgraduate course at FIAP.

## 👨‍💻 Developers

- Edson Antonio da Silva Junior
- Gabriel Ricardo dos Santos
- Luiz Henrique Romão de Carvalho
- Marcelo de Souza

## 💡 Technologies

![Java](https://img.shields.io/badge/Java-17-blue?style=for-the-badge&logo=java)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.4.0-brightgreen?style=for-the-badge)
![Maven](https://img.shields.io/badge/Maven-3.9.9-C71A36?style=for-the-badge&logo=apachemaven)
![MySQL](https://img.shields.io/badge/MySQL-4479A1?style=for-the-badge&logo=mysql&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-27.4.0-2496ED?style=for-the-badge&logo=docker)
![Swagger](https://img.shields.io/badge/Swagger-3.0-85EA2D?style=for-the-badge&logo=swagger)

```markdown
br.com.powerprogramers.ordermanager
├── 📁 config                           # Application configurations (Beans, properties, etc.)
├── 🎯 domain                           # Core domain layer of the application
│   ├── 📁 controller                   # Handles incoming requests and delegates to services
│   ├── 📁 dto                          # Data Transfer Objects (DTOs) for communication
│   ├── 📁 entity                       # Database entity models
│   ├── 📁 exceptions                   # Custom exception definitions
│   ├── 📁 gateway                      # Handles external communication and integrations
│   │   ├── 📁 mq                       # RabbitMQ configuration and event publishing
│   ├── 📁 mappers                      # Converts entities to DTOs and vice versa
│   ├── 📁 repository                   # Data access layer (Spring Data JPA repositories)
│   └── 📁 service                      # Business logic layer
│       ├── 📁 impl                     # Implementations of services
│       ├── 📁 usecases                 # Specific business use cases
└── 🚀 OrderManagerApplication.java     # Main Spring Boot application class

```

## 🔍 Architecture Explanation
This project follows a `Hexagonal Architecture` (also known as Ports and Adapters) combined with `DDD (Domain-Driven Design)` principles. This approach provides benefits such as modularity, scalability, and maintainability.

## 📌 Key Benefits
- ✅ Modularity: Clear separation between layers, making future expansions easier.
- ✅ Maintainability: Changes in one part of the system do not affect the rest.
- ✅ Testability: Each layer can be tested independently, ensuring reliability.
- ✅ Flexibility: Allows switching databases, message queues, or integrations without affecting business logic.

## 🔄 Execution Flow
- 1️⃣ Data Input: The `controller` receives the client `request`.
- 2️⃣ Validation & Processing: The service layer applies business logic and delegates tasks.
- 3️⃣ Data Access: The `repository` interacts with the database to store or retrieve information.
- 4️⃣ External Communication: The `gateway` handles `API calls and message queues`.
- 5️⃣ Response: The result is returned to the client via a `DTO`.

## 🧪 API Endpoint

The API can be explored and tested using Swagger. The documentation is available at:
[`Swagger`](http://localhost:8081/swagger-ui/index.html)

## License

This project is licensed under the [MIT License](https://www.notion.so/LICENSE).