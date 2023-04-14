package com.wangzhen.reader.model.local.update

import android.database.Cursor
import android.text.TextUtils
import android.util.Log
import org.greenrobot.greendao.AbstractDao
import org.greenrobot.greendao.database.Database
import org.greenrobot.greendao.internal.DaoConfig
import java.lang.reflect.InvocationTargetException
import java.util.*

/**
 * 数据库更新策略
 * Created by wangzhen on 2023/4/14
 */
object MigrationHelper {
    private const val CONVERSION_CLASS_NOT_FOUND_EXCEPTION =
        "MIGRATION HELPER - CLASS DOESN'T MATCH WITH THE CURRENT PARAMETERS"

    fun migrate(db: Database, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        generateTempTables(db, *daoClasses)
        deleteOriginalTables(db, *daoClasses)
        createOriginalTables(db, *daoClasses)
        restoreData(db, *daoClasses)
    }

    /**
     * 生成临时列表
     *
     * @param db
     * @param daoClasses
     */
    private fun generateTempTables(db: Database, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        for (i in daoClasses.indices) {
            val daoConfig = DaoConfig(db, daoClasses[i])
            var divider = ""
            val tableName = daoConfig.tablename
            val tempTableName = daoConfig.tablename + "_TEMP"
            val properties = ArrayList<String?>()
            val createTableStringBuilder = StringBuilder()
            createTableStringBuilder.append("CREATE TABLE ").append(tempTableName).append(" (")
            for (j in daoConfig.properties.indices) {
                val columnName = daoConfig.properties[j].columnName
                if (getColumns(db, tableName).contains(columnName)) {
                    properties.add(columnName)
                    var type: String? = null
                    try {
                        type = getTypeByClass(daoConfig.properties[j].type)
                    } catch (exception: Exception) {
                        exception.printStackTrace()
                    }
                    createTableStringBuilder.append(divider).append(columnName).append(" ")
                        .append(type)
                    if (daoConfig.properties[j].primaryKey) {
                        createTableStringBuilder.append(" PRIMARY KEY")
                    }
                    divider = ","
                }
            }
            createTableStringBuilder.append(");")
            db.execSQL(createTableStringBuilder.toString())
            val insertTableStringBuilder = StringBuilder()
            insertTableStringBuilder.append("INSERT INTO ").append(tempTableName).append(" (")
            insertTableStringBuilder.append(TextUtils.join(",", properties))
            insertTableStringBuilder.append(") SELECT ")
            insertTableStringBuilder.append(TextUtils.join(",", properties))
            insertTableStringBuilder.append(" FROM ").append(tableName).append(";")
            db.execSQL(insertTableStringBuilder.toString())
        }
    }

    /**
     * 通过反射，删除要更新的表
     */
    private fun deleteOriginalTables(
        db: Database, vararg daoClasses: Class<out AbstractDao<*, *>>
    ) {
        for (daoClass in daoClasses) {
            try {
                val method = daoClass.getMethod(
                    "dropTable", Database::class.java, Boolean::class.javaPrimitiveType
                )
                method.invoke(null, db, true)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 通过反射，重新创建要更新的表
     */
    private fun createOriginalTables(
        db: Database, vararg daoClasses: Class<out AbstractDao<*, *>>
    ) {
        for (daoClass in daoClasses) {
            try {
                val method = daoClass.getMethod(
                    "createTable", Database::class.java, Boolean::class.javaPrimitiveType
                )
                method.invoke(null, db, false)
            } catch (e: IllegalAccessException) {
                e.printStackTrace()
            } catch (e: InvocationTargetException) {
                e.printStackTrace()
            } catch (e: NoSuchMethodException) {
                e.printStackTrace()
            }
        }
    }

    /**
     * 存储新的数据库表 以及数据
     *
     * @param db
     * @param daoClasses
     */
    private fun restoreData(db: Database, vararg daoClasses: Class<out AbstractDao<*, *>>) {
        for (i in daoClasses.indices) {
            val daoConfig = DaoConfig(db, daoClasses[i])
            val tableName = daoConfig.tablename
            val tempTableName = daoConfig.tablename + "_TEMP"
            val properties: ArrayList<String> = ArrayList<String>()
            for (j in daoConfig.properties.indices) {
                val columnName = daoConfig.properties[j].columnName
                if (getColumns(db, tempTableName).contains(columnName)) {
                    properties.add(columnName)
                }
            }
            val insertTableStringBuilder = StringBuilder()
            insertTableStringBuilder.append("INSERT INTO ").append(tableName).append(" (")
            insertTableStringBuilder.append(TextUtils.join(",", properties))
            insertTableStringBuilder.append(") SELECT ")
            insertTableStringBuilder.append(TextUtils.join(",", properties))
            insertTableStringBuilder.append(" FROM ").append(tempTableName).append(";")
            val dropTableStringBuilder = StringBuilder()
            dropTableStringBuilder.append("DROP TABLE ").append(tempTableName)
            db.execSQL(insertTableStringBuilder.toString())
            db.execSQL(dropTableStringBuilder.toString())
        }
    }

    @Throws(Exception::class)
    private fun getTypeByClass(type: Class<*>): String {
        if (type == String::class.java) {
            return "TEXT"
        }
        if (type == Long::class.java || type == Int::class.java || type == Long::class.javaPrimitiveType) {
            return "INTEGER"
        }
        if (type == Boolean::class.java) {
            return "BOOLEAN"
        }
        val exception = Exception("$CONVERSION_CLASS_NOT_FOUND_EXCEPTION - Class: $type")
        exception.printStackTrace()
        throw exception
    }

    private fun getColumns(db: Database, tableName: String): List<String> {
        var columns: List<String> = ArrayList()
        var cursor: Cursor? = null
        try {
            cursor = db.rawQuery("SELECT * FROM $tableName limit 1", null)
            if (cursor != null) {
                columns = ArrayList(Arrays.asList(*cursor.columnNames))
            }
        } catch (e: Exception) {
            Log.v(tableName, e.message, e)
            e.printStackTrace()
        } finally {
            cursor?.close()
        }
        return columns
    }
}