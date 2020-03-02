# Тестовое задание на стажироваку ЦФТ Focus Start - Android (Томск)

Испольнитель: Усцов Олег.

Задача: простой строковый калькулятор.

Исполнение: приложение для Android <br>
Минимальная поддерживаемая версия - Android 5.0 (minSDK=21, targetSDK=26) <br>
Выполнено в Android Studio 3.5, язык Java

    Общий алгоритм реализации:
    ----------------------------
        1. Разбор строки:
            - проверка на недопустимые сиволы
            - проверка операндов (наличие дробной части после разделителя, единственный разделитель на целую и дробную часть)
            - проверка расстановки скобок - согласованность открытых и закрытых скобок        
            - проверка расстановки операторов - простая синтаксическая проверка
            - преобразование строки в массив строк из операторов и операндов
        2. Прреобразование в ОПЗ (обратную польскую запись) - алгоритм сортировочной станции
        3. Вычисление - стековая машина 


