package com.example.ivlm.goodbudget;

import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Set;

/**
 * Created by _IVLM on 3/15/2016.
 */
public class Accounts_Fragment extends Fragment {
    DatabaseHandler handler;
    Cursor cursorGetSumTransactionsExpense, cursorGetSumTransactionsCredit;
    TextView TextAllAccounts, TextTotalMyAccount, TextSubtotalMyAccount;
    double SUMExpense, SUMCredit, SUMTotal;
    LinearLayout LinearLayoutParent;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //-----Declare Variable-----------------------
        View view = inflater.inflate(R.layout.accounts_fragment, container, false);
        handler = new DatabaseHandler(getActivity());
        LinearLayoutParent = (LinearLayout) view.findViewById(R.id.LinearLayoutAccountParent);
        TextAllAccounts = (TextView) view.findViewById(R.id.TextAllAccounts);
        TextTotalMyAccount = (TextView) view.findViewById(R.id.TextTotalMyAccount);
        TextSubtotalMyAccount = (TextView) view.findViewById(R.id.TextSubtotalMyAccount);
        GetDataFromDatabase();
        SetTextOfTextView();
        return view;
    }
    public void SetTextOfTextView(){
        //---------------Set Text----------------------
        TextAllAccounts.setText(String.valueOf(SUMTotal));
        TextTotalMyAccount.setText(String.valueOf(SUMTotal));
        TextSubtotalMyAccount.setText(String.valueOf(SUMTotal));
    }
    public void GetDataFromDatabase(){
        //----------Get Data From Database---------------
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

    }

    @Override
    public void onResume() {
        super.onResume();
        GetDataFromDatabase();
        SetTextOfTextView();
    }
}
