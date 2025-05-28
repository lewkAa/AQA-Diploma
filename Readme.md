[![Java CI with Gradle](https://github.com/lewkAa/AQA-Diploma/actions/workflows/gradle.yml/badge.svg)](https://github.com/lewkAa/AQA-Diploma/actions/workflows/gradle.yml)
# Тестирование веб-формы покупки тура



[План тестирования](Docs/Plan.md)

## Начало работы

Склонируте  https://github.com/lewkAa/AQA-Diploma в свой репозиторий, <br/>
затем сделайте git pull, для того чтобы скачать проект на свою машину.

### Prerequisites
Установите следующие приложения:

```
Docker desktop
IntelliJ IDEA
```

### Установка и запуск

Пошаговый процесс установки и запуска:


1. Запустите Docker Desktop
2. Откройте проект в IntelliJ IDEA
3. В терминале IDEA пропишите ```docker compose up``` и дождитесь запуска контейнера.
4. Откройте еще один терминал в IDEA и пропишите <br/> 
```java -jar ./artifacts/aqa-shop.jar -P:jdbc.url="jdbc:mysql://localhost:3306/app" -P:jdbc.user="app" -P:jdbc.password="pass"```
5. Дождитесь запуска сервиса и после, в еще одном терминале пропишите ``` ./gradlew clean test allureReport```
6. Запустятится сборка и автотесты, дождитесь  окончания и пропишите в терминале ```./gradlew allureServe```
7. В веб-браузере откроются результаты прогона тестов в allure.



### Отчетная документация:

[Отчет о тестировании](Docs/Report.md)<br/>
[Отчет об автоматизации](Docs/Summary.md)
