package com.example.myfrags;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;


public class Fragment4 extends Fragment {
    private FragsData fragsData;
    private Observer<Integer> numberObserver;
    private EditText edit;
    private TextWatcher textWatcher;
    private boolean turnOffWatcher;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_4, container, false);
        edit = (EditText) view.findViewById(R.id.editTextNumber);
        fragsData = new ViewModelProvider(requireActivity()).get(FragsData.class);
        numberObserver = new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                turnOffWatcher = true;
                edit.setText(integer.toString());
            }
        };
        fragsData.counter.observe(getViewLifecycleOwner(), numberObserver);
        /*
        Usprawnienie pracy tego pola: możliwość skasowania tekstu,
        tak aby utrzymywało się puste pole przed wpisaniem nowej wartości
         */
        textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(!turnOffWatcher) {
                    if (s.length() == 0) {
                        Log.i("Test", s.toString());
                    }
                    Integer i;
                    try {
                        i = Integer.parseInt(s.toString());
                    } catch (NumberFormatException e) {
                        i = fragsData.counter.getValue();
                    }
                    fragsData.counter.setValue(i);
                } else {
                    turnOffWatcher = false;
                }
            }
        };
        edit.addTextChangedListener(textWatcher);
        return view;
    }
}