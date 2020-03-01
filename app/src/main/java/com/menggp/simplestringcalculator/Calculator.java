package com.menggp.simplestringcalculator;

import android.util.Log;

import java.util.ArrayList;

public class Calculator {

    private static final String LOG_TAG = "Calculator";

    private String sourceStr;
    private ArrayList<String> parsedStringsList;

    // метод проверяющий исходную строку на недопустимые сымволы
    public static boolean symbolsCheck(String str) {
        /* Список допустимых символов:
            Числа: 0 1 2 3 4 5 6 7 8 9
            Знаки разделени целой и дробной части: , .
            Знаки унарнах и бинарных операций: + - *(в двух вариантах) /(в двух вариантах)
            Знаки тернарного оператора: ? : > >= < <=
            Знаки скобок : ( )
            Пробел

            Проверяем - длина строки должна быть равна количеству легитимных символов в ней,
            проверяем ASCII коды символов:
                40..63 - кроме 59
                32, 215, 247
         */
        int legalSymbolCounter = 0;
        outer:
        for(char iter : str.toCharArray() ) {
            for (int i=40; i<=63; i++) {
                if ( ((int)iter == i) && ((int)iter != 59) ) {
                    legalSymbolCounter++;
                    continue outer;
                }
            }
            if ( ((int)iter == 247) || ((int)iter == 215) || ((int)iter == 32) )
                legalSymbolCounter++;
        } // end_outer_for

        if ( legalSymbolCounter != str.length() ) return false;
        return true;
    } // end_method


} // end_class
