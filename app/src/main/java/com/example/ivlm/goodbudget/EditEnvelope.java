package com.example.ivlm.goodbudget;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.IdentityHashMap;
import java.util.List;

public class EditEnvelope extends AppCompatActivity {
    DatabaseHandler handler;
    Cursor cursorCheckItem, cursorCompare;
    Toolbar toolbar;
    AutoCompleteTextView AutoCompleteEditEnvelope;
    EditText AmountEditEnvelope;
    TextView DeleteEditEnvelope, SaveEditEnvelope, TextPosition, TextAmounts;
    Spinner SpinnerType;
    List<String> Envelope = new ArrayList<String>();
    String EnvelopeName, Amount, Type;
    List<String> type = new ArrayList<String>();
    String compare;
    int Id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_envelope);
        handler = new DatabaseHandler(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2bbf8b"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.reeba_icon_mdpi);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //------------------------Get Data Intent----------------------------------
        EnvelopeName = getIntent().getExtras().getString("EnvelopeName");
        Amount = getIntent().getExtras().getString("Amount");
        Id = Integer.parseInt(getIntent().getExtras().getString("Id"));
        Type = getIntent().getExtras().getString("Type");

        //----------------------set Variable----------------------------------------
        DeleteEditEnvelope = (TextView) findViewById(R.id.DeleteEditEnvelope);
        SaveEditEnvelope = (TextView) findViewById(R.id.SaveEditEnvelope);
        TextPosition = (TextView) findViewById(R.id.TextPosition);
        TextPosition.setText(String.valueOf(Id));
        AutoCompleteEditEnvelope = (AutoCompleteTextView) findViewById(R.id.AutoCompleteEditEnvelope);
        AmountEditEnvelope = (EditText) findViewById(R.id.AmountEditEnvelope);
        SpinnerType = (Spinner) findViewById(R.id.SpinnerType);

        //-----------------Set item Spinner--&----AutoComplete-------------------------
        cursorCheckItem = handler.getDataEnvelopeCollections();
        while(cursorCheckItem.moveToNext()){
            Envelope.add(cursorCheckItem.getString(0));
        }
        cursorCheckItem.close();
        type.add("Monthly");type.add("Year");

        //----------------set Adapter---------------------------------------
        AutoCompleteEditEnvelope.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, Envelope));
        SpinnerType.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, type));

        //-----------------set Text-----------------------------------------
        AutoCompleteEditEnvelope.setText(EnvelopeName);
        AmountEditEnvelope.setText(Amount);
        if(Type.equals("Monthly")){
            SpinnerType.setSelection(0);
        }else if(Type.equals("Year")){
            SpinnerType.setSelection(1);
        }

        //-----------------------onClick Listener---------------------------
        DeleteEditEnvelope.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                opendialog(v);
            }
        });
        SaveEditEnvelope.setOnClickListener(new TextView.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(EditEnvelope.this, Home.class);
                cursorCompare = handler.getCompareEnvelope(AutoCompleteEditEnvelope.getText().toString());
                if(cursorCompare.moveToFirst()){
                    compare = cursorCompare.getString(1);
                }
                cursorCompare.close();
                if(AutoCompleteEditEnvelope.getText().toString().trim().equals(compare) || AutoCompleteEditEnvelope.getText().toString().equals(compare)) {
                    Toast.makeText(EditEnvelope.this, "You have another Envelope with name that", Toast.LENGTH_SHORT).show();
                }else {
                    handler.UpdateEnvelope(Id, AutoCompleteEditEnvelope.getText().toString(), Double.parseDouble(AmountEditEnvelope.getText().toString()), SpinnerType.getSelectedItem().toString());
                    setResult(RESULT_OK, intent);
                    finish();
                }
            }
        });
    }
    //-----------------------------Method-----------------------------------
    public void opendialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Delete");
        builder.setIcon(R.drawable.alert_dialog_icon);
        builder.setMessage("Are you sure you want to delete this Envelope? This can't be undone");
        builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(EditEnvelope.this, Home.class);
                handler.DeleteEnvelope(Id);
                handler.close();
                setResult(RESULT_OK, intent);
                finish();
            }
        });
        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_envelope, menu);
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
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
