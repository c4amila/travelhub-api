# ✈️ Travel Hub API

API RESTful desenvolvida em **Java** para gestão de passagens aéreas, reservas de hotéis e pacotes de viagem personalizados.
O projeto simula um sistema real de agência de turismo, com regras de negócio que garantem consistência entre voos, hospedagens e pacotes vendidos.

---
## Tecnologias
- **Java 17**
- **Spring Boot 4.1.1**
- **Spring Data JPA**
- **PostgreSQL**
- **Flyway**
- **Swagger/OpenAPI**
- **JUnit 5 + Mockito**
- **Docker**
- **Maven**

---
## Arquitetura

Em breve

## Principais Endpoints
| **Método** | **Endpoint** | **Descrição**        |
|------------|--------------|----------------------|
| POST       | `/voos`      | Cadastra um voo novo | 
| GET        | `/voos/{id}` | Busca voo por id     |
| GET        | `/voos`      | Listar todos os voos |
| GET        | `/voos/filtrar`| Filtrar voos       |
 | PATCH     | `/voos/{id}` | Atualizar voo        |