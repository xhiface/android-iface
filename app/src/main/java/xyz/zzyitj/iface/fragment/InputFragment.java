package xyz.zzyitj.iface.fragment;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatEditText;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import io.reactivex.disposables.Disposable;
import org.apache.commons.codec.binary.Base64;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.activity.MainActivity;
import xyz.zzyitj.iface.api.BaiduFaceService;
import xyz.zzyitj.iface.model.BaiduFaceUserAddVo;
import xyz.zzyitj.iface.ui.CircleImageView;

import java.io.ByteArrayOutputStream;

import static android.app.Activity.RESULT_OK;

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
    private static final int REQUEST_IMAGE_CAPTURE = 2;

    private View rootView;

    private CircleImageView headImageView;
    private AppCompatEditText phoneNumberEditText;
    private AppCompatEditText userNameEditText;
    private AppCompatButton inputButton;

    private final MainActivity mainActivity;

    public InputFragment(MainActivity mainActivity) {
        this.mainActivity = mainActivity;
    }

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

    private void initViews(View rootView) {
        headImageView = rootView.findViewById(R.id.fragment_input_head);
        phoneNumberEditText = rootView.findViewById(R.id.fragment_input_phone_number);
        userNameEditText = rootView.findViewById(R.id.fragment_input_user_name);
        headImageView.setDrawingCacheEnabled(true);
        headImageView.setOnClickListener(v -> {
            startCamera();
        });
        inputButton = rootView.findViewById(R.id.fragment_input_button);
        inputButton.setOnClickListener(v -> {
            BaiduFaceUserAddVo userAddDo = new BaiduFaceUserAddVo();
            userAddDo.setImageType("BASE64");
            Bitmap drawingCache = headImageView.getDrawingCache();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            drawingCache.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            userAddDo.setImage(Base64.encodeBase64String(baos.toByteArray()));
            userAddDo.setGroupId("user");
            userAddDo.setUid(phoneNumberEditText.getText().toString());
            Disposable disposable = BaiduFaceService.addUser(IFaceApplication.instance.getApiToken(), userAddDo)
                    .subscribe(body -> {
                        if (body.getErrorCode() == 0) {
                            Toast.makeText(mainActivity, R.string.input_success, Toast.LENGTH_LONG).show();
                            mainActivity.changeBottomTab(0);
                        }
                    }, throwable -> {
                        Toast.makeText(mainActivity, R.string.input_error, Toast.LENGTH_LONG).show();
                        Log.e(TAG, getString(R.string.input_error), throwable);
                    });
        });
    }

    private void startCamera() {
        if (ContextCompat.checkSelfPermission(mainActivity, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_GRANTED) {
            dispatchTakePictureIntent();
        } else if (ActivityCompat.shouldShowRequestPermissionRationale(mainActivity,
                Manifest.permission.CAMERA)) {
            Toast.makeText(mainActivity, R.string.camera_permission_not_granted, Toast.LENGTH_LONG).show();
        } else {
            ActivityCompat.requestPermissions(mainActivity, new String[]{Manifest.permission.CAMERA},
                    REQUEST_CAMERA_PERMISSION);
        }
    }

    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(mainActivity.getPackageManager()) != null) {
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
                    Toast.makeText(mainActivity, R.string.camera_permission_not_granted,
                            Toast.LENGTH_SHORT).show();
                }
                break;
            default:
        }
    }
}
