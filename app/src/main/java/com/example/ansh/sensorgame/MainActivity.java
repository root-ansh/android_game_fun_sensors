package com.example.ansh.sensorgame;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.HashMap;

import static com.example.ansh.sensorgame.R.id.clock;


public class MainActivity extends AppCompatActivity {
    private static final String TAG = "SENSORGAME";
    Spinner spinner;
    TextView timer;
    ImageView colorimg;
    String Message = "Start Game??";
    HashMap<String, String> table;

    SensorEventListener mysensooreventlistener;
    SensorManager mysensormanager;
    Sensor mysensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.e(TAG, "<onCreate started,contentview setted ");
        maketable();
        spinner = (Spinner) findViewById(R.id.search_spinner);
        timer = (TextView) findViewById(clock);
        colorimg = (ImageView) findViewById(R.id.colorimage);
        Log.e(TAG, "<...onCreate started,spinner,timer,colorimg found");

        mysensormanager = (SensorManager) this.getSystemService(Context.SENSOR_SERVICE);
        Log.e(TAG, "<...onCreate started,mysensormanager registered...>");

        mysensor = mysensormanager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        Log.e(TAG, "<...onCreate started,mysensor found...>");

        // attaching themysensormanager  and registering on mysensormnager in function startchangingpiccolor

        //creating a layout for it, just for f**k
        View customview = View.inflate(this, R.layout.boomboom, null);


        final AlertDialog al = new AlertDialog.Builder(this)


                .setIcon(R.mipmap.ic_launcher)
                .setTitle("Welcome")
                .setMessage(Message)
                .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                                        Log.e(TAG, "<...onCreate started,alert dialogue'spositive button is pressed,calling for  <...<...starttimeandchecker " );
//
//                                        starttimerandchecker();
//                                        Log.e(TAG, "<...onCreate started,alert dialogue'spositive button is pressed,calling for  <...<...startchangingcolor " );
//
//                                        startchangingpiccolor();
//                                        Log.e(TAG, "<...onCreate started,alert dialogue'spositive button is pressed,starttimeandchecker,startchangingcolor are executed...>...>" );
                        startgame();


                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Log.e(TAG, "<...onCreate started,alert dialogue's negetive buttton is pressed,finishing");


                        mysensormanager.unregisterListener(mysensooreventlistener);
                        Log.e(TAG, "<...onCreate started,unregistering mysensor listener");

                        finish();
                        Log.e(TAG, "<...onCreatefinished...>");


                    }
                })
                .setView(customview)
                .show();
        Log.e(TAG, "<...onCreate ,alert dialogue registered,got values and is showing...>");

        Log.e(TAG, "onCreate finished>");


    }

    public void maketable() {
        Log.e(TAG, "<...maketable:started ");
        table = new HashMap<>(8);

        table.put("000", "BLACK");
        table.put("00255", "BLUE");
        table.put("25500", "RED");
        table.put("2550255", "PINK");
        table.put("2552550", "YELLOW");
        table.put("0255255", "CYAN");
        table.put("255255255", "WHITE");
        table.put("02550", "GREEN");
        Log.e(TAG, "maketable:ENDED,values: " + table.toString() + "...>");

    }

    public void startchangingpiccolor() {

        mysensooreventlistener = new SensorEventListener() {
            @Override
            public void onSensorChanged(SensorEvent event) {
                float[] valuesxyz = event.values;
                int[] valuesRGB = new int[3];
                for (int i = 0; i < valuesRGB.length; i++) {
                    valuesRGB[i] = ((int) valuesxyz[i] % 2 == 0) ? 255 : 0;
                }
                colorimg.setBackgroundColor(Color.rgb(valuesRGB[0], valuesRGB[1], valuesRGB[2]));

            }

            @Override
            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }

        };
        mysensormanager.registerListener(mysensooreventlistener, mysensor, SensorManager.SENSOR_DELAY_NORMAL);


    }

    public void startgame() {
        startchangingpiccolor();

        String ans = "";
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                int color = ((ColorDrawable) colorimg.getBackground()).getColor();

//               Bitmap picturebm=((BitmapDrawable)colorimg.getBackground()).getBitmap();
//               int central_pixel=picturebm.getPixel(picturebm.getHeight()/2,picturebm.getWidth()/2);
//               //centrl pixel colrs
                int red = Color.red(color);
                int green = Color.green(color);
                int blue = Color.blue(color);
                String centralPixRGBstring = "" + red + green + blue;

                Log.e(TAG, "onItemSelected: " + centralPixRGBstring);

                final String tablevalue = table.get(centralPixRGBstring);


                String spinnerselecteditem = String.valueOf(spinner.getSelectedItem());


                String ans = "";
                if (spinnerselecteditem.equals(tablevalue)) {
                    ans = "YOU WINN!!!";
                } else {
                    ans = "OH NO, ITS WRONG :(   .correct answer is " + tablevalue;
                }


                AlertDialog al2 = new AlertDialog.Builder(MainActivity.this)
                        .setTitle(ans)
                        .setMessage("Play Again?")
                        .setPositiveButton("YES!", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startgame();
                            }
                        })
                        .setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                mysensormanager.unregisterListener(mysensooreventlistener);

                                finish();
                            }
                        })
                        .show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }


//   private  class timerAsynctask extends AsyncTask<Void,Integer,Void>{
//       @Override
//       protected void onProgressUpdate(Integer... values) {
//           super.onProgressUpdate(values);
//           timer.setText(values[0]);
//
//       }
//
//       @Override
//       protected Void doInBackground(Void... params) {
//           for (int i = 5; i >0 ; i--) {
//               try {
//                   Thread.sleep(1000);
//                   publishProgress(i);
//
//               } catch (InterruptedException e) {
//                   e.printStackTrace();
//               }
//
//           }
//           return null;
//       }
//   }

//

//    public void starttimerandchecker() {
//        Log.e(TAG, "<<...<starttimerandchecker: ,calling changetimethencallchecker");
//
//
//        String result = changetimethencallchecker();
//
//
//        Log.e(TAG, "<<...<starttimerandchecker: ,changetimethencallchecker'svalue successfully recieved...>");
//        Log.e(TAG, "<<...calling for a new alertdialogue al2");
//
//
//        //change pic call already made from first alertdialogue
//
//        AlertDialog al2 = new AlertDialog.Builder(MainActivity.this)
//                .setTitle(result)
//                .setMessage("wannaplayagain?")
//                .setPositiveButton("YES!", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        startgame();
//                    }
//                })
//                .setNegativeButton("NO", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        mysensormanager.unregisterListener(mysensooreventlistener);
//
//                        finish();
//                    }
//                })
//                .show();
//        Log.e(TAG, "<<... alertdialogue");
//
//
//
//    }
//    public String changetimethencallchecker(){ //and return result back to starttimeandchecker
//
////        timerAsynctask runtimer=new timerAsynctask();
////        runtimer.execute();
//        return callchecker();
//
//    }

//    public String callchecker() {
//        Bitmap picturebm=((BitmapDrawable)colorimg.getBackground()).getBitmap();
//        int central_pixel=picturebm.getPixel(picturebm.getHeight()/2,picturebm.getWidth()/2);
//        //centrl pixel colrs
//        int red= Color.red(central_pixel);
//        int green= Color.green(central_pixel);
//        int blue= Color.blue(central_pixel);
//        String centralPixRGBstring=""+red+green+blue;
//
//
//       final String tablevalue=table.get(centralPixRGBstring);
//
//       String ans="";
//        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                String spinnerselecteditem=String.valueOf(spinner.getSelectedItem());
//                String ans="";
//                if(   spinnerselecteditem.equals(tablevalue)   ){
//                    ans="YOU WINN!!!";
//                }
//                else{
//                    ans="OH NO, ITS WRONG :(   .correct answer is "+tablevalue;
//                }
//
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> parent) {
//
//            }
//        });
//        return ans;
//    }


}
