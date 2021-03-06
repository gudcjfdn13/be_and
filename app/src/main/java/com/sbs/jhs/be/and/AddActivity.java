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

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private EditText editTextTitle, editTextBody;
    private Button btnDoAdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        setTitle("게시물 추가");


        editTextTitle = findViewById(R.id.activity_add__editTextTitle);
        editTextBody = findViewById(R.id.activity_add__editTextBody);
        btnDoAdd = findViewById(R.id.activity_add__btnDoAdd);

        BeApiService beApiService = App.getBeApiService();

        btnDoAdd.setOnClickListener(v -> {
            int boardId = 1;
            String title = editTextTitle.getText().toString();

            if (title.length() == 0) {
                Toast.makeText(getApplicationContext(), "제목을 입력해주세요", Toast.LENGTH_SHORT).show();
                editTextTitle.requestFocus();
                return;
            }

            String body = editTextBody.getText().toString();

            if (body.length() == 0) {
                Toast.makeText(getApplicationContext(), "내용을 입력해주세요", Toast.LENGTH_SHORT).show();
                editTextBody.requestFocus();
                return;
            }


            Observable<ResultData<Map<String, Object>>> observable__UsrArticle__doAddArticleResultData = beApiService.UsrArticle__doAddArticle(boardId, title, body);
            mCompositeDisposable.add(observable__UsrArticle__doAddArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                int addId = Util.getAsInt(resultData.body.get("id"));
                Toast.makeText(getApplicationContext(), addId + "번 글 작성완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddActivity.this, ListActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }, throwable -> {
                Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
                Log.e(TAG, throwable.getMessage(), throwable);
            }));
        });
    }
}
