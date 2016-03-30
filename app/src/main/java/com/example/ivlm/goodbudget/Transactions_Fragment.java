package com.example.ivlm.goodbudget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by _IVLM on 3/15/2016.
 */
public class Transactions_Fragment extends Fragment {
    DatabaseHandler handler;
    Cursor cursorGetTransactionsData;
    List<Integer> IdTransactions = new ArrayList<>();
    List<String> TitleTransactions = new ArrayList<>();
    List<Double> AmountTransactions = new ArrayList<>();
    List<String> EnvelopeTransactions = new ArrayList<>();
    List<String> AccountTransactions = new ArrayList<>();
    List<String> TypeTransactions = new ArrayList<>();
    List<String> DateTransactions = new ArrayList<>();
    BaseAdapter baseAdapter;
    ListView ListViewTransactions;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.transactions_fragment, container, false);
        handler = new DatabaseHandler(getActivity());
        cursorGetTransactionsData = handler.getAllDataTransactions();
        while (cursorGetTransactionsData.moveToNext()){
            IdTransactions.add(cursorGetTransactionsData.getInt(0));
            TitleTransactions.add(cursorGetTransactionsData.getString(1));
            AmountTransactions.add(cursorGetTransactionsData.getDouble(3));
            EnvelopeTransactions.add(cursorGetTransactionsData.getString(4));
            AccountTransactions.add(cursorGetTransactionsData.getString(5));
            TypeTransactions.add(cursorGetTransactionsData.getString(6));
            DateTransactions.add(cursorGetTransactionsData.getString(8));
        }
        cursorGetTransactionsData.close();
        ListViewTransactions = (ListView) view.findViewById(R.id.ListViewTransactions);
        baseAdapter = new ListViewTransactionsAdapter(IdTransactions, TitleTransactions, AmountTransactions, EnvelopeTransactions, AccountTransactions, TypeTransactions, DateTransactions);
        ListViewTransactions.setAdapter(baseAdapter);
        return view;
    }

    class ListViewTransactionsAdapter extends BaseAdapter{
        List<Integer> _IdTransactions;
        List<String> _TitleTransactions;
        List<Double> _AmountTransactions;
        List<String> _EnvelopeTransactions;
        List<String> _AccountTransactions;
        List<String> _TypeTransactions;
        List<String> _DateTransactions;
        public ListViewTransactionsAdapter(List<Integer> IdTransactions, List<String> TitleTransactions, List<Double> AmountTransactions,
                                            List<String> EnvelopeTransactions, List<String> AccountTransactions,
                                            List<String> TypeTransactions, List<String> DateTransactions){
            _IdTransactions = IdTransactions;
            _TitleTransactions = TitleTransactions;
            _AmountTransactions = AmountTransactions;
            _EnvelopeTransactions = EnvelopeTransactions;
            _AccountTransactions = AccountTransactions;
            _TypeTransactions = TypeTransactions;
            _DateTransactions = DateTransactions;
        }
        @Override
        public int getCount() {
            return _IdTransactions.size();
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
        public View getView(int position, View convertView, ViewGroup parent) {
            if(convertView == null)
                convertView = getActivity().getLayoutInflater().inflate(R.layout.list_transactions, parent, false);

            String date = _DateTransactions.get(position);
            String[] format = date.split("/");
            String month = format[0];
            String year = format[2];
            String[] format2 = year.split("");
            TextView TextViewTitle = (TextView) convertView.findViewById(R.id.TextViewTitle);
            TextViewTitle.setText(_TitleTransactions.get(position));
            TextView TextViewEnvelopeAccount = (TextView) convertView.findViewById(R.id.TextViewEnvelopeAccount);
            if(_EnvelopeTransactions.get(position) == null){
                TextViewEnvelopeAccount.setText(_AccountTransactions.get(position));
            }else{
                TextViewEnvelopeAccount.setText(_EnvelopeTransactions.get(position) + " | " + _AccountTransactions.get(position));
            }
            TextView TextViewAmount = (TextView) convertView.findViewById(R.id.TextViewAmount);
            if(_TypeTransactions.get(position).equals("First") || _TypeTransactions.get(position).equals("Credit")) {
                TextViewAmount.setText("+"+String.valueOf(_AmountTransactions.get(position)));
                TextViewAmount.setTextColor(Color.parseColor("#2BBF8B"));
            }else{
                TextViewAmount.setTextColor(Color.parseColor("#333333"));
            }
            TextView TextViewDate = (TextView) convertView.findViewById(R.id.TextViewDate);
            TextViewDate.setText(month+"/"+format2[3]+format2[4]);
            return convertView;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        IdTransactions.clear();
        TitleTransactions.clear();
        AmountTransactions.clear();
        EnvelopeTransactions.clear();
        AccountTransactions.clear();
        TypeTransactions.clear();
        DateTransactions.clear();
        cursorGetTransactionsData = handler.getAllDataTransactions();
        while (cursorGetTransactionsData.moveToNext()){
            IdTransactions.add(cursorGetTransactionsData.getInt(0));
            TitleTransactions.add(cursorGetTransactionsData.getString(1));
            AmountTransactions.add(cursorGetTransactionsData.getDouble(3));
            EnvelopeTransactions.add(cursorGetTransactionsData.getString(4));
            AccountTransactions.add(cursorGetTransactionsData.getString(5));
            TypeTransactions.add(cursorGetTransactionsData.getString(6));
            DateTransactions.add(cursorGetTransactionsData.getString(8));
            baseAdapter.notifyDataSetChanged();
        }
        cursorGetTransactionsData.close();
    }

}
