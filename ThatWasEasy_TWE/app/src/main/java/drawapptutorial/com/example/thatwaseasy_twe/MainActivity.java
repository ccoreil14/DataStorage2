package drawapptutorial.com.example.thatwaseasy_twe;

import android.app.Dialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private Dialog addTaskDialog;
    private Dialog editTaskDialog;
    private Dialog readTaskDialog;
    private EditText nameField;
    private EditText descField;
    private EditText minuteField;
    private Spinner urgencyField;
    private CheckBox isComplete;
    private Button openAddTaskDialogBtn;
    private Button openEditTaskDialogBtn;
    private Button addTaskBtn;
    private Button updateTaskBtn;
    DBHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        db = new DBHandler(this);
        addTaskDialog = new Dialog(this);
        addTaskDialog.setContentView(R.layout.add_task_layout);
        addTaskDialog.setCancelable(false);


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        openAddTaskDialogBtn = (Button) findViewById(R.id.addBtn);
        openAddTaskDialogBtn.setOnClickListener(this);
        openAddTaskDialogBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                addTaskDialog.show();
            }
        });

        addTaskBtn = (Button) addTaskDialog.findViewById(R.id.addTaskBtn);
        addTaskBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                nameField = (EditText) addTaskDialog.findViewById(R.id.nameField);
                descField = (EditText) addTaskDialog.findViewById(R.id.descField);
                minuteField = (EditText) addTaskDialog.findViewById(R.id.minuteField);
                urgencyField = (Spinner) addTaskDialog.findViewById(R.id.urgencySpinner);
                addTaskFromForm(Integer.parseInt( minuteField.getText().toString()), nameField.getText().toString(), descField.getText().toString(), urgencyField.getSelectedItem().toString());
                seeListInConsole();
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
    }




    private void addTaskFromForm(int minutesNum, String taskName, String taskDesc, String urgencyType){
        db.addTask(new Task(minutesNum, taskName, taskDesc, urgencyType, "Not Complete", 0));
    }

    private  void updateTaskFromForm(int id, int minutesNum, String taskName, String taskDesc, String urgencyType, String isComplete, int timerNum){
        Task updatedTask = new Task(id, minutesNum, taskName, taskDesc, urgencyType, isComplete, timerNum);
        db.updateTask(updatedTask);
    }

    private void seeListInConsole(){
        List<Task> tasks= db.getAllTasks();

        for (Task task: tasks) {
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

    }
}
