package com.inviks.www.inviks1;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.NumberPicker;
import android.widget.Toast;

import java.util.HashSet;
import java.util.Set;


public class QuantityDialog extends DialogFragment implements View.OnClickListener{
    Button ok,cancel;
    NumberPicker qtyNumberPicker;
    private int selectedQty;
    public boolean okClicked;
    Communicator com;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view=inflater.inflate(R.layout.quantity_dialog,null);
        getDialog().setTitle("Enter quantity");
        ok=(Button)view.findViewById(R.id.btnOkQty);
        cancel=(Button)view.findViewById(R.id.btnCancelQty);
        qtyNumberPicker =(NumberPicker)view.findViewById(R.id.numberPickerQty);
        qtyNumberPicker.setMinValue(1);
        if(this.getTag()!=null)
        {
            int qty=Integer.valueOf(this.getTag());
            qtyNumberPicker.setMaxValue(qty);
        }

        ok.setOnClickListener(this);
        cancel.setOnClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity)
    {
        super.onAttach(activity);
        com=(Communicator)activity;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.btnOkQty)
        {
            com.okCallBack(qtyNumberPicker.getValue());
        }
        else if(v.getId()==R.id.btnCancelQty)
        {
            setSelectedQty(-1);
            dismiss();
        }
    }
    interface Communicator
    {
        public void okCallBack(int quantity);
    }
    public int getSelectedQty()
    {
        return selectedQty;
    }
    public void setSelectedQty(int value)
    {
        selectedQty=value;
    }
}
