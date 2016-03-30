package com.example.ivlm.goodbudget;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FIllEnvelope2 extends AppCompatActivity {
    Toolbar toolbar;
    DatabaseHandler handler;
    Cursor cursorGetMonthly, cursorGetYear, cursorGetAllAmount, cursorGetAllData;
    Button ButtonFromUnallocated, ButtonFromNewIncome;
    EditText EditTextAmount, EditTextReceivedFrom, EditTextNote;
    LinearLayout LinearLayoutFIllMonthly, LinearLayoutVerticalFillMonthly, LinearLayoutFIllMonthlyParent, LinearLayoutFillYearParent,
                LinearLayoutEnvelopes, LinearLayoutSubItem, LinearLayoutNote, LinearLayoutHowToFillEnvelopes, LinearLayoutDate;
    LinearLayout.LayoutParams ParamsLinearLayoutFIllMonthly, ParamsLayoutVerticalFIllMonthly,
                            ParamsImageEnvelope, ParamsImageArrowDown, ParamsTitleEnvelopeMonthly,
                            ParamsSelectedAmountMonthly;
    ImageView ImageEnvelope, ImageArrowDown;
    TextView TextViewTitleEnvelope, TextViewSelectedAmountMonthly, TextViewTextId, TextViewFillEnvelopes, TextViewDate;
    List<LinearLayout> ListLinearLayout = new ArrayList<LinearLayout>();
    List<String> ListTitle = new ArrayList<String>();
    List<Double> ListMaxBudget = new ArrayList<Double>();
    List<TextView> ListTextView = new ArrayList<TextView>();
    List<TextView> ListTextId = new ArrayList<TextView>();
    Map<String, Integer> map = new HashMap<>();
    Map<Integer, Integer> mapNewAmount = new HashMap<>();
    int ModeChangeAmount = -1;
    int TotalNewAMount = 0;
    Calendar calendar = Calendar.getInstance();
    Calendar process = Calendar.getInstance();
    DatePickerDialog datePickerDialog;
    int years,months,days;

    //-----------HashMap---------------------
    Map<Integer, Integer> mapAmount = new HashMap<>();

    //-----------Dialog Variable-------------
    CharSequence[] FillEnvelopeMethod = {"Fill Each Envelope", "Keep Unallocated"};

    //----------Process Variable-----------
    List<Integer> ListBudget = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_envelope2);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        handler = new DatabaseHandler(this);
        setSupportActionBar(toolbar);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2bbf8b"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.reeba_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //-------map---------------
        map.put("envelope", R.drawable.ic_emails);
        map.put("arrow_down", R.drawable.ic_arrow_drop_down_24dp_212121);

        //----Set Calendar for today-----------
        years = calendar.get(Calendar.YEAR);
        months = calendar.get(Calendar.MONTH);
        days = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(years,months,days);
        //-------Declare Variable---------
        LinearLayoutFIllMonthlyParent = (LinearLayout) findViewById(R.id.LinearLayoutEnvelopesMonthly);
        LinearLayoutFillYearParent = (LinearLayout) findViewById(R.id.LinearLayoutEnvelopesYear);
        LinearLayoutEnvelopes = (LinearLayout) findViewById(R.id.LinearLayoutEnvelopes);
        LinearLayoutSubItem = (LinearLayout) findViewById(R.id.LinearLayoutSubItem);
        LinearLayoutNote = (LinearLayout) findViewById(R.id.LinearLayoutNote);
        LinearLayoutHowToFillEnvelopes = (LinearLayout) findViewById(R.id.LinearLayoutHowToFillEnvelopes);
        TextViewFillEnvelopes = (TextView) findViewById(R.id.TextViewFillEnvelopes);
        ButtonFromUnallocated = (Button) findViewById(R.id.ButtonFromUnallocated);
        ButtonFromNewIncome = (Button) findViewById(R.id.ButtonFromNewIncome);
        ButtonFromNewIncome.setTransformationMethod(null);
        ButtonFromUnallocated.setTransformationMethod(null);
        EditTextAmount = (EditText) findViewById(R.id.EditTextAmount);
        TextViewDate = (TextView) findViewById(R.id.TextViewDate);
        LinearLayoutDate = (LinearLayout) findViewById(R.id.LinearLayoutDate);
        datePickerDialog = new DatePickerDialog(this,dateSetListener,years,months,days);
        EditTextReceivedFrom = (EditText) findViewById(R.id.EditTextReceivedFrom);
        EditTextNote = (EditText) findViewById(R.id.EditTextNote);

        //----------Set Text---------------------------------
        String FormatDate = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(FormatDate, Locale.getDefault());
        TextViewDate.setText(sdf.format(calendar.getTime()));

        //--------Call Method--------------------------------

        //----------------On Click Listener-------------------
        LinearLayoutHowToFillEnvelopes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogFillMethod();
            }
        });
        ButtonFromUnallocated.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FIllEnvelope2.this, FillEnvelope3.class));
                finish();
            }
        });
        EditTextAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    if (EditTextAmount.getText().length() == 0 || EditTextAmount.getText().toString().equals("0.00")) {
                        EditTextAmount.setText("");
                    }
                } else {
                    if (EditTextAmount.getText().length() == 0) {
                        EditTextAmount.setText("0.00");
                    }
                }
            }
        });
        LinearLayoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
    }

    private DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
            years = year;
            months = monthOfYear;
            days = dayOfMonth;
            String _days = "";
            String _month = "";
            if(days < 10){
                _days = "0"+String.valueOf(days);
            }else{
                _days = String.valueOf(days);
            }

            if(months < 10){
                _month = "0"+String.valueOf(months+1);
            }else{
                _month = String.valueOf(months+1);
            }
            TextViewDate.setText(_month + "/" + _days + "/" + String.valueOf(years));
            process.set(years, months, days);
        }
    };
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fill_envelope2, menu);
        return true;
    }
    public void ShowDialogAlert(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Your Envelope's Budget Higher\nThan Your Amount");
        builder.setMessage("Please try to increase your amount or decrease your envelope's budget");
        builder.setIcon(R.drawable.alert_dialog_icon);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void EnvelopesMonthly(){
        cursorGetAllAmount = handler.getCountData();
        while (cursorGetAllAmount.moveToNext()){
            mapAmount.put(cursorGetAllAmount.getInt(0),cursorGetAllAmount.getInt(4));
            mapNewAmount.put(cursorGetAllAmount.getInt(0),0);
        }
        cursorGetAllAmount.close();
        cursorGetMonthly = handler.getCountDataMonthly();
        while (cursorGetMonthly.moveToNext()) {
            LinearLayoutFIllMonthly = new LinearLayout(FIllEnvelope2.this);
            ParamsLinearLayoutFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayoutFIllMonthly.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutFIllMonthly.setPadding(0, 0, 0, 5);
            ParamsLinearLayoutFIllMonthly.setMargins(0,8,0,8);
            LinearLayoutFIllMonthly.setLayoutParams(ParamsLinearLayoutFIllMonthly);

            LinearLayoutVerticalFillMonthly = new LinearLayout(FIllEnvelope2.this);
            ParamsLayoutVerticalFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f);
            LinearLayoutVerticalFillMonthly.setOrientation(LinearLayout.VERTICAL);
            LinearLayoutVerticalFillMonthly.setLayoutParams(ParamsLayoutVerticalFIllMonthly);

            ImageEnvelope = new ImageView(FIllEnvelope2.this);
            ParamsImageEnvelope = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageEnvelope.setLayoutParams(ParamsImageEnvelope);
            ImageEnvelope.setImageResource(map.get("envelope"));

            ImageArrowDown = new ImageView(FIllEnvelope2.this);
            ParamsImageArrowDown = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.25f);
            ImageArrowDown.setLayoutParams(ParamsImageArrowDown);
            ImageArrowDown.setImageResource(map.get("arrow_down"));

            TextViewTitleEnvelope = new TextView(FIllEnvelope2.this);
            ParamsTitleEnvelopeMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            TextViewTitleEnvelope.setText(cursorGetMonthly.getString(1));
            TextViewTitleEnvelope.setLayoutParams(ParamsTitleEnvelopeMonthly);
            TextViewTitleEnvelope.setTextSize(15);
            TextViewTitleEnvelope.setGravity(Gravity.BOTTOM);
            TextViewTitleEnvelope.setTextColor(Color.parseColor("#010101"));

            TextViewSelectedAmountMonthly = new TextView(FIllEnvelope2.this);
            ParamsSelectedAmountMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            TextViewSelectedAmountMonthly.setLayoutParams(ParamsSelectedAmountMonthly);
            TextViewSelectedAmountMonthly.setTextSize(13);
            TextViewSelectedAmountMonthly.setText("No Change");
            TextViewSelectedAmountMonthly.setGravity(Gravity.TOP);

            TextViewTextId = new TextView(FIllEnvelope2.this);
            TextViewTextId.setText(String.valueOf(cursorGetMonthly.getInt(0)));

            ListTextId.add(TextViewTextId);
            ListTextView.add(TextViewSelectedAmountMonthly);
            ListLinearLayout.add(LinearLayoutFIllMonthly);
            ListTitle.add(cursorGetMonthly.getString(1));
            ListMaxBudget.add(cursorGetMonthly.getDouble(2));

            LinearLayoutVerticalFillMonthly.addView(TextViewTitleEnvelope);
            LinearLayoutVerticalFillMonthly.addView(TextViewSelectedAmountMonthly);
            LinearLayoutFIllMonthly.addView(ImageEnvelope);
            LinearLayoutFIllMonthly.addView(LinearLayoutVerticalFillMonthly);
            LinearLayoutFIllMonthly.addView(ImageArrowDown);
            LinearLayoutFIllMonthlyParent.addView(LinearLayoutFIllMonthly);
        }
        cursorGetMonthly.close();
        handler.close();
    }
    public void EnvelopeYear(){
        cursorGetYear = handler.getCountDataYear();
        while (cursorGetYear.moveToNext()) {
            LinearLayoutFIllMonthly = new LinearLayout(FIllEnvelope2.this);
            ParamsLinearLayoutFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayoutFIllMonthly.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutFIllMonthly.setPadding(0, 0, 0, 5);
            ParamsLinearLayoutFIllMonthly.setMargins(0,8,0,8);
            LinearLayoutFIllMonthly.setLayoutParams(ParamsLinearLayoutFIllMonthly);

            LinearLayoutVerticalFillMonthly = new LinearLayout(FIllEnvelope2.this);
            ParamsLayoutVerticalFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f);
            LinearLayoutVerticalFillMonthly.setOrientation(LinearLayout.VERTICAL);
            LinearLayoutVerticalFillMonthly.setLayoutParams(ParamsLayoutVerticalFIllMonthly);

            ImageEnvelope = new ImageView(FIllEnvelope2.this);
            ParamsImageEnvelope = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageEnvelope.setLayoutParams(ParamsImageEnvelope);
            ImageEnvelope.setImageResource(map.get("envelope"));

            ImageArrowDown = new ImageView(FIllEnvelope2.this);
            ParamsImageArrowDown = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.25f);
            ImageArrowDown.setLayoutParams(ParamsImageArrowDown);
            ImageArrowDown.setImageResource(map.get("arrow_down"));

            TextViewTitleEnvelope = new TextView(FIllEnvelope2.this);
            ParamsTitleEnvelopeMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            TextViewTitleEnvelope.setText(cursorGetYear.getString(1));
            TextViewTitleEnvelope.setLayoutParams(ParamsTitleEnvelopeMonthly);
            TextViewTitleEnvelope.setTextSize(15);
            TextViewTitleEnvelope.setGravity(Gravity.BOTTOM);
            TextViewTitleEnvelope.setTextColor(Color.parseColor("#010101"));

            TextViewSelectedAmountMonthly = new TextView(FIllEnvelope2.this);
            ParamsSelectedAmountMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            TextViewSelectedAmountMonthly.setLayoutParams(ParamsSelectedAmountMonthly);
            TextViewSelectedAmountMonthly.setTextSize(13);
            TextViewSelectedAmountMonthly.setText("No Change");
            TextViewSelectedAmountMonthly.setGravity(Gravity.TOP);

            TextViewTextId = new TextView(FIllEnvelope2.this);
            TextViewTextId.setText(String.valueOf(cursorGetYear.getInt(0)));

            ListTextId.add(TextViewTextId);
            ListTextView.add(TextViewSelectedAmountMonthly);
            ListLinearLayout.add(LinearLayoutFIllMonthly);
            ListTitle.add(cursorGetYear.getString(1));
            ListMaxBudget.add(cursorGetYear.getDouble(2));

            LinearLayoutVerticalFillMonthly.addView(TextViewTitleEnvelope);
            LinearLayoutVerticalFillMonthly.addView(TextViewSelectedAmountMonthly);
            LinearLayoutFIllMonthly.addView(ImageEnvelope);
            LinearLayoutFIllMonthly.addView(LinearLayoutVerticalFillMonthly);
            LinearLayoutFIllMonthly.addView(ImageArrowDown);
            LinearLayoutFillYearParent.addView(LinearLayoutFIllMonthly);
        }
        cursorGetYear.close();
        handler.close();

        for(int i=0; i<ListLinearLayout.size();i++){
            final int z = i;
            ListLinearLayout.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowDialogChangeAmountMethod(ListTitle.get(z),Integer.valueOf(ListTextId.get(z).getText().toString()), ListTextView.get(z));
                }
            });
        }
    }
    public void ShowDialogFillMethod(){
        AlertDialog.Builder builder = new AlertDialog.Builder(FIllEnvelope2.this);
        builder.setTitle("How do you want to fund your\nEnvelopes?").setIcon(R.drawable.eeba_happy_3)
                .setSingleChoiceItems(FillEnvelopeMethod, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                if (!TextViewFillEnvelopes.getText().toString().equals("Fill Each Envelope")) {
                                    TextViewFillEnvelopes.setText("Fill Each Envelope");
                                    ListLinearLayout.clear();
                                    ListTitle.clear();
                                    ListMaxBudget.clear();
                                    ListTextView.clear();
                                    ListTextId.clear();
                                    mapAmount.clear();
                                    LinearLayoutNote.setVisibility(View.VISIBLE);
                                    LinearLayoutSubItem.setVisibility(View.VISIBLE);
                                    LinearLayoutEnvelopes.setVisibility(View.VISIBLE);
                                    mapNewAmount.clear();
                                    EnvelopesMonthly();
                                    EnvelopeYear();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(FIllEnvelope2.this, "Already Selected", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                break;
                            case 1:
                                if (!TextViewFillEnvelopes.getText().toString().equals("Keep Unallocated")) {
                                    TextViewFillEnvelopes.setText("Keep Unallocated");
                                    ListLinearLayout.clear();
                                    ListTitle.clear();
                                    ListMaxBudget.clear();
                                    ListTextView.clear();
                                    ListTextId.clear();
                                    mapAmount.clear();
                                    mapNewAmount.clear();
                                    LinearLayoutSubItem.setVisibility(View.VISIBLE);
                                    LinearLayoutEnvelopes.setVisibility(View.GONE);
                                    LinearLayoutNote.setVisibility(View.VISIBLE);
                                    LinearLayoutFillYearParent.removeAllViews();
                                    LinearLayoutFIllMonthlyParent.removeAllViews();
                                    dialog.dismiss();
                                } else {
                                    Toast.makeText(FIllEnvelope2.this, "Already Selected", Toast.LENGTH_SHORT).show();
                                    dialog.dismiss();
                                }
                                break;
                        }
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void ShowDialogChangeAmountMethod(String title, final int id, final TextView textChanges){

        CharSequence[] Method = {"No Change", "Add Specific Amount"};
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setVisibility(View.GONE);
        AlertDialog.Builder builder = new AlertDialog.Builder(FIllEnvelope2.this);
        builder.setTitle(title).setIcon(R.drawable.ic_emails).setCancelable(true)
                .setView(editText)
        .setSingleChoiceItems(Method, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ModeChangeAmount = 0;
                        editText.setVisibility(View.GONE);
                        break;
                    case 1:
                        ModeChangeAmount = 1;
                        editText.setVisibility(View.VISIBLE);
                        editText.requestFocus();
                        break;
                }
            }
        }).setPositiveButton("Done", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ModeChangeAmount == 0) {
                    int lastAmount = mapAmount.get(id);
                    mapAmount.put(id, lastAmount);
                    textChanges.setText("No Change");
                    ListBudget.clear();
                    TotalNewAMount = 0;
                } else if (ModeChangeAmount == 1) {
                    int newAmount;
                    if (editText.getText().toString().equals("") || editText.getText().toString().trim().length() == 0) {
                        newAmount = 0;
                    }else{
                        newAmount = Integer.valueOf(editText.getText().toString());
                    }
                    int lastAmount = mapAmount.get(id);
                    mapAmount.put(id, lastAmount + newAmount);
                    mapNewAmount.put(id, newAmount);
                    textChanges.setText("Add budget :" + String.valueOf(newAmount));
                    ListBudget.clear();
                    TotalNewAMount = 0;
                } else {
                    Toast.makeText(FIllEnvelope2.this, "Wrong Selection", Toast.LENGTH_SHORT).show();
                    ListBudget.clear();
                    TotalNewAMount = 0;
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
        switch (id){
            case R.id.done:
            if(TextViewFillEnvelopes.getText().toString().equals("Fill Each Envelope")){
                ListBudget.clear();
                TotalNewAMount = 0;
                for(Map.Entry<Integer, Integer> entry : mapNewAmount.entrySet()){
                    ListBudget.add(entry.getValue());
                }
                for(int i=0; i<ListBudget.size();i++){
                    TotalNewAMount+= ListBudget.get(i);
                }
                if(Double.parseDouble(EditTextAmount.getText().toString()) < Double.parseDouble(String.valueOf(TotalNewAMount))){
                    ShowDialogAlert();
                    //Toast.makeText(FIllEnvelope2.this, EditTextAmount.getText().toString(), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(FIllEnvelope2.this, String.valueOf(TotalNewAMount), Toast.LENGTH_SHORT).show();
                }else {
                    cursorGetAllData = handler.getCountData();
                    while (cursorGetAllData.moveToNext()) {
                        handler.InsertNowAmount(mapAmount.get(cursorGetAllData.getInt(0)), cursorGetAllData.getInt(0));
                    }
                    String type;
                    if(Double.parseDouble(EditTextAmount.getText().toString()) < 0){
                        type = "Expense";
                    }else{
                        type = "Credit";
                    }
                    cursorGetAllData.close();
                    handler.InsertTransactions(EditTextReceivedFrom.getText().toString(),process.getTimeInMillis(),
                            TotalNewAMount,null,"My Account",type,EditTextNote.getText().toString(),TextViewDate.getText().toString());
                    handler.close();
                    finish();
                }
            }else if(TextViewFillEnvelopes.getText().toString().equals("Keep Unallocated")){
                    int amount;
                if(EditTextAmount.getText().length()==0){
                    amount = 0;
                }else{
                    amount = Integer.parseInt(EditTextAmount.getText().toString());
                }
                handler.InsertTransactions(EditTextReceivedFrom.getText().toString(),process.getTimeInMillis(),
                        TotalNewAMount,null,"My Account","Expense",EditTextNote.getText().toString(),TextViewDate.getText().toString());
                handler.InsertDataUnallocated(amount,"Plus");
                finish();
                //Toast.makeText(FIllEnvelope2.this, "Keep Unallocated", Toast.LENGTH_SHORT).show();
            }else{
                Toast.makeText(FIllEnvelope2.this, "Choose how to fill envelope", Toast.LENGTH_SHORT).show();
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
