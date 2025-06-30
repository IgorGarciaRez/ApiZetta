package com.zetta.api;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "API To-Do List",
				version = "1.0",
				description = """
            Esta API permite o cadastro de usuários com diferentes cargos (ADMIN, USUÁRIO, LEITOR) e gerenciamento de tarefas com prioridades e status.

            - **ADMIN**: acesso total.
            - **USUÁRIO**: acesso às próprias tarefas.
            - **LEITOR**: acesso de leitura para qualquer usuário.

            Requer autenticação via Bearer Token.
            """
		)
)
@SpringBootApplication
public class ApiApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiApplication.class, args);
	}

}
