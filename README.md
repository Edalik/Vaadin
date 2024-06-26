## Запуск приложения

Перед запуском необходимо в файле `.env` указать значения переменных.
Репозиторий является стандартным Maven проектом. Для запуска из командной строки,
введите `mvnw` (Windows) или `./mvnw` (Mac & Linux), затем откройте
http://localhost:8081 в браузере.

## Деплой в prod

Для создания релизной сборки используйте `mvnw clean package -Pproduction` (Windows),
или `./mvnw clean package -Pproduction` (Mac & Linux).
Эта команда соберёт .jar-библиотеку и все frontend зависимости и проект будет готов к развёртыванию.
Скомпилированные файлы будут находиться папке `target`.

## Деплой с использованием Docker

Для этого выполните команды:

```
mvn clean package -Pproduction
docker compose up
```

После запуска сервер будет доступен по адресу http://localhost
