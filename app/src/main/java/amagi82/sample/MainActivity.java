/*
 *
 *  * Copyright (C) 2015 James Pekarek
 *  *
 *  * Licensed under the Apache License, Version 2.0 (the "License");
 *  * you may not use this file except in compliance with the License.
 *  * You may obtain a copy of the License at
 *  *
 *  *     http://www.apache.org/licenses/LICENSE-2.0
 *  *
 *  * Unless required by applicable law or agreed to in writing, software
 *  * distributed under the License is distributed on an "AS IS" BASIS,
 *  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  * See the License for the specific language governing permissions and
 *  * limitations under the License.
 *
 */

package amagi82.sample;

import android.app.Activity;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import amagi82.flexibleratingbar.FlexibleRatingBar;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FlexibleRatingBar ratingBar = (FlexibleRatingBar) findViewById(R.id.flexibleRatingBar);
        FlexibleRatingBar ratingBar2 = (FlexibleRatingBar) findViewById(R.id.flexibleRatingBar2);
        FlexibleRatingBar ratingBar3 = (FlexibleRatingBar) findViewById(R.id.flexibleRatingBar3);
        FlexibleRatingBar ratingBar4 = (FlexibleRatingBar) findViewById(R.id.flexibleRatingBar4);

        ratingBar2.setPolygonVertices(0);
        ratingBar3.setPolygonVertices(4);
        ratingBar3.setStrokeWidth(5);
        ratingBar4.setPolygonVertices(6);
        ratingBar4.setColorFillOn(Color.rgb(111,63,182));
        ratingBar4.setColorFillOff(Color.argb(55,145,122,235));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
