package xyz.zzyitj.iface.fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import com.google.android.cameraview.CameraView;
import okhttp3.Call;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.ApiResponseCall;
import xyz.zzyitj.iface.api.FaceService;
import xyz.zzyitj.iface.model.ApiFaceSearchDo;
import xyz.zzyitj.iface.model.ApiFaceSearchDto;
import xyz.zzyitj.iface.model.ApiResponseBody;
import xyz.zzyitj.iface.util.DateUtils;

import java.io.IOException;


/**
 * xyz.zzyitj.iface.fragment
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 14:44
 * @since 1.0
 */
public class ClockFragment extends Fragment {
    private static final String TAG = ClockFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private View rootView;
    private CameraView cameraView;
    private TextView textView;
    private Button clockButton;

    private final CameraView.Callback cameraViewCallback = new CameraView.Callback() {
        @Override
        public void onPictureTaken(CameraView cameraView, byte[] data) {
            Log.d(TAG, "onPictureTaken " + data.length);
            ApiFaceSearchDo searchDo = new ApiFaceSearchDo();
            searchDo.setImageType("BASE64");
            searchDo.setImage(Base64.encodeBase64String(data));
            searchDo.setGroupIdList("user");
            FaceService.searchUser(IFaceApplication.instance.getApiToken(), searchDo,
                    new ApiResponseCall<ApiResponseBody<ApiFaceSearchDto>>() {
                        @Override
                        public void onSuccess(Call call, ApiResponseBody<ApiFaceSearchDto> body) {
                            Log.d(TAG, body.toString());
                            if (body.getErrorCode() == 0) {
                                Looper.prepare();
                                ApiFaceSearchDto.User user = body.getResult().getUserList().get(0);
                                Toast.makeText(getActivity(),
                                        user.getUserId() + " 相似度: " + user.getScore(),
                                        Toast.LENGTH_LONG).show();
                                Looper.loop();
                            }
                        }

                        @Override
                        public void onError(Call call, IOException e) {
                            Log.e(TAG, "search user error.", e);
                            Looper.prepare();
                            Toast.makeText(getActivity(), "error", Toast.LENGTH_LONG).show();
                            Looper.loop();
                        }
                    });
        }
    };

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_clock, container, false);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private void initViews(View rootView) {
        cameraView = rootView.findViewById(R.id.fragment_clock_camera);
        textView = rootView.findViewById(R.id.fragment_clock_hello);
        clockButton = rootView.findViewById(R.id.fragment_clock_button);
        cameraView.addCallback(cameraViewCallback);
        cameraView.setFacing(CameraView.FACING_FRONT);
        if (DateUtils.checkNowIsNoon()) {
            clockButton.setText(R.string.clock_out);
        } else {
            clockButton.setText(R.string.clock_in);
        }
        clockButton.setOnClickListener(v -> {
            if (cameraView.isCameraOpened()) {
                cameraView.takePicture();
            }
        });
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
    public void onRequestPermissionsResult(int requestCode, @NonNull @NotNull String[] permissions, @NonNull @NotNull int[] grantResults) {
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
