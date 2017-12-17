[![Build Status](https://travis-ci.org/Andrianov96/MyLife.-Purchases.svg?branch=master)](https://travis-ci.org/Andrianov96/MyLife.-Purchases)
[![codecov](https://codecov.io/gh/Andrianov96/MyLife.-Purchases/branch/master/graph/badge.svg)](https://codecov.io/gh/Andrianov96/MyLife.-Purchases)

# MyLife.-Purchases
## MyLife.Puchases - сервис для хранения информации о сделанных покупках 
Покупки вводятся вручную

После введения покупок, их можно посмотреть, а также выбрать часть из них по некоторым критериям (в данный момент критериев очень мало и работают не всегда нормально)

## Установка
install.sh - скачивание необходимых библиотек и создание базы данных.

## Запуск
run.sh

## Структура
HTML page + JS -> API (Scala) -> DB -> API (Scala) -> HTML page + JS

## Использованные технологии
1. HTML - веб-страница
2. jQuery - формирование json объекта из данных с HTML страницы
3. Akka HTTP(Scala) - обработка запросов на сервере
4. ScalikeJDBC - работа с базой данных
5. Mysql - работа с бд

## Ограничения
В данный момент сервис не масштабируется (нельзя запустить больше одной рабочей копии)
Пока нельзя создавать новых пользователей через веб-страницу. Есть два тестовых с логином и паролем - (loginv, passwordv) (loginv1, passwordv1)

# Copyright

Copyright © 2017 Andrianov Georgiy


