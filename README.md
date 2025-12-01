# Music Playlist Manager (Dynamic Version)

This repository contains the dynamic version of the music playlist manager project for the Technologies for Web Applications course at Politecnico di Milano. The project implements a music playlist manager with a MySQL database, a Java backend using JDBC, and a dynamic frontend built with JSP and JavaScript.

---

## Overview

The goal of this project was to implement a music playlist manager with a dynamic frontend using JSP and JavaScript. The backend is written in Java and connects to a MySQL database via JDBC. The site's style is managed using CSS.

---

## Project Structure

- `PlaylistMusicaleHTML/src/main/java/`: Contains the Java backend code and JDBC connection logic.
  - `beans/`: Contains Java classes representing data models.
  - `dao/`: Contains Data Access Object classes for database operations.
  - `controllers/`: Contains servlet classes handling HTTP requests.
- `PlaylistMusicaleHTML/src/main/webapp/`: Contains the frontend of the web application.
  - `WEB-INF/`: Contains JSP files and the `web.xml` deployment descriptor.
  - `resources/script/`: Contains the JavaScript files for dynamic frontend functionality.
  - `resources/style/`: Contains the CSS files for styling the site.
- `Deliverables/`: Contains the project documentation.

---

## How to Run the Project

1. Set up a MySQL database and create the tables as described in the project documentation.
2. Compile and run the Java backend.
3. Deploy the JSP files on a compatible web server.
4. Access the application through the web browser.

---

## Documentation

- [Project Documentation](Deliverables/Documentation.pdf)

---

## Technologies

- Java
- JDBC
- MySQL
- JSP
- JavaScript
- CSS

---

## License

This project is released under the MIT License.

---

## Notes

This project was developed as part of a university course in web applications. The code and documentation are provided for educational purposes.
