
package com.example.mymqttapplication;

import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.mymqttapplication.helper.ChartHelper;
import com.example.mymqttapplication.helper.MqttHelper;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;

import org.eclipse.paho.android.service.MqttAndroidClient;

public class MainActivity extends AppCompatActivity{
    MyService myService;
    boolean isBind=false;

    public MqttAndroidClient mqttAndroidClient;
    MqttHelper mqttHelper;
    ChartHelper mChart;
    static LineChart chart3, chart4;
    static BarChart chart1,chart2;
    //BarChart barChart;
    static MediaPlayer mp;
    static TextView flowRateA, flowRateB;
    static TextView volumeA,volumeB;
    static TextView tankLevelA,tankLevelB;
    static Button disconnect, btn_delete,btn_connect;
    Button btn_stats;
    static Button startService;
    static Switch valveStateA,valveStateB;
    static String topicFlowA = "esp143/flowrateA";
    static String topicVolumeA = "esp143/volumeA";
    static String topicFlowB = "esp143/flowrateB";
    static String topicVolumeB = "esp143/volumeB";
    static String topicLevel = "esp143/level";
    static String topicAlertA = "esp143/cautionA";
    static String topicAlertB = "esp143/cautionB";
    String val;
    WaterManagementModel waterManagementModel;
    DatabaseHelper databaseHelper;

    //LineData data;
    //SQLiteDatabase db;

    //loadChart load;

    /*LineDataSet dataSet=new LineDataSet(null,null);
    ArrayList<ILineDataSet> dataSet1=new ArrayList<>();
    LineData data;*/

    /*ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            MyStatisticsService.MyStatisticsServiceBinder myStatisticsServiceBinder= (MyStatisticsService.MyStatisticsServiceBinder)service;
            myStatisticsService= myStatisticsServiceBinder.getBinder();
            isBind =true;
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            isBind=false;
        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent= new Intent(this, MyStatisticsService.class);
        bindService(intent,mServiceConnection, Context.BIND_AUTO_CREATE);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(isBind){
            unbindService(mServiceConnection);
            isBind=false;
        }
    }*/

    //@SuppressLint("WrongThread")
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mp = MediaPlayer.create(this, R.raw.alertsignal);
        flowRateA = (TextView) findViewById(R.id.flowA);
        volumeA = (TextView) findViewById(R.id.volumeA);
        tankLevelA = (TextView) findViewById(R.id.levelA);
        flowRateB = (TextView) findViewById(R.id.flowB);
        volumeB = (TextView) findViewById(R.id.volumeB);
        //tankLevelB = (TextView) findViewById(R.id.levelB);
        disconnect = (Button) findViewById(R.id.disconnect);
        btn_delete=(Button)findViewById(R.id.delete);
       // startService=(Button)findViewById(R.id.startService);
       // btn_connect=(Button)findViewById(R.id.connect);
        valveStateA = (Switch) findViewById(R.id.valveA);
        valveStateB = (Switch) findViewById(R.id.valveB);

        databaseHelper = new DatabaseHelper(MainActivity.this);
        chart1 = (BarChart) findViewById(R.id.chart1);
        chart2 = (BarChart) findViewById(R.id.chart2);
        chart3 = (LineChart) findViewById(R.id.chart3);
        chart4 = (LineChart) findViewById(R.id.chart4);

        Context context=getApplicationContext();

        MyJobService.enqueueWork(context, new Intent());


        /** Commented part used different services and method to show chart on the app **/
        //startService(new Intent(this,MyService.class));
        //load = (loadChart) new loadChart().execute();
       /* Context context=getApplicationContext();
        Intent serviceIntent = new Intent(context, MyService.class);
        context.startService(serviceIntent);
        context.bindService(serviceIntent, new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                MyService.LocalService localService=(MyService.LocalService)service;
                myService=localService.getService();
                isBind=true;//retrieve an instance of the service here from the IBinder returned
                //from the onBind method to communicate with
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
            }
        }, Context.BIND_AUTO_CREATE);*/







        //db = databaseHelper.getReadableDatabase();
        // barChart=(BarChart)findViewById(R.id.chart);
        /*btn_connect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startMqtt();

                Toast.makeText(MainActivity.this,"Connected",Toast.LENGTH_SHORT).show();

            }
        });*/

        /*btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteOne(waterManagementModel);
                Toast.makeText(MainActivity.this,"Data deleted",Toast.LENGTH_SHORT).show();
            }
        });


        valveState.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(valveState.isChecked()){
                    mqttHelper.publishMessageOpen();
                    Toast.makeText(MainActivity.this,"Valve is OPEN",Toast.LENGTH_SHORT).show();
                }else{
                    mqttHelper.publishMessageClose();
                    Toast.makeText(MainActivity.this,"Valve is CLOSE",Toast.LENGTH_SHORT).show();
                }

            }
        });




        disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mqttHelper.disconnect();
                    Toast.makeText(MainActivity.this, "Successfully Disconnected", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(MainActivity.this, "Fuck OFFFFF", Toast.LENGTH_SHORT).show();
                }
            }
        });*/

        //mChart=new ChartHelper(chart);
       // chart.refreshDrawableState();
        //data.notifyDataChanged();
        //chart.notifyDataSetChanged();
       // Show();
        //addDataToGraph();
       // chart.invalidate();
        //load.doInBackground();

        //startMqtt();
    }

   /* private void displayMqtt(){
        final Handler handler=new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                if(myStatisticsService!=null){
                    myStatisticsService.startMqtt();
                }

            }
        });
    }*/





   /*public static void addDataToGraph() {
        databaseHelper = new DatabaseHelper(this);

        final ArrayList<Entry> yVals = new ArrayList<Entry>();
        final ArrayList<String> yData = databaseHelper.queryYData();

        for (int i = 0; i < databaseHelper.queryYData().size(); i++) {
            Entry lineEntry = new Entry(i, Float.parseFloat(yData.get(i)));
            yVals.add(lineEntry);
        }

        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<String> xData = databaseHelper.queryXData();

        for (int i = 0; i < databaseHelper.queryXData().size(); i++) {
            xVals.add(xData.get(i));

        }


        //data.addDataSet(new Entry(x, y)0);
        chart.refreshDrawableState();
        dataSet = new LineDataSet(yVals, "Vulume in Litre");
        dataSet1 = new ArrayList<>();
        dataSet1.add(dataSet);
        data = new LineData(dataSet1);
        //chart.clear();
        data.notifyDataChanged();
        chart.notifyDataSetChanged();
        chart.setData(data);
        chart.invalidate();
        chart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        chart.getDescription().setEnabled(true);
        chart.getDescription().setText("X axis = TIME , Y axis = Litre");
        chart.setTouchEnabled(true);
        chart.setDragEnabled(true);
        chart.setScaleEnabled(true);
        chart.setPinchZoom(true);
        chart.setHorizontalScrollBarEnabled(true);
        //chart.setVisibleXRange(1,10);
        chart.setVisibleXRangeMaximum(3);
        //chart.moveViewToX(5);
        chart.moveViewTo(10,10, YAxis.AxisDependency.RIGHT);
        //chart.moveViewTo(0f,data.getDataSets(dataSet1), YAxis.AxisDependency.LEFT)
        //chart.moveViewTo(set.getEntryCount() - 1, data.getYMax(), YAxis.AxisDependency.LEFT);
        //
        //chart.moveViewToX(data.getXMax());


    }

    /*private void Show() {
        dataSet.setValues(getDataValues());
        dataSet.setLabel("VOLUME in Litre");
        dataSet1.clear();
        dataSet1.add(dataSet);
        data=new LineData(dataSet1);
        chart.clear();
        chart.setData(data);
        chart.invalidate();

    }

    private ArrayList<Entry> getDataValues(){
        ArrayList<Entry> dataVals=new ArrayList<Entry>();
        String[] columns={"ID","DATE","VOLUME"};
        Cursor cursor=db.query("WATERMANAGEMENT_TABLE",columns,null,null,null,null,null);

        for(int i=0;i<cursor.getCount();i++){
            cursor.moveToNext();
            dataVals.add(new Entry(cursor.getFloat(1),cursor.getFloat(2)));

        }
        /*dataVals.add(new Entry(0,20));
        dataVals.add(new Entry(1,45));
        dataVals.add(new Entry(2,12));
        dataVals.add(new Entry(3,41));
        dataVals.add(new Entry(4,23));
        dataVals.add(new Entry(5,1));

        return dataVals;

    }*/

   /* private void startMqtt() {


        mqttHelper = new MqttHelper(getApplicationContext());
        mqttHelper.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {

            }

            @Override
            public void connectionLost(Throwable throwable) {

            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                //Log.w("Debug", mqttMessage.toString());
                if (topic.contains(topicFlow)) {
                    Log.w("Flow", mqttMessage.toString());
                    flowRate.setText(mqttMessage.toString());
                   //mChart.addEntry(Float.parseFloat(mqttMessage.toString()));
                } else if (topic.contains(topicVolume)) {
                    Log.w("Volume", mqttMessage.toString());
                    Volume.setText(mqttMessage.toString());
                    //mChart.addEntry(Float.parseFloat(mqttMessage.toString()));

                    date = new Date();
                    sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                    newDate = sdf.format(date);

                    //val =mqttMessage.toString();
                    try {
                        waterManagementModel = new WaterManagementModel(-1, newDate, mqttMessage.toString());
                        Toast.makeText(MainActivity.this, waterManagementModel.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(MainActivity.this, "Volume not inserted", Toast.LENGTH_LONG).show();
                        waterManagementModel = new WaterManagementModel(-1, "NA", "error");
                    }

                    databaseHelper = new DatabaseHelper(MainActivity.this);
                    boolean success = databaseHelper.addOne(waterManagementModel);
                    //addDataToGraph();
                    //chart.invalidate();
                    //load.doInBackground();
                    databaseHelper.close();
                    //db.close();
                    Toast.makeText(MainActivity.this, "Success=" + success, Toast.LENGTH_SHORT).show();
                    //Show();



                } else if (topic.contains(topicLevel)) {
                    Log.w("Level", mqttMessage.toString());
                    tankLevel.setText(mqttMessage.toString());
                } else if (topic.contains(topicAlert)) {
                    Log.w("Alert", mqttMessage.toString());
                    mp.start();
                    Toast.makeText(MainActivity.this, "Alert!!! Tank is FULL!!! Close VALVE !!!", Toast.LENGTH_LONG).show();
                    // Toast.makeText(MainActivity.this,"Alert!!! Tank is FULL!!!", 5).show();

                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

    }*/

    /*class loadChart extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {
            //ethe chat load cha code tak

            databaseHelper = new DatabaseHelper(MainActivity.this);

            final ArrayList<Entry> yVals = new ArrayList<Entry>();
            final ArrayList<String> yData = databaseHelper.queryYData();

            for (int i = 0; i < databaseHelper.queryYData().size(); i++) {
                Entry lineEntry = new Entry(i, Float.parseFloat(yData.get(i)));
                yVals.add(lineEntry);
            }

            final ArrayList<String> xVals = new ArrayList<String>();
            final ArrayList<String> xData = databaseHelper.queryXData();

            for (int i = 0; i < databaseHelper.queryXData().size(); i++) {
                xVals.add(xData.get(i));

            }*/


            //data.addDataSet(new Entry(x, y)0);
           /* chart1.refreshDrawableState();
            dataSet = new LineDataSet(yVals, "Vulume in Litre");
            dataSet1 = new ArrayList<>();
            dataSet1.add(dataSet);
            data = new LineData(dataSet1);
            //chart.clear();
            data.notifyDataChanged();
            chart1.notifyDataSetChanged();
            chart1.setData(data);
            chart1.invalidate();
            chart1.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
            chart1.getDescription().setEnabled(true);
            chart1.getDescription().setText("X axis = TIME , Y axis = Litre");
            chart1.setTouchEnabled(true);
            chart1.setDragEnabled(true);
            chart1.setScaleEnabled(true);
            chart1.setPinchZoom(true);
            chart1.setHorizontalScrollBarEnabled(true);
            //chart.setVisibleXRange(1,10);
            chart1.setVisibleXRangeMaximum(3);
            //chart.moveViewToX(5);
            chart1.moveViewTo(10,10, YAxis.AxisDependency.RIGHT);
            //chart.moveViewTo(0f,data.getDataSets(dataSet1), YAxis.AxisDependency.LEFT)
            //chart.moveViewTo(set.getEntryCount() - 1, data.getYMax(), YAxis.AxisDependency.LEFT);
            //
            //chart.moveViewToX(data.getXMax());

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }
    }*/

}


