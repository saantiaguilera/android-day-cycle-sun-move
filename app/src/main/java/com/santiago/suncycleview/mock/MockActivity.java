package com.santiago.suncycleview.mock;

import android.app.Activity;
import android.os.Bundle;

import com.santiago.suncycleview.R;

/**
 * Created by santiago on 27/03/16.
 */
public class MockActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_mock);
    }
}
