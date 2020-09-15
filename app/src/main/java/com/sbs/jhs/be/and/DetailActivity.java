package com.sbs.jhs.be.and;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.disposables.CompositeDisposable;
import io.reactivex.rxjava3.schedulers.Schedulers;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {
    private static final String TAG = "DetailActivity";
    private CompositeDisposable mCompositeDisposable = new CompositeDisposable();
    private List<Article> articles;
    private TextView textViewId, textViewRegDate, textViewTitle, textViewBody;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        final int id = getIntent().getIntExtra("id", -1);

        textViewId = findViewById(R.id.activity_detail__textViewId);
        textViewRegDate = findViewById(R.id.activity_detail__textViewRegDate);
        textViewTitle = findViewById(R.id.activity_detail__textViewTitle);
        textViewBody = findViewById(R.id.activity_detail__textViewBody);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://39.119.128.155:8089")
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
                .build();

        BeApiService beApiService = retrofit.create(BeApiService.class);

        findViewById(R.id.activity_detail__btnDoDelete).setOnClickListener(v -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);

            builder.setTitle(id + "번 글을 삭제합니다").setMessage("정말 삭제하시겠습니까?");

            builder.setPositiveButton("예", (dialog, which) -> {
                Observable<ResultData> observable__UsrArticle__doDeleteArticleResultData = beApiService.UsrArticle__doDeleteArticle(id);

                mCompositeDisposable.add(observable__UsrArticle__doDeleteArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
                    Toast.makeText(getApplicationContext(), id + "번 글 삭제 완료", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(DetailActivity.this, MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }, throwable -> {
                    Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
                    Log.e(TAG, throwable.getMessage(), throwable);
                }));
            });

            builder.setNegativeButton("아니요", (dialog, which) -> {});

            AlertDialog alertDialog = builder.create();
            alertDialog.show();
        });


        Observable<ResultData<BeApi__UsrArticle__getArticle__Body>> observable__UsrArticle__getArticleResultData = beApiService.UsrArticle__getArticle(id);

        mCompositeDisposable.add(observable__UsrArticle__getArticleResultData.subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(resultData -> {
            Article article = resultData.body.article;

            textViewId.setText(article.getId() + "번");
            textViewRegDate.setText(article.getRegDate() + "번");
            textViewTitle.setText(article.getTitle() + "번");
            textViewBody.setText(article.getBody() + "번");
        }, throwable -> {
            Toast.makeText(getApplicationContext(), "오류 발생", Toast.LENGTH_SHORT).show();
            Log.e(TAG, throwable.getMessage(), throwable);
        }));


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (!mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.dispose();
        }
    }
}