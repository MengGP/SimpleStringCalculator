package com.menggp.simplestringcalculator;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

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

        // Проверяем есть ли в строке хоть один символ
        if ( sourceString.length() > 0 ) {
            errorPanel.setVisibility(View.GONE);
        } else {
            errorPanel.setVisibility(View.VISIBLE);
            errorString.setText(R.string.zero_lenght_string);
            return;
        }

        // Проверяем строку на недопустимые символы
        if ( Calculator.symbolsCheck( sourceString )) {
            errorPanel.setVisibility(View.GONE);
        } else {
            errorPanel.setVisibility(View.VISIBLE);
            errorString.setText(R.string.illegal_symbol_error);
            return;
        }

        // Проверяем строку на корректыне операнды
        if ( Calculator.operandCheck( sourceString )) {
            errorPanel.setVisibility(View.GONE);
        } else {
            errorPanel.setVisibility(View.VISIBLE);
            errorString.setText(R.string.bad_operand);
            return;
        }

        // Проверяем скобки
        if ( Calculator.bracketCheck( sourceString ) ) {
            errorPanel.setVisibility(View.GONE);
        } else {
            errorPanel.setVisibility(View.VISIBLE);
            errorString.setText(R.string.bad_bracket);
            return;
        }











    } // end_method

} // end_class
