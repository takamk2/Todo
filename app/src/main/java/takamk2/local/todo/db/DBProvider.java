package takamk2.local.todo.db;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;

public class DBProvider extends ContentProvider {

    private static final int CODE_TASKS = 1;
    private static final int CODE_TASK_ITEM = 2;

    private static UriMatcher mUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        mUriMatcher.addURI(DBStore.AUTHORITY, DBStore.Tasks.PATH, CODE_TASKS);
        mUriMatcher.addURI(DBStore.AUTHORITY, DBStore.Tasks.PATH + "/#", CODE_TASK_ITEM);
    }

    private DBHelper mHelper = null;

    /* ----------------------------------------------------------------------------------------- */
    public DBProvider() {
        // NOP
    }

    @Override
    public boolean onCreate() {
        mHelper = new DBHelper(getContext());
        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        String table;
        switch (mUriMatcher.match(uri)) {
            case CODE_TASKS:
                table = DBStore.Tasks.TABLE_NAME;
                break;
            case CODE_TASK_ITEM:
                table = DBStore.Tasks.TABLE_NAME;
                selection = DBStore.Tasks.COLUMN_ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                break;
            default:
                throw new IllegalArgumentException("uri is invalid: uri=" + uri.toString());
        }

        SQLiteDatabase db = mHelper.getReadableDatabase();
        Cursor cursor = db.query(table, projection, selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);

        return cursor;
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        String table;
        switch (mUriMatcher.match(uri)) {
            case CODE_TASKS:
                table = DBStore.Tasks.TABLE_NAME;
                break;
            default:
                throw new IllegalArgumentException("uri is invalid: uri=" + uri.toString());
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        long newId = db.insert(table, null, values);
        Uri newUri = ContentUris.withAppendedId(uri, newId);
        getContext().getContentResolver().notifyChange(newUri, null);

        return newUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {

        String table;
        switch (mUriMatcher.match(uri)) {
            case CODE_TASKS:
                table = DBStore.Tasks.TABLE_NAME;
                break;
            case CODE_TASK_ITEM:
                table = DBStore.Tasks.TABLE_NAME;
                selection = DBStore.Tasks.COLUMN_ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                break;
            default:
                throw new IllegalArgumentException("uri is invalid: uri=" + uri.toString());
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.update(table, values, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        String table;
        switch (mUriMatcher.match(uri)) {
            case CODE_TASKS:
                table = DBStore.Tasks.TABLE_NAME;
                break;
            case CODE_TASK_ITEM:
                table = DBStore.Tasks.TABLE_NAME;
                selection = DBStore.Tasks.COLUMN_ID + " = ?";
                selectionArgs = new String[] { uri.getLastPathSegment() };
                break;
            default:
                throw new IllegalArgumentException("uri is invalid: uri=" + uri.toString());
        }

        SQLiteDatabase db = mHelper.getWritableDatabase();
        int count = db.delete(table, selection, selectionArgs);

        getContext().getContentResolver().notifyChange(uri, null);

        return count;
    }

    @Override
    public String getType(Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
