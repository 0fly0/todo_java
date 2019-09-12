package com.ye.todo;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import com.ye.todo.MysqlManager;

public class TodoActivity extends AppCompatActivity implements View.OnClickListener {
    private final String TAG = "TodoActivity";

    private Connection connection;
    private ListView lvTodo;
    ArrayList<String> listTodo = new ArrayList<>(4);
    private final int UPDATE_LIST = 1;

    private Handler mHandler = new Handler(){

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case UPDATE_LIST:
                    lvTodo.setAdapter((ArrayAdapter<String>)msg.obj);
                    break;
            }
        }
    };

    @Override
    public void onClick(View v) {

    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_todo);
        lvTodo = (ListView)findViewById(R.id.lvTodo);

        new Thread(()->{
            Log.d(TAG, "conn thread start!");
            connection = MysqlManager.connect();
            if (connection == null) {
                return;
            }
            String sql = "SELECT * FROM table_todo";
            try {
                PreparedStatement ps = connection.prepareStatement(sql);
                ResultSet rs = ps.executeQuery();
                while (rs.next()) {
                    int id = rs.getInt(1);
                    String title = rs.getString(2);
                    String remark = rs.getString(3);
                    Log.d(TAG, "id=" + id + " title=" + title + " remark=" + remark);
                    listTodo.add(title);
                }

                Log.d(TAG, "add all=" + listTodo.toString());
                ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listTodo);
                Message msg = mHandler.obtainMessage(UPDATE_LIST);
                msg.obj = adapter;
                mHandler.sendMessage(msg);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }).start();
    }
}
