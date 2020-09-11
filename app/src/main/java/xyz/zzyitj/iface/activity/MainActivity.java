package xyz.zzyitj.iface.activity;

import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.ApiConst;
import xyz.zzyitj.iface.api.AuthService;
import xyz.zzyitj.iface.model.Server;

/**
 * @author intent
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        init();
        initViews();
        initToken();
    }

    private void initViews() {
        textView = findViewById(R.id.main_hello);
    }

    /**
     * 初始化函数
     * 1、初始化ApiServer
     */
    private void init() {
        Server server = new Server();
        server.setHost(ApiConst.HOST);
        server.setPort(80);
        server.setHttps(false);
        IFaceApplication.instance.setServer(server);
    }

    /**
     * 初始化token
     */
    private void initToken() {
        if (IFaceApplication.instance.getToken() == null) {
            AuthService.getToken(IFaceApplication.instance.getServer())
                    .subscribe(token -> {
                        Log.d(TAG, token.toString());
                        if (token.getAccessToken() != null) {
                            IFaceApplication.instance.setToken(token);
                        } else {
                            Toast.makeText(MainActivity.this, "token cannot be null.", Toast.LENGTH_LONG).show();
                        }
                    }, throwable -> {
                        Log.e(TAG, "getToken error.", throwable);
                        Toast.makeText(MainActivity.this, "token error.", Toast.LENGTH_LONG).show();
                    });
        } else {
            Log.d(TAG, IFaceApplication.instance.getToken());
        }
    }
}