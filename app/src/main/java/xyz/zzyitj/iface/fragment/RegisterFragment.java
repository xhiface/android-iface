package xyz.zzyitj.iface.fragment;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.appcompat.widget.AppCompatSpinner;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.activity.LoginActivity;
import xyz.zzyitj.iface.activity.MainActivity;
import xyz.zzyitj.iface.api.ApiUserService;
import xyz.zzyitj.iface.api.BaiduApiConst;
import xyz.zzyitj.iface.api.BaiduFaceService;
import xyz.zzyitj.iface.model.ApiUserVo;
import xyz.zzyitj.iface.model.BaiduFaceUserAddVo;
import xyz.zzyitj.iface.ui.CircleImageView;
import xyz.zzyitj.iface.ui.ProgressDialog;
import xyz.zzyitj.iface.util.RegexUtils;

import java.io.ByteArrayOutputStream;
import java.util.Objects;

import static android.app.Activity.RESULT_OK;

/**
 * xyz.zzyitj.iface.fragment
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/12 15:58
 * @since 1.0
 */
public class RegisterFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = RegisterFragment.class.getSimpleName();
    private static final int REQUEST_CAMERA_PERMISSION = 1;
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private View rootView;

    private CircleImageView headImageView;
    private AppCompatEditText phoneNumberEditText;
    private AppCompatEditText emailEditText;
    private AppCompatEditText userNameEditText;
    private AppCompatEditText ageEditText;
    private AppCompatSpinner genderSpinner;
    private AppCompatEditText passwordEditText;
    private AppCompatButton registerButton;

    private final Activity activity;

    private Bitmap tempBitmap;
    /**
     * 性别
     * true为男
     * false为女
     */
    private boolean gender;
    /**
     * 是否正在注册
     */
    private boolean isRegistering;

    public RegisterFragment(Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_register, container, false);
        initViews(rootView);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (tempBitmap != null) {
            headImageView.setImageBitmap(tempBitmap);
        }
        clearViewsData();
    }

    private void clearViewsData() {
        Objects.requireNonNull(phoneNumberEditText.getText()).clear();
        Objects.requireNonNull(emailEditText.getText()).clear();
        Objects.requireNonNull(userNameEditText.getText()).clear();
        Objects.requireNonNull(ageEditText.getText()).clear();
        Objects.requireNonNull(passwordEditText.getText()).clear();
    }

    private void initViews(View rootView) {
        headImageView = rootView.findViewById(R.id.fragment_register_head);
        phoneNumberEditText = rootView.findViewById(R.id.fragment_register_phone_number);
        emailEditText = rootView.findViewById(R.id.fragment_register_email);
        userNameEditText = rootView.findViewById(R.id.fragment_register_user_name);
        ageEditText = rootView.findViewById(R.id.fragment_register_age);
        genderSpinner = rootView.findViewById(R.id.fragment_register_gender);
        passwordEditText = rootView.findViewById(R.id.fragment_register_password);
        registerButton = rootView.findViewById(R.id.fragment_register_button);
        headImageView.setOnClickListener(this);
        registerButton.setOnClickListener(this);
        genderSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gender = position == 0;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fragment_register_head:
                startCamera();
                break;
            case R.id.fragment_register_button:
                if (!isRegistering) {
                    registerUser();
                }
                break;
            default:
        }
    }

    private void registerUser() {
        if (tempBitmap != null) {
            BaiduFaceUserAddVo baiduFaceUserAddVo = new BaiduFaceUserAddVo();
            baiduFaceUserAddVo.setImageType(BaiduApiConst.IMAGE_TYPE_BASE_64);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            tempBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            baiduFaceUserAddVo.setImage(Base64.encodeBase64String(baos.toByteArray()));
            baiduFaceUserAddVo.setGroupId(BaiduApiConst.DEFAULT_GROUP);
            // api user vo
            ApiUserVo apiUserVo = new ApiUserVo();
            // phone number edittext
            if (TextUtils.isEmpty(phoneNumberEditText.getText())) {
                phoneNumberEditText.setError(getString(R.string.phone_number_cannot_empty));
                return;
            } else if (!RegexUtils.isMobileExact(phoneNumberEditText.getText())) {
                phoneNumberEditText.setError(getString(R.string.phone_number_format_error));
                return;
            } else {
                baiduFaceUserAddVo.setUid(phoneNumberEditText.getText().toString());
                apiUserVo.setPhoneNumber(phoneNumberEditText.getText().toString());
            }
            // email edittext
            if (TextUtils.isEmpty(emailEditText.getText())) {
                emailEditText.setError(getString(R.string.email_cannot_empty));
                return;
            } else if (!RegexUtils.isEmail(emailEditText.getText())) {
                emailEditText.setError(getString(R.string.email_format_error));
                return;
            } else {
                apiUserVo.setEmail(emailEditText.getText().toString());
            }
            // user name edittext
            if (TextUtils.isEmpty(userNameEditText.getText())) {
                userNameEditText.setError(getString(R.string.user_name_cannot_empty));
                return;
            } else {
                apiUserVo.setUsername(userNameEditText.getText().toString());
            }
            // age edittext
            if (TextUtils.isEmpty(ageEditText.getText())) {
                ageEditText.setError(getString(R.string.age_cannot_empty));
                return;
            } else {
                int age = Integer.parseInt(Objects.requireNonNull(ageEditText.getText()).toString());
                if (age > 140 || age < 1) {
                    ageEditText.setError(getString(R.string.age_format_error));
                    return;
                } else {
                    apiUserVo.setAge(age);
                }
            }
            // password edittext
            if (TextUtils.isEmpty(passwordEditText.getText())) {
                passwordEditText.setError(getString(R.string.password_cannot_empty));
                return;
            } else {
                apiUserVo.setPassword(passwordEditText.getText().toString());
            }
            apiUserVo.setGender(gender);
            apiUserVo.setGroupId(BaiduApiConst.DEFAULT_GROUP);
            ProgressDialog progressDialog = new ProgressDialog(activity, getString(R.string.registering));
            progressDialog.show();
            isRegistering = true;
            BaiduFaceService.addUser(IFaceApplication.instance.getApiToken(), baiduFaceUserAddVo)
                    .subscribe(body -> {
                        Log.d(TAG, apiUserVo.toString());
                        if (body.getErrorCode() == 0) {
                            // 百度注册成功
                            ApiUserService.register(apiUserVo)
                                    .subscribe(apiUserDto -> {
                                        progressDialog.dismiss();
                                        isRegistering = false;
                                        if (apiUserDto != null && apiUserDto.getRole() != null) {
                                            Toast.makeText(activity, R.string.register_success, Toast.LENGTH_LONG).show();
                                            IFaceApplication.instance.setUserDto(apiUserDto);
                                            if (activity instanceof LoginActivity) {
                                                IFaceApplication.instance.putUser(apiUserDto);
                                                // 跳转
                                                activity.startActivity(new Intent(activity, MainActivity.class));
                                                activity.finish();
                                            } else if (activity instanceof MainActivity) {
                                                MainActivity mainActivity = (MainActivity) (activity);
                                                mainActivity.changeBottomTab(MainActivity.CLOCK_FRAGMENT);
                                            }
                                        } else {
                                            Toast.makeText(activity, R.string.register_error, Toast.LENGTH_LONG).show();
                                        }
                                    }, throwable -> {
                                        Toast.makeText(activity, R.string.api_register_error, Toast.LENGTH_LONG).show();
                                        Log.e(TAG, "registerUser: ", throwable);
                                        progressDialog.dismiss();
                                        isRegistering = false;
                                    });
                        } else {
                            progressDialog.dismiss();
                            isRegistering = false;
                            Toast.makeText(activity, R.string.register_error + body.getErrorMsg(), Toast.LENGTH_LONG).show();
                        }
                    }, throwable -> {
                        Toast.makeText(activity, R.string.register_error, Toast.LENGTH_LONG).show();
                        Log.e(TAG, getString(R.string.register_error), throwable);
                        progressDialog.dismiss();
                        isRegistering = false;
                    });
        } else {
            Toast.makeText(activity, R.string.header_cannot_empty, Toast.LENGTH_LONG).show();
        }
    }

    private void startCamera() {
        if (ContextCompat.checkSelfPermission(activity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                Manifest.permission.CAMERA)) {
            Toast.makeText(activity, R.string.camera_permission_not_granted, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 @Nullable Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            assert data != null;
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            tempBitmap = imageBitmap;
            headImageView.setImageBitmap(imageBitmap);
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
                    Toast.makeText(activity, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
