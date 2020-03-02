package com.menggp.simplestringcalculator;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.MenuItem;

public class InfoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);

        // настройка Action bar
        ActionBar actionBar = getSupportActionBar();            // получем доступ к action bar
        actionBar.setTitle(R.string.title_info_activity);   // меняем заголовок
        actionBar.setHomeButtonEnabled(true);                   // активируем кнопку "home"
        actionBar.setDisplayHomeAsUpEnabled(true);              // отображаем кнопку "home"

    } // end_method

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case android.R.id.home :
                this.finish();
                return true;
        }

        return super.onOptionsItemSelected(item);
    } // end_method


} // end_class
