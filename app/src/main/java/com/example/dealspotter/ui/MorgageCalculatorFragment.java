package com.example.dealspotter.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.dealspotter.R;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MorgageCalculatorFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MorgageCalculatorFragment extends Fragment {

    private EditText principalEditText;
    private EditText interestRateEditText;
    private EditText loanTermEditText;
    private Button calculateButton;
    private TextView resultTextView;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public MorgageCalculatorFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MorgageCalculatorFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MorgageCalculatorFragment newInstance(String param1, String param2) {
        MorgageCalculatorFragment fragment = new MorgageCalculatorFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_morgage_calculator, container, false);

        principalEditText = view.findViewById(R.id.editTextPrincipal);
        interestRateEditText = view.findViewById(R.id.editTextInterestRate);
        loanTermEditText = view.findViewById(R.id.editTextLoanTerm);
        calculateButton = view.findViewById(R.id.buttonCalculate);
        resultTextView = view.findViewById(R.id.textViewResult);
        double value = 0;
        int val = 0;
        String value1 = Double.toString(value);
        String value2 = Integer.toString(val);
        interestRateEditText.setText(value1);
        loanTermEditText.setText(value2);
        principalEditText.setText(value1);

        calculateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                calculateMortgage();
            }
        });

        return view;

    }

    private void calculateMortgage() {
        double principal = Double.parseDouble(principalEditText.getText().toString());
        double interestRate = Double.parseDouble(interestRateEditText.getText().toString()) / 100.0;
        int loanTerm = Integer.parseInt(loanTermEditText.getText().toString());

        double monthlyRate = interestRate / 12.0;
        double termInMonths = loanTerm * 12;
        double monthlyPayment = (principal * monthlyRate) / (1 - Math.pow(1 + monthlyRate, -termInMonths));

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(Locale.ITALY);
        String formattedMonthlyPayment = currencyFormatter.format(monthlyPayment);

        resultTextView.setText(getString(R.string.result_format, formattedMonthlyPayment));
    }

}