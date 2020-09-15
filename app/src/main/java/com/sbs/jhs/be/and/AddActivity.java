package com.sbs.jhs.be.and;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class AddActivity extends AppCompatActivity {
    private static final String TAG = "AddActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private List<Article> articles;
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

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://39.119.128.155:8089")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        BeApiService beApiService = retrofit.create(BeApiService.class);


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


            Observable<ResultData> observable__UsrArticle__doAddArticleResultData = beApiService.UsrArticle__doAddArticle(boardId, title, body);
            mCompositeDisposable.add(observable__UsrArticle__doAddArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                Toast.makeText(getApplicationContext(), "작성 완료", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }, throwable -> {
                Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
                Log.e(TAG, throwable.getMessage(), throwable);
            }));
        });
    }
}
