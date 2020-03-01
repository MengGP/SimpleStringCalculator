package com.menggp.simplestringcalculator;

import android.util.Log;

import java.util.ArrayList;

public class StringHandler {


    private static final String LOG_TAG = "StringHandler";

    private String sourceStr;
    private ArrayList<String> parsedStringList;

    // метод проверяет исходную строку на недопустимые сымволы
    public static boolean symbolsCheck(String str) {
        /* Список допустимых символов:
            Числа: 0 1 2 3 4 5 6 7 8 9
            Знаки разделени целой и дробной части: , .
            Знаки унарнах и бинарных операций: + - *(в двух вариантах) /(в двух вариантах)
            Знаки тернарного оператора: ? : > >= < <=
            Знаки скобок: ( )
            Пробел и Enter: ASCII 10, 32

            Проверяем - длина строки должна быть равна количеству легитимных символов в ней,
            проверяем ASCII коды символов:
                40..63 - кроме 59
                10, 32, 215, 247
         */
        int legalSymbolCounter = 0;
        outer:
        for(char iter : str.toCharArray() ) {
            for (int i=40; i<=63; i++) {
                if ( (iter == i) && (iter != 59) ) {
                    legalSymbolCounter++;
                    continue outer;
                }
            }
            if ( (iter == 247) || (iter == 215) || (iter == 32) || (iter == 10) )
                legalSymbolCounter++;
        } // end_outer_for

        if ( legalSymbolCounter != str.length() ) return false;
        return true;
    } // end_method

    // метод проверяет операнды в строке (наличие дробной части после разделителя, единственный разделитель на целую и дробную часть)
    public static boolean operandCheck(String str) {
        /*
            Первая проверка - наличие дробной части после разделителя
                - после знака разделителя должно быть число, проверяем по ASCII коду
            Вторая проверка - единственная дробная часть в операнде
                , = 44 (ASCII)
                . = 46 (ASCII)
                0..9 = 48..57
         */

        char[] charArray = str.toCharArray();

        // проверка 1 - наличие дробной части после разделителья
        outer:
        for (int i=0; i<charArray.length; i++) {
            if ( ( charArray[i] == 44) || ( charArray[i] == 46) ) {
                for (int j=48; j<=57; j++) {
                    try {
                        if (charArray[i+1] == j) continue outer;
                    } catch (ArrayIndexOutOfBoundsException ex) {
                       // для случая если разделитель стоит в конце строки
                       return false;
                    }
                }
                return false;
            }
        } // end_outer_for

        // проверка 2 - едиственность дробной части в операндах
        int delimiterCnt = 0;
        outer:
        for (int i=0; i<charArray.length; i++) {
            if ( delimiterCnt > 1 ) return false;

            if ( ( charArray[i] == 44) || ( charArray[i] == 46) ) {
                delimiterCnt++;
                continue;
            }

            for (int j=48; j<=57; j++) {
                if ( charArray[i] == j ) continue outer;
            }

            delimiterCnt = 0;
        }  // end_outer_for

        return true;
    } // end_method

    // метод проверяет корректность расстановки скобок - согласованность открытых и закрытых скобок
    public static boolean bracketCheck(String str) {
        /*
            ( = 40 (ASCII)
            ) = 41 (ASCII)
         */

        int bktPairCounter = 0;
        for (char iter : str.toCharArray() ) {
            if ( (bktPairCounter < 1) && (iter == 41) ) return false;
            if ( iter == 40 ) bktPairCounter++;
            else if ( iter == 41 ) bktPairCounter--;
        }
        if ( bktPairCounter > 0 ) return false;

        return true;
    } // end_method

    // метод проверяет расстановку операторов - простая синтаксическая проверка
    public static boolean operatorCheck(String str) {
        /*
          ASCII:
            - = 45          - унарный или бинарный оператор
            * = 42 / 215    - бинарный оператор
            + = 43          - бинарный оператор
            / = 47 / 247    - бинарный оператор
            ? = 63          - часть тернарного оператора
            : = 58          - часть тернарного оператора
            < = 60          - часть тернарного оператора
            > = 62          - часть тернарного оператора
            = = 61          - часть тернарного оператора
            ------
            , = 44
            . = 46
            0..9 = 48..57
            ( = 40
            ) = 41
         */

        // Удаляем из строки символы "Space" и "Enter"
        char chSpace = 32;
        char chEnter = 10;
        str = str.replaceAll(Character.toString(chSpace), "");
        str = str.replaceAll(Character.toString(chEnter),"");

        char[] charArray = str.toCharArray();

        outer:
        for (int i=0; i<charArray.length; i++) {
            // проверка оператора для которого допустимы унарные операции: "-"
            if ( charArray[i] == 45 ) {
                try {
                    if ((charArray[i+1] == 44) || (charArray[i+1] == 46) || (charArray[i+1] == 40))
                        continue;   // дробное число начиная со знака разделителя или скобка
                    for (int j = 48; j <= 57; j++) {
                        if (charArray[i+1] == j) continue outer;
                    }
                    return false;
                } catch (ArrayIndexOutOfBoundsException ex ) {
                    return false;   // если после оператора строка заканчивается
                }
            }

            // проверяем бинарные операторы: *, +, /
            if ( (charArray[i] == 42) || (charArray[i] == 215) || (charArray[i] == 43) || (charArray[i] == 47) || (charArray[i] == 247) ) {
                // предыдущий символ: число или ")"
                // следующий: число или: ",", ".", "("
                try {
                    boolean symbolBefore = false;
                    boolean symbolAfter = false;
                    // проееряем предыдущий символ
                    if ( charArray[i-1] == 41 ) symbolBefore = true;
                    else {
                        for (int j = 48; j <= 57; j++) {
                            if ( charArray[i-1] == j ){
                                symbolBefore = true;
                                break;
                            }
                        }
                    }
                    // проверяем следующий символ
                    if ( (charArray[i+1] == 40) || (charArray[i+1] == 44) || (charArray[i+1] == 46) ) symbolAfter = true;
                    else {
                        for (int j = 48; j <= 57; j++) {
                            if ( charArray[i+1] == j ){
                                symbolAfter = true;
                                break;
                            }
                        }
                    }
                    // получаем итог
                    if ( !symbolBefore || !symbolAfter ) return false;

                } catch (ArrayIndexOutOfBoundsException ex ) {
                    return false; // если отсутствует символ перед или после оператора
                }
            }

            /* Проверки для тернарного оператора
                Символы: ? :
                    перед: 0..9 )
                    после: 0..9 - . , (
                Символы: > <
                    перед: 0..9 )
                    после: 0..9 - . , ( =
                Символ: =
                    перед: 0..9 ) > <
                    после: 0..9 - . , (
             */
            if ( (charArray[i] == 63) || (charArray[i] == 58) || (charArray[i] == 60) || (charArray[i] == 62) || (charArray[i] == 61) ) {
                try {
                    boolean symbolBefore = false;
                    boolean symbolAfter = false;

                    // проверяем символ ПЕРЕД
                    // символ ПЕРЕД равный ")" верен для всех частей тернарногооператора
                    if ( charArray[i-1]==41 ) symbolBefore=true;
                    // для символа "=" ПЕРЕД могут находится символы: > <
                    else if ( (charArray[i]==61) && (charArray[i-1]==60 || charArray[i-1]==62) ) symbolBefore=true;
                    // для всех частей тернарного оператора - число
                    else {
                        for (int j = 48; j <= 57; j++) {
                            if ( charArray[i-1] == j ){
                                symbolBefore = true;
                                break;
                            }
                        }
                    }

                    // проверяем символ ПОСЛЕ
                    // для всех ПОСЛЕ могут находиться символы: - . , (
                    if ( (charArray[i+1] == 40) || (charArray[i+1] == 44) || (charArray[i+1] == 46) || (charArray[i+1] == 45) ) symbolAfter=true;
                    // для символов ">" и "<" ПОСЛЕ могут находится символ: =
                    else if ( (charArray[i]==60 || charArray[i]==62) && (charArray[i+1]==61) ) symbolAfter=true;
                    // для всех частей тернарного оператора - число
                    else {
                        for (int j = 48; j <= 57; j++) {
                            if ( charArray[i+1] == j ){
                                symbolAfter = true;
                                break;
                            }
                        }
                    }

                    // получаем итог
                    if ( !symbolBefore || !symbolAfter ) return false;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    return false; // если отсутствует символ перед или после оператора
                }
            }

        } // end_outer_for


        return true;
    } // end_method


} // end_class
