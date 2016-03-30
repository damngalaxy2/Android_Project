package com.example.ivlm.goodbudget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ActionMenuView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class Income extends AppCompatActivity {
    DatabaseHandler handler;
    Cursor cursorCheck;
    Toolbar toolbar;

//------------------Variable------------------------------
    LinearLayout linearLayoutAdd, LinearLayoutAddSave;
    LinearLayout.LayoutParams paramsEds, paramTxt, parentLayout, paramSp;
    LinearLayout parent;
    EditText ed;
    TextView txt;
    Button ButtonAdd;
    List<EditText> AllEds = new ArrayList<EditText>();
    List<TextView> AllTxt = new ArrayList<TextView>();
    List<Spinner> AllSp = new ArrayList<Spinner>();
    List<String> item = new ArrayList<String>();
    String[] data;
    int[] ItemSelected;
    int [] TotalIncomePerRow;
    Spinner SpinnerItem;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_income);
        handler = new DatabaseHandler(this);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ColorDrawable colorDrawable = new ColorDrawable(Color.parseColor("#2bbf8b"));
        getSupportActionBar().setBackgroundDrawable(colorDrawable);
        getSupportActionBar().setHomeAsUpIndicator(R.drawable.reeba_icon);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //--------------Set spinner item ---------------------------
        item.add("Monthly");item.add("Weekly");item.add("Twice a month");

        //----------------Declare Variable--------------------
        LinearLayoutAddSave = (LinearLayout) findViewById(R.id.LinearLayoutAddSave);
        ButtonAdd = (Button) findViewById(R.id.ButtonAdd);
        linearLayoutAdd = (LinearLayout) findViewById(R.id.LinearLayoutAdd);
        parentLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsEds = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramsEds.width = 250;
        paramsEds.setMargins(20, 0, 0, 0);
        paramSp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        paramSp.width = 250;
        paramSp.height = 70;
        paramSp.setMargins(10, 0, 0, 0);
        paramTxt = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        paramTxt.setMargins(20, 0, 0, 0);

        cursorCheck = handler.getCountIncomeData();
        while(cursorCheck.moveToNext()){
            double amount = cursorCheck.getDouble(cursorCheck.getColumnIndex("Income"));
            int id = cursorCheck.getInt(cursorCheck.getColumnIndex("ID_Income"));
            int itemselected = cursorCheck.getInt(cursorCheck.getColumnIndex("Income_Type"));

            parent = new LinearLayout(this);
            parent.setOrientation(LinearLayout.HORIZONTAL);
            ed = new EditText(this);
            ed.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
            txt = new TextView(this);
            txt.setTextSize(18);
            SpinnerItem = new Spinner(this);
            SpinnerItem.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, item));

        //------------Set List Item-------------------------------------------
            AllEds.add(ed);
            AllTxt.add(txt);
            AllSp.add(SpinnerItem);
        //--------------------------Set View Programmatically-----------------
            ed.setId(R.id.custom_id);
            txt.setId(R.id.custom_id);

            ed.setLayoutParams(paramsEds);
            txt.setLayoutParams(paramTxt);
            SpinnerItem.setLayoutParams(paramSp);
            parent.setLayoutParams(parentLayout);

            ed.setText(String.valueOf(amount));
            txt.setText(String.valueOf(id + "."));
            txt.setTextColor(Color.parseColor("#000000"));
            SpinnerItem.setSelection(itemselected);

            parent.addView(txt);
            parent.addView(ed);
            parent.addView(SpinnerItem);
            linearLayoutAdd.addView(parent);

            data = new String[AllEds.size()];
            ItemSelected = new int[AllSp.size()];
            TotalIncomePerRow = new int[AllEds.size()];
        }
        cursorCheck.close();
        //-------------Set on Click Listener-----------------------------------
        ButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                parent = new LinearLayout(Income.this);
                parent.setOrientation(LinearLayout.HORIZONTAL);
                ed = new EditText(Income.this);
                ed.setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL);
                txt = new TextView(Income.this);
                txt.setTextSize(18);
                SpinnerItem = new Spinner(Income.this);
                SpinnerItem.setAdapter(new ArrayAdapter<String>(Income.this,android.R.layout.simple_spinner_dropdown_item,item));

                AllEds.add(ed);
                AllTxt.add(txt);
                AllSp.add(SpinnerItem);

                ed.setId(R.id.custom_id);
                txt.setId(R.id.custom_id);

                ed.setLayoutParams(paramsEds);
                txt.setLayoutParams(paramTxt);
                SpinnerItem.setLayoutParams(paramSp);
                parent.setLayoutParams(parentLayout);

                ed.setText("0.0");
                ed.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                    @Override
                    public void onFocusChange(View v, boolean hasFocus) {
                        if(hasFocus){
                            if(ed.getText().toString().equals("0.0")){
                                ed.setText("");
                            }
                        }
                    }
                });
                txt.setText(String.valueOf(AllTxt.size() + "."));
                txt.setTextColor(Color.parseColor("#000000"));
                SpinnerItem.setSelection(0);

                parent.addView(txt);
                parent.addView(ed);
                parent.addView(SpinnerItem);
                linearLayoutAdd.addView(parent);
                data = new String[AllEds.size()];
                ItemSelected = new int[AllSp.size()];
                TotalIncomePerRow = new int[AllEds.size()];
            }
        });
        LinearLayoutAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handler.DeleteDataIncome();
                handler.close();
                for(int i=0; i<AllEds.size(); i++){
                    data[i] = AllEds.get(i).getText().toString();
                    if(data[i].equals("")){
                        data[i] = "0.0";
                    }
                    ItemSelected[i] = AllSp.get(i).getSelectedItemPosition();
                    if(ItemSelected[i] == 0)
                    {
                        TotalIncomePerRow[i] = (int) Double.parseDouble(data[i]);
                    }else if(ItemSelected[i] == 1){
                        TotalIncomePerRow[i] = (int) Double.parseDouble(data[i])*4;
                    }else if(ItemSelected[i] == 2){
                        TotalIncomePerRow[i] = (int) Double.parseDouble(data[i])*2;
                    }
                    handler.InsertIncome((int) Double.parseDouble(AllTxt.get(i).getText().toString()),Double.parseDouble(data[i]),ItemSelected[i],TotalIncomePerRow[i]);
                    //Toast.makeText(Income.this, String.valueOf((int) Double.parseDouble(AllTxt.get(i).getText().toString())), Toast.LENGTH_SHORT).show();
                    handler.close();
                    setResult(RESULT_OK);
                    finish();
                }
            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_income, menu);
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
