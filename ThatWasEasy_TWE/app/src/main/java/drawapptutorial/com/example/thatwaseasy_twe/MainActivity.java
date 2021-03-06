package drawapptutorial.com.example.thatwaseasy_twe;

import android.app.Dialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;

import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private Dialog addTaskDialog;
    private Dialog editTaskDialog;
    private Dialog readTaskDialog;
    private EditText nameField;
    private TextView readName;
    private TextView readUrgency;
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
    private Button deleteTaskBtn;
    private Button timerBtn;
    private ListView taskList;
    private Task currentTask;
    private List<Task> tasks;
    private ArrayAdapter<Task> adapter;
    private RadioGroup sortBtns;
    private RadioButton sortUrgBtn;
    private RadioButton sortAlphBtn;
    private RadioButton sortMostTimeBtn;
    private RadioButton sortLeastTimeBtn;
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

        deleteTaskBtn = (Button) readTaskDialog.findViewById(R.id.deleteTaskBtn);
        deleteTaskBtn.setOnClickListener(this);

        timerBtn = (Button) readTaskDialog.findViewById(R.id.timerBtn);
        timerBtn.setOnClickListener(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sortBtns = (RadioGroup) this.findViewById(R.id.sortRadiosBtns);
        sortBtns.check(R.id.sortUrgencyBtn);

        sortUrgBtn = (RadioButton) this.findViewById(R.id.sortUrgencyBtn);
        sortAlphBtn = (RadioButton) this.findViewById(R.id.sortAlphabeticalBtn);
        sortMostTimeBtn = (RadioButton) this.findViewById(R.id.sortMostTimeBtn);
        sortLeastTimeBtn = (RadioButton) this.findViewById(R.id.sortLeastTimeBtn);

        tasks = db.getAllTasks();
        taskList = (ListView) findViewById(R.id.TaskList);

        updateSorting();
        adapter = new ArrayAdapter<Task>(this, R.layout.activity_listview, tasks) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                TextView view = (TextView) super.getView(position, convertView,parent);

                view.setText(tasks.get(position).toString());

                if (currentTimer != null && tasks.get(position) != currentTask) {
                    view.setBackgroundColor(Color.GRAY);
                }
                else if(tasks.get(position).getCompletion().equals("Complete")){
                    view.setBackgroundColor(Color.GREEN);
                }
                else if(tasks.get(position).getUrg().equals("Small")){
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
                if (currentTimer != null && tasks.get(position) != currentTask)
                    return;

                currentTask = tasks.get(position);
                Log.d("Task: ", "" + currentTask.getId());
                readName = (TextView) readTaskDialog.findViewById(R.id.readName);
                readUrgency = (TextView) readTaskDialog.findViewById(R.id.readUrgency);
                readDesc = (TextView) readTaskDialog.findViewById(R.id.readDesc);
                optimalTime = (TextView) readTaskDialog.findViewById(R.id.optimalTime);
                isComplete = (CheckBox) readTaskDialog.findViewById(R.id.isComplete);
                readName.setText(currentTask.getName());
                readDesc.setText(currentTask.getDesc());
                readUrgency.setText(currentTask.getUrg()+" Urgency");
                optimalTime.setText("Optimal Minutes:" + currentTask.getMinutes());
                isComplete.setText(currentTask.getCompletion());
                if(currentTask.getCompletion().equals("Not Complete")){
                    isComplete.setChecked(false);
                }
                else{
                    isComplete.setChecked(true);
                }
                isComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        if(isChecked){
                            currentTask.setCompletion("Complete");
                        }
                        else{
                            currentTask.setCompletion("Not Complete");
                        }
                        isComplete.setText(currentTask.getCompletion());
                        updateTaskFromForm(currentTask.getId(), currentTask.getMinutes(),currentTask.getName(),currentTask.getDesc(),currentTask.getUrg(),currentTask.getCompletion(),currentTask.getTimerNum());
                    }
                });
                readTaskDialog.show();
           }
        });

        openAddTaskDialogBtn = (Button) findViewById(R.id.addBtn);
        openAddTaskDialogBtn.setOnClickListener(this);

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
//            String log = "Id: " + task.getId() + " ,Name: " + task.getName() + " ,Description: " + task.getDesc() + " ,Estimated Minutes to complete: " + task.getMinutes() + " ,Urgency: " + task.getUrg() + " ,Is it Completed?: " + task.getCompletion() + " ,Current TimerTask Num: " + task.getTimerNum();
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
//            String log = "Id: " + task.getId() + " ,Name: " + task.getName() + " ,Description: " + task.getDesc() + " ,Estimated Minutes to complete: " + task.getMinutes() + " ,Urgency: " + task.getUrg() + " ,Is it Completed?: " + task.getCompletion() + " ,Current TimerTask Num: " + task.getTimerNum();
//            // Writing shops to log
//            Log.d("Task Yo: : ", log);
//        }
        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();

        sortBtns.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
//                Log.d("Clicked","Button");
                updateListView();
            }
        });

        for (Task task : tasks) {
            if (task.isRunning()) {
                currentTask = task;
                if (currentTask.getTimerStart() != 0)
                    currentTask.setTimerNum((int)(System.currentTimeMillis() - currentTask.getTimerStart()));
                startTimer();
            }
        }
    }

    private void updateListView() {
        Log.d("Update called: ", "True");
        //tasks = db.getAllTasks();
        updateSorting();
        adapter.notifyDataSetChanged();
    }


    private void addTaskFromForm(int minutesNum, String taskName, String taskDesc, String urgencyType) {
        db.addTask(new Task(minutesNum, taskName, taskDesc, urgencyType, "Not Complete", 0, false, 0));
        updateListView();
    }

    private void updateTaskFromForm(int id, int minutesNum, String taskName, String taskDesc, String urgencyType, String isComplete, int timerNum) {
        Task updatedTask = new Task(id, minutesNum, taskName, taskDesc, urgencyType, isComplete, timerNum, false, 0);
        db.updateTask(updatedTask);
        updateListView();
    }

    private void seeListInConsole() {
        for (Task task : tasks) {
            String log = "Id: " + task.getId() + " ,Name: " + task.getName() + " ,Description: " + task.getDesc() + " ,Estimated Minutes to complete: " + task.getMinutes() + " ,Urgency: " + task.getUrg() + " ,Is it Completed?: " + task.getCompletion() + " ,Current TimerTask Num: " + task.getTimerNum();
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
                case "Small":
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
        }else if(v.getId() == R.id.deleteTaskBtn){
            db.deleteTask(currentTask);
            readTaskDialog.dismiss();
            updateListView();
        }
        else if (v.getId() == R.id.timerBtn) {
            startOrFinishTimer();
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

    public void updateSorting(){
        tasks.clear();
        if(sortUrgBtn.isChecked()){
            Log.d("q","Urgent!");
            tasks.addAll(db.getTasksUrgent());
        }else if(sortAlphBtn.isChecked()){
            Log.d("q","Alphabetical!");
            tasks.addAll(db.getTasksAlphabetized());
        }else if(sortMostTimeBtn.isChecked()){
            Log.d("q","Most Time!");
            tasks.addAll(db.getTasksMostTime());
        }else if(sortLeastTimeBtn.isChecked()){
            Log.d("q","Least Time!");
            tasks.addAll(db.getTasksLeastTime());
        }

        if (currentTask != null) {
            for (Task task : tasks) {
                if (currentTask.getId() == task.getId())
                    currentTask = task;
            }
        }
    }

    private void disableInterface() {
        openAddTaskDialogBtn.setEnabled(false);
        sortBtns.setEnabled(false);
        sortUrgBtn.setEnabled(false);
        sortAlphBtn.setEnabled(false);
        sortMostTimeBtn.setEnabled(false);
        sortLeastTimeBtn.setEnabled(false);
        openEditTaskDialogBtn.setEnabled(false);
        deleteTaskBtn.setEnabled(false);
    }

    private void enableInterface() {
        openAddTaskDialogBtn.setEnabled(true);
        sortBtns.setEnabled(true);
        sortUrgBtn.setEnabled(true);
        sortAlphBtn.setEnabled(true);
        sortMostTimeBtn.setEnabled(true);
        sortLeastTimeBtn.setEnabled(true);
        openEditTaskDialogBtn.setEnabled(true);
        deleteTaskBtn.setEnabled(true);
    }

    private void startOrFinishTimer() {
        if (currentTimer == null) {
            currentTask.setTimerStart(System.currentTimeMillis() - currentTask.getTimerNum());
            startTimer();
        }
        else
            finishTimer();
    }

    private TimerTask currentTimer;
    private void startTimer() {
        currentTimer = new TimerTask(new TimerCallback() {
            @Override
            public void update(double time) {
                updateTime(time);
            }
        });
        currentTimer.execute(currentTask.getTimerNum() / 1000.0);
        disableInterface();
        readTaskDialog.dismiss();
        timerBtn.setText("Stop Timer");

        currentTask.setIsRunning(true);
        db.updateTask(currentTask);
    }

    private void updateTime(double time) {
        if ((time / 60.0 > currentTask.getMinutes()) != (currentTask.getTimerNum() / 1000.0) / 60.0 > currentTask.getMinutes()) {
            notifyOverTime();
        }

        currentTask.setTimerNum((int)(time * 1000));
        db.updateTask(currentTask);
        updateListView();
    }

    private void finishTimer() {
        currentTimer.finish();
        currentTimer = null;
        enableInterface();
        readTaskDialog.dismiss();
        timerBtn.setText("Start Timer");

        currentTask.setIsRunning(false);
        db.updateTask(currentTask);
    }

    private void notifyOverTime() {
        currentTask.setUrg("Critical");
        updateTaskFromForm(currentTask.getId(),currentTask.getMinutes(), currentTask.getName(),currentTask.getDesc(), currentTask.getUrg(),currentTask.getCompletion(),currentTask.getTimerNum());
        updateListView();
        Intent contentIntent = new Intent(this, MainActivity.class);
        PendingIntent pIntent = PendingIntent.getActivity(this, (int) System.currentTimeMillis(), contentIntent, 0);

        Notification notification = new Notification.Builder(this)
                .setContentTitle("Over time on " + currentTask.getName())
                .setContentText("You have gone over the preferred time of " + currentTask.getMinutes() + " minutes on the task " + currentTask.getName())
                .setContentIntent(pIntent)
                .setSmallIcon(R.drawable.common_plus_signin_btn_text_dark)
                .setAutoCancel(true)
                .build();

        NotificationManager manager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, notification);
    }
}
