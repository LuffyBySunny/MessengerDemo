package com.example.droodsunny.messenger;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

public class MainActivity extends AppCompatActivity {
    /*首先要实现一个自己的serviceConnection*/
    private Messenger mMessenger;
    private Messenger mGetReplyMessenger;

    boolean mBound;

    private ServiceConnection mConnection= new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            /*接收服务器onBind方法返回的Binder，并构造一个Messenger*/
            mMessenger=new Messenger(service);
            mBound=true;
            //用Message发送给服务端数据
            Message msg=Message.obtain(null,1);
            Bundle bundle=new Bundle();
            bundle.putString("data","Client sent a message");
            msg.setData(bundle);
            //将client的Messenger发送给service
            msg.replyTo=mGetReplyMessenger;
            try {
                mMessenger.send(msg);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }
        @Override
        public void onServiceDisconnected(ComponentName name) {
            mMessenger=null;
            mBound=false;

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);






    }

    @Override
    protected void onStart() {
        ComponentName componentName=new ComponentName("com.example.droodsunny.messenger","com.example.droodsunny.messenger.Myservice");
        Intent intent=new Intent();
      //  intent.setComponent(componentName);
       // intent.setClass(this,MyService.class);
        //intent.setClassName("com.example.droodsunny.messenger","com.example.droodsunny.messenger.Myservice");
        //绑定服务
        intent.setAction("com.service");
        intent.setPackage("com.example.droodsunny.messenger");
        bindService(intent,mConnection, Context.BIND_AUTO_CREATE);

        mGetReplyMessenger=new Messenger(new MessengerHandler());
        super.onStart();
    }
    @Override
    protected void onStop() {
        super.onStop();
    }
    public static class MessengerHandler extends Handler{
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 2:
                    Log.d("service",msg.getData().getString("data"));
            }
        }
    }
}
