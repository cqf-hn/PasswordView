package com.cqf.hn.view;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cqf.hn.lib.PasswordView;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    PasswordView mInputAuth;
    TextView mTv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mInputAuth = (PasswordView) findViewById(R.id.inputAuth);
        mTv = (TextView) findViewById(R.id.tv);
        mInputAuth.setListener(new PasswordView.InputCodeEndListener() {
            @Override
            public void onInputCodeEnd(CharSequence s) {
                Toast.makeText(MainActivity.this, s, Toast.LENGTH_SHORT).show();
                mInputAuth.removeText();
            }
        });
        mTv.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mInputAuth.requestEtFocus();
    }
}
