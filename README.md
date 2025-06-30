## API todo List - Desafio final ZettaLab
API REST desenvolvida em **Java com Spring Boot** para o gerenciamento de tarefas. Conta com autenticação JWT, controle de perfis de usuário e documentação Swagger.

## Cargos de Usuário
- **ADMIN**: acesso total a usuários e tarefas.
- **USUÁRIO**: pode gerenciar apenas suas próprias tarefas.
- **LEITOR**: acesso somente leitura às tarefas de qualquer usuário.

### Build:
- ./mvnw clean package -DskipTests
- docker build -t todolist-api .
- docker run -p 8080:8080 todolist-api

A aplicação será iniciada em http://localhost:8080/

Acesse a interface Swagger UI: http://localhost:8080/swagger-ui/index.html

### Login

Após registrar um usuário, você deve fazer login para obter um token:

POST /auth/login
{
"email": "usuario@email.com",
"senha": "123"
}

Use o token retornado como Authorization: Bearer <token> em todas as demais requisições.