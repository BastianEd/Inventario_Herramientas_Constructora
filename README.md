# 🛠️ Sistema de Gestión de Inventario de Herramientas para Constructoras

Este proyecto es una aplicación robusta y eficiente diseñada para la gestión integral del inventario de herramientas en empresas constructoras. Permite un control detallado sobre las herramientas disponibles, su estado, los préstamos a trabajadores y la categorización, optimizando así la operatividad y reduciendo pérdidas.

## ✨ Características Principales

*   **Gestión de Herramientas**:
    *   Registro, actualización y eliminación de herramientas.
    *   Control de cantidad disponible, prestada y dañada.
    *   Funcionalidades para prestar y devolver herramientas, actualizando automáticamente el inventario.
*   **Gestión de Categorías de Herramientas**:
    *   Clasificación de herramientas por tipo, marca y antigüedad.
    *   CRUD completo para la administración de categorías.
*   **Gestión de Trabajadores**:
    *   Registro y administración de la información de los trabajadores.
    *   Asociación de préstamos de herramientas a trabajadores específicos.
*   **Gestión de Préstamos**:
    *   Registro de préstamos de herramientas a trabajadores.
    *   Control de fechas de préstamo y devolución.
    *   Funcionalidad para devolver préstamos, actualizando el estado de la herramienta.
*   **Gestión de Inventario General**:
    *   Visión consolidada del stock total, herramientas prestadas y dañadas.
*   **API RESTful con Soporte HATEOAS**:
    *   Exposición de endpoints RESTful para todas las operaciones, facilitando la integración con otras aplicaciones.
    *   **Versiones V1 (REST tradicional)** y **V2 (HATEOAS)** de los controladores para una mayor flexibilidad y escalabilidad, permitiendo a los clientes navegar por la API a través de enlaces.
*   **Documentación de API (Swagger/OpenAPI)**:
    *   Documentación interactiva de todos los endpoints para facilitar su uso y prueba.
*   **Carga de Datos Inicial (DataLoader)**:
    *   Generación de datos ficticios al inicio de la aplicación para pruebas y desarrollo.

## 🚀 Tecnologías Utilizadas

El proyecto está construido con un stack tecnológico moderno y robusto:

*   **Backend**:
    *   **Java 17**: Lenguaje de programación principal.
    *   **Spring Boot 3.x**: Framework para el desarrollo rápido de aplicaciones Java.
    *   **Spring Data JPA**: Para la persistencia de datos y la interacción con la base de datos.
    *   **Spring HATEOAS**: Para la construcción de APIs RESTful que siguen el principio HATEOAS (Hypermedia as the Engine of Application State), enriqueciendo las respuestas con enlaces navegables.
    *   **Lombok**: Para reducir el código boilerplate (getters, setters, constructores, etc.).
    *   **Maven**: Herramienta de gestión de proyectos y dependencias.
*   **Base de Datos**:
    *   **MySQL**: Sistema de gestión de bases de datos relacional.
*   **Herramientas de Desarrollo y Pruebas**:
    *   **JUnit 5**: Framework para pruebas unitarias.
    *   **Mockito**: Para la creación de mocks en pruebas unitarias.
    *   **Datafaker**: Para la generación de datos de prueba realistas.
    *   **Swagger UI / OpenAPI 3**: Para la documentación y prueba interactiva de la API.
    *   **Postman**: Herramienta recomendada para probar los endpoints de la API.

## ⚙️ Requisitos Previos

Antes de ejecutar el proyecto, asegúrate de tener instalado lo siguiente:

*   **Java Development Kit (JDK) 17 o superior**.
*   **Apache Maven 3.x o superior**.
*   **MySQL Server 8.x o superior** (o un cliente MySQL compatible).
*   Un IDE como IntelliJ IDEA, Eclipse o VS Code con soporte para Spring Boot.

## 📦 Instalación y Ejecución

Sigue estos pasos para poner en marcha el proyecto en tu entorno local:

### 1. Clonar el Repositorio

```bash
git clone https://github.com/BastianEd/Inventario_Herramientas_Constructora.git
cd Inventario_Herramientas_Constructora-main
```

## 2. Configuración de la Base de Datos MySQL
Crear la Base de Datos: Abre tu cliente MySQL (MySQL Workbench, línea de comandos, etc.) y ejecuta el siguiente comando para crear la base de datos:
      CREATE DATABASE IF NOT EXISTS inventario_db;
      CREATE DATABASE IF NOT EXISTS inventariodb_dev;
      CREATE DATABASE IF NOT EXISTS inventariodb_test;



## Configurar Credenciales: 
   Abre el archivo src/main/resources/application.properties y configura las credenciales de tu base de datos:
      spring.datasource.url=jdbc:mysql://localhost:3306/inventario_db
      spring.datasource.username=tu_usuario_mysql
      spring.datasource.password=tu_contraseña_mysql
      spring.jpa.hibernate.ddl-auto=update
      spring.jpa.show-sql=true
      spring.jpa.properties.hibernate.format_sql=true

   Reemplaza tu_usuario_mysql y tu_contraseña_mysql con tus credenciales.
##📚 Documentación de la API (Swagger UI)
   Una vez que la aplicación esté en ejecución, puedes acceder a la documentación interactiva de la API a través de Swagger UI en tu navegador:
            http://localhost:8080/swagger-ui.html
   Aquí podrás ver todos los endpoints disponibles, sus parámetros, modelos de respuesta y probar las solicitudes directamente.

##Desarrollado con ❤️ por Los Pulentos (Ignacio, Bastian, Nicolas, Vicente y Juan)
   
