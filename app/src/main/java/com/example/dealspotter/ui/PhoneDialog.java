package com.example.dealspotter.ui;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.dealspotter.R;

public class PhoneDialog extends Dialog {

    private String phonenumber;

    public PhoneDialog(@NonNull Context context, String phone) {
        super(context);
        this.phonenumber = phone;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phonedialog);

        final TextView phone = findViewById(R.id.phonedialognumber);
        phone.setText(phonenumber);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        dismiss();
    }
}
