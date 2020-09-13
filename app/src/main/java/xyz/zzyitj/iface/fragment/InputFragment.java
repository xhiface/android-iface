package xyz.zzyitj.iface.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.cameraview.CameraView;
import io.reactivex.disposables.Disposable;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.FaceService;
import xyz.zzyitj.iface.model.ApiFaceUserAddDo;
import xyz.zzyitj.iface.util.ThreadPoolExecutorUtils;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * xyz.zzyitj.iface.fragment
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 15:58
 * @since 1.0
 */
public class InputFragment extends Fragment {
    private static final String TAG = InputFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private View rootView;

    private CameraView cameraView;
    private ImageView headImageView;
    private EditText phoneNumberEditText;
    private EditText userNameEditText;
    private Button inputButton;

    private byte[] temp;

    private final CameraView.Callback cameraViewCallback = new CameraView.Callback() {
        @Override
        public void onPictureTaken(CameraView cameraView, byte[] data) {
            temp = data;
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            headImageView.setImageBitmap(bitmap);
            headImageView.setRotation(-90);
        }
    };

    private static ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(
            1, new ThreadPoolExecutorUtils.CustomThreadFactory());
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            if (cameraView.isCameraOpened()) {
                cameraView.takePicture();
            }
        }
    };

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_input, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        startCamera();
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    private void initViews(View rootView) {
        cameraView = rootView.findViewById(R.id.fragment_input_camera);
        headImageView = rootView.findViewById(R.id.fragment_input_head);
        phoneNumberEditText = rootView.findViewById(R.id.fragment_input_phone_number);
        userNameEditText = rootView.findViewById(R.id.fragment_input_user_name);
        inputButton = rootView.findViewById(R.id.fragment_input_button);
        cameraView.addCallback(cameraViewCallback);
        cameraView.setFacing(CameraView.FACING_FRONT);
        inputButton.setOnClickListener(v -> {
            ApiFaceUserAddDo userAddDo = new ApiFaceUserAddDo();
            userAddDo.setImageType("BASE64");
            userAddDo.setImage(Base64.encodeBase64String(temp));
            userAddDo.setGroupId("user");
            userAddDo.setUid(phoneNumberEditText.getText().toString());
            Disposable disposable = FaceService.addUser(IFaceApplication.instance.getApiToken(), userAddDo)
                    .subscribe(body -> {
                        if (body.getErrorCode() == 0) {
                            Toast.makeText(getActivity(), R.string.input_success, Toast.LENGTH_LONG).show();
                        }
                    }, throwable -> {
                        Toast.makeText(getActivity(), R.string.input_error, Toast.LENGTH_LONG).show();
                        Log.e(TAG, getString(R.string.input_error), throwable);
                    });
        });
        scheduledExecutorService.scheduleAtFixedRate(() -> {
            Message message = new Message();
            message.what = 1;
            handler.sendMessage(message);
        }, 0, 2, TimeUnit.SECONDS);
    }

    private void startCamera() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            if (cameraView != null) {
                cameraView.start();
            }
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(), R.string.camera_permission_not_granted, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void stopCamera() {
        if (cameraView != null) {
            cameraView.stop();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions,
                                           @NonNull @NotNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CAMERA_PERMISSION:
                if (permissions.length != 1 || grantResults.length != 1) {
                    throw new RuntimeException("Error on requesting camera permission.");
                }
                if (grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity(), R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
