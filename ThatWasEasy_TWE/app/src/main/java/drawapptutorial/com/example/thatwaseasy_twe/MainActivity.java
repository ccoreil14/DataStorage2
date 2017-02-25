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
import android.widget.EditText;

import java.util.List;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private Dialog myDialog;
    private EditText nameField;
    private EditText descField;
    private EditText minuteField;
    private Button addTaskBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        DBHandler db = new DBHandler(this);

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addTaskBtn = (Button) findViewById(R.id.addBtn);
        addTaskBtn.setOnClickListener(this);
        addTaskBtn.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                callAddTaskDialog();
            }
        });

        Log.d("Insert: ", "Inserting ..");
        db.addTask(new Task(3, "Make Test Tasks", "I need to make a test task to make sure all the functions are all working appropriately", "High", "Not Complete", 0));
        db.addTask(new Task(1, "Run Test", "Run test to make sure all the functions are all working appropriately", "High", "Not Complete", 0));


        Log.d("Reading: ", "Reading all shops after adding..");
        List<Task> tasks= db.getAllTasks();

        for (Task task: tasks) {
            String log = "Id: " + task.getId() + " ,Name: " + task.getName() + " ,Description: " + task.getDesc() + " ,Estimated Minutes to complete: " + task.getMinutes() + " ,Urgency: " + task.getUrg() + " ,Is it Completed?: " + task.getCompletion() + " ,Current Timer Num: " + task.getTimerNum();
            // Writing shops to log
            Log.d("Task: : ", log);
        }

        for(int i = 1; i <=2; i++) db.deleteTask(db.getTask(i));

        Log.d("Reading: ", "Reading all shops again after deleting..");
        tasks = db.getAllTasks();

        for (Task task: tasks) {
            String log = "Id: " + task.getId() + " ,Name: " + task.getName() + " ,Description: " + task.getDesc() + " ,Estimated Minutes to complete: " + task.getMinutes() + " ,Urgency: " + task.getUrg() + " ,Is it Completed?: " + task.getCompletion() + " ,Current Timer Num: " + task.getTimerNum();
            // Writing shops to log
            Log.d("Task Yo: : ", log);
        }
    }




    private void callAddTaskDialog()
    {
        myDialog = new Dialog(this);
        myDialog.setContentView(R.layout.add_task_layout);
        myDialog.setCancelable(false);

        nameField = (EditText) myDialog.findViewById(R.id.nameField);
        descField = (EditText) myDialog.findViewById(R.id.descField);
        minuteField = (EditText) myDialog.findViewById(R.id.minuteField);
        myDialog.show();


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
