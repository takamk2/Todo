package takamk2.local.todo;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v4.widget.CursorAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import takamk2.local.todo.db.DBStore;
import takamk2.local.todo.util.Utils;

/**
 * A placeholder fragment containing a simple view.
 */
public class TaskListFragment extends Fragment {

    private static final int TASKS_LOADER_ID = 1;

    private TasksAdapter mTasksAdapter = null;

    private ListView mLvTasks = null;

    LoaderManager.LoaderCallbacks<Cursor> mTasksLoaderCallbacks
            = new LoaderManager.LoaderCallbacks<Cursor>() {

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            String[] projection = DBStore.Tasks.PROJECTION_ALL;
            String selection = null;
            String[] selectionArgs = null;
            String sortOrder = null;
            return new CursorLoader(getActivity(), DBStore.Tasks.CONTENT_URI, projection, selection,
                    selectionArgs, sortOrder);
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor cursor) {
            mTasksAdapter.swapCursor(cursor);
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {
            mTasksAdapter.swapCursor(null);
        }
    };

    /* ------------------------------------------------------------------------------------------ */
    // static method
    /* ------------- */
    public static TaskListFragment newInstance() {
        TaskListFragment fragment = new TaskListFragment();
        return fragment;
    }

    /* ------------------------------------------------------------------------------------------ */
    // listener, callbacks
    /* ------------------- */

    /* ------------------------------------------------------------------------------------------ */
    // class method
    /* ------------ */
    public TaskListFragment() {
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mTasksAdapter = new TasksAdapter(getActivity());

        getLoaderManager().initLoader(TASKS_LOADER_ID, null, mTasksLoaderCallbacks);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_task_list, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLvTasks = (ListView) view.findViewById(R.id.tasks);
        mLvTasks.setAdapter(mTasksAdapter);

        getLoaderManager().restartLoader(TASKS_LOADER_ID, null, mTasksLoaderCallbacks);
    }

    @Override
    public void onResume() {
        super.onResume();

        long now = Utils.getTimestampOfNow();
        ContentValues values = new ContentValues();
        values.put(DBStore.Tasks.COLUMN_TITLE, "TEST");
        values.put(DBStore.Tasks.COLUMN_CREATED, now);
        values.put(DBStore.Tasks.COLUMN_MODIFIED, now);
        getContext().getContentResolver().insert(DBStore.Tasks.CONTENT_URI, values);
    }

    /* ------------------------------------------------------------------------------------------ */
    private class TasksAdapter extends CursorAdapter {

        public TasksAdapter(Context context) {
            super(context, null, CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        }

        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            LayoutInflater inflater =
                    (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_tasks, parent, false);
            return view;
        }

        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            TextView tvName = (TextView) view.findViewById(R.id.title);

            String title = cursor.getString(cursor.getColumnIndex(DBStore.Tasks.COLUMN_TITLE));
            tvName.setText(title);
        }
    }
}
