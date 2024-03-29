## Курсовой проект - Сервис перевода денег
### Описание проекта

Данное приложение представляет собой серверную часть сервиса перевода денег с карты на карту.  
Разработана на языке Java с помощью фреймворка Spring Boot.   

### Описание серверной части
+ Серверная часть представляет собой REST API сервис, реализующий методы перевода с одной карты на другую
в соответствии с [протоколом](https://github.com/netology-code/jd-homeworks/blob/master/diploma/MoneyTransferServiceSpecification.yaml).  
+ Все изменения (включая ошибки) записываются в лог файл с указанием всей необходимой информации  

### Функционал серверной части
+ Перевод денег с карты на карту  
+ На сервере имеется имитация базы данных (в виде потокобезопасной коллекции) с данными банковских карт, 
в связи с чем реализован функционал валидации:  
1. Номера банковской карты отправителя
2. Срока действия и CVV кода карты
3. Недостаточной суммы денег для отправления  
4. Отсутствия счета в необходимой валюте
+ Обработка ошибок подтверждения операции (включая неверный код верификации и сам номер операции)

### Используемый стэк
+ Spring Boot
+ Gradle
+ Docker, Docker Compose
+ Lombok
+ Logback

### Тесты
Для основных функциональных модулей написаны тесты с использованием:
+ JUnit
+ Mockito
+ Hamcrest
+ Truth
+ TestContainers (интеграционные тесты)

### Запуск приложения 
В терминале, находясь в корневой директории проекта, необходимо ввести команду:  
```docker-compose up```  
В результате в соответствии с Dockerfile`ом будет создан и запущен контейнер серверной части 
по адресу http://localhost:5500/  

Теперь можно отправлять запросы с фронта:
+ либо с https://serp-ya.github.io/card-transfer/
+ либо локально развернуть свою копию фронта, предварительно прописав в файле .env строку REACT_APP_API_URL=http://localhost:5500

### Примеры запросов
На сервере содержится информация о следующих банковских картах:  
| № карты отправителя | Срок действия | CVV | Суммы на счете |  
|---|---|---|---|  
| 1111222233334444 | 01/30 | 111 | 50000 RUR |  
| 9999888877776666 | 10/25 | 222 | 100000 EUR |  
| 1234567890123456 | 05/27 | 333 | 10000 USD |
| 1122334455667788 | 03/31 | 444 | 150000 RUR, 20000 EUR |  
| 9876543210987654 | 08/24 | 555 | 250000 RUR, 5000 USD, 1000 EUR |  

Можно делать запросы, полагаясь на эту информацию.  

### Логи приложения
Чтобы просмотреть логи из контейнера серверной части приложения, можно скопировать их,
введя команду:  
```docker cp <id контейнера>:/app/log/history.log .```

Enjoy!
