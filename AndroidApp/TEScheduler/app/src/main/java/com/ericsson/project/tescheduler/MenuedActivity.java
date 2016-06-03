package com.ericsson.project.tescheduler;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

/**
 * Created by eilygar on 2016-04-29.
 */
public class MenuedActivity extends AppCompatActivity{

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_navigation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if(id == R.id.action_mainmap) {
            startActivity(new Intent(this, MapsMainActivity.class));
        }

        if (id == R.id.action_parameters) {
            startActivity(new Intent(this, ParametersActivity.class));
        }

        if (id == R.id.action_confirmpoi) {
            startActivity(new Intent(this, ConfirmPoIActivity.class));
        }

        if (id == R.id.action_directions) {
            startActivity(new Intent(this, DirectionsActivity.class));
        }

        return super.onOptionsItemSelected(item);
    }
}
