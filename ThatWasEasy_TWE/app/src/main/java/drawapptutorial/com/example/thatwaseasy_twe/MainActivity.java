package drawapptutorial.com.example.thatwaseasy_twe;

import android.app.Dialog;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity implements SensorEventListener, View.OnClickListener {

    private Dialog myDialog;
    private EditText nameField;
    private EditText descField;
    private EditText minuteField;
    private Button addTaskBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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
