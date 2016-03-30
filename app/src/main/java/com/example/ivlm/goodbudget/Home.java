package com.example.ivlm.goodbudget;

import android.app.ActionBar;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class Home extends AppCompatActivity {
    DatabaseHandler handler;
    Cursor cursorCheck, cursorMonthly, cursorYear, cursorIncome, cursorSUMMonthly, cursorSUMYear, cursorSUMall;
    Calendar calendar = Calendar.getInstance();
    Toolbar toolbar;
    //--------------Variable-----------------
    int SUMAmountMonthly, SUMAmountYear, SUMTotal;
    double SUMAll;
    Button AddEnvelopes;
    ListView ListViewMonthly, ListViewOther;
    TextView TextTimeSet, TextTotalAmounts, TextTimeOther, TextTotalIncome, TextSave, TextBack;
    ImageView MenuMonthly, MenuOther;
    List<String> envelopeMonthliesName = new ArrayList<String>();
    List<Double> envelopeMonthliesAmount = new ArrayList<Double>();
    List<Integer> envelopeMonthliesId = new ArrayList<Integer>();
    List<String> envelopeMonthliesType = new ArrayList<String>();
    List<String> envelopeOthersName = new ArrayList<String>();
    List<Double> envelopeOthersAmount = new ArrayList<Double>();
    List<Integer> envelopeOthersId = new ArrayList<Integer>();
    List<String> envelopeOthersType = new ArrayList<String>();
    BaseAdapter baseAdapterMonthly;
    BaseAdapter baseAdapterYear;
    LinearLayout RelativeLayoutIncome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2bbf8b"));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.reeba_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        //--------------------Variable-------------------
        handler = new DatabaseHandler(this);
        AddEnvelopes = (Button) findViewById(R.id.AddEnvelope);
        AddEnvelopes.setTransformationMethod(null);
        TextTimeSet = (TextView) findViewById(R.id.TextTimeSet);
        TextTotalAmounts = (TextView) findViewById(R.id.TextTotalAmounts);
        TextTimeOther = (TextView) findViewById(R.id.TextTimeOther);
        TextTotalIncome = (TextView) findViewById(R.id.TextTotalIncome);
        cursorIncome = handler.getSumIncome();
        if(cursorIncome.moveToFirst()){
            String SumIncome = cursorIncome.getString(0);
            TextTotalIncome.setText(String.valueOf(SumIncome));
        }
        cursorIncome.close();
        TextSave = (TextView) findViewById(R.id.TextSave);
        TextBack = (TextView) findViewById(R.id.TextBack);
        MenuMonthly = (ImageView) findViewById(R.id.MenuMonthly);
        MenuOther = (ImageView) findViewById(R.id.MenuOther);
        ListViewMonthly = (ListView) findViewById(R.id.ListViewMonthly);
        ListViewOther = (ListView) findViewById(R.id.ListViewOther);
        RelativeLayoutIncome = (LinearLayout) findViewById(R.id.RelativeLayoutIncome);

        //-------Set Text Total Amount of Envelope--------------
        cursorSUMMonthly = handler.getSumMonthly();
        if(cursorSUMMonthly.moveToFirst()){
            SUMAmountMonthly = cursorSUMMonthly.getInt(0);
        }
        cursorSUMMonthly.close();
        cursorSUMYear = handler.getSumYear();
        if(cursorSUMYear.moveToFirst()){
            SUMAmountYear = (cursorSUMYear.getInt(0))/12;
        }
        cursorSUMYear.close();
        SUMTotal = SUMAmountMonthly + SUMAmountYear;
        TextTotalAmounts.setText(String.valueOf(SUMTotal));
        //----------------First Statement------------------------
            firstmethod();

        //-----------------Set on Click Listener-----------------
        MenuMonthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewMonthly.setVisibility(View.VISIBLE);
                ListViewOther.setVisibility(View.GONE);
            }
        });
        MenuOther.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ListViewOther.setVisibility(View.VISIBLE);
                ListViewMonthly.setVisibility(View.GONE);
            }
        });
        AddEnvelopes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, AddEnvelope.class);
                startActivityForResult(intent, 1);
            }
        });
        RelativeLayoutIncome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Home.this, Income.class);
                startActivityForResult(intent,2);
            }
        });
        TextBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        TextSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                opendialog(v);
                //ShowNumberPickerDialog(Home.this);
            }
        });
    }

    //-----------------------------Method-----------------------------------
    public void opendialog(View view){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Great! Your budget is set.");
        builder.setIcon(R.drawable.eeba_happy_3);
        builder.setMessage("Now let's put money in your \nEnvelopes. Then you can spend from them");
        builder.setPositiveButton("Let me do it", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int id) {
                Intent intent = new Intent(Home.this, FillEnvelope.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Fill'em for me!", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                cursorCheck = handler.getCountData();
                while (cursorCheck.moveToNext()){
                    handler.InsertNowAmount(cursorCheck.getDouble(2),cursorCheck.getInt(0));
                }
                cursorCheck.close();
                cursorSUMall = handler.getSumAll();
                if(cursorSUMall.moveToFirst()){
                    SUMAll = cursorSUMall.getDouble(0);
                }
                cursorSUMall.close();
                String date = "MM/dd/yyyy";
                SimpleDateFormat sdf = new SimpleDateFormat(date, Locale.getDefault());
                handler.InsertTransactions("Initial FIll Envelope", calendar.getTimeInMillis(),
                        SUMAll, null, "My Account", "First", null, sdf.format(calendar.getTime()));
                handler.close();
                Intent intent = new Intent(Home.this, FragmentParent.class);
                startActivity(intent);
            }
        });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
   class Monthly extends BaseAdapter{
        List<String> name;
        List<Double> amount;
        List<Integer> id;
        List<String> type;

        public Monthly(List<String> Name, List<Double> Amount, List<Integer> Id, List<String> Type){
            name = Name;
            amount = Amount;
            id = Id;
            type = Type;
        }
       @Override
       public int getCount() {
           return name.size();
       }

       @Override
       public Object getItem(int position) {
           return position;
       }

       @Override
       public long getItemId(int position) {
           return 0;
       }

       @Override
        public View getView(final int position, View view, ViewGroup parent){
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listmonthly, parent, false);

            LinearLayout LinearLayoutListMonthly = (LinearLayout) view.findViewById(R.id.LinearLayoutListMonthly);
            final TextView EnvelopeName = (TextView) view.findViewById(R.id.EnvelopeName);
            EnvelopeName.setText(name.get(position));
            final TextView Amount = (TextView) view.findViewById(R.id.Amount);
            Amount.setText(String.valueOf(amount.get(position)));

            //-------------------------set OnClickListener----------------
            LinearLayoutListMonthly.setOnClickListener(new LinearLayout.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Home.this, EditEnvelope.class);
                    intent.putExtra("EnvelopeName",name.get(position));
                    intent.putExtra("Amount",String.valueOf(amount.get(position)));
                    intent.putExtra("Id", String.valueOf(id.get(position)));
                    intent.putExtra("Type", type.get(position));
                    startActivityForResult(intent, 1);
                }
            });

            return view;
        }
    }

    class Others extends BaseAdapter{
        List<String> name;
        List<Double> amount;
        List<Integer> id;
        List<String> type;
        public Others(List<String> Name, List<Double> Amount, List<Integer> Id, List<String> Type){
            name = Name;
            amount = Amount;
            id = Id;
            type = Type;
        }

        @Override
        public int getCount() {
            return name.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View view, ViewGroup parent){
            if(view == null)
                view = getLayoutInflater().inflate(R.layout.listmonthly, parent, false);
            LinearLayout LinearLayoutListMonthly = (LinearLayout) view.findViewById(R.id.LinearLayoutListMonthly);
            final TextView EnvelopeName = (TextView) view.findViewById(R.id.EnvelopeName);
            EnvelopeName.setText(name.get(position));
            final TextView Amount = (TextView) view.findViewById(R.id.Amount);
            Amount.setText(String.valueOf(amount.get(position)));

            //-------------------------set OnClickListener----------------
            LinearLayoutListMonthly.setOnClickListener(new LinearLayout.OnClickListener() {
                public void onClick(View v) {
                    Intent intent = new Intent(Home.this, EditEnvelope.class);
                    intent.putExtra("EnvelopeName",name.get(position));
                    intent.putExtra("Amount",String.valueOf(amount.get(position)));
                    intent.putExtra("Id",String.valueOf(id.get(position)));
                    intent.putExtra("Type", type.get(position));
                    startActivityForResult(intent, 1);
                }
            });

            return view;
        }
    }

    //-----------------Method------------------------------------------
    private void firstmethod(){
        cursorMonthly = handler.getCountDataMonthly();
        while(cursorMonthly.moveToNext()){
            String name = cursorMonthly.getString(cursorMonthly.getColumnIndex("Envelope"));
            double amount = cursorMonthly.getDouble(cursorMonthly.getColumnIndex("Envelope_Amount"));
            int Id = cursorMonthly.getInt(cursorMonthly.getColumnIndex("ID"));
            String type = cursorMonthly.getString(cursorMonthly.getColumnIndex("Envelope_Type"));
            envelopeMonthliesName.add(name);
            envelopeMonthliesAmount.add(amount);
            envelopeMonthliesId.add(Id);
            envelopeMonthliesType.add(type);
        }
        cursorMonthly.close();
        baseAdapterMonthly = new Monthly(envelopeMonthliesName, envelopeMonthliesAmount, envelopeMonthliesId, envelopeMonthliesType);
        ListViewMonthly.setAdapter(baseAdapterMonthly);
        //---------------------------------------------------
        cursorYear = handler.getCountDataYear();
        while (cursorYear.moveToNext()){
            String name = cursorYear.getString(cursorYear.getColumnIndex("Envelope"));
            double amount = cursorYear.getDouble(cursorYear.getColumnIndex("Envelope_Amount"));
            int Id = cursorYear.getInt(cursorYear.getColumnIndex("ID"));
            String type = cursorYear.getString(cursorYear.getColumnIndex("Envelope_Type"));
            envelopeOthersName.add(name);
            envelopeOthersAmount.add(amount);
            envelopeOthersId.add(Id);
            envelopeOthersType.add(type);
        }
        cursorYear.close();
        baseAdapterYear = new Others(envelopeOthersName, envelopeOthersAmount, envelopeOthersId, envelopeOthersType);
        ListViewOther.setAdapter(baseAdapterYear);
        handler.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1){
            if(resultCode == RESULT_OK) {
                envelopeMonthliesAmount.clear();
                envelopeMonthliesName.clear();
                envelopeMonthliesId.clear();
                envelopeMonthliesType.clear();
                envelopeOthersName.clear();
                envelopeOthersAmount.clear();
                envelopeOthersId.clear();
                envelopeOthersType.clear();
                cursorMonthly = handler.getCountDataMonthly();
                while(cursorMonthly.moveToNext()){
                    String name = cursorMonthly.getString(cursorMonthly.getColumnIndex("Envelope"));
                    double amount = cursorMonthly.getDouble(cursorMonthly.getColumnIndex("Envelope_Amount"));
                    int Id = cursorMonthly.getInt(cursorMonthly.getColumnIndex("ID"));
                    String type = cursorMonthly.getString(cursorMonthly.getColumnIndex("Envelope_Type"));
                    envelopeMonthliesName.add(name);
                    envelopeMonthliesAmount.add(amount);
                    envelopeMonthliesId.add(Id);
                    envelopeMonthliesType.add(type);
                    baseAdapterMonthly.notifyDataSetChanged();
                }
                cursorMonthly.close();
                cursorYear = handler.getCountDataYear();
                while (cursorYear.moveToNext()){
                    String name = cursorYear.getString(cursorYear.getColumnIndex("Envelope"));
                    double amount = cursorYear.getDouble(cursorYear.getColumnIndex("Envelope_Amount"));
                    int Id = cursorYear.getInt(cursorYear.getColumnIndex("ID"));
                    String type = cursorYear.getString(cursorYear.getColumnIndex("Envelope_Type"));
                    envelopeOthersName.add(name);
                    envelopeOthersAmount.add(amount);
                    envelopeOthersId.add(Id);
                    envelopeOthersType.add(type);
                    baseAdapterYear.notifyDataSetChanged();
                }
                cursorYear.close();

                //-------Set Text Total Amount of Envelope--------------
                cursorSUMMonthly = handler.getSumMonthly();
                if(cursorSUMMonthly.moveToFirst()){
                    SUMAmountMonthly = cursorSUMMonthly.getInt(0);
                }
                cursorSUMMonthly.close();
                cursorSUMYear = handler.getSumYear();
                if(cursorSUMYear.moveToFirst()){
                    SUMAmountYear = (cursorSUMYear.getInt(0))/12;
                }
                cursorSUMYear.close();
                SUMTotal = SUMAmountMonthly + SUMAmountYear;
                TextTotalAmounts.setText(String.valueOf(SUMTotal));
            }
        }
        if(requestCode == 2){
            if(resultCode == RESULT_OK){
                cursorIncome = handler.getSumIncome();
                if(cursorIncome.moveToFirst()){
                    String income = cursorIncome.getString(0);
                    TextTotalIncome.setText(income);
                }
            }
        }
    }

}
