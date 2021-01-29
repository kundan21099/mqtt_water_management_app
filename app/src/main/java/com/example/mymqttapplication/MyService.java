package com.example.mymqttapplication;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.mymqttapplication.helper.MqttHelper;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.github.mikephil.charting.utils.Utils;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

import static com.example.mymqttapplication.MainActivity.chart1;
import static com.example.mymqttapplication.MainActivity.chart2;


public class MyService  extends Service {



   // String topicFlow = "esp143/flowrate";
   // String topicVolume = "esp143/volume";
   // String topicLevel = "esp143/level";
    //String topicAlert = "esp143/caution;
    WaterManagementModel waterManagementModel;
    private DatabaseHelper databaseHelper=new DatabaseHelper(this);
    MqttHelper mqttHelper;

    LineDataSet dataSet=new LineDataSet(null,null);
    ArrayList<ILineDataSet> dataSet1=new ArrayList<>();
    LineData data;

    BarDataSet dataSetB=new BarDataSet(null,null);
    ArrayList<IBarDataSet> dataSet1B=new ArrayList<>();
    BarData dataB;

    Date dateB;
    SimpleDateFormat sdfB;
    String newDateB;
    Date date;
    SimpleDateFormat sdf;
    String newDate;
    Context context;

    private final IBinder mBinder = new LocalService();



    @Override
    public void onCreate() {
        super.onCreate();
        //startForeground(1,new Notification());

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        addDataToGraph();
        addDataToGraphB();


        //chart1.invalidate();

        MainActivity.valveStateA.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(MainActivity.valveStateA.isChecked()){
                    mqttHelper.publishMessageOpenA();
                    Toast.makeText(getApplicationContext(),"Valve is OPEN",Toast.LENGTH_SHORT).show();
                }else{
                    mqttHelper.publishMessageCloseA();
                    Toast.makeText(getApplicationContext(),"Valve is CLOSE",Toast.LENGTH_SHORT).show();
                }

            }
        });

        MainActivity.valveStateB.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(MainActivity.valveStateB.isChecked()){
                    mqttHelper.publishMessageOpenB();
                    Toast.makeText(getApplicationContext(),"Valve B OPEN",Toast.LENGTH_SHORT).show();
                }else{
                    mqttHelper.publishMessageCloseB();
                    Toast.makeText(getApplicationContext(),"Valve B CLOSE",Toast.LENGTH_SHORT).show();
                }

            }
        });

        MainActivity.btn_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseHelper.deleteOne(waterManagementModel);
                Toast.makeText(getApplicationContext(),"Data deleted",Toast.LENGTH_SHORT).show();

                //Toast.makeText(MainActivity.this,"Data deleted",Toast.LENGTH_SHORT).show();
            }
        });

        MainActivity.disconnect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    mqttHelper.disconnect();
                    Toast.makeText(getApplicationContext(), "Successfully Disconnected", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "Fuck OFFFFF", Toast.LENGTH_SHORT).show();
                }
            }
        });

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
                if (topic.contains(MainActivity.topicFlowA)) {
                    Log.w("Flow", mqttMessage.toString());
                    MainActivity.flowRateA.setText(mqttMessage.toString());
                    //mChart.addEntry(Float.parseFloat(mqttMessage.toString()));
                } else if (topic.contains(MainActivity.topicVolumeA)) {
                    Log.w("Volume", mqttMessage.toString());
                    MainActivity.volumeA.setText(mqttMessage.toString());
                    //mChart.addEntry(Float.parseFloat(mqttMessage.toString()));

                    dateB = new Date();
                    sdfB = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                    newDateB = sdfB.format(dateB);

                    //val =mqttMessage.toString();
                    try {
                        waterManagementModel = new WaterManagementModel(-1, newDateB, mqttMessage.toString());
                        Toast.makeText(getApplicationContext(), waterManagementModel.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Volume not inserted", Toast.LENGTH_LONG).show();
                        waterManagementModel = new WaterManagementModel(-1, "NA", "error");
                    }

                    //databaseHelper = new DatabaseHelper(this);
                    boolean successA = databaseHelper.addOne(waterManagementModel);
                    addDataToGraph();
                    //chart1.invalidate();
                    databaseHelper.close();
                    //db.close();
                    Toast.makeText(getApplicationContext(), "SuccessA=" + successA, Toast.LENGTH_SHORT).show();
                    //Show();



                } else if (topic.contains(MainActivity.topicFlowB)){
                    Log.w("Flow", mqttMessage.toString());
                    MainActivity.flowRateB.setText(mqttMessage.toString());
                } else if (topic.contains(MainActivity.topicVolumeB)){
                    Log.w("Volume", mqttMessage.toString());
                    MainActivity.volumeB.setText(mqttMessage.toString());

                    date = new Date();
                    sdf = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
                    newDate = sdf.format(date);
                    try {
                        waterManagementModel = new WaterManagementModel(-1, newDate, mqttMessage.toString());
                        Toast.makeText(getApplicationContext(), waterManagementModel.toString(), Toast.LENGTH_LONG).show();
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "Volume not inserted", Toast.LENGTH_LONG).show();
                        waterManagementModel = new WaterManagementModel(-1, "NA", "error");
                    }

                    //databaseHelper = new DatabaseHelper(this);
                    boolean successB = databaseHelper.addTwo(waterManagementModel);
                    addDataToGraphB();
                    //chart1.invalidate();
                    databaseHelper.close();
                    //db.close();
                    Toast.makeText(getApplicationContext(), "SuccessB=" + successB, Toast.LENGTH_SHORT).show();
                    //Show();
                } else if (topic.contains(MainActivity.topicLevel)) {
                    Log.w("Level", mqttMessage.toString());
                    MainActivity.tankLevelA.setText(mqttMessage.toString());
                } else if (topic.contains(MainActivity.topicAlertA)) {
                    Log.w("Alert", mqttMessage.toString());
                    MainActivity.mp.start();
                    //Toast.makeText(MainActivity.this, "Alert!!! Tank is FULL!!! Close VALVE !!!", Toast.LENGTH_LONG).show();
                    // Toast.makeText(MainActivity.this,"Alert!!! Tank is FULL!!!", 5).show();
                }else if (topic.contains(MainActivity.topicAlertB)){
                    MainActivity.mp.stop();
                }


            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });

        return START_STICKY;
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    public class LocalService extends Binder {
        MyService getService()
        {
            return MyService.this;
        }
    }



    public void addDataToGraph() {
        //databaseHelper = new DatabaseHelper(this);

        final ArrayList<BarEntry> yVals = new ArrayList<BarEntry>();
        final ArrayList<String> yData = databaseHelper.queryYData();

        for (int i = 0; i < databaseHelper.queryYData().size(); i++) {
            BarEntry barEntry = new BarEntry(i, Float.parseFloat(yData.get(i)));
            yVals.add(barEntry);
        }

        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<String> xData = databaseHelper.queryXDataB();

        for (int i = 0; i < databaseHelper.queryXData().size(); i++) {
            xVals.add(xData.get(i));

        }


       // Utils.init(getApplicationContext());

        //data.addDataSet(new Entry(x, y)0);
        chart1.refreshDrawableState();
        dataSetB = new BarDataSet(yVals, "Vulume in Litre");
        dataSet1B = new ArrayList<>();
        dataSet1B.add(dataSetB);
        Utils.init(getApplicationContext());
        dataB = new BarData(dataSet1B);
        chart1.clear();
        dataB.notifyDataChanged();
        chart1.notifyDataSetChanged();
        chart1.setData(dataB);
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


    }

    public void addDataToGraphB() {
        //databaseHelper = new DatabaseHelper(this);

        final ArrayList<BarEntry> yValsB = new ArrayList<BarEntry>();
        final ArrayList<String> yDataB = databaseHelper.queryYDataB();

        for (int i = 0; i < databaseHelper.queryYDataB().size(); i++) {
            BarEntry barEntry = new BarEntry(i, Float.parseFloat(yDataB.get(i)));
            yValsB.add(barEntry);
        }

        final ArrayList<String> xVals = new ArrayList<String>();
        final ArrayList<String> xData = databaseHelper.queryXDataB();

        for (int i = 0; i < databaseHelper.queryXData().size(); i++) {
            xVals.add(xData.get(i));

        }


        // Utils.init(getApplicationContext());

        //data.addDataSet(new Entry(x, y)0);
        chart2.refreshDrawableState();
        dataSetB = new BarDataSet(yValsB, "Vulume in Litre");
        dataSet1B = new ArrayList<>();
        dataSet1B.add(dataSetB);
        Utils.init(getApplicationContext());
        dataB = new BarData(dataSet1B);
        chart2.clear();
        dataB.notifyDataChanged();
        chart2.notifyDataSetChanged();
        chart2.setData(dataB);
        chart2.invalidate();
        chart2.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xVals));
        chart2.getDescription().setEnabled(true);
        chart2.getDescription().setText("X axis = TIME , Y axis = Litre");
        chart2.setTouchEnabled(true);
        chart2.setDragEnabled(true);
        chart2.setScaleEnabled(true);
        chart2.setPinchZoom(true);
        chart2.setHorizontalScrollBarEnabled(true);
        //chart.setVisibleXRange(1,10);
        chart2.setVisibleXRangeMaximum(3);
        //chart.moveViewToX(5);
        chart2.moveViewTo(10,10, YAxis.AxisDependency.RIGHT);

        //chart.moveViewTo(0f,data.getDataSets(dataSet1), YAxis.AxisDependency.LEFT)
        //chart.moveViewTo(set.getEntryCount() - 1, data.getYMax(), YAxis.AxisDependency.LEFT);
        //
        //chart.moveViewToX(data.getXMax());


    }
}
