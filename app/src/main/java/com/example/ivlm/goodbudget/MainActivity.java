package com.example.ivlm.goodbudget;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class MainActivity extends Activity {
    DatabaseHandler handler;
    Cursor cursor, cursorCheckData;
    //--------------Variable-------------------------
    Button CreateHousehold;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        handler = new DatabaseHandler(this);
        //---------------------set Variable-----------------------------
        CreateHousehold = (Button) findViewById(R.id.CreateNewHousehold);
        CreateHousehold.setTransformationMethod(null);

        //---------------------setOnClickListener-----------------------
        CreateHousehold.setOnClickListener(new Button.OnClickListener(){
            public void onClick(View v){
                startActivity(new Intent(MainActivity.this, Home.class));
            }
        });
        cursor = handler.getCountIncomeData();
        int size = cursor.getCount();
        if(size == 0){
            handler.InsertIncome(1, 500, 0, 500);
        }
        cursor.close();
        cursorCheckData = handler.getCountData();
        int sizes = cursorCheckData.getCount();
        if(sizes == 0){
            handler.InsertData("Groceries", 240, "Monthly");
            handler.InsertData("Gas", 100, "Monthly");
            handler.InsertData("Savings", 100, "Year");
            handler.InsertDataUnallocated(0,"Plus");
        }
        cursorCheckData.close();
        handler.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
