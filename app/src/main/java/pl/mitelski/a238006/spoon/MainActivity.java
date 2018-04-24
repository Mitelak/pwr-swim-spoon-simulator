package pl.mitelski.a238006.spoon;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity implements SensorEventListener {
    private Spoon spoon = new Spoon();
    private SensorManager sensorManager;
    private Sensor sensorLight;
    private Sensor sensorGyroscope;

    private long lastUpdate = 0;
    private float last_x;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViewById(R.id.layout).setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_FULLSCREEN);

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        sensorGyroscope = sensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        sensorManager.registerListener(this, sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);

        sensorLight = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_FASTEST);

        setSpoonByLvl();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        Sensor sensor = event.sensor;

        if (sensor.getType() == Sensor.TYPE_GYROSCOPE && spoon.isEmpty()) {
            float x = event.values[0];

            long currentTime = System.currentTimeMillis();

            if (currentTime - lastUpdate > 100) {
                lastUpdate = currentTime;
                float speed_x = Math.abs(x - last_x);
                if (x < -5) {
                    fillWithFood();
                }
                last_x = x;
            }

        }
        if (sensor.getType() == Sensor.TYPE_LIGHT && !spoon.isEmpty()) {
            float light = event.values[0];

            if (light < 10) {
                eatFoodFromSpoon();
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    protected void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        sensorManager.registerListener(this, sensorGyroscope, SensorManager.SENSOR_DELAY_NORMAL);
        sensorManager.registerListener(this, sensorLight, SensorManager.SENSOR_DELAY_NORMAL);
    }

    private void fillWithFood() {
        ImageView food = findViewById(R.id.food);
        food.setImageResource(spoon.getFood());
        food.setVisibility(View.VISIBLE);
    }

    private void eatFoodFromSpoon() {
        ImageView foodView = findViewById(R.id.food);
        foodView.setVisibility(View.INVISIBLE);
        spoon.eat();
        setSpoonByLvl();
    }

    private void setSpoonByLvl() {
        ImageView spoonView = findViewById(R.id.spoon);
        spoonView.setImageResource(spoon.getSpoon());
    }
}
