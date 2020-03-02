package com.menggp.simplestringcalculator;

import android.text.TextUtils;
import android.util.Log;

import java.util.ArrayList;
import java.util.Stack;

/*
    Класс реализующий логику вычисления выражения
        - пребразование списка в список в Оратной Польской Записи (ОПЗ) алгоритмом сортировочной станции
        - вычисление выражения в ОПЗ алгоритмом стековой машины
 */
public class PolishCalc {

    private static final String LOG_TAG = "PolishCalc";
//    private static Object PolishCalcException;

    // Выбрасываемое исключение при ошибках Сортировочной станции и Стековой машины
    static class PolishCalcException extends Exception {};          // общее исключение
    static class PolishCalcExceptionTernar extends Exception {};    // ошибка в тернарном операторе


    private StringHandler sourceData;
    private boolean convertResult;
    private float result;
    private ArrayList<String> opzData;      // данные после обработки сортировочной станцией

    public PolishCalc(StringHandler sourceData) throws PolishCalcException, PolishCalcExceptionTernar {
        this.sourceData = sourceData;

        this.opzData = sortMachine( sourceData );


    }

    // Getters and Setters
    public boolean getConvertResult() {
        return convertResult;
    }

    public float getResult() {
        return result;
    }
    // --- end_getters_and_setters

    // Сортировночная машина - преобразует данные в ОПЗ
    private static ArrayList<String> sortMachine(StringHandler data) throws PolishCalcException, PolishCalcExceptionTernar {
        ArrayList<String> opz = new ArrayList<>();
        Stack<String> stack = new Stack<>();
        ArrayList<String> srcDataList = data.getParsedDataList();


        /* Вносим коррекцию в исходную последовательность:
                - в числах заменяем разделитель: "," на "."
                - заменяем символы ASCII=247 на "/"
                - заменяем символы ASCII=215 на "*"
                - унарную операцию "-" преодразуем к бинарной добавлением 0 перед оператором
                - токены ">" + "=" и "<" + "=" оббъединяем в ">=" и "<="
                */
        String currStr = "";
        String prevStr = "";
        for (int i=0; i<srcDataList.size(); i++) {
            currStr = srcDataList.get(i);
            try {
                prevStr = srcDataList.get(i-1);
            } catch (ArrayIndexOutOfBoundsException ex) {
                prevStr = "s";
            }

            if ( isNumber( currStr ) ) srcDataList.set(i, currStr.replace(",", "."));
            else if ( currStr.toCharArray()[0]==215 ) srcDataList.set(i,"*");
            else if ( currStr.toCharArray()[0]==247 ) srcDataList.set(i,"/");
            else if ( currStr.equals("-") && !isNumber(prevStr) ) srcDataList.add(i,"0");
            else if ( currStr.equals("=") && (prevStr.equals(">") || prevStr.equals("<")) ) {
                srcDataList.set(i-1, prevStr+currStr);
                srcDataList.remove(i);
                i--;
            }
        }
        // --- конец блока коррекции данных

//        String tmp="";
//        for (String iter : srcDataList ) tmp+= " " + iter;
//        Log.d(LOG_TAG, tmp);


        // Сортировочная станция
        /*
            Для тернарного оператора:
                - токены: > < >= <= ? :
                - приоритет: выше бинарных операций
                    --- > < >= <=
                    --- ? :
                - при извлечении : из стека - ожидаем в стеке ? (если нет - бросаем исключение)
                    токен ? из стека вынимаем но в итог не заносим - тернарным оператором будет :
                - в остальноv общий алгоритм
         */

        String stackStr = "";
        for (String iter : srcDataList ) {
            if ( isNumber(iter)) opz.add(iter);                                                                 //   num - в данные
            else if ( iter.equals("(")) stack.push(iter);                                                       //   (   - в стек
            else if ( iter.equals(")") )                                                                        //   )   - выгребаем стек до (
                while ( true ) {                                                                                //          или бросаем исключение если выгребли весь
                    if (stack.empty()) {
                        throw new PolishCalcException();
                    }
                    stackStr = stack.pop();
                    if ( stackStr.equals("(") ) break;
                    else opz.add(stackStr);
                }
            else if ( iter.equals("+") || iter.equals("-") ) {                                                  // бинарная операция: + -
                while (true) {
                    if (stack.empty()) {
                        stack.push(iter);
                        break;
                    }
                    if ( stack.peek().equals(">") || stack.peek().equals(">") || stack.peek().equals("<") || stack.peek().equals(">=") || stack.peek().equals("<=") ||
                            stack.peek().equals("?") ) {
                        Log.d(LOG_TAG, " --- catch --- ");
                        throw new PolishCalcExceptionTernar(); }                                                       // если тут токен тернарного оператора - запись неверна
                    else if ( stack.peek().equals(":")) {
                        opz.add( stack.pop() );
                        stackStr = stack.pop();
                        if ( !stackStr.equals("?") ) throw new PolishCalcExceptionTernar();
                    }
                    else if ( stack.peek().equals("*") || stack.peek().equals("/") || stack.peek().equals("+") || stack.peek().equals("-") )
                        opz.add( stack.pop() );
                    else {
                        stack.push(iter);
                        break;
                    }
                }
            }
            else if ( iter.equals("*") || iter.equals("/") ) {                                                  // бинарная операция: * /
                while (true) {
                    if (stack.empty()) {
                        stack.push(iter);
                        break;
                    }
                    if ( stack.peek().equals(">") || stack.peek().equals(">") || stack.peek().equals("<") || stack.peek().equals(">=") || stack.peek().equals("<=") ||
                            stack.peek().equals("?")
                    )
                        throw new PolishCalcExceptionTernar();                                                       // если тут токен тернарного оператора - запись неверна
                    else if ( stack.peek().equals(":")) {
                        opz.add( stack.pop() );
                        stackStr = stack.pop();
                        if ( !stackStr.equals("?") ) throw new PolishCalcExceptionTernar();
                    }
                    else if ( stack.peek().equals("*") || stack.peek().equals("/") )
                        opz.add( stack.pop() );
                    else {
                        stack.push(iter);
                        break;
                    }
                }
            }
            else if ( iter.equals(">") || iter.equals("<") || iter.equals(">=") || iter.equals("<="))           // если оператор сравнения тернарной операции
                stack.push( iter );                                                                             //      - ложим в стек как наиболее приоритетный
            else if ( iter.equals("?") ) {                                                                      // если ? - то в верши стека должен быть оператор сравнения
                stackStr = stack.pop();
                if ( stackStr.equals(">") || stackStr.equals("<") || stackStr.equals(">=") || stackStr.equals("<="))
                    opz.add(stackStr);
                else throw new PolishCalcExceptionTernar();
                stack.push(iter);
            }
            else if (iter.equals(":")) stack.push( iter );                                                      // если : - ложим в стек

            // else
        }
        while (true) {                                                                                             // выгребаем остаток стека
            if ( stack.empty() ) break;
            stackStr = stack.pop();
            if (stackStr.equals("(") || stackStr.equals(")")) throw new PolishCalcException();
            else if ( stackStr.equals(":") ) {
                opz.add(stackStr);
                stackStr = stack.pop();
                if ( !stackStr.equals("?") ) throw new PolishCalcExceptionTernar();
            }
            else opz.add(stackStr);
        }

//        tmp="";
//        for (String iter : opz ) tmp+= " " + iter;
//        Log.d(LOG_TAG, tmp);

        return opz;
    } // end_method

    // Проверяем явеляется ли строка числом
    private static boolean isNumber(String str) {
        /*  ASCII
                 , = 44
                . = 46           */
        for (char iter : str.toCharArray() ) {
            if ( !Character.isDigit(iter) && !(iter==44) && !(iter==46) ) return false;
        }
        return true;
    } // end_method


} // end_class
