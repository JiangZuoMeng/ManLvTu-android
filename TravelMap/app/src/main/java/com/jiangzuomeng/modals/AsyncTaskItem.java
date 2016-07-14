package com.jiangzuomeng.modals;

import android.content.ContentValues;

import com.jiangzuomeng.database.ManLvTuSQLDataType;

/**
 * Created by ekuri-PC on 2015/12/1.
 */
public class AsyncTaskItem implements ManLvTuSQLDataType {
    public final static String ASYNC_TASK_ITEM_TABLE_NAME = "AsyncTaskItem";
    public final static int TRAVEL_TPYE = 1;
    public final static int USER_TPYE = 2;
    public final static int TRAVEL_ITEM_TPYE = 3;
    public final static int COMMENT_TPYE = 4;

    public final static int INSERT_ACTION = 1;
    public final static int UPDATE_ACTION = 2;
    public final static int REMOVE_ACTION = 3;
    public final static int QUERY_ACTION = 4;

    public int id;
    public int targetId;
    public int targetType;
    public int actionType;
    @Override
    public ContentValues makeSQLContentValues() {
        ContentValues contentValues = new ContentValues();
        contentValues.put("targetId", this.targetId);
        contentValues.put("targetType", this.targetType);
        contentValues.put("actionType", this.actionType);
        return contentValues;
    }

    public static String makeCreateTableSQLString() {
        return "CREATE TABLE IF NOT EXISTS " + ASYNC_TASK_ITEM_TABLE_NAME +
                "(id INTEGER PRIMARY KEY AUTOINCREMENT, targetType INTEGER," +
                "actionType INTEGER)";
    }
}
