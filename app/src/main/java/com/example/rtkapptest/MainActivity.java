package com.example.rtkapptest;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import android.util.Log;

import org.json.JSONObject;


public class MainActivity extends AppCompatActivity {

    EditText inputText;
    EditText inputTextIp;
    Button btnConnect, btnSend;
    Socket socket;
    OutputStream outputStream;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        inputText = findViewById(R.id.inputText);
        inputTextIp = findViewById(R.id.inputTextIp);
        btnConnect = findViewById(R.id.btnConnect);
        btnSend = findViewById(R.id.btnSend);

        btnConnect.setOnClickListener(v -> {
            try {
                connectToServer();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });
        btnSend.setOnClickListener(v -> sendMessage());
    }

    private void connectToServer() throws IOException {
        new Thread(() -> {
            try {
                Log.v("myTAG", "Connect to server elindult!");
                String ip_addr = String.valueOf(inputTextIp.getText());
                Log.v("myTAG", ip_addr);
                Socket socket = new Socket(ip_addr, 5000);
                OutputStream out = socket.getOutputStream();

                // Adatok
                String event = "BUTTON_PRESSED";
                String message = String.valueOf(inputText.getText());

                // JSON objektum
                JSONObject json = new JSONObject();
                json.put("event", event);
                json.put("message", message);


                out.write(json.toString().getBytes());
                out.flush();

                socket.close();
                Log.v("myTAG", "Kapcsolat bontva");

            } catch (Exception e) {
                e.printStackTrace();
                Log.e("myTAG", "Hiba a csatlakozás során: " + e.getMessage());
            }
        }).start();
    }

    private void sendMessage() {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            if (socket != null) socket.close();
        } catch (Exception ignored) {}
    }

}