package takamk2.local.todo.db;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by takamk2 on 17/03/14.
 * <p>
 * The Edit Fragment of Base Class.
 */

public class DBStore {

    public static final String AUTHORITY = "takamk2.local.todo.dbprovider";

    public static abstract class BaseColumns2 implements BaseColumns {

        public static final String COLUMN_ID = BaseColumns._ID;
        public static final String COLUMN_CREATED = "created";
        public static final String COLUMN_MODIFIED = "modified";
    }

    public static abstract class Tasks extends BaseColumns2 {

        public static final String PATH = "tasks";
        public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + PATH);

        public static final String TABLE_NAME = PATH;

        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_COMPLETED = "completed";

        public static final String CREATE_TABLE =
                "create table " + TABLE_NAME + " ( " +
                        COLUMN_ID + " integer primary key autoincrement, " +
                        COLUMN_TITLE + " text not null, " +
                        COLUMN_COMPLETED + " integer, " +
                        COLUMN_CREATED + " integer not null, " +
                        COLUMN_MODIFIED + " integer not null " +
                        ");";
        public static final String DROP_TABLE =
                "drop table if exists " + TABLE_NAME;

        public static final String[] PROJECTION_ALL = {
                COLUMN_ID,
                COLUMN_TITLE,
                COLUMN_COMPLETED,
                COLUMN_CREATED,
                COLUMN_MODIFIED
        };
    }
}
