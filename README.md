# 🔍 App Short URL
![Imagen del proyecto](https://raw.githubusercontent.com/victorgranadosjimenez/app_mkm/refs/heads/master/Captura.JPG?raw=true)


## Ejemplo en vivo
https://victorgranados.com/app_mkm/


## Descripción 📑
App MKM es una aplicación que permite crear alertas automáticas para cartas de Magic: The Gathering listadas en Cardmarket.
El usuario define condiciones personalizadas (nombre, set, idioma, país, precio, condición mínima, etc.), y el sistema revisa Cardmarket automáticamente una vez al día.
Si se encuentra una carta que cumple los criterios, la app envía un correo electrónico de aviso mediante Brevo (Sendinblue).
La app también guarda un historial de alertas exitosas, permitiendo revisar un gráfico con los precios por los que ha pasado cada carta.

IMPORTANTE: La app actualmente está diseñada para que endpoints "sensibles" como pueden ser el de crear las alertas o gestionarlas (borrarlas) solo se accedan tras logearse el usuario, el cual hasta ahora solo está activo el mio.
Otros endpoints si son accesibles como el historial de alertas o los gráficos de precio a lo largo del tiempo de cada carta.





## 🧱 Arquitectura del proyecto
ENTIDADES:
Alert para guardar las alertas de los usuarios
AlertHistory para guardar un historial de alertas

SERVICES:
CardmarketScraperService que obtiene listados de cartas desde el html de Cardmarket
Se filtran listados según condición, precio, país y idioma

SCHEDULER:
AlertChecker ejecuta la comprobación periódica de alertas (una vez al día)
Se filtraron los listados según los criterios de la alerta
Se guarda el historial y se envía un correo al usuario si se detecta un match

💻 Frontend
El frontend es una interfaz sencilla desarrollada con HTML, CSS y JavaScript, que interactúa con el backend mediante fetch.
Formulario HTML para crear alertas con Select de idiomas y Select de sets legales del formato Premodern de MTG.
Validación básica de campos
Uso de fetch para enviar alertas al backend




⚙️ Cómo funciona
1. El usuario crea la alerta de la carta, set, condición, precio máximo, país y uno o más idiomas.

2. La alerta se guarda en la base de datos.

3. Comprobación automática:
Cada día a las 08:00 AM, el backend ejecuta AlertChecker.
Se buscan listados en Cardmarket que cumplan todos los criterios.

4. Si se encuentra un match se guarda en AlertHistory y se envía un correo al usuario.

5. Cada alerta activada se registra con un snapshot de los datos de la alerta y el precio del listado.

6. Permite revisar alertas previas sin depender de los correos enviados.




## Tecnologías 🛠
[![JAVA](https://img.shields.io/badge/Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://es.wikipedia.org/wiki/Java_(lenguaje_de_programaci%C3%B3n))
[![SPRINGBOOT](https://img.shields.io/badge/SpringBoot-6DB33F?style=flat-square&logo=Spring&logoColor=white)](https://en.wikipedia.org/wiki/Spring_Boot)
[![MYSQL](https://img.shields.io/badge/-SQL-000?&logo=MySQL&logoColor=4479A1)](https://en.wikipedia.org/wiki/MySql)
[![JAVASCRIPT](https://shields.io/badge/JavaScript-F7DF1E?logo=JavaScript&logoColor=000&style=flat-square)](https://en.wikipedia.org/wiki/JavaScript)

Backend: Java + Spring Boot
Base de datos: MySQL (JPA / Hibernate)
Frontend: HTML + JavaScript (fetch API)
Automatización: Spring @Scheduled para comprobación periódica de alertas
Servicios externos: Cardmarket (scraper), correo electrónico para notificaciones


## Vista previa del proyecto
Si quieres hechas un vistazo al proyecto, te recomiendo:

![Imagen del proyecto](https://raw.githubusercontent.com/victorgranadosjimenez/app_mkm/refs/heads/master/Captura.JPG?raw=true)
![Imagen del proyecto](https://raw.githubusercontent.com/victorgranadosjimenez/app_mkm/refs/heads/master/Captura2.JPG?raw=true)
![Imagen del proyecto](https://raw.githubusercontent.com/victorgranadosjimenez/app_mkm/refs/heads/master/Captura3.JPG?raw=true)
![Imagen del proyecto](https://raw.githubusercontent.com/victorgranadosjimenez/app_mkm/refs/heads/master/Captura4.JPG?raw=true)




## Autor ✒️
VÍCTOR GRANADOS JIMÉNEZ

<img src="https://avatars.githubusercontent.com/u/57761479?v=4" width=115><br>

* [Portafolio](https://victorgranados.com/)
* [Perfil Github](https://github.com/victorgranadosjimenez)
* [Correo](granadosvictor01@gmail.com)
* [LinkedIn](www.linkedin.com/in/victorgranadosjimenez/)



## Instalación
Este proyecto no necesita de instalación. Simplemente abre la carpeta o haz doble click en el .html



## Licencia 📄
MIT Public License v3.0
No puede usarse comencialmente.
