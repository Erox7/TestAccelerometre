package udl.eps.testaccelerometre;

import android.app.Activity;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import android.widget.TextView;
import android.widget.Toast;
/*
BONES PRACTIQUES APLICADES:
1. Verify sensors before you use them
2. Unregister sensors listeners.
3. Don't block the onSensorChanged() method
4. Choose sensor delays carefully
5. Avoid using deprecated methods or sensor types
6. Test your sensor code on a physycal device or use a sensor simulator

El 7é no l'aplique, l'afegim al manifest.

 */
public class TestAccelerometreActivity extends Activity implements SensorEventListener {
  private SensorManager sensorManager;
  private boolean color = false;
  private TextView view;
  private long lastUpdate;


/** Called when the activity is first created. */

  @Override
  public void onCreate(Bundle savedInstanceState) {


    super.onCreate(savedInstanceState);
    setContentView(R.layout.main);
    view = (TextView) findViewById(R.id.textView2);
    view.setBackgroundColor(Color.GREEN);
    TextView bottomView = (TextView) findViewById(R.id.textView3);
    bottomView.setBackgroundColor(Color.YELLOW);
    //ACCELOMETER
    sensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
    TextView center = (TextView) findViewById(R.id.textView);
    if (sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER) != null) {
      Sensor accelometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
      sensorManager.registerListener(this,
              accelometer,
              SensorManager.SENSOR_DELAY_NORMAL);
      // register this class as a listener for the accelerometer sensor

      String capabilities = "Resolution: " + accelometer.getResolution() + "\nPower: " +
              accelometer.getPower() + "\nMaximum Range: " + accelometer.getMaximumRange();
      center.setText(getString(R.string.shake) + "\n" + capabilities);
    }else{
      center.setText(getString(R.string.noAccel));
    }

    //LIGHT SENSOR
    if (sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT) != null) {
      Sensor light = sensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
      sensorManager.registerListener(this,
              light,
              SensorManager.SENSOR_DELAY_NORMAL);
      // register this class as a listener for the accelerometer sensor
      bottomView.setText(getString(R.string.lightSensorUp) + "\n" + light.getMaximumRange());
    }else{
      bottomView.setText(getString(R.string.noLightSensor));
    }


      lastUpdate = System.currentTimeMillis();

  }

  @Override
  public void onSensorChanged(SensorEvent event) {
          getAccelerometer(event);
  }

  private void getAccelerometer(SensorEvent event) {
    float values[] = event.values;
    
    float x = values[0];
    float y = values[1];
    float z = values[2];

    float accelationSquareRoot = (x * x + y * y + z * z)
        / (SensorManager.GRAVITY_EARTH * SensorManager.GRAVITY_EARTH);
    long actualTime = System.currentTimeMillis();
    if (accelationSquareRoot >= 2) 
    {
      if (actualTime - lastUpdate < 200) {
        return;
      }
      lastUpdate = actualTime;

      Toast.makeText(this, R.string.shuffed, Toast.LENGTH_SHORT).show();
      if (color) {
        view.setBackgroundColor(Color.GREEN);

      } else {
        view.setBackgroundColor(Color.RED);
      }
      color = !color;
    }
  }

  @Override
  public void onAccuracyChanged(Sensor sensor, int accuracy) {
    // Do something here if sensor accuracy changes.
  }

  @Override
  protected void onPause() {
    // unregister listener
    super.onPause();
    sensorManager.unregisterListener(this);
  }
} 
