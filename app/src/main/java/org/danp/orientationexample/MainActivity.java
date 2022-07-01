package org.danp.orientationexample;

import androidx.appcompat.app.AppCompatActivity;

import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    SensorManager sensorManager;
    Sensor sensorGra;
    Sensor sensorMag;

    PointGravity pg;
    PointMagnetic pm;

    float[] accelerometerReading = new float[3];
    float[] magnetometerReading = new float[3];

    public float[] rotationMatrix = new float[9];
    public float[] inclinationMatrix = new float[9];
    public float[] orientationAngles = new float[]{0,0,0};
    public float inclinacion = 0;

    private class PointGravity{
        public TextView x;
        public TextView y;
        public TextView z;

        public PointGravity(TextView x, TextView y, TextView z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }
    }
    private class PointMagnetic{
        public TextView x;

        public PointMagnetic(TextView x) {
            this.x = x;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        init();
    }
    //TYPE_MAGNETIC_FIELD
    private void init(){
      sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
      sensorGra = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      sensorMag = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);

      pg = new PointGravity((TextView)findViewById(R.id.gX),(TextView)findViewById(R.id.gY),(TextView)findViewById(R.id.gZ));
      pm = new PointMagnetic((TextView)findViewById(R.id.mX));
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {

            start();

            SensorManager.getRotationMatrix(rotationMatrix, inclinationMatrix,
                    accelerometerReading, magnetometerReading);
            SensorManager.getOrientation(rotationMatrix, orientationAngles);
            inclinacion = SensorManager.getInclination(inclinationMatrix);

            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                System.arraycopy(event.values, 0, accelerometerReading,
                        0, accelerometerReading.length);
            } else if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
                System.arraycopy(event.values, 0, magnetometerReading,
                        0, magnetometerReading.length);
            }


            updateOrientationAngles();

            System.out.println(Arrays.toString(orientationAngles));

            pg.x.setText(String.valueOf(Math.round(Math.toDegrees(orientationAngles[0]))));
            pg.y.setText(String.valueOf(Math.round(Math.toDegrees(orientationAngles[1]))));
            pg.z.setText(String.valueOf(Math.round(Math.toDegrees(orientationAngles[2]))));
            pm.x.setText(String.valueOf(Math.round(Math.toDegrees(inclinacion))));
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        public void updateOrientationAngles() {
            // Update rotation matrix, which is needed to update orientation angles.
            SensorManager.getRotationMatrix(rotationMatrix, null,
                    accelerometerReading, magnetometerReading);

            // "mRotationMatrix" now has up-to-date information.

            SensorManager.getOrientation(rotationMatrix, orientationAngles);

            // "mOrientationAngles" now has up-to-date information.
        }
    };
    //start();
    private void start(){
        sensorManager.registerListener(sensorEventListener, sensorGra,SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(sensorEventListener, sensorMag,SensorManager.SENSOR_DELAY_NORMAL);
    }
    private void stop(){
        sensorManager.unregisterListener(sensorEventListener);
    }

    @Override
    protected void onPause(){
        stop();
        super.onPause();
    }
    @Override
    protected void onResume(){
        start();
        super.onResume();
    }
}



