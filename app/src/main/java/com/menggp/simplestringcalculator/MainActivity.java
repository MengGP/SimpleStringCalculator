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
        EditText sourceString = (EditText) findViewById(R.id.sourceString);
        TextView resultString = (TextView) findViewById(R.id.resultString);
        TextView errorString = (TextView) findViewById(R.id.errorString);

        // Проверяем есть ли в строке хоть один символ
        if ( sourceString.getText().toString().length() > 0 ) {
            errorPanel.setVisibility(View.GONE);
        } else {
            errorPanel.setVisibility(View.VISIBLE);
            errorString.setText(R.string.zero_lenght_string);
            return;
        }

        // Проверяем строку на недопустимые символы
        if ( Calculator.symbolsCheck( sourceString.getText().toString() )) {
            errorPanel.setVisibility(View.GONE);
        } else {
            errorPanel.setVisibility(View.VISIBLE);
            errorString.setText(R.string.illegal_symbol_error);
            return;
        }












    } // end_method

} // end_class
