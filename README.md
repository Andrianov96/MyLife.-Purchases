[![Build Status](https://travis-ci.org/Andrianov96/MyLife.-Purchases.svg?branch=master)](https://travis-ci.org/Andrianov96/MyLife.-Purchases)
[![codecov](https://codecov.io/gh/Andrianov96/MyLife.-Purchases/branch/master/graph/badge.svg)](https://codecov.io/gh/Andrianov96/MyLife.-Purchases)

# MyLife.-Purchases
## MyLife.Puchases - сервис для хранения информации о сделанных покупках 
Покупки вводятся вручную

После введения покупок, их можно посмотреть, а также выбрать часть из них по некоторым критериям (в данный момент критериев очень мало и работают не всегда нормально)

## Установка
install.sh

## Запуск
run.sh

## Структура
HTML page + JS -> API (Scala) -> DB -> API (Scala) -> HTML page + JS

## Использованные технологии
1. HTML - веб-страница
2. jQuery - формирование json объекта из данных с HTML страницы
3. Akka HTTP(Scala) - обработка запросов на сервере
4. ScalikeJDBC - работа с базой данных

## Поддерживаемые технологии
В данный момент работа возможна только в браузере Internet Explorer, более современные браузеры пока не поддерживаются.

(UPDATE) Добавлена поддержка современных браузеров, однако в данный момент для их использования необходимо в системных файлах в ручную указать соответсвие между localhost.com и 127.0.0.1 (поддержка добавлена в ветке 'Linux').

# Copyright

Copyright © 2017 Andrianov Georgiy


