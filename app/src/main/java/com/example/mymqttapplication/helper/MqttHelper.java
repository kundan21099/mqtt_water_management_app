
package com.example.mymqttapplication.helper;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;


public class MqttHelper {
    public MqttAndroidClient mqttAndroidClient;
    private static final String TAG = MqttHelper.class.getSimpleName();
    final String serverUri = "tcp://broker.hivemq.com:1883"; // this is public broker

    final String clientId = "<add you client id here>";

    final String topic = "<add your topic here>";
    static String user = "<add use here>";
    MqttConnectOptions options;
    static String password = "<add password if broker client is password protected>";
    String msg = "Warning: Network Connection Interrupted !!!";

    public MqttHelper(Context context) {
        mqttAndroidClient = new MqttAndroidClient(context, serverUri, clientId);
        options = new MqttConnectOptions();
        // options.setUserName(user);
        //  options.setPassword(password.toCharArray());
        options.setAutomaticReconnect(true);
        options.setKeepAliveInterval(120);
        options.setCleanSession(false);
        //options.setWill("<your topic>",msg.getBytes(),0,false);

        mqttAndroidClient.setCallback(new MqttCallbackExtended() {
            @Override
            public void connectComplete(boolean b, String s) {
                Log.w("mqtt", s);
            }

            @Override
            public void connectionLost(Throwable throwable) {
                Log.w("Mqtt", "Connection Lost");
            }

            @Override
            public void messageArrived(String topic, MqttMessage mqttMessage) throws Exception {
                Log.w("Mqtt", mqttMessage.toString());
            }

            @Override
            public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

            }
        });
        connect();
    }

    public void setCallback(MqttCallbackExtended callback) {
        mqttAndroidClient.setCallback(callback);
    }


    private void connect() {

        try {
            IMqttToken token = mqttAndroidClient.connect(options);
            token.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    // We are connected
                    Log.w("Mqtt", "onSuccess");
                    subscribeToTopic();
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken, Throwable exception) {
                    // Something went wrong e.g. connection timeout or firewall problems
                    Log.d(TAG, "onFailure");

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }


    public void subscribeToTopic() {
        int qos = 0;
        try {
            IMqttToken subToken = mqttAndroidClient.subscribe(topic, qos);
            subToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt", "Subscribed");
                    // The message was published

                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Log.w("Mqtt", "not subscribed");
                    // The subscription could not be performed, maybe the user was not
                    // authorized to subscribe on the specified topic e.g. using wildcards

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }

    }

    public void disconnect() {
        try {
            IMqttToken disconToken = mqttAndroidClient.disconnect();
            disconToken.setActionCallback(new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken asyncActionToken) {
                    Log.w("Mqtt", "Disconnected");
                    // we are now successfully disconnected
                }

                @Override
                public void onFailure(IMqttToken asyncActionToken,
                                      Throwable exception) {
                    Log.w("Mqtt", "Disconnected with error");

                    // something went wrong, but probably we are disconnected anyway
                }
            });
        } catch (MqttException e) {
            //Toast.makeText(MainActivity.this,"Discoonected with error",Toast.LENGTH_SHORT).show();
        }


    }

    public void publishMessageOpenA() {
        try {
            String publishMessage ="onA";
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload((publishMessage.getBytes()));
            mqttAndroidClient.publish("esp143/valveA", mqttMessage);
            Log.w("Mqtt","ValveA is OPENED");

        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public void publishMessageCloseA() {
        try {
            String publishMessage ="offA";
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload((publishMessage.getBytes()));
            mqttAndroidClient.publish("esp143/valveA", mqttMessage);
            Log.w("Mqtt","ValveA is CLOSED");

        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public void publishMessageOpenB() {
        try {
            String publishMessage ="onB";
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload((publishMessage.getBytes()));
            mqttAndroidClient.publish("esp143/valveB", mqttMessage);
            Log.w("Mqtt","ValveB is OPENED");
        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }

    public void publishMessageCloseB() {
        try {
            String publishMessage ="offB";
            MqttMessage mqttMessage = new MqttMessage();
            mqttMessage.setPayload((publishMessage.getBytes()));
            mqttAndroidClient.publish("esp143/valveB", mqttMessage);
            Log.w("Mqtt","ValveB is CLOSED");

        } catch (MqttException ex) {
            ex.printStackTrace();
        }
    }
}