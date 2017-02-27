package drawapptutorial.com.example.thatwaseasy_twe;

/**
 * Created by Christian Coreil on 2/24/2017.
 */

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import java.util.ArrayList;
import java.util.List;

public class DBHandler extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 3;
    // Database Name
    private static final String DATABASE_NAME = "taskList";
    // Contacts table name
    private static final String TABLE_TASKS= "tasks";
    // Shops Table Columns names
    private static final String KEY_ID = "id";
    private static final String KEY_NUM_MINUTES = "task_minutes";
    private static final String KEY_NAME = "task_name";
    private static final String KEY_DESC = "task_description";
    private static final String KEY_URGENCY = "task_urgency";
    private static final String KEY_COMPLETION_STATUS = "task_isComplete";
    private static final String KEY_TIMER_NUM = "task_timer_num";
    private static final String KEY_RUNNING = "task_running";
    private static final String KEY_TIMER_START = "tast_timer_start";

    public DBHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TASK_TABLE = "CREATE TABLE " + TABLE_TASKS+ "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_NUM_MINUTES + " NUMBER,"
                + KEY_NAME + " TEXT,"
                + KEY_DESC + " TEXT,"
                + KEY_URGENCY + " TEXT,"
                + KEY_COMPLETION_STATUS + " TEXT,"
                + KEY_TIMER_NUM + " TEXT,"
                + KEY_RUNNING + " BOOLEAN,"
                + KEY_TIMER_START + " TEXT" + ")";
        db.execSQL(CREATE_TASK_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
// Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
// Creating tables again
        onCreate(db);
    }

    public void addTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NUM_MINUTES, task.getMinutes());
        values.put(KEY_NAME, task.getName());
        values.put(KEY_DESC, task.getDesc());
        values.put(KEY_URGENCY, task.getUrg());
        values.put(KEY_COMPLETION_STATUS, task.getCompletion());
        values.put(KEY_TIMER_NUM, task.getTimerNum());

// Inserting Row
        db.insert(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    public Task getTask(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TASKS, new String[]{KEY_ID, KEY_NUM_MINUTES,
                        KEY_NAME, KEY_DESC, KEY_URGENCY, KEY_COMPLETION_STATUS, KEY_TIMER_NUM, KEY_RUNNING, KEY_TIMER_START}, KEY_ID + "=?",
                new String[]{String.valueOf(id)}, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        Task information = new Task(
                Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)),
                cursor.getString(2),
                cursor.getString(3),
                cursor.getString(4),
                cursor.getString(5),
                Integer.parseInt(cursor.getString(6)),
                "1".equals(cursor.getString(7)),
                Long.parseLong(cursor.getString(8)));
// return shop
        return information;
    }

    private List<Task> getTaskList(Cursor cursor) {
        List<Task> taskList = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task();
                task.setId(Integer.parseInt(cursor.getString(0)));
                task.setMinutes(Integer.parseInt(cursor.getString(1)));
                task.setName(cursor.getString(2));
                task.setDesc(cursor.getString(3));
                task.setUrg(cursor.getString(4));
                task.setCompletion(cursor.getString(5));
                task.setTimerNum(Integer.parseInt(cursor.getString(6)));
                task.setIsRunning("1".equals(cursor.getString(7)));
                task.setTimerStart(cursor.getString(8) == null ? 0 : Long.parseLong(cursor.getString(8)));
// Adding contact to list
                taskList.add(task);
            } while (cursor.moveToNext());
        }
        return taskList;
    }

    public List<Task> getAllTasks() {
// Select All Query
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        return getTaskList(cursor);
    }

    public List<Task> getTasksAlphabetized(){
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, "task_name");

        return getTaskList(cursor);
    }

    public List<Task> getTasksUrgent(){
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, "task_urgency");

        return getTaskList(cursor);
    }

    public List<Task> getTasksMostTime(){
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, "task_minutes DESC");

        return getTaskList(cursor);
    }

    public List<Task> getTasksLeastTime(){
        List<Task> taskList = new ArrayList<Task>();
        String selectQuery = "SELECT * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.query(TABLE_TASKS, null, null, null, null, null, "task_minutes ASC");

        return getTaskList(cursor);
    }

    public int getTaskCount() {
        String countQuery = "SELECT * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

// return count
        return cursor.getCount();
    }

    public int updateTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(KEY_NAME, task.getName());
        values.put(KEY_DESC, task.getDesc());
        values.put(KEY_URGENCY, task.getUrg());
        values.put(KEY_NUM_MINUTES, task.getMinutes());
        values.put(KEY_COMPLETION_STATUS, task.getCompletion());
        values.put(KEY_TIMER_NUM, task.getTimerNum());
        values.put(KEY_RUNNING, task.isRunning());
        values.put(KEY_TIMER_START, task.getTimerStart());

// updating row
        return db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[]{String.valueOf(task.getId())});
    }

    // Deleting a shop
    public void deleteTask(Task task) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getId()) });
        db.close();
    }
}

