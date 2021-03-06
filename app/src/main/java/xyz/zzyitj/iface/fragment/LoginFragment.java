package xyz.zzyitj.iface.fragment;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.*;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.*;
import android.widget.RelativeLayout;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.OpencvJni;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.activity.MainActivity;
import xyz.zzyitj.iface.api.ApiUserService;
import xyz.zzyitj.iface.api.BaiduApiConst;
import xyz.zzyitj.iface.api.BaiduFaceService;
import xyz.zzyitj.iface.model.*;
import xyz.zzyitj.iface.ui.ProgressDialog;
import xyz.zzyitj.iface.util.CameraHelper;
import xyz.zzyitj.iface.util.CameraUtils;
import xyz.zzyitj.iface.util.RegexUtils;
import xyz.zzyitj.iface.util.Utils;

import java.io.*;

/**
 * xyz.zzyitj.iface.fragment
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 11:13
 * @since 1.0
 */
public class LoginFragment extends Fragment implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = LoginFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 0;
    private View rootView;

    private RelativeLayout relativeLayout;
    private AppCompatEditText phoneNumberEditText;
    private AppCompatEditText passwordEditText;
    private AppCompatButton loginButton;

    private ProgressDialog progressDialog;

    private OpencvJni openCvJni;
    private CameraHelper cameraHelper;
    private int cameraId = Camera.CameraInfo.CAMERA_FACING_FRONT;
    private SurfaceView surfaceView;
    private boolean isLogin = false;

    private void login(byte[] data) {
        progressDialog.show();
        BaiduFaceSearchVo searchDo = new BaiduFaceSearchVo();
        searchDo.setImageType(BaiduApiConst.IMAGE_TYPE_BASE_64);
        searchDo.setImage(Base64.encodeBase64String(data));
        searchDo.setGroupIdList(BaiduApiConst.DEFAULT_GROUP);
        BaiduFaceService.searchUser(IFaceApplication.instance.getApiToken(), searchDo)
                .subscribe(body -> {
                    Log.d(TAG, body.toString());
                    if (body.getErrorCode() == 0) {
                        BaiduFaceSearchDto.User user = body.getResult().getUserList().get(0);
                        if (user.getScore() > BaiduApiConst.SAME_USER_MIN_SCORE) {
                            ApiUserService.getUserFromPhoneNumber(user.getUserId())
                                    .subscribe(apiUserDto -> {
                                        Log.d(TAG, "onPictureTaken: " + apiUserDto.toString());
                                        userLoginSuccess(apiUserDto);
                                    }, throwable -> {
                                        progressDialog.dismiss();
                                        isLogin = false;
                                        startOpenCV();
                                        Toast.makeText(getActivity(), R.string.login_error, Toast.LENGTH_LONG).show();
                                    });
                        } else {
                            progressDialog.dismiss();
                            isLogin = false;
                            startOpenCV();
                            Toast.makeText(getActivity(), R.string.no_same_face_with_library, Toast.LENGTH_LONG).show();
                        }
                    } else {
                        progressDialog.dismiss();
                        isLogin = false;
                        startOpenCV();
                        Toast.makeText(getActivity(), body.getErrorMsg(), Toast.LENGTH_LONG).show();
                    }
                }, throwable -> {
                    progressDialog.dismiss();
                    isLogin = false;
                    startOpenCV();
                    Log.e(TAG, "onPictureTaken: ", throwable);
                });
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initOpenCV(rootView);
        initViews(rootView);
        return rootView;
    }

    private void initOpenCV(View rootView) {
        openCvJni = new OpencvJni();
        surfaceView = rootView.findViewById(R.id.fragment_login_surface_view);
        surfaceView.getHolder().addCallback(this);
        cameraHelper = new CameraHelper(cameraId);
        cameraHelper.setPreviewCallback(this);

        Utils.copyAssets(getActivity(), "lbpcascade_frontalface.xml");
    }

    private void initViews(View rootView) {
        progressDialog = new ProgressDialog(getActivity(), getString(R.string.logging));
        relativeLayout = rootView.findViewById(R.id.fragment_login_relative);
        phoneNumberEditText = rootView.findViewById(R.id.fragment_login_phone_number);
        passwordEditText = rootView.findViewById(R.id.fragment_login_password);
        loginButton = rootView.findViewById(R.id.fragment_login_button);
        loginButton.setOnClickListener(v -> {
            if (relativeLayout.getVisibility() == View.VISIBLE) {
                userLogin();
            }
        });
    }

    public void swapLoginMode() {
        if (relativeLayout.getVisibility() == View.VISIBLE) {
            startOpenCV();
            relativeLayout.setVisibility(View.GONE);
            surfaceView.setVisibility(View.VISIBLE);
        } else {
            stopCamera();
            surfaceView.setVisibility(View.GONE);
            relativeLayout.setVisibility(View.VISIBLE);
        }
    }

    private void userLogin() {
        progressDialog.show();
        ApiUserLoginVo apiUserLoginVo = new ApiUserLoginVo();
        // phone number edittext
        if (TextUtils.isEmpty(phoneNumberEditText.getText())) {
            phoneNumberEditText.setError(getString(R.string.phone_number_cannot_empty));
            progressDialog.dismiss();
            return;
        } else if (!RegexUtils.isMobileExact(phoneNumberEditText.getText())) {
            phoneNumberEditText.setError(getString(R.string.phone_number_format_error));
            progressDialog.dismiss();
            return;
        } else {
            apiUserLoginVo.setPhoneNumber(phoneNumberEditText.getText().toString());
        }
        // password edittext
        if (TextUtils.isEmpty(passwordEditText.getText())) {
            passwordEditText.setError(getString(R.string.password_cannot_empty));
            progressDialog.dismiss();
            return;
        } else {
            apiUserLoginVo.setPassword(passwordEditText.getText().toString());
        }
        ApiUserService.login(apiUserLoginVo)
                .subscribe(this::userLoginSuccess, throwable -> {
                    Log.e(TAG, "userLogin: ", throwable);
                    Toast.makeText(getActivity(), R.string.login_error, Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                });
    }

    private void userLoginSuccess(ApiUserDto apiUserVo) {
        if (apiUserVo != null) {
            // 登录成功
            IFaceApplication.instance.putUser(apiUserVo);
            progressDialog.dismiss();
            isLogin = false;
            startOpenCV();
            Toast.makeText(getActivity(), apiUserVo.getUsername() + getString(R.string.login_success),
                    Toast.LENGTH_LONG).show();
            // 跳转
            getActivity().startActivity(new Intent(getActivity(), MainActivity.class));
            getActivity().finish();
        } else {
            progressDialog.dismiss();
            isLogin = false;
            startOpenCV();
            Toast.makeText(getActivity(), getString(R.string.login_fail),
                    Toast.LENGTH_LONG).show();
        }
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

    private void stopCamera() {
        cameraHelper.stopPreview();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull @NotNull String[] permissions,
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


    @Override
    public void onPreviewFrame(byte[] data, Camera camera) {
        boolean haveFace = openCvJni.postData(data, CameraHelper.WIDTH, CameraHelper.HEIGHT, cameraId);
        if (haveFace && !isLogin) {
            isLogin = true;
            byte[] faceData = CameraUtils.runInPreviewFrame(data, camera);
            login(faceData);
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
