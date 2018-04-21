package com.example.droodsunny.messenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.util.Log;

public class MyService extends Service {

    //服务器先实现一个handler，用于响应客户端发起的请求
    private static  class ServiceHandler extends Handler{

        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 1:
                   Bundle bundle=msg.getData();
                   Log.d("receive",bundle.getString("data"));
                   //得到client的messenger
                   Messenger client=msg.replyTo;
                   Message message=new Message();
                   Bundle bundle1=new Bundle();
                   bundle1.putString("data","reply to client");
                   message.setData(bundle1);
                   message.what=2;
                   try {
                       client.send(message);
                   } catch (RemoteException e) {
                       e.printStackTrace();
                   }
                   break;
           }
        }
    }

    //创建一个基于Handler的Messenger实例
    private final Messenger mMessenger=new Messenger(new ServiceHandler());
    public MyService() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
       /*通过Messenger实例得到一个IBinder对象，并返还给客户端*/
       return mMessenger.getBinder();
    }
}
