package com.wangzhen.reader.model.local;

import android.util.Log;

import com.wangzhen.reader.model.bean.BookChapterBean;
import com.wangzhen.reader.model.bean.BookRecordBean;
import com.wangzhen.reader.model.bean.CollBookBean;
import com.wangzhen.reader.model.gen.BookChapterBeanDao;
import com.wangzhen.reader.model.gen.BookRecordBeanDao;
import com.wangzhen.reader.model.gen.CollBookBeanDao;
import com.wangzhen.reader.model.gen.DaoSession;
import com.wangzhen.reader.utils.BookManager;
import com.wangzhen.reader.utils.IOUtils;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.util.List;

import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;

/**
 * Created by wangzhen on 17-5-8.
 * 存储关于书籍内容的信息(CollBook(收藏书籍),BookChapter(书籍列表),ChapterInfo(书籍章节),BookRecord(记录))
 */

public class BookRepository {
    private static final String TAG = "CollBookManager";
    private static volatile BookRepository sInstance;
    private DaoSession mSession;
    private CollBookBeanDao mCollBookDao;

    private BookRepository() {
        mSession = DaoDbHelper.getInstance()
                .getSession();
        mCollBookDao = mSession.getCollBookBeanDao();
    }

    public static BookRepository getInstance() {
        if (sInstance == null) {
            synchronized (BookRepository.class) {
                if (sInstance == null) {
                    sInstance = new BookRepository();
                }
            }
        }
        return sInstance;
    }

    public void saveCollBook(CollBookBean bean) {
        mCollBookDao.insertOrReplace(bean);
    }

    public void saveCollBooks(List<CollBookBean> beans) {
        mCollBookDao.insertOrReplaceInTx(beans);
    }

    /**
     * 异步存储BookChapter
     *
     * @param beans
     */
    public void saveBookChaptersWithAsync(List<BookChapterBean> beans) {
        mSession.startAsyncSession()
                .runInTx(
                        () -> {
                            //存储BookChapterBean
                            mSession.getBookChapterBeanDao()
                                    .insertOrReplaceInTx(beans);
                            Log.d(TAG, "saveBookChaptersWithAsync: " + "进行存储");
                        }
                );
    }

    /**
     * 存储章节
     *
     * @param folderName
     * @param fileName
     * @param content
     */
    public void saveChapterInfo(String folderName, String fileName, String content) {
        File file = BookManager.getBookFile(folderName, fileName);
        //获取流并存储
        Writer writer = null;
        try {
            writer = new BufferedWriter(new FileWriter(file));
            writer.write(content);
            writer.flush();
        } catch (IOException e) {
            e.printStackTrace();
            IOUtils.close(writer);
        }
    }

    public void saveBookRecord(BookRecordBean bean) {
        mSession.getBookRecordBeanDao()
                .insertOrReplace(bean);
    }

    /*****************************get************************************************/
    public CollBookBean getCollBook(String bookId) {
        CollBookBean bean = mCollBookDao.queryBuilder()
                .where(CollBookBeanDao.Properties._id.eq(bookId))
                .unique();
        return bean;
    }


    public List<CollBookBean> getCollBooks() {
        return mCollBookDao
                .queryBuilder()
                .orderDesc(CollBookBeanDao.Properties.LastRead)
                .list();
    }


    //获取书籍列表
    public Single<List<BookChapterBean>> getBookChaptersInRx(String bookId) {
        return Single.create(new SingleOnSubscribe<List<BookChapterBean>>() {
            @Override
            public void subscribe(SingleEmitter<List<BookChapterBean>> e) throws Exception {
                List<BookChapterBean> beans = mSession
                        .getBookChapterBeanDao()
                        .queryBuilder()
                        .where(BookChapterBeanDao.Properties.BookId.eq(bookId))
                        .list();
                e.onSuccess(beans);
            }
        });
    }

    //获取阅读记录
    public BookRecordBean getBookRecord(String bookId) {
        return mSession.getBookRecordBeanDao()
                .queryBuilder()
                .where(BookRecordBeanDao.Properties.BookId.eq(bookId))
                .unique();
    }
}
