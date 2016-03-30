package com.example.ivlm.goodbudget;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.AvoidXfermode;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class FillEnvelope extends AppCompatActivity {
    DatabaseHandler handler;
    Cursor cursorGetMonthly, cursorGetAllData, cursorGetYear, cursorGetSumAll;
    Toolbar toolbar;

    ImageView ImageArrowDownFIllEnvelope;
    LinearLayout LinearLayoutFIllEnvelope, LinearLayoutFillAccount, i, LinearSave;
    TextView TextFillEnvelopeSelection, TextFillAccountSelection, TextFillDate, TextViewNext, TextViewBack;
    CharSequence[] ModeFillEnvelope ={"Fill All Envelope", "Fill Individually"};
    CharSequence[] ModeFillAccount ={"My Account"};
    String ModeFillAccountSelection, FormatDate;
    int ModeFillEnvelopeSelection = -1;
    String ModeFillBudget = "No Change";
    EditText FillAmount, NoteTransactions;
    Calendar calendar = Calendar.getInstance();
    File fileFolder,fileName;

    //------------Fill Monthly Variable----------------------
    Map<String, Integer> map= new HashMap<String, Integer>();
    LinearLayout LinearLayoutFillMonthlyIndividuallyParent, LinearLayoutFillMonthlyAllParent;
    LinearLayout.LayoutParams ParamsLinearLayoutFIllMonthly, ParamsImageEnvelope, ParamsImageArrowDown,
            ParamsLayoutVerticalFIllMonthly, ParamsTitleEnvelopeMonthly, ParamsProgressBarMonthly,
            ParamsSelectedAmountMonthly;
    LinearLayout LinearLayoutFIllMonthly, LinearLayoutVerticalFillMonthly;
    TextView TextViewTitleEnvelope, TextViewSelectedAmountMonthly, TextViewTextId;
    ImageView ImageEnvelope, ImageArrowDown;
    ProgressBar ProgressBarMonthly;
    List<LinearLayout> ListLinearLayout = new ArrayList<LinearLayout>();
    List<String> ListTitle = new ArrayList<String>();
    List<Double> ListMaxBudget = new ArrayList<Double>();
    List<ProgressBar> ListProgressBar = new ArrayList<ProgressBar>();
    List<TextView> ListTextView = new ArrayList<TextView>();
    List<TextView> ListTextId = new ArrayList<TextView>();
    Map<Integer, Integer> MapAmount = new HashMap<>();
    
    //------Fill Year Varible----------------------------------
    LinearLayout LinearLayoutFillYearIndividuallyParent, LinearLayoutFillYearAllParent;

    //---------Check Variable--------------------------------
    double amount = 0;
    double maxAmount = 0;
    ArrayList<Integer> ListBudget = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fill_envelope);
        handler = new DatabaseHandler(FillEnvelope.this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2bbf8b"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.reeba_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //---------Declare Variable----------------------------
        ImageArrowDownFIllEnvelope = (ImageView) findViewById(R.id.ImageArrowDownFillEnvelope);
        LinearLayoutFIllEnvelope = (LinearLayout) findViewById(R.id.LinearLayoutFillEnvelopes);
        TextFillEnvelopeSelection = (TextView) findViewById(R.id.TextFillEnvelopeSelection);
        TextFillAccountSelection = (TextView) findViewById(R.id.TextFillAccountSelection);
        LinearLayoutFillAccount = (LinearLayout) findViewById(R.id.LinearLayoutFillAccount);
        FillAmount = (EditText) findViewById(R.id.FillAmount);
        TextFillDate = (TextView) findViewById(R.id.TextFillDate);
        TextViewNext = (TextView) findViewById(R.id.TextViewNext);
        TextViewBack = (TextView) findViewById(R.id.TextViewBack);
        i = (LinearLayout) findViewById(R.id.i);
        LinearSave = (LinearLayout) findViewById(R.id.LinearSave);
        LinearLayoutFillYearAllParent = (LinearLayout) findViewById(R.id.LinearLayoutFillYearAllParent);
        LinearLayoutFillMonthlyAllParent = (LinearLayout) findViewById(R.id.LinearLayoutFillMonthlyAllParent);
        LinearLayoutFillYearIndividuallyParent = (LinearLayout) findViewById(R.id.LinearLayoutFillYearIndividuallyParent);
        LinearLayoutFillMonthlyIndividuallyParent = (LinearLayout) findViewById(R.id.LinearLayoutFillMonthlyIndividuallyParent);
        NoteTransactions = (EditText) findViewById(R.id.EditTextNoteTransactions);

        //------Set Text-----------------------------
        FormatDate = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(FormatDate, Locale.US);
        TextFillDate.setText(sdf.format(calendar.getTime()));
        TextFillDate.setEnabled(false);
        FillAmount.setText(String.valueOf(amount));

        //-------------Set On Click Listener---------
        FillAmount.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    ShowDialogInsertAmount();
                }
            }
        });
        LinearLayoutFIllEnvelope.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogFillEnvelopes(ModeFillEnvelopeSelection);
            }
        });
        LinearLayoutFillAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ShowDialogFillAccount();
            }
        });
        TextViewBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextViewNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for(Map.Entry<Integer, Integer> map : MapAmount.entrySet()){
                    ListBudget.add(map.getValue());
                }
                for(int i=0;i<ListBudget.size();i++) {
                    maxAmount += Double.parseDouble(String.valueOf(ListBudget.get(i)));
                }
                if(Double.parseDouble(FillAmount.getText().toString()) < maxAmount){
                    ShowDialogAlert();
                }else{
                    //Toast.makeText(FillEnvelope.this, String.valueOf(maxAmount), Toast.LENGTH_SHORT).show();
                    cursorGetAllData = handler.getCountData();
                    while (cursorGetAllData.moveToNext()) {
                        handler.InsertNowAmount(MapAmount.get(cursorGetAllData.getInt(0)), cursorGetAllData.getInt(0));
                        //Toast.makeText(FillEnvelope.this, String.valueOf(MapAmount.get(cursorGetAllData.getInt(0))), Toast.LENGTH_SHORT).show();
                    }
                    //Toast.makeText(FillEnvelope.this, "Success", Toast.LENGTH_SHORT).show();
                    cursorGetAllData.close();
                    handler.InsertTransactions("Initial FIll Envelope",calendar.getTimeInMillis(),
                            maxAmount,null,TextFillAccountSelection.getText().toString(), "First", NoteTransactions.getText().toString(), TextFillDate.getText().toString());
                    startActivity(new Intent(FillEnvelope.this, FragmentParent.class));
                }
            }
        });

        //-------if Statement-----------------
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
    public void ShowDialogFillEnvelopes(int Selected){
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("How do you want to fund your\nEnvelopes?").setSingleChoiceItems(ModeFillEnvelope, Selected, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        if(!TextFillEnvelopeSelection.getText().toString().equals("Fill All Envelope")) {
                            TextFillEnvelopeSelection.setText("Fill All Envelope");
                            MapAmount.clear();
                            ListLinearLayout.clear();
                            ListTitle.clear();
                            ListMaxBudget.clear();
                            ListProgressBar.clear();
                            ListTextView.clear();
                            ListTextId.clear();
                            cursorGetSumAll = handler.getSumAll();
                            if (cursorGetSumAll.moveToFirst()) {
                                FillAmount.setText(String.valueOf(cursorGetSumAll.getDouble(0)));
                            }
                            cursorGetSumAll.close();
                            FillAll();
                            LinearLayoutFillMonthlyIndividuallyParent.removeAllViews();
                            LinearLayoutFillYearIndividuallyParent.removeAllViews();
                            dialog.dismiss();
                        }else {
                            dialog.dismiss();
                        }
                        break;
                    case 1:
                        if(!TextFillEnvelopeSelection.getText().toString().equals("Fill Individually")) {
                            TextFillEnvelopeSelection.setText("Fill Individually");
                            MapAmount.clear();
                            ListLinearLayout.clear();
                            ListTitle.clear();
                            ListMaxBudget.clear();
                            ListProgressBar.clear();
                            ListTextView.clear();
                            ListTextId.clear();
                            FillAmount.setText(String.valueOf(amount));
                            FillIndividually();
                            LinearLayoutFillYearAllParent.removeAllViews();
                            LinearLayoutFillMonthlyAllParent.removeAllViews();
                            dialog.dismiss();
                        }else {
                            dialog.dismiss();
                        }
                        break;
                }
            }
        }).setIcon(R.drawable.eeba_happy_3);
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void ShowDialogFillAccount(){
        AlertDialog alertDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Account").setSingleChoiceItems(ModeFillAccount, 0, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ModeFillAccountSelection = (String) ModeFillAccount[which];
                        break;
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                TextFillAccountSelection.setText(ModeFillAccountSelection);
            }
        });
        alertDialog = builder.create();
        alertDialog.show();
    }
    public void ShowDialogFillBudget(String Title, final double MaxBudget, final int Id, final ProgressBar progress, final TextView textChanges){
        final CharSequence[] ModeSetBudget = {"No Change","Set Budget Amt ("+MaxBudget+")","Set Specific Amount"};
        final EditText editText = new EditText(this);
        editText.setVisibility(View.GONE);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Title).setIcon(R.drawable.ic_emails).setView(editText);
        builder.setSingleChoiceItems(ModeSetBudget, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        ModeFillBudget = (String) ModeSetBudget[which];
                        editText.setVisibility(View.GONE);
                        break;
                    case 1:
                        ModeFillBudget = (String) ModeSetBudget[which];
                        editText.setVisibility(View.GONE);
                        break;
                    case 2:
                        ModeFillBudget = (String) ModeSetBudget[which];
                        editText.setVisibility(View.VISIBLE);
                        editText.requestFocus();
                        break;
                }
            }
        }).setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (ModeFillBudget.equals("No Change")) {
                    MapAmount.put(Id, 0);
                    progress.setProgress(MapAmount.get(Id));
                    textChanges.setText("No Change");
                    //Toast.makeText(getApplicationContext(), "One", Toast.LENGTH_SHORT).show();
                    ListBudget.clear();
                    maxAmount = 0;
                } else if (ModeFillBudget.equals("Set Budget Amt (" + MaxBudget + ")")) {
                    MapAmount.put(Id, (int) MaxBudget);
                    textChanges.setText("Set to budgeted amount of " + MaxBudget);
                    progress.setProgress(MapAmount.get(Id));
                    //Toast.makeText(getApplicationContext(), "Two", Toast.LENGTH_SHORT).show();
                    ListBudget.clear();
                    maxAmount = 0;
                } else if (ModeFillBudget.equals("Set Specific Amount")) {
                    if(editText.getText().toString().equals("")||editText.getText().toString().length() == 0){
                        MapAmount.put(Id, 0);
                        textChanges.setText("Set to specific amount of 0.0");
                    }else if(!editText.getText().toString().equals("") || editText.getText().toString().length() > 0){
                        MapAmount.put(Id, Integer.valueOf(editText.getText().toString()));
                        textChanges.setText("Set to specific amount of " + String.valueOf(Double.parseDouble(editText.getText().toString())));
                    }
                    progress.setProgress(MapAmount.get(Id));
                    //Toast.makeText(getApplicationContext(), "Three", Toast.LENGTH_SHORT).show();
                    ListBudget.clear();
                    maxAmount = 0;
                }
                //Toast.makeText(getApplicationContext(), String.valueOf(Id), Toast.LENGTH_SHORT).show();
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    public void ShowDialogInsertAmount(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final EditText editText = new EditText(this);
        editText.setInputType(InputType.TYPE_CLASS_NUMBER);
        builder.setTitle("Set your amount");
        builder.setView(editText);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (editText.getText().toString().trim().length() <= 0) {
                    amount = 0;
                }else {
                    amount = Double.parseDouble(editText.getText().toString());
                }
                FillAmount.setText(String.valueOf(amount));
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }

    public void FillIndividually(){
        i.setVisibility(View.VISIBLE);
        LinearSave.setVisibility(View.VISIBLE);
        LinearLayoutFillMonthlyIndividuallyParent.setVisibility(View.VISIBLE);
        LinearLayoutFillYearIndividuallyParent.setVisibility(View.VISIBLE);
        LinearLayoutFillMonthlyAllParent.setVisibility(View.GONE);
        LinearLayoutFillYearAllParent.setVisibility(View.GONE);
        //-------Set Default Map to 0 if empty----------
        cursorGetAllData = handler.getCountData();
        while (cursorGetAllData.moveToNext()){
            MapAmount.put(cursorGetAllData.getInt(0),0);
        }
        cursorGetAllData.close();

        //----------Declare Variable----------------------
        map.put("envelope", R.drawable.ic_emails);
        map.put("arrow_down", R.drawable.ic_arrow_drop_down_24dp_212121);

        cursorGetMonthly = handler.getCountDataMonthly();
        while (cursorGetMonthly.moveToNext()) {

            LinearLayoutFIllMonthly = new LinearLayout(FillEnvelope.this);
            ParamsLinearLayoutFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayoutFIllMonthly.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutFIllMonthly.setPadding(0, 0, 0, 5);
            LinearLayoutFIllMonthly.setLayoutParams(ParamsLinearLayoutFIllMonthly);

            LinearLayoutVerticalFillMonthly = new LinearLayout(FillEnvelope.this);
            ParamsLayoutVerticalFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f);
            ParamsLayoutVerticalFIllMonthly.setMargins(0, 0, 0, 5);
            LinearLayoutVerticalFillMonthly.setOrientation(LinearLayout.VERTICAL);
            LinearLayoutVerticalFillMonthly.setLayoutParams(ParamsLayoutVerticalFIllMonthly);

            ImageEnvelope = new ImageView(FillEnvelope.this);
            ParamsImageEnvelope = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageEnvelope.setLayoutParams(ParamsImageEnvelope);
            ImageEnvelope.setPadding(23, 23, 23, 23);
            ImageEnvelope.setImageResource(map.get("envelope"));

            ImageArrowDown = new ImageView(FillEnvelope.this);
            ParamsImageArrowDown = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageArrowDown.setLayoutParams(ParamsImageArrowDown);
            ImageArrowDown.setPadding(23, 23, 23, 23);
            ImageArrowDown.setImageResource(map.get("arrow_down"));

            TextViewTitleEnvelope = new TextView(FillEnvelope.this);
            ParamsTitleEnvelopeMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            TextViewTitleEnvelope.setText(cursorGetMonthly.getString(1));
            TextViewTitleEnvelope.setLayoutParams(ParamsTitleEnvelopeMonthly);
            TextViewTitleEnvelope.setTextSize(15);
            TextViewTitleEnvelope.setGravity(Gravity.BOTTOM);
            TextViewTitleEnvelope.setTextColor(Color.parseColor("#010101"));

            ProgressBarMonthly = new ProgressBar(FillEnvelope.this, null, android.R.attr.progressBarStyleHorizontal);
            ParamsProgressBarMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            ProgressBarMonthly.setLayoutParams(ParamsProgressBarMonthly);
            ProgressBarMonthly.getProgressDrawable().setColorFilter(Color.parseColor("#7FFFBD"), PorterDuff.Mode.MULTIPLY);
            ProgressBarMonthly.setMax(cursorGetMonthly.getInt(2));
            ProgressBarMonthly.setProgress(0);

            TextViewSelectedAmountMonthly = new TextView(FillEnvelope.this);
            ParamsSelectedAmountMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            TextViewSelectedAmountMonthly.setLayoutParams(ParamsSelectedAmountMonthly);
            TextViewSelectedAmountMonthly.setTextSize(13);
            TextViewSelectedAmountMonthly.setGravity(Gravity.TOP);
            TextViewSelectedAmountMonthly.setText("No Change");
            TextViewSelectedAmountMonthly.setTextColor(Color.parseColor("#010101"));

            TextViewTextId = new TextView(FillEnvelope.this);
            TextViewTextId.setText(String.valueOf(cursorGetMonthly.getInt(0)));

            ListTextId.add(TextViewTextId);
            ListTextView.add(TextViewSelectedAmountMonthly);
            ListLinearLayout.add(LinearLayoutFIllMonthly);
            ListTitle.add(cursorGetMonthly.getString(1));
            ListMaxBudget.add(cursorGetMonthly.getDouble(2));
            ListProgressBar.add(ProgressBarMonthly);

            LinearLayoutVerticalFillMonthly.addView(TextViewTitleEnvelope);
            LinearLayoutVerticalFillMonthly.addView(ProgressBarMonthly);
            LinearLayoutVerticalFillMonthly.addView(TextViewSelectedAmountMonthly);
            LinearLayoutFIllMonthly.addView(ImageEnvelope);
            LinearLayoutFIllMonthly.addView(LinearLayoutVerticalFillMonthly);
            LinearLayoutFIllMonthly.addView(ImageArrowDown);
            LinearLayoutFillMonthlyIndividuallyParent.addView(LinearLayoutFIllMonthly);
        }
        cursorGetMonthly.close();
        handler.close();
        FillYearIndividually();
    }
    public void FillYearIndividually(){
        //----------Declare Variable----------------------
        LinearLayoutFillYearAllParent = (LinearLayout) findViewById(R.id.LinearLayoutFillYearAllParent);
        LinearLayoutFillMonthlyAllParent = (LinearLayout) findViewById(R.id.LinearLayoutFillMonthlyAllParent);
        LinearLayoutFillYearIndividuallyParent = (LinearLayout) findViewById(R.id.LinearLayoutFillYearIndividuallyParent);
        map.put("envelope", R.drawable.ic_emails);
        map.put("arrow_down", R.drawable.ic_arrow_drop_down_24dp_212121);

        cursorGetYear = handler.getCountDataYear();
        while (cursorGetYear.moveToNext()) {

            LinearLayoutFIllMonthly = new LinearLayout(FillEnvelope.this);
            ParamsLinearLayoutFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayoutFIllMonthly.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutFIllMonthly.setPadding(0, 0, 0, 5);
            LinearLayoutFIllMonthly.setLayoutParams(ParamsLinearLayoutFIllMonthly);

            LinearLayoutVerticalFillMonthly = new LinearLayout(FillEnvelope.this);
            ParamsLayoutVerticalFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f);
            LinearLayoutVerticalFillMonthly.setOrientation(LinearLayout.VERTICAL);
            LinearLayoutVerticalFillMonthly.setLayoutParams(ParamsLayoutVerticalFIllMonthly);

            ImageEnvelope = new ImageView(FillEnvelope.this);
            ParamsImageEnvelope = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageEnvelope.setLayoutParams(ParamsImageEnvelope);
            ImageEnvelope.setPadding(23, 23, 23, 23);
            ImageEnvelope.setImageResource(map.get("envelope"));

            ImageArrowDown = new ImageView(FillEnvelope.this);
            ParamsImageArrowDown = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageArrowDown.setLayoutParams(ParamsImageArrowDown);
            ImageArrowDown.setPadding(23, 23, 23, 23);
            ImageArrowDown.setImageResource(map.get("arrow_down"));

            TextViewTitleEnvelope = new TextView(FillEnvelope.this);
            ParamsTitleEnvelopeMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            TextViewTitleEnvelope.setText(cursorGetYear.getString(1));
            TextViewTitleEnvelope.setLayoutParams(ParamsTitleEnvelopeMonthly);
            TextViewTitleEnvelope.setTextSize(15);
            TextViewTitleEnvelope.setGravity(Gravity.BOTTOM);
            TextViewTitleEnvelope.setTextColor(Color.parseColor("#010101"));

            ProgressBarMonthly = new ProgressBar(FillEnvelope.this, null, android.R.attr.progressBarStyleHorizontal);
            ParamsProgressBarMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            ProgressBarMonthly.setLayoutParams(ParamsProgressBarMonthly);
            ProgressBarMonthly.getProgressDrawable().setColorFilter(Color.parseColor("#7FFFBD"), PorterDuff.Mode.MULTIPLY);
            ProgressBarMonthly.setMax(cursorGetYear.getInt(2));
            ProgressBarMonthly.setProgress(0);

            TextViewSelectedAmountMonthly = new TextView(FillEnvelope.this);
            ParamsSelectedAmountMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            TextViewSelectedAmountMonthly.setLayoutParams(ParamsSelectedAmountMonthly);
            TextViewSelectedAmountMonthly.setTextSize(13);
            TextViewSelectedAmountMonthly.setGravity(Gravity.TOP);
            TextViewSelectedAmountMonthly.setText("No Change");
            TextViewSelectedAmountMonthly.setTextColor(Color.parseColor("#010101"));

            TextViewTextId = new TextView(FillEnvelope.this);
            TextViewTextId.setText(String.valueOf(cursorGetYear.getInt(0)));

            ListTextId.add(TextViewTextId);
            ListTextView.add(TextViewSelectedAmountMonthly);
            ListLinearLayout.add(LinearLayoutFIllMonthly);
            ListTitle.add(cursorGetYear.getString(1));
            ListMaxBudget.add(cursorGetYear.getDouble(2));
            ListProgressBar.add(ProgressBarMonthly);

            LinearLayoutVerticalFillMonthly.addView(TextViewTitleEnvelope);
            LinearLayoutVerticalFillMonthly.addView(ProgressBarMonthly);
            LinearLayoutVerticalFillMonthly.addView(TextViewSelectedAmountMonthly);
            LinearLayoutFIllMonthly.addView(ImageEnvelope);
            LinearLayoutFIllMonthly.addView(LinearLayoutVerticalFillMonthly);
            LinearLayoutFIllMonthly.addView(ImageArrowDown);
            LinearLayoutFillYearIndividuallyParent.addView(LinearLayoutFIllMonthly);
        }
        cursorGetYear.close();
        handler.close();

        for(int i=0;i<ListLinearLayout.size();i++){
            final int z = i;
            ListLinearLayout.get(i).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //for(int i=0; i<ArrayId.length; i++){
                    //    Toast.makeText(FillEnvelope.this, ListTextId.get(z).getText().toString(), Toast.LENGTH_SHORT).show();
                    //}
                    ShowDialogFillBudget(ListTitle.get(z), ListMaxBudget.get(z),
                            Integer.valueOf(ListTextId.get(z).getText().toString())
                            , ListProgressBar.get(z), ListTextView.get(z));
                }
            });
        }
    }

    public void FillAll(){
        i.setVisibility(View.VISIBLE);
        LinearSave.setVisibility(View.VISIBLE);
        LinearLayoutFillMonthlyAllParent.setVisibility(View.VISIBLE);
        LinearLayoutFillYearAllParent.setVisibility(View.VISIBLE);
        LinearLayoutFillMonthlyIndividuallyParent.setVisibility(View.GONE);
        LinearLayoutFillYearIndividuallyParent.setVisibility(View.GONE);
        //-------Set Default Map to 0 if empty----------
        cursorGetAllData = handler.getCountData();
        while (cursorGetAllData.moveToNext()){
            MapAmount.put(cursorGetAllData.getInt(0),cursorGetAllData.getInt(2));
        }
        cursorGetAllData.close();

        //----------Declare Variable----------------------
        map.put("envelope", R.drawable.ic_emails);
        map.put("arrow_down", R.drawable.ic_arrow_drop_down_24dp_212121);

        cursorGetMonthly = handler.getCountDataMonthly();
        while (cursorGetMonthly.moveToNext()) {
            LinearLayoutFIllMonthly = new LinearLayout(FillEnvelope.this);
            ParamsLinearLayoutFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayoutFIllMonthly.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutFIllMonthly.setPadding(0, 0, 0, 5);
            LinearLayoutFIllMonthly.setLayoutParams(ParamsLinearLayoutFIllMonthly);

            LinearLayoutVerticalFillMonthly = new LinearLayout(FillEnvelope.this);
            ParamsLayoutVerticalFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f);
            ParamsLayoutVerticalFIllMonthly.setMargins(0, 0, 0, 5);
            LinearLayoutVerticalFillMonthly.setOrientation(LinearLayout.VERTICAL);
            LinearLayoutVerticalFillMonthly.setLayoutParams(ParamsLayoutVerticalFIllMonthly);

            ImageEnvelope = new ImageView(FillEnvelope.this);
            ParamsImageEnvelope = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageEnvelope.setLayoutParams(ParamsImageEnvelope);
            ImageEnvelope.setPadding(23, 23, 23, 23);
            ImageEnvelope.setImageResource(map.get("envelope"));

            ImageArrowDown = new ImageView(FillEnvelope.this);
            ParamsImageArrowDown = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageArrowDown.setLayoutParams(ParamsImageArrowDown);
            ImageArrowDown.setPadding(23, 23, 23, 23);
            ImageArrowDown.setImageResource(map.get("arrow_down"));
            ImageArrowDown.setVisibility(View.INVISIBLE);

            TextViewTitleEnvelope = new TextView(FillEnvelope.this);
            ParamsTitleEnvelopeMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            TextViewTitleEnvelope.setText(cursorGetMonthly.getString(1));
            TextViewTitleEnvelope.setLayoutParams(ParamsTitleEnvelopeMonthly);
            TextViewTitleEnvelope.setTextSize(15);
            TextViewTitleEnvelope.setGravity(Gravity.BOTTOM);
            TextViewTitleEnvelope.setTextColor(Color.parseColor("#010101"));

            ProgressBarMonthly = new ProgressBar(FillEnvelope.this, null, android.R.attr.progressBarStyleHorizontal);
            ParamsProgressBarMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            ProgressBarMonthly.setLayoutParams(ParamsProgressBarMonthly);
            ProgressBarMonthly.getProgressDrawable().setColorFilter(Color.parseColor("#7FFFBD"), PorterDuff.Mode.MULTIPLY);
            ProgressBarMonthly.setMax(cursorGetMonthly.getInt(2));
            ProgressBarMonthly.setProgress(cursorGetMonthly.getInt(2));

            TextViewSelectedAmountMonthly = new TextView(FillEnvelope.this);
            ParamsSelectedAmountMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            TextViewSelectedAmountMonthly.setLayoutParams(ParamsSelectedAmountMonthly);
            TextViewSelectedAmountMonthly.setTextSize(13);
            TextViewSelectedAmountMonthly.setGravity(Gravity.TOP);
            TextViewSelectedAmountMonthly.setText("Add Budgeted Amount of " + cursorGetMonthly.getString(2));
            TextViewSelectedAmountMonthly.setTextColor(Color.parseColor("#010101"));

            TextViewTextId = new TextView(FillEnvelope.this);
            TextViewTextId.setText(String.valueOf(cursorGetMonthly.getInt(0)));

            ListTextId.add(TextViewTextId);

            LinearLayoutVerticalFillMonthly.addView(TextViewTitleEnvelope);
            LinearLayoutVerticalFillMonthly.addView(ProgressBarMonthly);
            LinearLayoutVerticalFillMonthly.addView(TextViewSelectedAmountMonthly);
            LinearLayoutFIllMonthly.addView(ImageEnvelope);
            LinearLayoutFIllMonthly.addView(LinearLayoutVerticalFillMonthly);
            LinearLayoutFIllMonthly.addView(ImageArrowDown);
            LinearLayoutFillMonthlyAllParent.addView(LinearLayoutFIllMonthly);
        }
        cursorGetMonthly.close();
        handler.close();
        FillYearAll();
    }
    public void FillYearAll(){
        //----------Declare Variable----------------------
        map.put("envelope", R.drawable.ic_emails);
        map.put("arrow_down", R.drawable.ic_arrow_drop_down_24dp_212121);

        cursorGetYear = handler.getCountDataYear();
        while (cursorGetYear.moveToNext()) {
            LinearLayoutFIllMonthly = new LinearLayout(FillEnvelope.this);
            ParamsLinearLayoutFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayoutFIllMonthly.setOrientation(LinearLayout.HORIZONTAL);
            LinearLayoutFIllMonthly.setPadding(0, 0, 0, 5);
            LinearLayoutFIllMonthly.setLayoutParams(ParamsLinearLayoutFIllMonthly);

            LinearLayoutVerticalFillMonthly = new LinearLayout(FillEnvelope.this);
            ParamsLayoutVerticalFIllMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 0.5f);
            LinearLayoutVerticalFillMonthly.setOrientation(LinearLayout.VERTICAL);
            LinearLayoutVerticalFillMonthly.setLayoutParams(ParamsLayoutVerticalFIllMonthly);

            ImageEnvelope = new ImageView(FillEnvelope.this);
            ParamsImageEnvelope = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageEnvelope.setLayoutParams(ParamsImageEnvelope);
            ImageEnvelope.setPadding(23, 23, 23, 23);
            ImageEnvelope.setImageResource(map.get("envelope"));

            ImageArrowDown = new ImageView(FillEnvelope.this);
            ParamsImageArrowDown = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1.25f);
            ImageArrowDown.setLayoutParams(ParamsImageArrowDown);
            ImageArrowDown.setPadding(23, 23, 23, 23);
            ImageArrowDown.setImageResource(map.get("arrow_down"));
            ImageArrowDown.setVisibility(View.INVISIBLE);

            TextViewTitleEnvelope = new TextView(FillEnvelope.this);
            ParamsTitleEnvelopeMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            TextViewTitleEnvelope.setText(cursorGetYear.getString(1));
            TextViewTitleEnvelope.setLayoutParams(ParamsTitleEnvelopeMonthly);
            TextViewTitleEnvelope.setTextSize(15);
            TextViewTitleEnvelope.setGravity(Gravity.BOTTOM);
            TextViewTitleEnvelope.setTextColor(Color.parseColor("#010101"));

            ProgressBarMonthly = new ProgressBar(FillEnvelope.this, null, android.R.attr.progressBarStyleHorizontal);
            ParamsProgressBarMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            ProgressBarMonthly.setLayoutParams(ParamsProgressBarMonthly);
            ProgressBarMonthly.getProgressDrawable().setColorFilter(Color.parseColor("#7FFFBD"), PorterDuff.Mode.MULTIPLY);
            ProgressBarMonthly.setMax(cursorGetYear.getInt(2));
            ProgressBarMonthly.setProgress(cursorGetYear.getInt(2));

            TextViewSelectedAmountMonthly = new TextView(FillEnvelope.this);
            ParamsSelectedAmountMonthly = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT, 1f);
            TextViewSelectedAmountMonthly.setLayoutParams(ParamsSelectedAmountMonthly);
            TextViewSelectedAmountMonthly.setTextSize(13);
            TextViewSelectedAmountMonthly.setGravity(Gravity.TOP);
            TextViewSelectedAmountMonthly.setText("Add budgeted amount of " + cursorGetYear.getString(2));
            TextViewSelectedAmountMonthly.setTextColor(Color.parseColor("#010101"));

            TextViewTextId = new TextView(FillEnvelope.this);
            TextViewTextId.setText(String.valueOf(cursorGetYear.getInt(0)));

            ListTextId.add(TextViewTextId);

            LinearLayoutVerticalFillMonthly.addView(TextViewTitleEnvelope);
            LinearLayoutVerticalFillMonthly.addView(ProgressBarMonthly);
            LinearLayoutVerticalFillMonthly.addView(TextViewSelectedAmountMonthly);
            LinearLayoutFIllMonthly.addView(ImageEnvelope);
            LinearLayoutFIllMonthly.addView(LinearLayoutVerticalFillMonthly);
            LinearLayoutFIllMonthly.addView(ImageArrowDown);
            LinearLayoutFillYearAllParent.addView(LinearLayoutFIllMonthly);
        }
        cursorGetYear.close();
        handler.close();
    }
    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
            MenuInflater inflater = getMenuInflater();
            inflater.inflate(R.menu.context_menu_fill_envelopes, menu);
            menu.setHeaderTitle("How do you want to fund your\nEnvelopes?");
            menu.setHeaderIcon(R.drawable.eeba_happy_3);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_fill_envelope, menu);
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
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
