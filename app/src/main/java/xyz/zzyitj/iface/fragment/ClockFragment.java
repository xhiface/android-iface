package xyz.zzyitj.iface.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.*;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.Disposable;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.OpencvJni;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.activity.MainActivity;
import xyz.zzyitj.iface.api.ApiClockService;
import xyz.zzyitj.iface.api.BaiduApiConst;
import xyz.zzyitj.iface.api.BaiduFaceService;
import xyz.zzyitj.iface.model.BaiduFaceSearchVo;
import xyz.zzyitj.iface.model.BaiduFaceSearchDto;
import xyz.zzyitj.iface.ui.ProgressDialog;
import xyz.zzyitj.iface.util.CameraHelper;
import xyz.zzyitj.iface.util.CameraUtils;
import xyz.zzyitj.iface.util.DateUtils;
import xyz.zzyitj.iface.util.Utils;

import java.io.File;


/**
 * xyz.zzyitj.iface.fragment
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 14:44
 * @since 1.0
 */
public class ClockFragment extends Fragment implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = ClockFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 1;

    private View rootView;

    private final MainActivity mainActivity;

    private OpencvJni openCvJni;
    private CameraHelper cameraHelper;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private SurfaceView surfaceView;

    private ProgressDialog progressDialog;

    private boolean isClock;

    private void clock(byte[] data) {
        progressDialog.show();
        BaiduFaceSearchVo searchDo = new BaiduFaceSearchVo();
        searchDo.setImageType(BaiduApiConst.IMAGE_TYPE_BASE_64);
        searchDo.setImage(Base64.encodeBase64String(data));
        searchDo.setGroupIdList(BaiduApiConst.DEFAULT_GROUP);
        @SuppressLint("CheckResult") Disposable disposable = BaiduFaceService.searchUser(IFaceApplication.instance.getApiToken(), searchDo)
                .subscribe(body -> {
                    Log.d(TAG, body.toString());
                    if (body.getErrorCode() == 0) {
                        BaiduFaceSearchDto.User user = body.getResult().getUserList().get(0);
                        if (user.getScore() > BaiduApiConst.SAME_USER_MIN_SCORE) {
                            ApiClockService.addClock(user.getUserId())
                                    .subscribe(apiClockDto -> {
                                        Log.d(TAG, "onPictureTaken: " + apiClockDto.toString());
                                        progressDialog.dismiss();
                                        isClock = false;
                                        startOpenCV();
                                        switch (apiClockDto.getStatus()) {
                                            case 1:
                                                Toast.makeText(getActivity(),
                                                        apiClockDto.getUsername() + getString(R.string.clock_success),
                                                        Toast.LENGTH_LONG).show();
                                                break;
                                            case 2:
                                                Toast.makeText(getActivity(),
                                                        apiClockDto.getUsername() + getString(R.string.clock_success) + "，你已" + getString(R.string.late),
                                                        Toast.LENGTH_LONG).show();
                                                break;
                                            case 3:
                                                Toast.makeText(getActivity(),
                                                        apiClockDto.getUsername() + getString(R.string.clock_success) + "，你已" + getString(R.string.leave_early),
                                                        Toast.LENGTH_LONG).show();
                                                break;
                                            default:
                                        }
                                    }, throwable -> {
                                        Log.e(TAG, "onPictureTaken: ", throwable);
                                        progressDialog.dismiss();
                                        isClock = false;
                                        startOpenCV();
                                        Toast.makeText(getActivity(), R.string.clock_error, Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            progressDialog.dismiss();
                            isClock = false;
                            startOpenCV();
                            Toast.makeText(getActivity(), R.string.no_same_face_with_library, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        isClock = false;
                        startOpenCV();
                        Toast.makeText(getActivity(), body.getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                }, throwable -> {
                    Log.e(TAG, "onPictureTaken: ", throwable);
                    progressDialog.dismiss();
                    isClock = false;
                    startOpenCV();
                });
    }

    public ClockFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_clock, container, false);
        initOpenCV(rootView);
        initViews(rootView);
        return rootView;
    }

    private void initOpenCV(View rootView) {
        openCvJni = new OpencvJni();
        surfaceView = rootView.findViewById(R.id.fragment_clock_surface_view);
        surfaceView.getHolder().addCallback(this);
        cameraHelper = new CameraHelper(cameraId);
        cameraHelper.setPreviewCallback(this);

        Utils.copyAssets(getActivity(), "lbpcascade_frontalface.xml");
    }

    @Override
    public void onResume() {
        super.onResume();
        startOpenCV();
    }

    private void startOpenCV() {
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            String path = new File(Environment.getExternalStorageDirectory(), "lbpcascade_frontalface.xml")
                    .getAbsolutePath();
            cameraHelper.startPreview();
            openCvJni.init(path);
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                Manifest.permission.CAMERA)) {
            Toast.makeText(getActivity(), R.string.camera_permission_not_granted, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        stopCamera();
    }

    private void initViews(View rootView) {
        progressDialog = new ProgressDialog(mainActivity, getString(R.string.clocking));
    }

    private void stopCamera() {
        cameraHelper.stopPreview();
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

    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        boolean haveFace = openCvJni.postData(data, CameraHelper.WIDTH, CameraHelper.HEIGHT, cameraId);
        if (haveFace && !isClock) {
            isClock = true;
            byte[] faceData = CameraUtils.runInPreviewFrame(data, camera);
            clock(faceData);
        }
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder holder) {
    }

    @Override
    public void surfaceChanged(@NonNull SurfaceHolder holder, int format, int width, int height) {
        openCvJni.setSurface(holder.getSurface());
    }

    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder holder) {

    }
}
