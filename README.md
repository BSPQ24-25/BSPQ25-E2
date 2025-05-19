# BSPQ25-E2

Welcome to the BSPQ25-E2 project: Deuspotify

## 💻 Members

Ane Arrate, Endika Blanco, Javier Gutierrez, Adrían Román, Asier Loinaz & María Vivar

## 🔥 Features

Deuspotify is an open source music player based on modern Java technologies.

- Create your own user
- Upload your own songs
- Make custom public or private playlists
- Listen in real time to your music
- Suscribe to other user's playlists

## 📦 Requirements

- Java 17 🍵
- MySQL 🐬
- Maven 🪶
- Mockito 🍸
- JaCoCo 📊
- Git 🐈
- Doxygen 📖
- Docker 🐋

## ⚙️ Installation guide

1. Clone this repository

    ```bash
    git clone https://github.com/BSPQ24-25/BSPQ25-E2.git
    cd BSPQ25-E2
2. Run the DB initialization script

    ```bash
    mysql -h 127.0.0.1 -u root -p < src/main/resources/deuspotify-db-setup.sql

3. Run the application using Maven:

    - For a first and clean install of dependencies, run:

     ``` bash
        mvn clean install
     ```

    - For running the application skipping the tests:

     ``` bash
        mvn -DskipTests spring-boot:run
     ```

    - For a full application run with tests:

    ``` bash
        mvn spring-boot:run
    ```

4. Access the application: <http://localhost:8080>
You can also find the application at <http://1e1f2b77-67eb-464f-accf-8d6c93442d98.duckdns.org:8080>

## 🐋 Launch the app with Docker

You can also run the app with one command using Docker.

- To start the container without changes:

    ``` bash
        docker-compose up
    ```

- If you have modified the Dockerfile, dependencies or other configurations:

    ``` bash
        docker-compose up --build 
    ```

- You can also pull the docker image from DockerHub:

    ``` bash
        docker pull endikablanco/deuspotify
    ```

## 📚 Documentation and Reports

You can generate documentation and reports by running the following commands:

1. Generate doxygen reports with `mvn doxygen:report` or `mvn site`
2. Run unit tests and generate coverage report with `mvn test jacoco:report`
3. Run integration tests with `mvn -Pintegration integration-test`
4. Run performance tests with `mvn -Pperformance integration-test`
5. Move performance tests report with `mvn -Pperformance resources:copy-resources@copy-perf-report`

All reports should be visible on the target folder.

### For a full technical documentation and reports, use the following links

- [Doxygen in depth documentation](https://bspq24-25.github.io/BSPQ25-E2/doxygen/html/)
- [Maven site report](https://bspq24-25.github.io/BSPQ25-E2/site/)
- [Jacoco report for test coverage](https://bspq24-25.github.io/BSPQ25-E2/site/jacoco)
- [Performance reports](https://bspq24-25.github.io/BSPQ25-E2/perf/perf-report.html)
- [![Ask DeepWiki](https://deepwiki.com/badge.svg)](https://deepwiki.com/BSPQ24-25/BSPQ25-E2)
