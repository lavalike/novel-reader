package com.example.newbiechen.ireader.model.remote;

import com.example.newbiechen.ireader.model.bean.ChapterInfoBean;

import io.reactivex.Single;

/**
 * Created by newbiechen on 17-4-20.
 */

public class RemoteRepository {
    private static RemoteRepository sInstance;
    private final BookApi mBookApi;

    private RemoteRepository() {
        mBookApi = RemoteHelper.getInstance().getRetrofit().create(BookApi.class);
    }

    public static RemoteRepository getInstance() {
        if (sInstance == null) {
            synchronized (RemoteHelper.class) {
                if (sInstance == null) {
                    sInstance = new RemoteRepository();
                }
            }
        }
        return sInstance;
    }

    public Single<ChapterInfoBean> getChapterInfo(String url) {
        return mBookApi.getChapterInfoPackage(url)
                .map(bean -> bean.getChapter());
    }
}
