package com.example.ivlm.goodbudget;

import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by _IVLM on 3/15/2016.
 */
public class Envelopes_Fragment extends Fragment{
    DatabaseHandler handler;
    Cursor cursorSUMMonthly, cursorSUMYear, cursorGetMonthly, cursorGetYear, cursorGetUnallocatedPlus, cursorGetUnallocatedMinus;
    double SUMMonthly, SUMYear, SUMPureYear, SUMUnallocatedPlus, SUMUnallocatedMinus;
    LinearLayout LinearLayoutMainEnvelopesMonthly, HorizontalEnvelopes1,
            HorizontalEnvelopes2, HorizontalEnvelopes3, VerticalEnvelope,LinearLayoutMainEnvelopesYear;
    TextView TextEnvelopesEnvelope, TextEnvelopesTextProgressAmount,
            TextTotalAmountEnvelopesEnvelope, TextTotalMonthly, TextTotalYear,
            TextEnvelopesTextProgress, TextViewTotalUnallocated, TextViewProgressUnallocated;
    ProgressBar ProgressEnvelopes;
    LinearLayout.LayoutParams ParamsEnvelopesTextEnvelope, ParamsEnvelopesTextProgressAmount,
            ParamsEnvelopesHorizontalLayout, ParamsEnvelopesVerticalLayout,
            ParamsProgressBarEnvelopes, ParamsEnvelopesTextProgress, ParamsEnvelopesHorizontalLayout2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        handler = new DatabaseHandler(getActivity());
        View view = inflater.inflate(R.layout.envelopes_fragment, container, false);
        //----------------Declare Variable-------
        TextTotalAmountEnvelopesEnvelope = (TextView) view.findViewById(R.id.TextTotalAmountEnvelopesEnvelope);
        TextTotalMonthly = (TextView) view.findViewById(R.id.TextTotalMonthly);
        TextTotalYear = (TextView) view.findViewById(R.id.TextTotalYear);
        LinearLayoutMainEnvelopesMonthly = (LinearLayout) view.findViewById(R.id.LinearLayoutMainEnvelopesMonthly);
        LinearLayoutMainEnvelopesYear = (LinearLayout) view.findViewById(R.id.LinearLayoutMainEnvelopesYear);
        TextViewTotalUnallocated = (TextView) view.findViewById(R.id.TextTotalUnallocated);
        TextViewProgressUnallocated = (TextView) view.findViewById(R.id.TextViewUnallocatedProgress);

        Envelopes();
        return view;
    }

    public void Envelopes(){
        //---------Change the Text of TextView------
        cursorSUMMonthly = handler.getSUMNowAmountMonthly();
        if(cursorSUMMonthly.moveToFirst()){
            SUMMonthly = cursorSUMMonthly.getDouble(0);
        }
        cursorSUMMonthly.close();
        cursorSUMYear = handler.getSUMNowAmountYear();
        if(cursorSUMYear.moveToFirst()){
            SUMYear = Math.floor((cursorSUMYear.getDouble(0)/12)*100)/100;
            SUMPureYear = cursorSUMYear.getDouble(0);
        }
        cursorSUMYear.close();
        TextTotalAmountEnvelopesEnvelope.setText(String.valueOf("All Envelopes: " + (SUMMonthly+SUMYear)));
        TextTotalMonthly.setText(String.valueOf(SUMMonthly));
        TextTotalYear.setText(String.valueOf(SUMPureYear));

        //---------------------Create View Programmatically------------------------------------

        cursorGetMonthly = handler.getCountDataMonthly();
        while (cursorGetMonthly.moveToNext()) {
            VerticalEnvelope = new LinearLayout(getActivity());
            ParamsEnvelopesVerticalLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ParamsEnvelopesVerticalLayout.setMargins(0,0,0,10);
            VerticalEnvelope.setOrientation(LinearLayout.VERTICAL);

            HorizontalEnvelopes1 = new LinearLayout(getActivity());
            ParamsEnvelopesHorizontalLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            HorizontalEnvelopes1.setLayoutParams(ParamsEnvelopesHorizontalLayout);

            HorizontalEnvelopes2 = new LinearLayout(getActivity());
            ParamsEnvelopesHorizontalLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            HorizontalEnvelopes2.setLayoutParams(ParamsEnvelopesHorizontalLayout);

            HorizontalEnvelopes3 = new LinearLayout(getActivity());
            ParamsEnvelopesHorizontalLayout2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
            ParamsEnvelopesHorizontalLayout2.setMargins(25,5,25,0);
            HorizontalEnvelopes3.setLayoutParams(ParamsEnvelopesHorizontalLayout2);
            HorizontalEnvelopes3.setBackgroundColor(Color.parseColor("#DEDEDE"));

            TextEnvelopesEnvelope = new TextView(getActivity());
            ParamsEnvelopesTextEnvelope = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            ParamsEnvelopesTextEnvelope.setMargins(30, 5, 0, 0);
            TextEnvelopesEnvelope.setLayoutParams(ParamsEnvelopesTextEnvelope);
            TextEnvelopesEnvelope.setText(cursorGetMonthly.getString(1));
            TextEnvelopesEnvelope.setTextColor(Color.parseColor("#333333"));
            TextEnvelopesEnvelope.setTextSize(18);

            TextEnvelopesTextProgressAmount = new TextView(getActivity());
            ParamsEnvelopesTextProgressAmount = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            ParamsEnvelopesTextProgressAmount.setMargins(0, 5, 30, 0);
            TextEnvelopesTextProgressAmount.setLayoutParams(ParamsEnvelopesTextProgressAmount);
            TextEnvelopesTextProgressAmount.setText(String.valueOf(cursorGetMonthly.getDouble(4)));
            TextEnvelopesTextProgressAmount.setGravity(Gravity.END);
            TextEnvelopesTextProgressAmount.setTextColor(Color.parseColor("#333333"));
            TextEnvelopesTextProgressAmount.setTextSize(18);

            ProgressEnvelopes = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
            ParamsProgressBarEnvelopes = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
            ParamsProgressBarEnvelopes.setMargins(28, 5, 0, 0);
            ProgressEnvelopes.setLayoutParams(ParamsProgressBarEnvelopes);
            ProgressEnvelopes.getProgressDrawable().setColorFilter(Color.parseColor("#7FFFBD"), PorterDuff.Mode.MULTIPLY);
            ProgressEnvelopes.setMax(cursorGetMonthly.getInt(2));
            ProgressEnvelopes.setProgress(cursorGetMonthly.getInt(4));

            TextEnvelopesTextProgress = new TextView(getActivity());
            ParamsEnvelopesTextProgress = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.3f);
            ParamsEnvelopesTextProgress.setMargins(0, 5, 28, 0);
            TextEnvelopesTextProgress.setLayoutParams(ParamsEnvelopesTextProgress);
            TextEnvelopesTextProgress.setText(String.valueOf(cursorGetMonthly.getDouble(2)));
            TextEnvelopesTextProgress.setGravity(Gravity.END);

            HorizontalEnvelopes1.addView(TextEnvelopesEnvelope);
            HorizontalEnvelopes1.addView(TextEnvelopesTextProgressAmount);
            HorizontalEnvelopes2.addView(ProgressEnvelopes);
            HorizontalEnvelopes2.addView(TextEnvelopesTextProgress);
            VerticalEnvelope.addView(HorizontalEnvelopes1);
            VerticalEnvelope.addView(HorizontalEnvelopes2);
            VerticalEnvelope.addView(HorizontalEnvelopes3);
            LinearLayoutMainEnvelopesMonthly.addView(VerticalEnvelope);
        }
        cursorGetMonthly.close();

        cursorGetYear = handler.getCountDataYear();
        while (cursorGetYear.moveToNext()) {
            VerticalEnvelope = new LinearLayout(getActivity());
            ParamsEnvelopesVerticalLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            ParamsEnvelopesVerticalLayout.setMargins(0,0,0,10);
            VerticalEnvelope.setOrientation(LinearLayout.VERTICAL);

            HorizontalEnvelopes1 = new LinearLayout(getActivity());
            ParamsEnvelopesHorizontalLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            HorizontalEnvelopes1.setLayoutParams(ParamsEnvelopesHorizontalLayout);

            HorizontalEnvelopes2 = new LinearLayout(getActivity());
            ParamsEnvelopesHorizontalLayout = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            HorizontalEnvelopes2.setLayoutParams(ParamsEnvelopesHorizontalLayout);

            HorizontalEnvelopes3 = new LinearLayout(getActivity());
            ParamsEnvelopesHorizontalLayout2 = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, 3);
            ParamsEnvelopesHorizontalLayout2.setMargins(25,5,25,0);
            HorizontalEnvelopes3.setLayoutParams(ParamsEnvelopesHorizontalLayout2);
            HorizontalEnvelopes3.setBackgroundColor(Color.parseColor("#DEDEDE"));

            TextEnvelopesEnvelope = new TextView(getActivity());
            ParamsEnvelopesTextEnvelope = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            ParamsEnvelopesTextEnvelope.setMargins(30, 5, 0, 0);
            TextEnvelopesEnvelope.setLayoutParams(ParamsEnvelopesTextEnvelope);
            TextEnvelopesEnvelope.setText(cursorGetYear.getString(1));
            TextEnvelopesEnvelope.setTextColor(Color.parseColor("#333333"));
            TextEnvelopesEnvelope.setTextSize(18);

            TextEnvelopesTextProgressAmount = new TextView(getActivity());
            ParamsEnvelopesTextProgressAmount = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1f);
            ParamsEnvelopesTextProgressAmount.setMargins(0, 5, 30, 0);
            TextEnvelopesTextProgressAmount.setLayoutParams(ParamsEnvelopesTextProgressAmount);
            TextEnvelopesTextProgressAmount.setText(String.valueOf(cursorGetYear.getDouble(4)));
            TextEnvelopesTextProgressAmount.setGravity(Gravity.END);
            TextEnvelopesTextProgressAmount.setTextColor(Color.parseColor("#333333"));
            TextEnvelopesTextProgressAmount.setTextSize(18);

            ProgressEnvelopes = new ProgressBar(getActivity(), null, android.R.attr.progressBarStyleHorizontal);
            ParamsProgressBarEnvelopes = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 0.7f);
            ParamsProgressBarEnvelopes.setMargins(28, 5, 0, 0);
            ProgressEnvelopes.setLayoutParams(ParamsProgressBarEnvelopes);
            ProgressEnvelopes.getProgressDrawable().setColorFilter(Color.parseColor("#7FFFBD"), PorterDuff.Mode.MULTIPLY);
            ProgressEnvelopes.setMax(cursorGetYear.getInt(2));
            ProgressEnvelopes.setProgress(cursorGetYear.getInt(4));

            TextEnvelopesTextProgress = new TextView(getActivity());
            ParamsEnvelopesTextProgress = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, 1.3f);
            ParamsEnvelopesTextProgress.setMargins(0, 5, 28, 0);
            TextEnvelopesTextProgress.setLayoutParams(ParamsEnvelopesTextProgress);
            TextEnvelopesTextProgress.setText(String.valueOf(cursorGetYear.getDouble(2)));
            TextEnvelopesTextProgress.setGravity(Gravity.END);

            HorizontalEnvelopes1.addView(TextEnvelopesEnvelope);
            HorizontalEnvelopes1.addView(TextEnvelopesTextProgressAmount);
            HorizontalEnvelopes2.addView(ProgressEnvelopes);
            HorizontalEnvelopes2.addView(TextEnvelopesTextProgress);
            VerticalEnvelope.addView(HorizontalEnvelopes1);
            VerticalEnvelope.addView(HorizontalEnvelopes2);
            VerticalEnvelope.addView(HorizontalEnvelopes3);
            LinearLayoutMainEnvelopesYear.addView(VerticalEnvelope);
        }
        cursorGetYear.close();
    }
    @Override
    public void onResume() {
        super.onResume();
        LinearLayoutMainEnvelopesYear.removeAllViews();
        LinearLayoutMainEnvelopesMonthly.removeAllViews();
        VerticalEnvelope.removeAllViews();
        HorizontalEnvelopes1.removeAllViews();
        HorizontalEnvelopes2.removeAllViews();
        VerticalEnvelope.removeAllViews();
        Envelopes();
    }

}
