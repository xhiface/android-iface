package xyz.zzyitj.iface.activity;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.gson.Gson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.AuthService;
import xyz.zzyitj.iface.api.FaceService;
import xyz.zzyitj.iface.model.ApiFaceUserAddDo;
import xyz.zzyitj.iface.model.ApiTokenDto;

import java.io.IOException;
import java.util.Objects;

/**
 * @author intent
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private TextView textView;
    private Button button;

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
        button = findViewById(R.id.main_button);
        button.setOnClickListener(v -> {
            Log.d(TAG, "start add face user");
            ApiFaceUserAddDo userAddDo = new ApiFaceUserAddDo();
            userAddDo.setImageType("URL");
            userAddDo.setImage("http://viapi-test.oss-cn-shanghai.aliyuncs.com/%E4%BA%BA%E8%84%B81%E6%AF%941.png");
            userAddDo.setGroupId("admin");
            userAddDo.setUid("a_qwrgqwf");
            FaceService.addUser(IFaceApplication.instance.getApiToken(), userAddDo)
                    .enqueue(new Callback() {
                        @Override
                        public void onFailure(@NotNull Call call, @NotNull IOException e) {
                            Log.e(TAG, "add user error.", e);
                        }

                        @Override
                        public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                            Log.d(TAG, Objects.requireNonNull(response.body()).string());
                        }
                    });
        });
    }

    /**
     * 初始化函数
     */
    private void init() {
    }

    /**
     * 初始化token
     */
    private void initToken() {
        if (IFaceApplication.instance.getApiToken() == null) {
            AuthService.getToken().enqueue(new Callback() {
                @Override
                public void onFailure(@NotNull Call call, @NotNull IOException e) {
                    Log.e(TAG, "getToken error.", e);
                    Toast.makeText(MainActivity.this,
                            "token error.", Toast.LENGTH_LONG).show();
                }

                @Override
                public void onResponse(@NotNull Call call, @NotNull Response response) throws IOException {
                    String body = Objects.requireNonNull(response.body()).string();
                    ApiTokenDto apiTokenDto = new Gson().fromJson(body, ApiTokenDto.class);
                    if (apiTokenDto != null && apiTokenDto.getAccessToken() != null) {
                        IFaceApplication.instance.setApiToken(apiTokenDto);
                        Log.d(TAG, apiTokenDto.toString());
                    } else {
                        Toast.makeText(MainActivity.this,
                                "token cannot be null.", Toast.LENGTH_LONG).show();
                    }
                }
            });
        } else {
            Log.d(TAG, IFaceApplication.instance.getApiToken());
        }
    }
}