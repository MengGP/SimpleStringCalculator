package com.menggp.simplestringcalculator;

import android.util.Log;

import java.util.ArrayList;

public class Calculator {

    private static final String LOG_TAG = "Calculator";

    private String sourceStr;
    private ArrayList<String> parsedStringsList;

    // метод проверяет исходную строку на недопустимые сымволы
    public static boolean symbolsCheck(String str) {
        /* Список допустимых символов:
            Числа: 0 1 2 3 4 5 6 7 8 9
            Знаки разделени целой и дробной части: , .
            Знаки унарнах и бинарных операций: + - *(в двух вариантах) /(в двух вариантах)
            Знаки тернарного оператора: ? : > >= < <=
            Знаки скобок : ( )
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
    public static boolean operndCheck(String str) {
        /*
            Первая проверка - наличие дробной части после разделителя
                - после знака разделителя должно быть число, проверяем по ASCII коду
            Вторая проверка - единственная дробная часть в операнде
                , = 44 (ASCII)
                . = 46 (ASCII)
                0..9 = 48..57
         */

        // проверка 1 - наличие дробной части после разделителья
        char[] charArray = str.toCharArray();
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


} // end_class
