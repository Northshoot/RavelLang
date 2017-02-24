package edu.stanford.tethys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import org.stanford.ravel.generated.GatewaySpace;

/**
 * Created by gcampagn on 2/6/17.
 */
public class MainActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        // start the service if not already
        startService(new Intent(this, GatewaySpace.class));
    }
}