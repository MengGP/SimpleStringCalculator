package com.menggp.simplestringcalculator;

import android.util.Log;

import java.util.ArrayList;

public class StringHandler {


    private static final String LOG_TAG = "StringHandler";

    private String sourceStr;
    private ArrayList<String> parsedStringList;

    public StringHandler(String sourceStr) {
        this.sourceStr = sourceStr;

        // исключаем из исходной строки
        // Удаляем из строки символы "Space" и "Enter"
        char chSpace = 32;
        char chEnter = 10;
        sourceStr = sourceStr.replaceAll(Character.toString(chSpace), "");
        sourceStr = sourceStr.replaceAll(Character.toString(chEnter),"");

    } // end_constructor

    public ArrayList<String> getParsedStringList() {
        return parsedStringList;
    } // end_getter

    // метод проводит комплексную сиснетаксическую проверку строки и возвращеет код ошибки
    public static int complexCheck(String str) {
        /*
            -1 - нет ошибок
            1 - пустая строка
            2 - недопустимые сомволы
            3 - некорректыне операнды
            4 - несогласованные скобки
            5 - некорректно расставлены операторы
         */

        if ( !zeroLenghtCheck( str ) )   return 1;
        if ( !symbolsCheck( str ) )      return 2;
        if ( !operandCheck( str ) )      return 3;
        if ( !bracketCheck( str ) )      return 4;
        if ( !operatorCheck( str ) )     return 5;

        return -1;
    } // end_method

    // метод проверяет длину строки
    private static boolean zeroLenghtCheck(String str) {
        if ( str.length() < 1 ) return false;
        return true;
    } // end_method

    // метод проверяет исходную строку на недопустимые сымволы
    private static boolean symbolsCheck(String str) {
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
    private static boolean operandCheck(String str) {
        /*
            Первая проверка - наличие дробной части после разделителя
                - после знака разделителя должно быть число, проверяем по ASCII коду
            Вторая проверка - единственная дробная часть в операнде
                , = 44 (ASCII)
                . = 46 (ASCII)
                0..9 = 48..57
         */

        char[] charArray = str.toCharArray();

        // проверка 1 - наличие дробной части после разделителя
        for (int i=0; i<charArray.length; i++) {
            if ( ( charArray[i] == 44) || ( charArray[i] == 46) ) {
                try {
                    if ( !Character.isDigit(charArray[i+1]) ) return false;
                } catch(ArrayIndexOutOfBoundsException ex) {
                    return false; // для случая если разделитель стоит в конце строки
                }
            }
        } // end_outer_for

        // проверка 2 - едиственность дробной части в операндах
        int delimiterCnt = 0;
        outer:
        for (int i=0; i<charArray.length; i++) {
            if ( delimiterCnt > 1 ) return false;
            else if ( ( charArray[i] == 44) || ( charArray[i] == 46) ) delimiterCnt++;
            else if ( Character.isDigit(charArray[i]) ) continue;
            else delimiterCnt = 0;
        }  // end_outer_for

        return true;
    } // end_method

    // метод проверяет корректность расстановки скобок - согласованность открытых и закрытых скобок
    private static boolean bracketCheck(String str) {
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
    private static boolean operatorCheck(String str) {
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

        for (int i=0; i<charArray.length; i++) {
            // проверка оператора для которого допустимы унарные операции: "-"
            if ( charArray[i] == 45 ) {
                try {
                    if ((charArray[i+1] == 44) || (charArray[i+1] == 46) || (charArray[i+1] == 40))
                        continue;   // дробное число начиная со знака разделителя или скобка
                    else if ( Character.isDigit(charArray[i+1]) ) continue;
                    else return false;
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
                    else if ( Character.isDigit(charArray[i-1]) ) symbolBefore = true;
                    // проверяем следующий символ
                    if ( (charArray[i+1] == 40) || (charArray[i+1] == 44) || (charArray[i+1] == 46) ) symbolAfter = true;
                    else if ( Character.isDigit(charArray[i+1]) ) symbolAfter = true;

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
                    else if ( Character.isDigit(charArray[i-1]) ) symbolBefore = true;

                    // проверяем символ ПОСЛЕ
                    // для всех ПОСЛЕ могут находиться символы: - . , (
                    if ( (charArray[i+1] == 40) || (charArray[i+1] == 44) || (charArray[i+1] == 46) || (charArray[i+1] == 45) ) symbolAfter=true;
                    // для символов ">" и "<" ПОСЛЕ могут находится символ: =
                    else if ( (charArray[i]==60 || charArray[i]==62) && (charArray[i+1]==61) ) symbolAfter=true;
                    // для всех частей тернарного оператора - число
                    else if ( Character.isDigit(charArray[i+1]) ) symbolAfter = true;

                    // получаем итог
                    if ( !symbolBefore || !symbolAfter ) return false;
                } catch (ArrayIndexOutOfBoundsException ex) {
                    return false; // если отсутствует символ перед или после оператора
                }
            }

        }

        return true;
    } // end_method

    // метод проверяет является ли символ числом

} // end_class
