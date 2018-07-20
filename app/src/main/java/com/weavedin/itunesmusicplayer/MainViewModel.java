package com.weavedin.itunesmusicplayer;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;
import android.os.Looper;
import android.util.Log;

import com.weavedin.itunesmusicplayer.data.models.Result;
import com.weavedin.itunesmusicplayer.data.models.SearchResult;
import com.weavedin.itunesmusicplayer.data.remote.ApiService;
import com.weavedin.itunesmusicplayer.db.PlayerDb;
import com.weavedin.itunesmusicplayer.db.entity.Search;

import java.util.List;
import java.util.logging.Handler;

import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableObserver;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainViewModel extends ViewModel {

    private SearchResult mResult;
    private OnResultReceivedListener listener;
    private Result mCurrentResult;
    private PlayerDb mRoomDatabase;

    public void initCall(ApiService mApiService, String query) {
        listener.onResultRequest();
        mApiService.getTracks(query).enqueue(new Callback<SearchResult>() {
            @Override
            public void onResponse(Call<SearchResult> call, Response<SearchResult> response) {
                if (response.body() != null) {
                    mResult = response.body();
                    listener.onResultSuccess();
                }
            }

            @Override
            public void onFailure(Call<SearchResult> call, Throwable t) {
                listener.onResultFailure(t.getMessage());
            }
        });
    }

    public void setmRoomDatabase(PlayerDb mRoomDatabase) {
        this.mRoomDatabase = mRoomDatabase;
    }

    public void insertSearch(final String query) {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                mRoomDatabase.searchDao().insertSearchRecord(new Search(query));
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<Search>> getAllSearches() {
        return mRoomDatabase.searchDao().fetchAllQueries();
    }

    public void insertFavourite(final Result result) {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                mRoomDatabase.favouritesDao().insertFavouriteRecord(result);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public void deleteFavourite(final Result result) {
        Completable.create(new CompletableOnSubscribe() {
            @Override
            public void subscribe(CompletableEmitter emitter) throws Exception {
                mRoomDatabase.favouritesDao().deleteFavouriteRecord(result);
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe();
    }

    public LiveData<List<Result>> getAllFavourites() {
        return mRoomDatabase.favouritesDao().fetchAllFavourites();
    }

    public void setResultReceivedListener(OnResultReceivedListener listener) {
        this.listener = listener;
    }

    public List<Result> getResults(int start, int end) {
        if (end > mResult.getResults().size()) {
            return mResult.getResults().subList(start, mResult.getResults().size());
        } else {
            return mResult.getResults().subList(start, end);
        }
    }

    public int getResultCount() {
        return mResult.getResultCount();
    }

    public Result getmCurrentResult() {
        return mCurrentResult;
    }

    public void setmCurrentResult(Result mCurrentResult) {
        this.mCurrentResult = mCurrentResult;
    }

    public interface OnResultReceivedListener {

        void onResultRequest();

        void onResultSuccess();

        void onResultFailure(String message);
    }
}
