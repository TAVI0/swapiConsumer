# SWAPI Consumer

![Java](https://img.shields.io/badge/Java-8%2B-blue.svg) ![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.x-brightgreen.svg) ![Maven](https://img.shields.io/badge/Maven-3.x-C71A36.svg) ![Docker](https://img.shields.io/badge/Docker-ready-2496ED.svg)

API REST en **Java 8+** y **Spring Boot 3** que actúa como puente entre tu aplicación y la **Star Wars API** ([swapi.tech](https://www.swapi.tech/)). Proporciona endpoints propios, documentación Swagger y autenticación básica.

> **Demo en producción**: [https://swapiconsumer.onrender.com/](https://swapiconsumer.onrender.com/)

---

## ✨ Funcionalidades

* **Endpoints REST** para Personas, Películas, Naves, Vehículos y más.
* **Paginación** y filtro por *id* o *name* (query `?name=Luke`).
* **Autenticación HTTP Basic** (`admin` / `admin`).
* **Documentación interactiva** vía Swagger UI (`/swagger-ui.html`).
* **Dockerfile** listo para empaquetar la app en un contenedor.

---

## 🗂️ Tecnologías

| Categoría    | Stack                                          |
| ------------ | ---------------------------------------------- |
| Core         | Java 8+, Spring Boot 3, Spring Web             |
| Persistencia | — (la API es *stateless*, solo actúa de proxy) |
| Seguridad    | Spring Security (HTTP Basic)                   |
| Docs         | springdoc‑openapi (Swagger)                    |
| Test         | JUnit 5 + Spring Test                          |

---

## 🛠️ Requisitos locales

| Herramienta | Versión mínima   |
| ----------- | ---------------- |
| JDK         | 8                |
| Maven       | 3.9              |
| Docker      | 20.10 (opcional) |

---

## 🚀 Uso rápido

### Con Docker

```bash
# Clona el proyecto
$ git clone https://github.com/TAVI0/swapiConsumer.git
$ cd swapiConsumer

# Construye la imagen
$ docker build -t swapiconsumer:latest .

# Lanza el contenedor (puerto 8080 -> 8080)
$ docker run -p 8080:8080 swapiconsumer:latest
```

Accede a `http://localhost:8080/swagger-ui.html` para explorar los endpoints.

### Con Maven (dev)

```bash
./mvnw spring-boot:run
```

La aplicación se inicia en `http://localhost:8080`.

---

## 🔑 Autenticación

Todas las rutas bajo `/api/v1/**` requieren cabecera **Authorization: Basic** con las credenciales por defecto:

```text
Username: admin
Password: admin
```

Puedes cambiar estos valores en `application.yml` o mediante variables de entorno.

---

## 📌 Endpoints principales

| Método | Ruta                  | Descripción                  |
| ------ | --------------------- | ---------------------------- |
| `GET`  | `/api/v1/people`      | Lista paginada de personajes |
| `GET`  | `/api/v1/people/{id}` | Detalle por ID               |
| `GET`  | `/api/v1/films`       | Películas                    |
| `GET`  | `/api/v1/starships`   | Naves espaciales             |
| `GET`  | `/api/v1/vehicles`    | Vehículos                    |
| `GET`  | `/api/v1/planets`     | Planetas                     |

Para filtrar por nombre: `GET /api/v1/people?name=Leia`.

---

## 📝 Licencia

Distribuido bajo la licencia **MIT**. Consulta `LICENSE` para más información.
