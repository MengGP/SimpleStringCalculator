package com.menggp.simplestringcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;

/*
   Класс - MainActivity
 */
public class MainActivity extends AppCompatActivity {

    private static final String LOG_TAG = "MainActivity";

    View errorPanel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        errorPanel = findViewById(R.id.errorPanel);
        errorPanel.setVisibility(View.GONE);

    } // end_method

    // обработчик нажатия кнопки - "рассчитать"
    public void calculate(View view) {

        errorPanel = findViewById(R.id.errorPanel);
        EditText sourceText = (EditText) findViewById(R.id.sourceText);
        TextView resultText = (TextView) findViewById(R.id.resultText);
        TextView errorString = (TextView) findViewById(R.id.errorString);

        String sourceString = sourceText.getText().toString();

        // Создаем объект исходных данные - в конструкторе вызываются проверки строки и разбор на токены
        StringHandler data = new StringHandler(sourceString);

        // Проверяем введенную строку на синтаксическую корректность
        int checkSrtResult = data.getCheckResult();
        errorPanel.setVisibility(View.VISIBLE);
        switch (checkSrtResult) {
            case 1:
                errorString.setText(R.string.zero_lenght_string);
                return;
            case 2:
                errorString.setText(R.string.illegal_symbol_error);
                return;
            case 3:
                errorString.setText(R.string.bad_operand);
                return;
            case 4:
                errorString.setText(R.string.bad_bracket);
                return;
            case 5:
                errorString.setText(R.string.bad_operator);
                return;
            default:
                errorPanel.setVisibility(View.GONE);
        } // end-switch

        try {
            PolishCalc calcResult = new PolishCalc( data );

        } catch ( PolishCalc.PolishCalcException ex ) {
            errorPanel.setVisibility(View.VISIBLE);
            errorString.setText(R.string.bad_general);
            return;
        } catch ( PolishCalc.PolishCalcExceptionTernar ex) {
            errorPanel.setVisibility(View.VISIBLE);
            errorString.setText(R.string.bad_ternar);
            return;
        }

        resultText.setText("GOOD EXPRESSION!");










    } // end_method

} // end_class
