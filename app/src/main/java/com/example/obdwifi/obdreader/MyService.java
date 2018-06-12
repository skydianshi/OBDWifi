package com.example.obdwifi.obdreader;

import android.app.Service;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.widget.Toast;

import com.example.obdwifi.obd.BusInitObdCommand;
import com.example.obdwifi.obd.EngineCoolantTemperatureObdCommand;
import com.example.obdwifi.obd.EngineRPMObdCommand;
import com.example.obdwifi.obd.IntakeManifoldPressureObdCommand;
import com.example.obdwifi.obd.MassAirFlowObdCommand;
import com.example.obdwifi.obd.ObdProtocols;
import com.example.obdwifi.obd.SpeedObdCommand;
import com.example.obdwifi.obd.ThrottlePositionObdCommand;
import com.example.obdwifi.obd.protocol.EchoOffObdCommand;
import com.example.obdwifi.obd.protocol.LineFeedOffObdCommand;
import com.example.obdwifi.obd.protocol.ObdResetCommand;
import com.example.obdwifi.obd.protocol.SelectProtocolObdCommand;
import com.example.obdwifi.obd.protocol.TimeoutObdCommand;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.UUID;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;




public class MyService extends Service {
    private MyBinder binder = new MyBinder();
    private BlockingQueue<ObdCommand> _queue = new LinkedBlockingQueue<ObdCommand>();
    Socket _socket = null;      //wifi通信socket
    InputStream in;
    OutputStream os;

    IPostListener _callback;

    public  class MyBinder extends Binder {
        public void setListener(IPostListener callback){//这边定义调用接口的方法，同时在下面读取数据的时候调用这个方法则实现了实时更新UI界面的效果
            _callback = callback;
            System.out.println("binded");
        }//将service里面的接口和activity里面的接口连接起来，从而可以调用activity中已经实现的方法
        public void addJob(){
            addJobToQueue();
        }
    }

    public void addJobToQueue(){
        try {
            _queue.put(new SpeedObdCommand());
            _queue.put(new EngineRPMObdCommand());
            _queue.put(new MassAirFlowObdCommand());
            _queue.put(new IntakeManifoldPressureObdCommand());
            _queue.put(new EngineCoolantTemperatureObdCommand());
            _queue.put(new ThrottlePositionObdCommand());
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public void initialJob(){//EML327初始化工作
        try {
            _queue.put(new ObdResetCommand());
            _queue.put(new EchoOffObdCommand());
            _queue.put(new LineFeedOffObdCommand());
		/*
		 * * Will send second-time based on tests. * * TODO this can be done w/o
		 * having to queue jobs by just issuing * command.run(),
		 * command.getResult() and validate the result.
		 */
            _queue.put(new TimeoutObdCommand(25)); // 等待响应250ms
            // For now set protocol to AUTO
            _queue.put(new SelectProtocolObdCommand(ObdProtocols.AUTO));
            _queue.put(new BusInitObdCommand());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }


    @Override
    public IBinder onBind(Intent intent) {
        System.out.println("Service is Binded");
        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        System.out.println("service is created");

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        System.out.println("-----onStartCommand");
        startService();
        return super.onStartCommand(intent, flags, startId);
    }

    public void startService(){
        getSocket();
        ReadThread.start();
    }

    Thread ReadThread=new Thread(){

        public void run(){
            //接收线程
                try{
                    while(true){
                        ObdCommand command = null;
                        try {
                            command = (ObdCommand)_queue.take();//从队列中取出要更新的任务，如果队伍中没有任务，就一直等在这边等待任务
                        }catch (Exception e){
                            e.printStackTrace();
                        }
                        try{
                            command.sendCommand(os);
                        }catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        command.readResult(in);
                        if(_callback!=null){//提醒UI线程更新
                            _callback.stateUpdate(command,command.getFormattedResult().toString());
                        }
                    }
                }catch(IOException e){
                    System.out.println("发送数据或者读取数据失败");
                }

        }
    };


    //activity中蓝牙与OBD已经建立了通道，这里通过地址获得通道，从而在service中进行数据传输
    public void getSocket(){
        new Thread(){
            @Override
            public void run() {
                try {
                    _socket = new Socket("192.168.0.10",35000);
                    in = _socket.getInputStream();
                    os = _socket.getOutputStream();
                    //初始化EML327
                    initialJob();
                    System.out.println("连接成功");
                } catch (IOException e) {
                    System.out.println("wifi未连接");
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return true;
    }
}
