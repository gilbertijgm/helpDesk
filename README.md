# 🗂️ HELP DESK - Sistema de Gestión de Tickets e incidentes

**HELP DESK** es una API REST desarrollada con **Spring Boot**, diseñada para gestionar y documentar incidentes por medios de tickets. Tiene como funcion la creacion de ticket, listado de los mismos aplicando filtrado dinamicos, validaciones, seguridad y documentacion en Swagger. Incluye arquitectura en capas, manejo de errores y respuesta estructurada tipo `ApiResponse`.

---
#  Tabla de Contenidos
1. Tecnologías utilizadas

2. Estructura del Proyecto

3. Funcionalidades

4. Instalación y Ejecucion

5. Uso

6. API Endpoints

7. Validaciones incluidas

8. Documentación Swagger

9. Contribuciones

10. Autor

11. Licencia

---
##  Tecnologías utilizadas

- Java 17
- Spring Boot 3.4.5
- Spring Data JPA (Hibernate)
- MySQL
- MapStruct
- Lombok
- Swagger (OpenAPI)
- Spring Security + JWT
- Maven
- JUnit + Mockito (en progreso)

  ---

##  Estructura del Proyecto

📁 src

└── main

        ├──  java
        │ └── com.proyecto.helpDesk
          │ ├── config (contiene configuracion de spring security y jwt)
          │ ├── controller (contiene DTOs y controladores)
          │ ├── entities (contiene la entidades)
          │ ├── exceptions (manejo de las diferentes excepciones)
          │ ├── persistence (DAO: capa de persistencia; contiene todos los metodos para la interaccion con la base de datos)
          │ ├── repository (contiene las interfaces)
          │ ├── response (contiene las clases para respueesta estructurada)
          │ ├── service (logica de negocio)
          │ └── utils (utilidades generales)
        └── resources   
            ├── application.properties

---

##  Funcionalidades
- Registro y Logueo de Usuarios (implementacion de acceso por token)
- Crear tickets con validaciones
- Listar tickets con paginacion y filtrado dinamico:
    - Estado ( `ABIERTO`, `EN_PROCESO`, etc.)
    - Prioridad (`CRITICA`, `ALTA`, etc.)
    - Categoria
    - Fecha de Inicio y/o Fecha Fin
    - Por creador del ticket
    - Por tecnico asignado
- Listar tickets validacion por rol:
    - cliente: solo puede acceder a los tickets creados por el
    - tecnico: solo puede acceder a los tickets que le fueron asignados a el
    - admin: puede acceder a todos los tickets
- Buscar Ticket por ID: aplicando las mismas reglas de validacion por rol
- Creacion de comentario a un ticket
- Manejo de errores con mensajes personalizados
- Documentación Swagger lista en `/swagger-ui/index.html`
- Respuesta estructurada con `statusCode`, `message` y `data`
-  Mapeo automático entre entidades y DTOs usando MapStruct
-  Evita duplicar lógicas en el código gracias a capa DAO y utilidades comunes

---

## 🛠 Instalación y Ejecución

1. **Clonar el repositorio**

```bash
git clone https://github.com/gilbertijgm/task.git
cd task
```
2. **Configurar base de datos en application.properties**

spring.application.name=helpDesk

**Configuracion de la base de datos**

spring.datasource.url=jdbc:mysql://localhost:3306/helpDesk?seSSL=false&serverTimezone=UTC
spring.datasource.username=root
spring.datasource.password=

**Configuracion de Hibernate**

spring.jpa.database-platform=org.hibernate.dialect.MySQLDialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true

**Configuracion de SpringSecurity**

#spring.security.user.name=gilberto
#spring.security.user.password=2345

**Configuracion de JWT**

security.jwt.key.private=f4f5d547e7073377346ac42e70245e26938725c5ade6f6783c98425eb4ce0ba0
security.jwt.user.generator=AUTH0JWT-BACKEND

logging.level.org.springframework.security=DEBUG

⚠️ Asegurate de crear previamente la base de datos task_db en MySQL.

3. **Compilar y correr el proyecto**
```bash
mvn clean install
mvn spring-boot:run
```

---

## 🚀 Uso
Una vez en ejecución, puedes acceder a:

- http://localhost:8080/auth/login

- http://localhost:8080/swagger-ui.html (documentación interactiva)

---

##  API Endpoints (ENDPOINT PRINCIPALES)
| Verbo  | Endpoint                           | Descripción                             |
| ------ | ---------------------------------- | --------------------------------------- |
| POST   | `/api/ticket/crear`                | Crea un nuev ticket                     |
| PATCH  | `/api/ticket/asignarTecnico/{id}`  | Asigna un tecnico a un ticket especifico|
| PATCH  | `/api/ticket/actualizarEstado/{id}`| Actualiza solo el estado del ticket     |
| GET    | `/api/ticket/listado`              | Lista paginada y filtrada de tickets    |
| GET    | `/api//ticket/ticket/{id}`         | Obtiene tarea por ID                    |

🔎 Filtros dinámicos:
El endpoint /tasks soporta filtros combinables a través de Query Params:
```bash
/api/ticket/tickets?estado=ABIERTO&prioridad=ALTA&categoria=soporte&idTecnico=3&idCategoria=3
```
- estado: enum (ABIERTO, EN_PROCESO, RESUELTO, CERRADO)

- prioridad: enum (CRITICA, ALTA, MEDIA, BAJA) 

- palabraClave: busca por título o descripción

- fechaInicio y fechaFin: filtra por rango de fechas de vencimiento

- creadoPor y tecnico asignado

Estos filtros se aplican dinámicamente mediante Criteria API en el repositorio custom.

---

##  Validaciones incluidas
- Ticket
  - Título y descripción: Campo requerido, no pueden estar vacíos. Min = 2, Max = 50 caracteres
    
  -Descripción: Campo requerido, no pueden estar vacíos. Min = 2 y Max = 250 caracteres
  
  - prioridad: Campo requerido, prioridad no puede estar vacio

  - Categoria: obligatorio y debe ser uno de los definidos en el enum

- Comentario
  - Mensaje = Campo requerido, mensaje no puede estar vacio. Min = 2 y Max = 250 caracteres

- Login y Registro:
  - username: Campo requerido, no pueden estar vacíos.
  - password: Campo requerido, no pueden estar vacíos.
  - email: Campo requerido, no pueden estar vacíos.
---
## Seguridad

Este proyecto implementa seguridad basada en JWT (JSON Web Token) utilizando Spring Security. El sistema maneja autenticación, autorización por roles y permisos específicos. A continuación, se describe el enfoque utilizado:

🧾 Esquema de autenticación y autorización
- Autenticación:
  
  Los usuarios se autentican mediante POST /auth/login, donde se valida el username y password. Si las credenciales son válidas, se genera un JWT que incluye los roles y permisos del usuario.

- JWT personalizado:

  El token JWT generado contiene:

    - sub (username del usuario)

    - authorities: roles y permisos separados por coma

    - iat, exp, jti, nbf, iss (claims estándar)

- Verificación del token:

  En cada solicitud protegida, el token es validado mediante un filtro personalizado que extrae y verifica la firma del JWT, y carga el usuario autenticado en el SecurityContext.

 - Roles disponibles
   
    El sistema define tres roles principales, implementados como un Enum (Rol):

      -Rol	Descripción
   
        -ADMIN:	Puede ver y modificar todos los tickets, usuarios y entidades
   
        -TECNICO:	Solo puede ver y resolver tickets asignados a él
   
        -CLIENTE:	Solo puede ver y crear sus propios tickets
   
  Cada rol está asociado a una lista de permisos (PermissionEntity), como READ, CREATE, etc., que se cargan como GrantedAuthorities en Spring Security.
  
---

##  Documentación Swagger
Disponible en:

http://localhost:8080/swagger-ui.html

Expone todos los endpoints, parámetros, modelos y respuestas esperadas.

---
##  Contribuciones
¡Bienvenidas! Para contribuir:

- Haz fork del repositorio

- Crea una rama feature (git checkout -b feature/nueva-función)

- Haz commit de tus cambios (git commit -m "Añade algo")

- Haz push a la rama (git push origin feature/nueva-función)

- Abre un Pull Request

---

##  Autor
Desarrollado por Gilberto J. Gutiérrez

Linkedin:  www.linkedin.com/in/gilbertojgutierrezm

---

##  Licencia

Este proyecto es de uso libre para fines educativos y personales.
