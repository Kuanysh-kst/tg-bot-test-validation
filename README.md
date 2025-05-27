## Подготовка бота
Для того чтобы бот работал корректно нужно будет заполнить файл .env
```shell
BOT_TOKEN=токен бота
BOT_OWNER=id владельца
BOT_USERNAME=username бота
```

## Запуск Postgres
Для работы с базой нужно будет установить image
```shell
docker pull bitnami/postgresql
```

Чтобы запустить контейнеры, указанные в файле, нужно выполнить команду
```shell
docker-compose up
```
## Остановка контейнера
Чтобы остановить контейнер с базой данных, достаточно выполнить команду
```shell
docker-compose down -v
```
из корневой папки проекта

## Запуск проетка 
Запускаете через команду
```shell
./mvnw spring-boot:run
```
## Просмотр базы
в терминале пишете команду
```shell
docker exec -it <id контейнера> bash
psql -U admin -d webapp
```
и вводите пароль admin

## Для управления базы из терминала с помощью bash
psql -U admin -d webtest -h localhost -p 5432

## Работа с ngrok
файл ngrok-v3-stable-windows-amd64.zip находится в корневой папке проекта, его нужно распаковать и запустить, в нем прописываете:
```shell
ngrok http 8080  
```
Полученный адрес нужно скопировать и отпаравить в botfather


