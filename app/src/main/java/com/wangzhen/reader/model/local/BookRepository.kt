package com.wangzhen.reader.model.local

import com.wangzhen.reader.model.bean.BookChapterBean
import com.wangzhen.reader.model.bean.BookRecordBean
import com.wangzhen.reader.model.bean.CollBookBean
import com.wangzhen.reader.model.gen.BookChapterBeanDao
import com.wangzhen.reader.model.gen.BookRecordBeanDao
import com.wangzhen.reader.model.gen.CollBookBeanDao
import com.wangzhen.reader.model.gen.DaoSession
import io.reactivex.Single

/**
 * BookRepository
 * Created by wangzhen on 2023/4/13
 */
class BookRepository private constructor() {
    private val session: DaoSession = DaoDbHelper.session
    private val bookDao: CollBookBeanDao = session.collBookBeanDao

    fun saveCollBook(bean: CollBookBean) {
        bookDao.insertOrReplace(bean)
    }

    fun saveCollBooks(beans: List<CollBookBean?>) {
        bookDao.insertOrReplaceInTx(beans)
    }

    /**
     * 异步存储BookChapter
     *
     * @param beans
     */
    fun saveBookChaptersWithAsync(beans: List<BookChapterBean>) {
        session.startAsyncSession().runInTx {
            session.bookChapterBeanDao.insertOrReplaceInTx(beans)
        }
    }

    fun saveBookRecord(bean: BookRecordBean) {
        session.bookRecordBeanDao.insertOrReplace(bean)
    }

    fun getCollBook(bookId: String): CollBookBean? {
        return bookDao.queryBuilder().where(CollBookBeanDao.Properties._id.eq(bookId)).unique()
    }

    fun getCollBookByPath(path: String?): CollBookBean? {
        return bookDao.queryBuilder().where(CollBookBeanDao.Properties.Cover.eq(path)).unique()
    }

    val collBooks: List<CollBookBean>
        get() = bookDao.queryBuilder().orderDesc(CollBookBeanDao.Properties.LastRead).list()

    fun getBookChaptersInRx(bookId: String): Single<List<BookChapterBean>> {
        return Single.create { e ->
            e.onSuccess(
                session.bookChapterBeanDao.queryBuilder()
                    .where(BookChapterBeanDao.Properties.BookId.eq(bookId)).list()
            )
        }
    }

    fun getBookRecord(bookId: String): BookRecordBean? {
        return session.bookRecordBeanDao.queryBuilder()
            .where(BookRecordBeanDao.Properties.BookId.eq(bookId)).unique()
    }

    fun deleteBook(book: CollBookBean) {
        bookDao.delete(book)
    }

    companion object {
        @JvmStatic
        val instance = BookRepository()
    }
}