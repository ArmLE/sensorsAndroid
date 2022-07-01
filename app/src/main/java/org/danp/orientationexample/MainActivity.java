package org.danp.orientationexample;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.TextView;

import java.util.Arrays;

public class MainActivity extends AppCompatActivity {

    int whip=0;
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
      /*
      if(sensorGra == null || sensorMag == null)
         finish();
      */

      /*
      sensorManager.registerListener(sensorEventListener, sensorGra,SensorManager.SENSOR_DELAY_NORMAL);
      sensorManager.registerListener(sensorEventListener, sensorMag,SensorManager.SENSOR_DELAY_NORMAL);
      */
      pg = new PointGravity((TextView)findViewById(R.id.gX),(TextView)findViewById(R.id.gY),(TextView)findViewById(R.id.gZ));
      pm = new PointMagnetic((TextView)findViewById(R.id.mX));
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            /*
            float gfX = 0,gfY,gfZ;
            float mfX,mfY,mfZ;
            */
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

            /*
            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                gfX= event.values[0];
                gfY= event.values[1];
                gfZ= event.values[2];

                pg.x.setText(String.valueOf(gfX));
                pg.y.setText(String.valueOf(gfY));
                pg.z.setText(String.valueOf(gfZ));
            }else if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
                mfX= event.values[0];
                mfY= event.values[1];
                mfZ= event.values[2];

                pm.x.setText(String.valueOf(mfX));
                pm.y.setText(String.valueOf(mfY));
                pm.z.setText(String.valueOf(mfZ));
            }

            System.out.println("valor giro " + gfX);
            if(gfX<-5 && whip==0){
                whip++;
                getWindow().getDecorView().setBackgroundColor(Color.BLUE);
            }else if(gfX>5 && whip==1){
                whip++;
                getWindow().getDecorView().setBackgroundColor(Color.RED);
            }
            if(whip==2){
                sound();
                whip=0;
            }
            */

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
    private void sound(){
        MediaPlayer mediaPlayer= MediaPlayer.create(this,R.raw.latigo);
        mediaPlayer.start();
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



