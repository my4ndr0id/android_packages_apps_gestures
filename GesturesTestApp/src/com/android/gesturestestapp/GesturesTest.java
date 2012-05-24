/* Copyright (c) 2012, Code Aurora Forum. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are
 * met:
 *     * Redistributions of source code must retain the above copyright
 *       notice, this list of conditions and the following disclaimer.
 *     * Redistributions in binary form must reproduce the above
 *       copyright notice, this list of conditions and the following
 *       disclaimer in the documentation and/or other materials provided
 *       with the distribution.
 *     * Neither the name of Code Aurora Forum, Inc. nor the names of its
 *       contributors may be used to endorse or promote products derived
 *       from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED "AS IS" AND ANY EXPRESS OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON-INFRINGEMENT
 * ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR
 * BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN
 * IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 */

package com.android.gesturestestapp;

import android.app.Activity;
import android.content.Context;
import android.hardware.gesturedev.*;
import android.hardware.gesturedev.GestureParameters.CoordinateRange;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class GesturesTest extends Activity implements OnClickListener, GestureDevice.GestureListener {
	TextView status;
	Button   button1;
	Button   button2;
	Button   button3;
    Button   button4;
    Button   button5;
    Button   button6;
    Button   button7;
    private static final String TAG = "GesturesTest:";

    private static final int MENU_GROUP_MODE = 1;
    private static final int MENU_GROUP_SUBMODE = 2;
    private static final int MENU_GROUP_ORIENTATION = 3;
    private static final int MENU_GROUP_COORDINATE = 4;
    private static final int MENU_GROUP_CLICK_MODE = 5;
    private static final int MENU_GROUP_CURSOR_TYPE = 6;
    private static final int MENU_GROUP_CAMERA = 7;
    private static final int MENU_GROUP_TOUCH_ENABLED = 8;

    private static final int ID_MENU_MODE_OFF = 1;
    private static final int ID_MENU_MODE_NEAR_SWIPE = 2;
    private static final int ID_MENU_MODE_HAND_DETECT = 3;
    private static final int ID_MENU_MODE_ENGAGEMENT = 4;
    private static final int ID_MENU_MODE_ENGAGEMENT_SWIPE = 5;
    private static final int ID_MENU_MODE_HAND_TRACKING = 6;

    private static final int ID_MENU_SUB_MODE_NONE = 7;
	
    private static final int ID_MENU_ORIENTATION_0 = 8;
    private static final int ID_MENU_ORIENTATION_90 = 9;
    private static final int ID_MENU_ORIENTATION_180 = 10;
    private static final int ID_MENU_ORIENTATION_270 = 11;

    private static final int ID_MENU_COORDINATE_MODE_NORMALIZED = 12;
    private static final int ID_MENU_COORDINATE_MODE_SCREEN = 13;

    private static final int ID_MENU_CLICK_MODE_NONE = 14;
    private static final int ID_MENU_CLICK_MODE_HOVER = 15;
    private static final int ID_MENU_CLICK_MODE_POSE = 16;

    private static final int ID_MENU_CURSOR_TYPE_OFF = 17;
    private static final int ID_MENU_CURSOR_TYPE_CROSS = 18;

    private static final int ID_MENU_CAMERA_1 = 19;
    private static final int ID_MENU_CAMERA_2 = 20;

    private static final int ID_MENU_TOUCH_ENABLED = 21;
    private static final int ID_MENU_TOUCH_DISABLED = 22;

    int mode = GestureParameters.GESTURE_MODE_NEAR_SWIPE;
    int submode = GestureParameters.GESTURE_SUB_MODE_NONE;
    int cursor_type = GestureParameters.GESTURE_CURSOR_TYPE_OFF;
    int click_mode = GestureParameters.GESTURE_CLICK_MODE_NONE;
    int coordinate_mode = GestureParameters.GESTURE_COORDINATE_MODE_NORMALIZED;
    int orientation = GestureParameters.GESTURE_ORIENTATION_0;
    boolean touch_enabled = false;
    int camera_id = 1;

    int mNumOfDevices = 0;
    GestureDevice mDevice = null;
    private final GestureDevice.ErrorCallback mErrorCallback = new GestureErrorCallback();

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        button1 = (Button)  this.findViewById(R.id.Button1);
        button2 = (Button)  this.findViewById(R.id.Button2);
        button3 = (Button)  this.findViewById(R.id.Button3);
        button4 = (Button)  this.findViewById(R.id.Button4);
        button5 = (Button)  this.findViewById(R.id.Button5);
        button6 = (Button)  this.findViewById(R.id.Button6);
        button7 = (Button)  this.findViewById(R.id.Button7);
        
        button1.setOnClickListener(this);
        button2.setOnClickListener(this);
        button3.setOnClickListener(this);
        button4.setOnClickListener(this);
        button5.setOnClickListener(this);
        button6.setOnClickListener(this);
        button7.setOnClickListener(this);
    }
    
    public void onClick(View v) 
    {
    	int nMyId = v.getId();
    	if (nMyId==R.id.Button1)
    	{
            Log.e(TAG, "Query Number of Devices");
            mNumOfDevices = GestureDevice.getNumberOfGestureDevices();
            Toast.makeText(getApplicationContext(), "Query Number of Devices: " + mNumOfDevices, Toast.LENGTH_SHORT).show();
    	}
    	else if (nMyId==R.id.Button2)
    	{
    		Log.e(TAG,"Open device");
            if (mNumOfDevices == 0) {
                mNumOfDevices = GestureDevice.getNumberOfGestureDevices();
            }
            if(mNumOfDevices >0) {
                openDevice(mNumOfDevices-1);
            }
    	}
    	else if (nMyId==R.id.Button3)
    	{
    		Log.e(TAG,"Close device");
            closeDevice();
    	}
        else if (nMyId==R.id.Button4)
        {
            Log.e(TAG,"Set Parameters");
            if (mDevice != null) {
                try {
                    GestureParameters param = mDevice.getParameters();
                    param.setCameraInput(camera_id);
                    param.setGestureMode(mode);
                    param.setGestureSubMode(submode);
                    param.setCursorType(cursor_type);
                    param.setClickMode(click_mode);
                    param.setCoordinateMode(coordinate_mode);
                    param.setOrientation(orientation);
                    param.setTouchEnabled(touch_enabled);

                    // set coordinate range
                    DisplayMetrics metrics = new DisplayMetrics(); 
                    getWindowManager().getDefaultDisplay().getMetrics(metrics); 
                    int height = metrics.heightPixels; 
                    int width = metrics.widthPixels; 
                    CoordinateRange range = new CoordinateRange((float)0, (float)width, (float)0, (float)height, (float)0, (float)0);
                    param.setCoordinateRange(range);
                    //param.setExtendedConfig("12345678901234567890123456789012345678901234567890");
                    Log.e(TAG, "Parameters: " + param.flatten());
                    mDevice.setParameters(param);
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error during set parameters", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (nMyId==R.id.Button5)
        {
            Log.e(TAG,"Get Parameters");
            if (mDevice != null) {
                try {
                    GestureParameters param = mDevice.getParameters();
                    if(param != null) {
                        Log.e(TAG, "Parameters: " + param.flatten());
                    }
                    CoordinateRange range = param.getCoordinateRange();
                    if (range != null) {
                        Log.e(TAG, "CoordRange:(" + range.x_min + "," + range.x_max + "),("
                                                  + range.y_min + "," + range.y_max + "),("
                                                  + range.z_min + "," + range.z_max + ")");
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error during get parameters", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (nMyId==R.id.Button6)
        {
            Log.e(TAG,"Start Gestures");
            if (mDevice != null) {
                try {
                    mDevice.startGesture();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error starting gesture", Toast.LENGTH_SHORT).show();
                }
            }
        }
        else if (nMyId==R.id.Button7)
        {
            Log.e(TAG,"Stop Gestures");
            if (mDevice != null) {
                try {
                    mDevice.stopGesture();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Error stopping gesture", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
   
    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);

        SubMenu sm_mode = menu.addSubMenu("Mode");
        sm_mode.add(MENU_GROUP_MODE,ID_MENU_MODE_OFF,Menu.NONE,"Mode Off");
        sm_mode.add(MENU_GROUP_MODE,ID_MENU_MODE_NEAR_SWIPE,Menu.NONE,"Near Swipe");
        sm_mode.add(MENU_GROUP_MODE,ID_MENU_MODE_HAND_DETECT,Menu.NONE,"Hand Detect");
        sm_mode.add(MENU_GROUP_MODE,ID_MENU_MODE_ENGAGEMENT,Menu.NONE,"Engagement");
        sm_mode.add(MENU_GROUP_MODE,ID_MENU_MODE_ENGAGEMENT_SWIPE,Menu.NONE,"Engagement Swipe");
        sm_mode.add(MENU_GROUP_MODE,ID_MENU_MODE_HAND_TRACKING,Menu.NONE,"Hand tracking");
        sm_mode.setGroupCheckable(MENU_GROUP_MODE, true, true);
        sm_mode.getItem(1).setChecked(true);
     
        SubMenu sm_submode = menu.addSubMenu("SubMode");
        sm_submode.add(MENU_GROUP_SUBMODE,ID_MENU_SUB_MODE_NONE,Menu.NONE,"None");
        sm_submode.setGroupCheckable(MENU_GROUP_SUBMODE, true, true);
        sm_submode.getItem(0).setChecked(true);
        
        SubMenu sm_oriet = menu.addSubMenu("Orientation");
        sm_oriet.add(MENU_GROUP_ORIENTATION,ID_MENU_ORIENTATION_0,Menu.NONE,"Orientation 0");
        sm_oriet.add(MENU_GROUP_ORIENTATION,ID_MENU_ORIENTATION_90,Menu.NONE,"Orientation 90");
        sm_oriet.add(MENU_GROUP_ORIENTATION,ID_MENU_ORIENTATION_180,Menu.NONE,"Orientation 180");
        sm_oriet.add(MENU_GROUP_ORIENTATION,ID_MENU_ORIENTATION_270,Menu.NONE,"Orientation270");
        sm_oriet.setGroupCheckable(MENU_GROUP_ORIENTATION, true, true);
        sm_oriet.getItem(0).setChecked(true);
     
        SubMenu sm_coord = menu.addSubMenu("Coordinate");
        sm_coord.add(MENU_GROUP_COORDINATE,ID_MENU_COORDINATE_MODE_NORMALIZED,Menu.NONE,"Normalized");
        sm_coord.add(MENU_GROUP_COORDINATE,ID_MENU_COORDINATE_MODE_SCREEN,Menu.NONE,"Screen");
        sm_coord.setGroupCheckable(MENU_GROUP_COORDINATE, true, true);
        sm_coord.getItem(0).setChecked(true);

        SubMenu sm_click = menu.addSubMenu("click");
        sm_click.add(MENU_GROUP_CLICK_MODE,ID_MENU_CLICK_MODE_NONE,Menu.NONE,"None");
        sm_click.add(MENU_GROUP_CLICK_MODE,ID_MENU_CLICK_MODE_HOVER,Menu.NONE,"hover");
        sm_click.add(MENU_GROUP_CLICK_MODE,ID_MENU_CLICK_MODE_POSE,Menu.NONE,"Pose");
        sm_click.setGroupCheckable(MENU_GROUP_CLICK_MODE, true, true);
        sm_click.getItem(0).setChecked(true);

        SubMenu sm_cursor = menu.addSubMenu("Cursor");
        sm_cursor.add(MENU_GROUP_CURSOR_TYPE,ID_MENU_CURSOR_TYPE_OFF,Menu.NONE,"Cursor Off");
        sm_cursor.add(MENU_GROUP_CURSOR_TYPE,ID_MENU_CURSOR_TYPE_CROSS,Menu.NONE,"Cursor Cross");
        sm_cursor.setGroupCheckable(MENU_GROUP_CURSOR_TYPE, true, true);
        sm_cursor.getItem(0).setChecked(true);

        SubMenu sm_camera = menu.addSubMenu("Camera");
        sm_camera.add(MENU_GROUP_CAMERA,ID_MENU_CAMERA_1,Menu.NONE,"Back Camera");
        sm_camera.add(MENU_GROUP_CAMERA,ID_MENU_CAMERA_2,Menu.NONE,"Front Camera");
        sm_camera.setGroupCheckable(MENU_GROUP_CAMERA, true, true);
        sm_camera.getItem(1).setChecked(true);

        SubMenu sm_touch = menu.addSubMenu("Touch");
        sm_touch.add(MENU_GROUP_TOUCH_ENABLED,ID_MENU_TOUCH_ENABLED,Menu.NONE,"Touch Enabled");
        sm_touch.add(MENU_GROUP_TOUCH_ENABLED,ID_MENU_TOUCH_DISABLED,Menu.NONE,"Touch Disabled");
        sm_touch.setGroupCheckable(MENU_GROUP_TOUCH_ENABLED, true, true);
        sm_touch.getItem(1).setChecked(true);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId()) {
        case ID_MENU_MODE_OFF:
            mode = GestureParameters.GESTURE_MODE_OFF;
            break;
        case ID_MENU_MODE_NEAR_SWIPE:
            mode = GestureParameters.GESTURE_MODE_NEAR_SWIPE;
            break;
        case ID_MENU_MODE_HAND_DETECT:
            mode = GestureParameters.GESTURE_MODE_HAND_DETECT;
            break;
        case ID_MENU_MODE_ENGAGEMENT:
            mode = GestureParameters.GESTURE_MODE_ENGAGEMENT;
            break;
        case ID_MENU_MODE_ENGAGEMENT_SWIPE:
            mode = GestureParameters.GESTURE_MODE_ENGAGEMENT_SWIPE;
            break;
        case ID_MENU_MODE_HAND_TRACKING:
            mode = GestureParameters.GESTURE_MODE_HAND_TRACKING;
            break;
        case ID_MENU_SUB_MODE_NONE:
            submode = GestureParameters.GESTURE_SUB_MODE_NONE;
            break;
        case ID_MENU_ORIENTATION_0:
            orientation = GestureParameters.GESTURE_ORIENTATION_0;
            break;
        case ID_MENU_ORIENTATION_90:
            orientation = GestureParameters.GESTURE_ORIENTATION_90;
            break;
        case ID_MENU_ORIENTATION_180:
            orientation = GestureParameters.GESTURE_ORIENTATION_180;
            break;
        case ID_MENU_ORIENTATION_270:
            orientation = GestureParameters.GESTURE_ORIENTATION_270;
            break;
        case ID_MENU_COORDINATE_MODE_NORMALIZED:
            coordinate_mode = GestureParameters.GESTURE_COORDINATE_MODE_NORMALIZED;
            break;
        case ID_MENU_COORDINATE_MODE_SCREEN:
            coordinate_mode = GestureParameters.GESTURE_COORDINATE_MODE_SCREEN;
            break;
        case ID_MENU_CLICK_MODE_NONE:
            click_mode = GestureParameters.GESTURE_CLICK_MODE_NONE;
            break;
        case ID_MENU_CLICK_MODE_HOVER:
            click_mode = GestureParameters.GESTURE_CLICK_MODE_HOVER;
            break;
        case ID_MENU_CLICK_MODE_POSE:
            click_mode = GestureParameters.GESTURE_CLICK_MODE_POSE;
            break;
        case ID_MENU_CURSOR_TYPE_OFF:
            cursor_type = GestureParameters.GESTURE_CURSOR_TYPE_OFF;
            break;
        case ID_MENU_CURSOR_TYPE_CROSS:
            cursor_type = GestureParameters.GESTURE_CURSOR_TYPE_CROSS;
            break;
        case ID_MENU_CAMERA_1:
            camera_id = 0;
            break;
        case ID_MENU_CAMERA_2:
            camera_id = 1;
            break;
        case ID_MENU_TOUCH_ENABLED:
            touch_enabled = true;
            break;
        case ID_MENU_TOUCH_DISABLED:
            touch_enabled = false;
            break;
        default:
            break;
        }
        item.setChecked(true);
        return super.onOptionsItemSelected(item);
    }

    private void openDevice(int deviceId) {
        closeDevice();

        try {
            mDevice = GestureDevice.open(deviceId);
            if (mDevice != null) {
                mDevice.registerGestureListener(this, true);
                mDevice.registerErrorCallback(mErrorCallback, true);
            }
        } catch (Exception e) {
            Toast.makeText(getApplicationContext(), "Error opening device", Toast.LENGTH_SHORT).show();
        }
    }

    private void closeDevice() {
        if (mDevice != null) {
            try {
                mDevice.registerGestureListener(this, false);
                mDevice.registerErrorCallback(mErrorCallback, false);
                GestureDevice.close(mDevice);
                mDevice = null;
            } catch (Exception e) {
                Toast.makeText(getApplicationContext(), "Error closing device", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onGestureResult(GestureResult[] results, GestureDevice gesture) {
        Log.e(TAG,"Get gesture result from lower layer");
        if (results != null) {
            Log.e(TAG,"Total results: " + results.length);
            for (int i = 0; i < results.length; i++) {
                Log.e(TAG, "Index("+i+"): type = " + results[i].type +
                      "; subtype = " + results[i].subtype + 
                      "; version = " + results[i].version + 
                      "; id = " + results[i].id);
                Log.e(TAG, "                     " +
                      "timestamp = " + results[i].timestamp +
                      "; velocity = " + results[i].velocity +
                      "; confidence = " + results[i].confidence);
                if (results[i].location != null) {
                    Log.e(TAG,"Location: total points" + results[i].location.length);
                    for (int j = 0; j < results[i].location.length; j++) {
                        Log.e(TAG,"Point " + j + ": x=" + results[i].location[j].x +
                              "; y=" + results[i].location[j].y +
                              "; z=" + results[i].location[j].z);
                    }
                } else {
                    Log.e(TAG,"Location is null");
                }

                if (results[i].extension != null) {
                    Log.e(TAG,"Extended Info: size" + results[i].extension.length);
                } else {
                    Log.e(TAG,"No extended info avail");
                }
            }
        }
    }

    public class GestureErrorCallback
            implements GestureDevice.ErrorCallback {
        private static final String TAG = "GestureErrorCallback";
    
        public void onError(int error, GestureDevice device) {
            Log.e(TAG, "Got gesture device error callback. error=" + error);
            if (error == GestureDevice.GESTURE_ERROR_SERVER_DIED) {
                // We are not sure about the current state of the app (in preview or
                // snapshot or recording). Closing the app is better than creating a
                // new Camera object.
                throw new RuntimeException("Media server died.");
            }
        }
    };
}