[![Build Status](https://travis-ci.org/Andrianov96/MyLife.-Purchases.svg?branch=master)](https://travis-ci.org/Andrianov96/MyLife.-Purchases)
[![codecov](https://codecov.io/gh/Andrianov96/MyLife.-Purchases/branch/master/graph/badge.svg)](https://codecov.io/gh/Andrianov96/MyLife.-Purchases)

# MyLife.-Purchases
## MyLife.Puchase - сервис для хранения информации о сделанных покупках 
Покупки вводятся вручную

Можно посмотреть уже сделанные покупки и выбрать часть из них по некоторым критериям (в данный момент критериев очень мало)

## Структура
HTML page + JS -> API (Scala) -> DB -> API (Scala) -> HTML page + JS

## Использованные технологии
1. HTML - веб-страница
2. jQuery - формирование json объекта из данных с HTML страницы
3. Akka HTTP(Scala) - обработка запросов на сервере
4. ScalikeJDBC - работа с базой данных

# Copyright

Copyright © 2017 Andrianov Georgiy


