package xyz.zzyitj.iface.activity;

import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.google.android.cameraview.CameraView;
import okhttp3.Call;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.ApiResponseCall;
import xyz.zzyitj.iface.api.AuthService;
import xyz.zzyitj.iface.api.FaceService;
import xyz.zzyitj.iface.model.ApiFaceUserAddDo;
import xyz.zzyitj.iface.model.ApiFaceUserAddDto;
import xyz.zzyitj.iface.model.ApiResponseBody;
import xyz.zzyitj.iface.model.ApiTokenDto;

import java.io.IOException;

/**
 * @author intent
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private CameraView cameraView;
    private TextView textView;
    private Button button;

    private CameraView.Callback cameraViewCallback = new CameraView.Callback() {
        @Override
        public void onCameraOpened(CameraView cameraView) {
            Log.d(TAG, "onCameraOpened");
        }

        @Override
        public void onCameraClosed(CameraView cameraView) {
            Log.d(TAG, "onCameraClosed");
        }

        @Override
        public void onPictureTaken(CameraView cameraView, byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        init();
        initViews();
        initToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
        startCamera();
    }

    @Override
    protected void onPause() {
        super.onPause();
        stopCamera();
    }

    private void startCamera() {
        if (cameraView != null) {
            cameraView.start();
        }
    }

    private void stopCamera() {
        if (cameraView != null) {
            cameraView.stop();
        }
    }

    private void initViews() {
        cameraView = findViewById(R.id.main_camera);
        cameraView.addCallback(cameraViewCallback);
        cameraView.setFacing(CameraView.FACING_FRONT);
        textView = findViewById(R.id.main_hello);
        button = findViewById(R.id.main_button);
        button.setOnClickListener(v -> {
            Log.d(TAG, "start add face user");
            ApiFaceUserAddDo userAddDo = new ApiFaceUserAddDo();
            userAddDo.setImageType("URL");
            userAddDo.setImage("http://viapi-test.oss-cn-shanghai.aliyuncs.com/%E4%BA%BA%E8%84%B81%E6%AF%941.png");
            userAddDo.setGroupId("user");
            userAddDo.setUid("a_qwrgqwf");
            FaceService.addUser(IFaceApplication.instance.getApiToken(), userAddDo,
                    new ApiResponseCall<ApiResponseBody<ApiFaceUserAddDto>>() {
                        @Override
                        public void onSuccess(Call call, ApiResponseBody<ApiFaceUserAddDto> body) {
                            Log.d(TAG, body.toString());
                        }

                        @Override
                        public void onError(Call call, IOException e) {
                            Log.e(TAG, "add user error.", e);
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
            AuthService.getToken(new ApiResponseCall<ApiTokenDto>() {
                @Override
                public void onSuccess(Call call, ApiTokenDto apiTokenDto) {
                    if (apiTokenDto != null && apiTokenDto.getAccessToken() != null) {
                        IFaceApplication.instance.setApiToken(apiTokenDto);
                        Log.d(TAG, apiTokenDto.toString());
                    } else {
                        Toast.makeText(MainActivity.this,
                                "token cannot be null.", Toast.LENGTH_LONG).show();
                    }
                }

                @Override
                public void onError(Call call, IOException e) {
                    Log.e(TAG, "getToken error.", e);
                    Toast.makeText(MainActivity.this,
                            "token error.", Toast.LENGTH_LONG).show();
                }
            });
        } else {
            Log.d(TAG, IFaceApplication.instance.getApiToken());
        }
    }
}