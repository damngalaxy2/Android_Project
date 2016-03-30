package com.example.ivlm.goodbudget;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.LauncherActivity;
import android.content.ClipData;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AddTransaction extends AppCompatActivity {
    DatabaseHandler handler;
    Toolbar toolbar;
    Cursor cursorGetAllEnvelope, cursorGetSumTransactionsExpense, cursorGetSumTransactionsCredit, cursorGetSpecificNowAmount;
    EditText EditTextAmount, EditTextNote;
    Spinner SpinnerEnvelope, SpinnerAccount, SpinnerMode;
    List<String> Account = new ArrayList<String>();
    List<String> ListMode = new ArrayList<String>();
    Calendar today = Calendar.getInstance();
    int years, months, days;
    double SUMExpense, SUMCredit, SUMTotal, specificNowAmount, newSpecificNowAmount;
    String name, phone;
    ArrayList<String> arrayList = new ArrayList<String>();
    ArrayAdapter<String> adapter;
    List<String> ListItemEnvelope = new ArrayList<>();
    AutoCompleteTextView AutoCompletePayee;
    TextView TextViewDate;
    LinearLayout LinearLayoutDatePicker;
    DatePickerDialog datePickerDialog;
    String[] ItemAutoComplete = {"A&E Stores","A&P","A.C Moor,e Arts & Crafts","ACE Hardware","AE Outfitters","AT&T","Aaron Rents","Abercrombire & Fitch"
                                ,"B.C Moore & Sons","BART","BB&T","BJ's Wholesale Club","Babies R Us","BabyUniverse","Bally Total Fitness","Banana Republic"
                                ,"CDW","CSK Auto","Cabela's","Cache","California Pizza Kitchen","Canadian Tire","Casual Male"
                                ,"DSW","DVD Play","Danka Business Systems","Deb Shops","Delhaize America","Dick's Sporting Goods","Dilliard's","Discount Auto Paris"
                                ,"E&J Lawrence Corp","Eastern Mountain Sports","Eddie Braur","Edwin Watts Golf","Einstein Bros Angles","Electronics Boutique","Euromarket Designs","Express"
                                ,"Factory Card and Party Outlet","Factory Connection","Family Dollar Stores","Famous Footwear","Fastenal Company","Federated Department Stores","Fifth & Mission Garage"
                                ,"G.I. Joe's","GSI Commerce","Gabriel Brothers","GameStop","Gander Mountain Company","Gap","Garden Ridge","General Nutrition Centers"
                                ,"HH Gregg","Hancock Fabrics","Handy Hardware Wholesale","Hanover Direct","Harold's Stores","Harris Teater","Harvey Electronics","Hastungs Entertainment"
                                ,"IKEA","IKON Office Solutions","Ibiza","Ingram Micro","Initial Envelope Fill","Intimate Brands","iParty"
                                ,"J Crew","J&R MusicComp World","J. Jill Group","J.C Penney","JCPenney","Jack in the Box","Jamba Juice","Jean Coutu Group"
                                ,"K's Merchandise Mart","K2","KB Toys","KFC","Kenneth Cole","Kirkland's","Kmart"
                                ,"LL Bean","Liberty Media","Limited Brands","Linens 'N Things","Liquidation World","Liquidgolf","Longs Drug Stores","Lowe's"
                                ,"MTS Incorporated","Macaroni Grill","Mack's Sport Shop","Marc Glassman","Marty's Shoes","May Department Stores","Mayor's Jewelers","McDonald's"
                                ,"NWL","National Stores","Navarre","Navy Exchange Service Command","Neiman Marcus Group","New York & Co","Nordstrom","Norm Thompson Outfitters"
                                ,"O'Reilly Automotive","Office Depot","OfficeMax","Old Navy","One Price Clothing","Orchard Supply Hardware","Overstock.com"
                                ,"P.C Richard & Son","PC Connection","PC Mall","PC Richard","PETCO Animal Supplies","PETsMART","Pacific Sunwear Of California","Paragon Sportic Goods"
                                ,"QVC","Quizno's","Qwest"
                                ,"REI (Rectreational Equipment)","REX Stores"," RONA","RadioShack","Rag Shops","Redbox","Reeds Jewelers","Regal Cinema"
                                ,"S&K Famous Brands","SYNNEX","Safeway","Sainsbury's","Saks","Sam's Club","Savers","Scheels All Sports"
                                ,"T-Mobile","THQ","TJX US Speciality","Taco Bell","Taco Bueno","Take-Two Interactive","Talbots","Target"
                                ,"Ultra Salon Cosmetics & Fragrance","Ultimate Electronics","United Rentals","United Retail Group","United Stationers","Urban Outfitters"
                                ,"Van Heusen","Variety Wholesalers","Verizon","Viacom","Visteon","Von Maur"
                                ,"W.W. Grainger","Wal Mart","Wal-Mart","Walgreens","Wendy's","West Marine","Wet Seal","Weyco Group"
                                ,"XM Satellite Radio"
                                ,"Zale","Zappos.com"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_transaction);
        handler = new DatabaseHandler(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2bbf8b"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.reeba_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //-----------Declare Variable--------------------------------------
        SpinnerEnvelope = (Spinner) findViewById(R.id.SpinnerEnvelope);
        SpinnerAccount = (Spinner) findViewById(R.id.SpinnerAccount);
        SpinnerMode = (Spinner) findViewById(R.id.SpinnerMode);
        AutoCompletePayee = (AutoCompleteTextView) findViewById(R.id.AutoCompleteText);
        EditTextAmount = (EditText) findViewById(R.id.EditTextAmount);
        EditTextNote = (EditText) findViewById(R.id.EditTextNote);
        TextViewDate = (TextView) findViewById(R.id.TextViewDate);
        LinearLayoutDatePicker = (LinearLayout) findViewById(R.id.LinearLayoutDatePicker);
        updateLabel();

        //-------------Set AutoComplete Item--------------
        getContact(this.getContentResolver());
        for(int i = 0; i<ItemAutoComplete.length; i++){
            arrayList.add(ItemAutoComplete[i]);
        }

        //-------------------Set Spinner Item-------------------------------
        cursorGetSumTransactionsExpense = handler.getSumTransactionsExpense();
        if(cursorGetSumTransactionsExpense.moveToFirst()){
            SUMExpense = cursorGetSumTransactionsExpense.getDouble(0);
        }
        cursorGetSumTransactionsExpense.close();
        cursorGetSumTransactionsCredit = handler.getSumTransactionsCredit();
        if(cursorGetSumTransactionsCredit.moveToFirst()){
            SUMCredit = cursorGetSumTransactionsCredit.getDouble(0);
        }
        cursorGetSumTransactionsCredit.close();
        SUMTotal = SUMCredit - SUMExpense;
        Account.add("- Select Account -");Account.add("My Account [" + String.valueOf(SUMTotal) + "]");
        ListMode.add("Expense");ListMode.add("Credit");
        ListItemEnvelope.add("- Select Envelope -");
        cursorGetAllEnvelope = handler.getCountData();
        while (cursorGetAllEnvelope.moveToNext()){
            ListItemEnvelope.add(cursorGetAllEnvelope.getString(1)+" [" +String.valueOf(cursorGetAllEnvelope.getDouble(4))+ "]");
        }
        cursorGetAllEnvelope.close();

        //-------------------Set Array Adapter & Change Adapter Model & Register Adapter to Spinner-------------
        SpinnerEnvelope.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ListItemEnvelope));
        SpinnerAccount.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Account));
        SpinnerMode.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, ListMode));

        //--------------------------------------------------------------------------------
        years = today.get(Calendar.YEAR);
        months = today.get(Calendar.MONTH);
        days = today.get(Calendar.DAY_OF_MONTH);

        today.set(years, months, days);
        //-----------Set Date Picker Dialog------------------
        datePickerDialog = new DatePickerDialog(this,mDateSetListener, years,months,days);
        //------------Set onClickListener---------------------------
        TextViewDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
            }
        });
        LinearLayoutDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                datePickerDialog.show();
                //new DatePickerDialog(AddTransaction.this, mDateSetListener, today.get(Calendar.YEAR), today.get(Calendar.MONTH), today.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }
    private DatePickerDialog.OnDateSetListener mDateSetListener =
            new DatePickerDialog.OnDateSetListener() {
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
                    TextViewDate.setText(_month+"/"+_days+"/"+String.valueOf(years));
                }
            };
    private void updateLabel(){
        String myFormat = "MM/dd/yyyy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());

        TextViewDate.setText(sdf.format(today.getTime()));
    }
    public void getContact(ContentResolver contentResolver){
        Cursor phones = contentResolver.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,null,null,null);
        while (phones.moveToNext()){
            name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
            phone = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            arrayList.add(name + " " + phone);
        }
        phones.close();
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_dropdown_item_1line, arrayList);
        AutoCompletePayee.setAdapter(adapter);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_add_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (item.getItemId()){
            case android.R.id.home:
                finish();
                break;
            case R.id.done:
                String Test = SpinnerEnvelope.getSelectedItem().toString();
                String[] Split1 = Test.split("\\s");
                cursorGetSpecificNowAmount = handler.getSpecificNowAmount(Split1[0]);
                if(cursorGetSpecificNowAmount.moveToFirst()){
                    specificNowAmount = cursorGetSpecificNowAmount.getDouble(2);
                }
                cursorGetSpecificNowAmount.close();
                if(SpinnerMode.getSelectedItemPosition() == 0) {
                    newSpecificNowAmount = specificNowAmount - Double.parseDouble(EditTextAmount.getText().toString());
                }else if(SpinnerMode.getSelectedItemPosition() == 1){
                    newSpecificNowAmount = Double.parseDouble(EditTextAmount.getText().toString()) + specificNowAmount;
                }
                handler.UpdateSpecificNowAmount(Split1[0],newSpecificNowAmount);
                if(AutoCompletePayee.getText().length() == 0 || EditTextAmount.getText().length() == 0 ||
                        SpinnerEnvelope.getSelectedItemPosition() == 0 || SpinnerAccount.getSelectedItemPosition() == 0){
                    Toast.makeText(AddTransaction.this, "Some field still empty", Toast.LENGTH_SHORT).show();
                }else{
                    handler.InsertTransactions(AutoCompletePayee.getText().toString(), 0,Double.parseDouble(EditTextAmount.getText().toString()),
                            Split1[0],"My Account",SpinnerMode.getSelectedItem().toString(),EditTextNote.getText().toString(),TextViewDate.getText().toString());
                    finish();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
