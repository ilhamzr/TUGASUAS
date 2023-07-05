package com.example.projekakhirmulai;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.ProgressBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    TextView txt_currentAccel, txt_prevAccel, txt_acceleration;
    ProgressBar prog_shakeMeter;
    //  define the sensor variabel
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;
    private double accelerationCurrentValue;
    private double accelerationPreviousValue;


    private double changeInAccelleration;
    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent sensorEvent) {
            float x = sensorEvent.values[0];
            float y = sensorEvent.values[1];
            float z = sensorEvent.values[2];

            accelerationCurrentValue = Math.sqrt((x * x + y * y + z * z));
            double changeInAccelleration = Math.abs(accelerationCurrentValue - accelerationPreviousValue);
            accelerationPreviousValue = accelerationCurrentValue;
            //  update text views
            txt_currentAccel.setText("current = " + (int) accelerationCurrentValue);
            txt_prevAccel.setText("prev = " + (int) accelerationPreviousValue);
            txt_acceleration.setText("Acceleatuion chnge = " + (int) changeInAccelleration);

            prog_shakeMeter.setProgress( (int)changeInAccelleration);

            // change colors based on amount of shaking
            if (changeInAccelleration > 14){
                txt_acceleration.setBackgroundColor(Color.RED);
            }
            else if (changeInAccelleration > 5) {
                txt_acceleration.setBackgroundColor(Color.parseColor("#fcb103"));
            }
            else if (changeInAccelleration > 2 ) {
                txt_acceleration.setBackgroundColor((Color.YELLOW));
            }
            else {
                txt_acceleration.setBackgroundColor(getResources().getColor(com.google.android.material.R.color.design_default_color_background));
            }

        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int i) {

        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        txt_acceleration = findViewById(R.id.txt_accel);
        txt_currentAccel = findViewById(R.id.txt_currentAccel);
        txt_prevAccel = findViewById(R.id.txt_prevAccel);

        prog_shakeMeter = findViewById(R.id.prog_shakeMeter);

        //  initialze sensor objects
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

    }

    protected void onResume() {
        super.onResume();
        mSensorManager.registerListener(sensorEventListener, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(sensorEventListener);
    }
}
