# 📚 Sistema de Gestión de Biblioteca

Un sistema de consola desarrollado en Java para administrar de forma eficiente los recursos y usuarios de una biblioteca. El proyecto implementa el patrón de arquitectura **MVC (Modelo-Vista-Controlador)** junto con **DAO (Data Access Object)** para el manejo de datos y persistencia en archivos de texto plano.

## 🚀 Características Principales

* **CRUD Completo:** Gestión total de entidades maestras (Libros, Lectores, Bibliotecarios) y entidades relacionales (Préstamos, Asignaciones).
* **Persistencia Automática:** Los datos se almacenan de forma persistente en archivos `.txt` en formato separado por comas. El sistema genera estos archivos de forma automática si no existen en la primera ejecución.
* **Autogeneración de IDs Inteligente:** Creación automática de identificadores alfanuméricos con formato específico (ej. `L0001`, `LEC0014`, `B0006`) utilizando un archivo de control histórico (`idshistoricos.txt`).
* **Normalización de Datos:** Todo el texto ingresado por el usuario se normaliza automáticamente a MAYÚSCULAS para evitar duplicados y errores de búsqueda.
* **Módulo de Consultas Avanzadas:** Búsquedas cruzadas que permiten visualizar:
  * El libro específico que tiene prestado un lector.
  * La lista de lectores que atiende un bibliotecario.
  * El bibliotecario asignado a un lector específico.
* **Validaciones Robustas:** Prevención de errores en tiempo de ejecución, bloqueando operaciones de modificación, eliminación o consulta si los registros están vacíos.


## 🏗️ Arquitectura del Sistema

El proyecto está dividido en capas bien definidas para garantizar un código limpio y escalable:

* **Views (Vistas):** Interfaz de consola interactiva. Muestran los menús y capturan los datos del usuario usando la clase `Mostrar` para unificar el diseño.
* **Controllers (Controladores):** Orquestan la lógica de negocio, comunicando las Vistas con los DAOs.
* **DAOs (Data Access Objects):** Capa de acceso a datos que implementa la interfaz genérica `ICrud`. Se encargan exclusivamente de leer, escribir y crear los archivos `.txt` en caso de que no existan.
* **DTOs (Data Transfer Objects):** Objetos utilizados para transportar la información de forma segura entre las diferentes capas del sistema.


## 📁 Estructura de Archivos

El sistema administra los siguientes archivos en el directorio raíz del proyecto (se autogeneran en caso de no existir):

* `libros.txt`
* `lectores.txt`
* `bibliotecarios.txt`
* `librosidshistoricos.txt`
* `lectoresidshistoricos.txt`
* `bibliotecariosidshistoricos.txt`


## ⚙️ Cómo ejecutar el proyecto

1. Clonar el repositorio.
2. Abrir el proyecto en tu IDE de preferencia.
3. Compilar y ejecutar la clase `Main`.
4. ¡Listo! El sistema se encargará de inicializar la estructura de archivos necesaria y podrás empezar a operar de inmediato.