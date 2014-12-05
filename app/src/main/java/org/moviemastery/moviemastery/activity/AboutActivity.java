package org.moviemastery.moviemastery.activity;

import android.app.Activity;
import android.os.Bundle;
import android.text.method.LinkMovementMethod;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import org.moviemastery.moviemastery.R;

import hugo.weaving.DebugLog;

public class AboutActivity extends Activity {

    @DebugLog
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        TextView retrofit = (TextView) findViewById(R.id.retrofitLabel);
        retrofit.setMovementMethod(LinkMovementMethod.getInstance());
        TextView butterknife = (TextView) findViewById(R.id.butterknifeLabel);
        butterknife.setMovementMethod(LinkMovementMethod.getInstance());
        TextView guava = (TextView) findViewById(R.id.guavaLabel);
        guava.setMovementMethod(LinkMovementMethod.getInstance());
        TextView apacheCommons = (TextView) findViewById(R.id.apacheCommonsLabel);
        apacheCommons.setMovementMethod(LinkMovementMethod.getInstance());
        TextView easyAdapter = (TextView) findViewById(R.id.easyAdapterLabel);
        easyAdapter.setMovementMethod(LinkMovementMethod.getInstance());
        getActionBar().setDisplayShowHomeEnabled(false);
        getActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_about, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
