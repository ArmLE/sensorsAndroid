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

public class MainActivity extends AppCompatActivity {

    int whip=0;
    SensorManager sensorManager;
    Sensor sensorGra;
    Sensor sensorMag;

    PointGravity pg;
    PointMagnetic pm;

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
        public TextView y;
        public TextView z;

        public PointMagnetic(TextView x, TextView y, TextView z) {
            this.x = x;
            this.y = y;
            this.z = z;
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
      if(sensorGra == null || sensorMag == null)
         finish();
      sensorManager.registerListener(sensorEventListener, sensorGra,SensorManager.SENSOR_DELAY_NORMAL);
      sensorManager.registerListener(sensorEventListener, sensorMag,SensorManager.SENSOR_DELAY_NORMAL);

      pg = new PointGravity((TextView)findViewById(R.id.gX),(TextView)findViewById(R.id.gY),(TextView)findViewById(R.id.gZ));
      pm = new PointMagnetic((TextView)findViewById(R.id.mX),(TextView)findViewById(R.id.mY),(TextView)findViewById(R.id.mZ));
    }

    private SensorEventListener sensorEventListener = new SensorEventListener() {
        @Override
        public void onSensorChanged(SensorEvent event) {
            float gfX = 0,gfY,gfZ;
            float mfX,mfY,mfZ;

            if(event.sensor.getType()==Sensor.TYPE_ACCELEROMETER){
                gfX= event.values[0];
                gfY= event.values[1];
                gfZ= event.values[2];

                pg.x.setText(String.valueOf(gfX));
                pg.y.setText(String.valueOf(gfY));
                pg.z.setText(String.valueOf(gfZ));
            }
            if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD){
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
            /*
            for(int i=0; i < event.values.length;i++){
                Log.d("VALOR "+i+":",""+event.values[i]);
            }*/
            start();
        }

        @Override
        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }
    };
    //start();
    private void start(){
        sensorManager.registerListener(sensorEventListener, sensorGra,SensorManager.SENSOR_DELAY_NORMAL);
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



