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
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FillEnvelope3 extends AppCompatActivity {
    Toolbar toolbar;
    DatabaseHandler handler;
    Cursor cursorGetMonthly, cursorGetYear, cursorGetAllAmount;
    Map<Integer, Integer> mapAmount = new HashMap<>();
    Map<Integer, Integer> mapNewAmount = new HashMap<>();
    Button ButtonFromNewIncome, ButtonFromUnallocated;
    LinearLayout LinearLayoutFIllMonthly, LinearLayoutVerticalFillMonthly, LinearLayoutFIllMonthlyParent,
            LinearLayoutFillYearParent, LinearLayoutDate, LinearLayoutAmount;
    LinearLayout.LayoutParams ParamsLinearLayoutFIllMonthly, ParamsLayoutVerticalFIllMonthly, ParamsImageEnvelope,
                    ParamsImageArrowDown, ParamsTitleEnvelopeMonthly, ParamsSelectedAmountMonthly;
    TextView TextViewTitleEnvelope, TextViewSelectedAmountMonthly, TextViewTextId, TextViewDate, TextViewAmount;
    ImageView ImageEnvelope, ImageArrowDown;
    List<LinearLayout> ListLinearLayout = new ArrayList<LinearLayout>();
    List<String> ListTitle = new ArrayList<String>();
    List<Double> ListMaxBudget = new ArrayList<Double>();
    List<TextView> ListTextView = new ArrayList<TextView>();
    List<TextView> ListTextId = new ArrayList<TextView>();
    Map<String, Integer> map = new HashMap<>();
    int ModeChangeAmount = -1;
    EditText EditTextNote, EditTextMonthlyEnvelopeFill;
    DatePickerDialog datePickerDialog;
    Calendar calendar = Calendar.getInstance();
    Calendar process = Calendar.getInstance();
    int years,months,days;
    int TotalAmount = 0;

    //---------Process Vatiable-------------
    List<Integer> ListBudget = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_envelope3);
        handler = new DatabaseHandler(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2bbf8b"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.reeba_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //-------Set calendar To Nowadays-------------
        years = calendar.get(Calendar.YEAR);
        months = calendar.get(Calendar.MONTH);
        days = calendar.get(Calendar.DAY_OF_MONTH);
        calendar.set(years, months, days);

        //---------Declare Variable--------------
        LinearLayoutFIllMonthlyParent = (LinearLayout) findViewById(R.id.LinearLayoutMonthlyParent);
        LinearLayoutFillYearParent = (LinearLayout) findViewById(R.id.LinearLayoutYearParent);
        ButtonFromNewIncome = (Button) findViewById(R.id.ButtonFromNewIncome);
        ButtonFromUnallocated = (Button) findViewById(R.id.ButtonFromUnallocated);
        ButtonFromNewIncome.setTransformationMethod(null);
        ButtonFromUnallocated.setTransformationMethod(null);
        TextViewAmount = (TextView) findViewById(R.id.TextViewAmount);
        EditTextMonthlyEnvelopeFill = (EditText) findViewById(R.id.EditTextMonthlyEnvelopeFill);
        EditTextNote = (EditText) findViewById(R.id.EditTextNote);
        datePickerDialog = new DatePickerDialog(this,onDateSetListener,years,months,days);
        TextViewDate = (TextView) findViewById(R.id.TextViewDate);
        LinearLayoutDate = (LinearLayout) findViewById(R.id.LinearLayoutDate);
        LinearLayoutAmount = (LinearLayout) findViewById(R.id.LinearLayoutAmount);

        //------------Set Text-------------------
        String dateFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat, Locale.getDefault());
        TextViewDate.setText(sdf.format(calendar.getTime()));
        TextViewAmount.setText("0.00");

        //-------map-----------------------------
        map.put("envelope", R.drawable.ic_emails);
        map.put("arrow_down", R.drawable.ic_arrow_drop_down_24dp_212121);

        //--set On Click Listener------
        ButtonFromNewIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(FillEnvelope3.this, FIllEnvelope2.class));
                finish();
            }
        });
        LinearLayoutDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        LinearLayoutAmount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogInsertAmount();
            }
        });

        //--------Call method------------------
        EnvelopesMonthly();
        EnvelopeYear();
    }

    private DatePickerDialog.OnDateSetListener onDateSetListener = new DatePickerDialog.OnDateSetListener() {
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

            process.set(years,months,days);
        }
    };

    public void ShowDialogInsertAmount(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        editText.setText("0");
        editText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                    if(editText.getText().length()== 0 || editText.getText().toString().equals("0")){
                        editText.setText("");
                    }
                }else{
                    if(editText.getText().length()== 0){
                        editText.setText("0");
                    }
                }
            }
        });
        builder.setTitle("Set Amount").setView(editText).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextViewAmount.setText(editText.getText().toString());
                dialog.dismiss();
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
        AlertDialog.Builder builder = new AlertDialog.Builder(FillEnvelope3.this);
        builder.setTitle(title).setIcon(R.drawable.ic_emails).setCancelable(true)
                .setView(editText)
                .setSingleChoiceItems(Method, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case 0:
                                ModeChangeAmount = 0;
                                editText.setVisibility(View.VISIBLE);
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
                    TotalAmount = 0;
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
                    TotalAmount = 0;
                } else {
                    Toast.makeText(FillEnvelope3.this, "Wrong Selection", Toast.LENGTH_SHORT).show();
                }
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
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
            LinearLayoutFIllMonthly = new LinearLayout(FillEnvelope3.this);
            ParamsLinearLayoutFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayoutFIllMonthly.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutFIllMonthly.setPadding(0, 0, 0, 5);
            ParamsLinearLayoutFIllMonthly.setMargins(0,8,0,8);
            LinearLayoutFIllMonthly.setLayoutParams(ParamsLinearLayoutFIllMonthly);

            LinearLayoutVerticalFillMonthly = new LinearLayout(FillEnvelope3.this);
            ParamsLayoutVerticalFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f);
            LinearLayoutVerticalFillMonthly.setOrientation(LinearLayout.VERTICAL);
            LinearLayoutVerticalFillMonthly.setLayoutParams(ParamsLayoutVerticalFIllMonthly);

            ImageEnvelope = new ImageView(FillEnvelope3.this);
            ParamsImageEnvelope = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageEnvelope.setLayoutParams(ParamsImageEnvelope);
            ImageEnvelope.setImageResource(map.get("envelope"));

            ImageArrowDown = new ImageView(FillEnvelope3.this);
            ParamsImageArrowDown = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.25f);
            ImageArrowDown.setLayoutParams(ParamsImageArrowDown);
            ImageArrowDown.setImageResource(map.get("arrow_down"));

            TextViewTitleEnvelope = new TextView(FillEnvelope3.this);
            ParamsTitleEnvelopeMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            TextViewTitleEnvelope.setText(cursorGetMonthly.getString(1));
            TextViewTitleEnvelope.setLayoutParams(ParamsTitleEnvelopeMonthly);
            TextViewTitleEnvelope.setTextSize(15);
            TextViewTitleEnvelope.setGravity(Gravity.BOTTOM);
            TextViewTitleEnvelope.setTextColor(Color.parseColor("#010101"));

            TextViewSelectedAmountMonthly = new TextView(FillEnvelope3.this);
            ParamsSelectedAmountMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            TextViewSelectedAmountMonthly.setLayoutParams(ParamsSelectedAmountMonthly);
            TextViewSelectedAmountMonthly.setTextSize(13);
            TextViewSelectedAmountMonthly.setText("No Change");
            TextViewSelectedAmountMonthly.setGravity(Gravity.TOP);

            TextViewTextId = new TextView(FillEnvelope3.this);
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
            LinearLayoutFIllMonthly = new LinearLayout(FillEnvelope3.this);
            ParamsLinearLayoutFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayoutFIllMonthly.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutFIllMonthly.setPadding(0, 0, 0, 5);
            ParamsLinearLayoutFIllMonthly.setMargins(0,8,0,8);
            LinearLayoutFIllMonthly.setLayoutParams(ParamsLinearLayoutFIllMonthly);

            LinearLayoutVerticalFillMonthly = new LinearLayout(FillEnvelope3.this);
            ParamsLayoutVerticalFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f);
            LinearLayoutVerticalFillMonthly.setOrientation(LinearLayout.VERTICAL);
            LinearLayoutVerticalFillMonthly.setLayoutParams(ParamsLayoutVerticalFIllMonthly);

            ImageEnvelope = new ImageView(FillEnvelope3.this);
            ParamsImageEnvelope = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageEnvelope.setLayoutParams(ParamsImageEnvelope);
            ImageEnvelope.setImageResource(map.get("envelope"));

            ImageArrowDown = new ImageView(FillEnvelope3.this);
            ParamsImageArrowDown = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1.25f);
            ImageArrowDown.setLayoutParams(ParamsImageArrowDown);
            ImageArrowDown.setImageResource(map.get("arrow_down"));

            TextViewTitleEnvelope = new TextView(FillEnvelope3.this);
            ParamsTitleEnvelopeMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            TextViewTitleEnvelope.setText(cursorGetYear.getString(1));
            TextViewTitleEnvelope.setLayoutParams(ParamsTitleEnvelopeMonthly);
            TextViewTitleEnvelope.setTextSize(15);
            TextViewTitleEnvelope.setGravity(Gravity.BOTTOM);
            TextViewTitleEnvelope.setTextColor(Color.parseColor("#010101"));

            TextViewSelectedAmountMonthly = new TextView(FillEnvelope3.this);
            ParamsSelectedAmountMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT,1f);
            TextViewSelectedAmountMonthly.setLayoutParams(ParamsSelectedAmountMonthly);
            TextViewSelectedAmountMonthly.setTextSize(13);
            TextViewSelectedAmountMonthly.setText("No Change");
            TextViewSelectedAmountMonthly.setGravity(Gravity.TOP);

            TextViewTextId = new TextView(FillEnvelope3.this);
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
        for(int i=0;i<ListLinearLayout.size();i++){
            final int z = i;
            ListLinearLayout.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ShowDialogChangeAmountMethod(ListTitle.get(z), Integer.valueOf(ListTextId.get(z).getText().toString()), ListTextView.get(z));
                }
            });
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fill_envelope3, menu);
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
        switch (id){
            case R.id.home:
                finish();
                break;
            case R.id.done:
                ListBudget.clear();
                TotalAmount = 0;
                for(Map.Entry<Integer, Integer> entry : mapNewAmount.entrySet()){
                    ListBudget.add(entry.getValue());
                }
                for(int i =0; i<ListBudget.size();i++){
                    TotalAmount+=ListBudget.get(i);
                }
                if(Double.parseDouble(TextViewAmount.getText().toString()) < Double.parseDouble(String.valueOf(TotalAmount))){
                    ShowDialogAlert();
                    //Toast.makeText(FillEnvelope3.this, String.valueOf(TextViewAmount.getText().toString()), Toast.LENGTH_SHORT).show();
                    //Toast.makeText(FillEnvelope3.this, String.valueOf(TotalAmount), Toast.LENGTH_SHORT).show();
                }else{
                    cursorGetAllAmount = handler.getCountData();
                    while (cursorGetAllAmount.moveToNext()){
                        handler.InsertNowAmount(mapAmount.get(cursorGetAllAmount.getInt(0)),cursorGetAllAmount.getInt(0));
                    }
                    cursorGetAllAmount.close();
                    String type;
                    if(Double.parseDouble(TextViewAmount.getText().toString()) < 0){
                        type = "Expense";
                    }else{
                        type = "Credit";
                    }
                    handler.InsertTransactions(EditTextMonthlyEnvelopeFill.getText().toString(),process.getTimeInMillis(),
                            Double.parseDouble(TextViewAmount.getText().toString()),null,"My Account",type,EditTextNote.getText().toString(),
                            TextViewDate.getText().toString());
                    handler.InsertDataUnallocated(TotalAmount,"Minus");
                    handler.close();
                    finish();
                }
        }
        return super.onOptionsItemSelected(item);
    }
}
