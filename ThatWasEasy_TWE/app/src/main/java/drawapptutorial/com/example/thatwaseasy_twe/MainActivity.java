package drawapptutorial.com.example.thatwaseasy_twe;

import android.app.Dialog;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private Dialog addTaskDialog;
    private Dialog editTaskDialog;
    private Dialog readTaskDialog;
    private EditText nameField;
    private TextView readName;
    private EditText descField;
    private TextView readDesc;
    private EditText minuteField;
    private TextView optimalTime;
    private Spinner urgencyField;
    private CheckBox isComplete;
    private Button openAddTaskDialogBtn;
    private Button openEditTaskDialogBtn;
    private Button addTaskBtn;
    private Button updateTaskBtn;
    private ListView taskList;
    private Task currentTask;
    private List<Task> tasks;
    private ArrayAdapter<Task> adapter;
    DBHandler db;
    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBHandler(this);
        addTaskDialog = new Dialog(this);
        readTaskDialog = new Dialog(this);
        editTaskDialog = new Dialog(this);
        addTaskDialog.setContentView(R.layout.add_task_layout);
        readTaskDialog.setContentView(R.layout.read_task_layout);
        editTaskDialog.setContentView(R.layout.update_task_layout);
        addTaskDialog.setCancelable(false);

        openEditTaskDialogBtn = (Button) readTaskDialog.findViewById(R.id.openEditDialogBtn);
        openEditTaskDialogBtn.setOnClickListener(this);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tasks = db.getAllTasks();
        taskList = (ListView) findViewById(R.id.TaskList);
        adapter = new ArrayAdapter<Task>(this, R.layout.activity_listview, tasks) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView,parent);
                if(tasks.get(position).getUrg().equals("Low")){
                    view.setBackgroundColor(Color.BLUE);
                }
                else if(tasks.get(position).getUrg().equals("Medium"))
                {
                    view.setBackgroundColor(Color.CYAN);
                }
                else if(tasks.get(position).getUrg().equals("High"))
                {
                    view.setBackgroundColor(Color.YELLOW);
                }
                else if(tasks.get(position).getUrg().equals("Critical"))
                {
                    view.setBackgroundColor(Color.RED);
                }
                return view;
            }
        };
        taskList.setAdapter(adapter);

        taskList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                currentTask = (Task) parent.getItemAtPosition(position);
                Log.d("Task: ", "" + currentTask.getId());
                readName = (TextView) readTaskDialog.findViewById(R.id.readName);
                readDesc = (TextView) readTaskDialog.findViewById(R.id.readDesc);
                optimalTime = (TextView) readTaskDialog.findViewById(R.id.optimalTime);
                isComplete = (CheckBox) readTaskDialog.findViewById(R.id.isComplete);
                readName.setText(currentTask.getName());
                readDesc.setText(currentTask.getDesc());
                optimalTime.setText("" + currentTask.getMinutes());
                isComplete.setText(currentTask.getCompletion());
                readTaskDialog.show();

            }
        });


        openAddTaskDialogBtn = (Button) findViewById(R.id.addBtn);
        openAddTaskDialogBtn.setOnClickListener(this);
//        openAddTaskDialogBtn.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View v){
//
//            }
//        });

        addTaskBtn = (Button) addTaskDialog.findViewById(R.id.addTaskBtn);
        addTaskBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nameField = (EditText) addTaskDialog.findViewById(R.id.nameField);
                descField = (EditText) addTaskDialog.findViewById(R.id.descField);
                minuteField = (EditText) addTaskDialog.findViewById(R.id.minuteField);
                urgencyField = (Spinner) addTaskDialog.findViewById(R.id.urgencySpinner);
                addTaskFromForm(Integer.parseInt(minuteField.getText().toString()), nameField.getText().toString(), descField.getText().toString(), urgencyField.getSelectedItem().toString());
                seeListInConsole();
                nameField.setText("");
                descField.setText("");
                minuteField.setText("");
                urgencyField.setSelection(0);
                updateListView();
                addTaskDialog.dismiss();

            }
        });


//        Log.d("Insert: ", "Inserting ..");
//        db.addTask(new Task(3, "Make Test Tasks", "I need to make a test task to make sure all the functions are all working appropriately", "High", "Not Complete", 0));
//        db.addTask(new Task(1, "Run Test", "Run test to make sure all the functions are all working appropriately", "High", "Not Complete", 0));
//
//
//        Log.d("Reading: ", "Reading all shops after adding..");
//        List<Task> tasks= db.getAllTasks();
//
//        for (Task task: tasks) {
//            String log = "Id: " + task.getId() + " ,Name: " + task.getName() + " ,Description: " + task.getDesc() + " ,Estimated Minutes to complete: " + task.getMinutes() + " ,Urgency: " + task.getUrg() + " ,Is it Completed?: " + task.getCompletion() + " ,Current Timer Num: " + task.getTimerNum();
//            // Writing shops to log
//            Log.d("Task: : ", log);
//        }
//
//        for(int i = 1; i <=2; i++) db.deleteTask(db.getTask(i));
//
//        Log.d("Reading: ", "Reading all shops again after deleting..");
//        tasks = db.getAllTasks();
//
//        for (Task task: tasks) {
//            String log = "Id: " + task.getId() + " ,Name: " + task.getName() + " ,Description: " + task.getDesc() + " ,Estimated Minutes to complete: " + task.getMinutes() + " ,Urgency: " + task.getUrg() + " ,Is it Completed?: " + task.getCompletion() + " ,Current Timer Num: " + task.getTimerNum();
//            // Writing shops to log
//            Log.d("Task Yo: : ", log);
//        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    private void updateListView() {
        tasks = db.getAllTasks();
        adapter = new ArrayAdapter<Task>(this, R.layout.activity_listview, tasks) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = super.getView(position, convertView,parent);
                if(tasks.get(position).getCompletion().equals("Complete")){
                    view.setBackgroundColor(Color.GREEN);
                }
                else if(tasks.get(position).getUrg().equals("Low")){
                    view.setBackgroundColor(Color.BLUE);
                }
                else if(tasks.get(position).getUrg().equals("Medium"))
                {
                    view.setBackgroundColor(Color.CYAN);
                }
                else if(tasks.get(position).getUrg().equals("High"))
                {
                    view.setBackgroundColor(Color.YELLOW);
                }
                else if(tasks.get(position).getUrg().equals("Critical"))
                {
                    view.setBackgroundColor(Color.RED);
                }
                return view;
            }
        };
        taskList.setAdapter(adapter);
    }


    private void addTaskFromForm(int minutesNum, String taskName, String taskDesc, String urgencyType) {
        db.addTask(new Task(minutesNum, taskName, taskDesc, urgencyType, "Not Complete", 0));
    }

    private void updateTaskFromForm(int id, int minutesNum, String taskName, String taskDesc, String urgencyType, String isComplete, int timerNum) {
        Task updatedTask = new Task(id, minutesNum, taskName, taskDesc, urgencyType, isComplete, timerNum);
        db.updateTask(updatedTask);
        updateListView();
    }

    private void seeListInConsole() {
        List<Task> tasks = db.getAllTasks();

        for (Task task : tasks) {
            String log = "Id: " + task.getId() + " ,Name: " + task.getName() + " ,Description: " + task.getDesc() + " ,Estimated Minutes to complete: " + task.getMinutes() + " ,Urgency: " + task.getUrg() + " ,Is it Completed?: " + task.getCompletion() + " ,Current Timer Num: " + task.getTimerNum();
            // Writing shops to log
            Log.d("Task Yo: : ", log);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.addBtn) {
            addTaskDialog.show();
        } else if (v.getId() == R.id.openEditDialogBtn) {
            readTaskDialog.dismiss();
            Log.d("this shit-->", currentTask.getName());

            nameField = (EditText) editTaskDialog.findViewById(R.id.nameField);
            descField = (EditText) editTaskDialog.findViewById(R.id.descField);
            minuteField = (EditText) editTaskDialog.findViewById(R.id.minuteField);
            urgencyField = (Spinner) editTaskDialog.findViewById(R.id.urgencySpinner);

            nameField.setText(currentTask.getName());
            descField.setText(currentTask.getDesc());
            minuteField.setText("" + currentTask.getMinutes());
            switch (currentTask.getUrg()) {
                case "Low":
                    urgencyField.setSelection(0);
                    break;
                case "Medium":
                    urgencyField.setSelection(1);
                    break;
                case "High":
                    urgencyField.setSelection(2);
                    break;
                case "Critical":
                    urgencyField.setSelection(3);
                    break;
            }

            editTaskDialog.show();
            updateTaskBtn = (Button) editTaskDialog.findViewById(R.id.updateTaskBtn);
            updateTaskBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateTaskFromForm(currentTask.getId(), Integer.parseInt(minuteField.getText().toString()), nameField.getText().toString(), descField.getText().toString(), urgencyField.getSelectedItem().toString(), currentTask.getCompletion(), currentTask.getMinutes());
                    editTaskDialog.dismiss();
                }
            });
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("Main Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }
}
