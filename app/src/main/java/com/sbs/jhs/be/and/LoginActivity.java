package com.sbs.jhs.be.and;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Map;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private EditText editTextLoginId, editTextLoginPw;
    private Button btnDoLogin, btnJoin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setTitle("로그인");

        editTextLoginId = findViewById(R.id.activity_login__loginId);
        editTextLoginPw = findViewById(R.id.activity_login__loginPw);
        btnDoLogin = findViewById(R.id.activity_login__btnDoLogin);
        btnJoin = findViewById(R.id.activity_login__btnJoin);

        BeApiService beApiService = App.getBeApiService();

        btnDoLogin.setOnClickListener(v -> {
            String loginId = editTextLoginId.getText().toString();

            if (loginId.length() == 0) {
                Toast.makeText(getApplicationContext(), "아이디를 입력해주세요", Toast.LENGTH_SHORT).show();
                editTextLoginId.requestFocus();
                return;
            }

            String loginPw = editTextLoginPw.getText().toString();

            if (loginPw.length() == 0) {
                Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_SHORT).show();
                editTextLoginPw.requestFocus();
                return;
            }


            Observable<ResultData<Map<String, Object>>> observable__UsrMember__doLoginResultData = beApiService.UsrMember__doLogin(loginId, loginPw);
            mCompositeDisposable.add(observable__UsrMember__doLoginResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                if (resultData.isSuccess()) {
                    String authKey = (String) resultData.body.get("authKey");

                    Toast.makeText(getApplicationContext(), "인증키(" + authKey + ")를 발급 받았습니다.", Toast.LENGTH_SHORT).show();

                    Intent intent = new Intent(LoginActivity.this, ListActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                    startActivity(intent);
                }else {
                    Toast.makeText(getApplicationContext(), resultData.msg, Toast.LENGTH_SHORT).show();

                    if(resultData.resultCode.equals("F-1")) {
                        editTextLoginPw.requestFocus();
                    }else if (resultData.resultCode.equals("F-2")) {
                        editTextLoginId.requestFocus();
                    }
                }


            }, throwable -> {
                Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
                Log.e(TAG, throwable.getMessage(), throwable);
            }));
        });
    }
}