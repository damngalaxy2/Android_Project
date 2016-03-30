package com.example.ivlm.goodbudget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class AddEnvelope extends AppCompatActivity {
    DatabaseHandler handler;
    Cursor cursorCheckItem, cursorGetItem, cursorCompare;
    Toolbar toolbar;
    //------------------Variable--------------------
    EditText Amount;
    AutoCompleteTextView EnvelopeName;
    TextView AutoCompleteResult;
    LinearLayout LinearLayoutSaveAddEnvelope;
    Spinner SpinnerType;
    String compare;
    String[] Envelope = {"Allowance","Apartment","AT&T","Auto","Auto Gas","Auto Insurance",
            "Baby","Babysitting","Bills","Blow Money",
            "Cable","Cable & Internet","Car","Car Gas","Car Insurance","Car Loan","Car Payment","Cash","Cell Phone","Cellular South","Chase","Child Support","Childcare","Cigarettes","Clothes","Clothing/Accessories","Coffee","Comcast","Credit Card","Credit Cards",
            "Date Nights","Daycare","Debt Repayment","Dining Out",
            "Eating Out","Education","Electric","Electricity","Entertainment","Extra",
            "Family","Fast Food","Fixed Expenses","Food","Food Eating Out","Groceries","Gas","Food Groceries","Fuel","Fun", "Fun Money",
            "Gasoline","Gifts","Giving","Going Out","Grocery","Gym",
            "Hair","Haircuts","Health & Beauty","Health Insurance","House","House Payment","Household",
            "Insurance","internet",
            "kids",
            "Laundry","Life Insurance","Loan","Loan Payments","Lunch",
            "Medical","Misc","Miscellaneous","Mom","Monthly","Mortgage",
            "Netflix",
            "Other",
            "Personal","Personal Care","Petrol","Pets","Phone","Phone Bills","Power",
            "Rent","Renters Insurance","Restaurants",
            "Satellite TV","School","Shopping","Smokes","Spending","Sprint","Storage","Student Loans",
            "T-Mobile","Telephone","Tithe","Tithing & Offerings","Toiletries","Transportation","Truck",
            "Utilities",
            "Verizon",
            "Water","Weekly Expenses","Work"};
    List<String> item = new ArrayList<String>();
    List<String> type = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_envelope);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2bbf8b"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.reeba_icon_mdpi);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //-------------------setVariable-----------------
        handler = new DatabaseHandler(this);
        EnvelopeName = (AutoCompleteTextView) findViewById(R.id.EnvelopeName);
        EnvelopeName.requestFocus();
        AutoCompleteResult = (TextView) findViewById(R.id.AutoCompleteResult);
        Amount = (EditText) findViewById(R.id.Amount);
        LinearLayoutSaveAddEnvelope = (LinearLayout) findViewById(R.id.linearLayoutSave);
        SpinnerType = (Spinner) findViewById(R.id.SpinnerType);

        //----------------------First statement----------------------------
        cursorCheckItem = handler.getDataEnvelopeCollections();
        int size = cursorCheckItem.getCount();
        if(size == 0){
            for(int i = 0; i<Envelope.length; i++){
                handler.InsertDataSpinnerEnvelope(Envelope[i]);
            }
        }
        cursorCheckItem.close();
        cursorGetItem = handler.getDataEnvelopeCollections();
        while (cursorGetItem.moveToNext()){
            item.add(cursorGetItem.getString(cursorGetItem.getColumnIndex("Envelope_Collections")));
        }
        cursorGetItem.close();
        handler.close();
        //----------set spinner item----------------------------------------
        type.add("Monthly");type.add("Year");

        //----------------set Adapter---------------------------------------
        EnvelopeName.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, item));
        SpinnerType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type));

        //----------------set TextChanged-----------------------------------

        //----------------set onClickListener-------------------------------
        LinearLayoutSaveAddEnvelope.setOnClickListener(new LinearLayout.OnClickListener(){
            public void onClick(View v){
                cursorCompare = handler.getCompareEnvelope(EnvelopeName.getText().toString());
                if(cursorCompare.moveToFirst()){
                    compare = cursorCompare.getString(1);
                }
                cursorCompare.close();
                if(EnvelopeName.getText().length() == 0 || Amount.getText().length() == 0){
                    Toast.makeText(getApplicationContext(), "All field are required", Toast.LENGTH_SHORT).show();
                }else if(EnvelopeName.getText().toString().trim().equals(compare) || EnvelopeName.getText().toString().equals(compare)){
                    Toast.makeText(AddEnvelope.this, "You have another Envelope with name that", Toast.LENGTH_SHORT).show();
                }
                else{
                    handler.InsertData(EnvelopeName.getText().toString(), Double.parseDouble(Amount.getText().toString()),SpinnerType.getSelectedItem().toString());
                    Intent intent = new Intent(AddEnvelope.this, Home.class);
                    intent.putExtra("EnvelopeName", EnvelopeName.getText().toString());
                    intent.putExtra("Amount", Amount.getText().toString());
                    intent.putExtra("Type",SpinnerType.getSelectedItem().toString());
                    handler.close();
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
        Amount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus){

                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_envelope, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
