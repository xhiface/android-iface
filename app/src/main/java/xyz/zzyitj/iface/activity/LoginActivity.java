package xyz.zzyitj.iface.activity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.roughike.bottombar.BottomBar;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.BaiduAuthService;
import xyz.zzyitj.iface.fragment.LoginFragment;
import xyz.zzyitj.iface.fragment.RegisterFragment;
import xyz.zzyitj.iface.model.ApiUserDto;

/**
 * xyz.zzyitj.iface.activity
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 11:03
 * @since 1.0
 */
public class LoginActivity extends AppCompatActivity {
    private static final String TAG = LoginActivity.class.getSimpleName();
    public static final int LOGIN_FRAGMENT = 0;
    public static final int REGISTER_FRAGMENT = 1;

    private BottomBar bottomBar;

    private LoginFragment loginFragment;
    private RegisterFragment registerFragment;

    @Override
    protected void onCreate(@Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        initViews();
        initToken();
    }

    private void init() {
        ApiUserDto apiUserDto = IFaceApplication.instance.getUser();
        if (apiUserDto != null) {
            startActivity(new Intent(this, MainActivity.class));
            finish();
        }
    }

    private void initViews() {
        loginFragment = new LoginFragment();
        registerFragment = new RegisterFragment(this);
        if (!loginFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.login_content, loginFragment)
                    .commit();
        }
        bottomBar = findViewById(R.id.login_bottom_bar);
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_login:
                    changeFragment(LOGIN_FRAGMENT);
                    break;
                case R.id.tab_register:
                    changeFragment(REGISTER_FRAGMENT);
                    break;
                default:
            }
        });
    }

    public void changeFragment(int fragmentId) {
        switch (fragmentId) {
            case LOGIN_FRAGMENT:
                if (registerFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(registerFragment)
                            .add(R.id.login_content, loginFragment)
                            .commit();
                }
                break;
            case REGISTER_FRAGMENT:
                if (loginFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(loginFragment)
                            .add(R.id.login_content, registerFragment)
                            .commit();
                }
                break;
            default:
        }
    }

    /**
     * 初始化token
     */
    private void initToken() {
        if (IFaceApplication.instance.getApiToken() == null) {
            BaiduAuthService.getToken()
                    .subscribe(apiTokenDto -> {
                        if (apiTokenDto != null && apiTokenDto.getAccessToken() != null) {
                            IFaceApplication.instance.setApiToken(apiTokenDto);
                            Log.d(TAG, apiTokenDto.toString());
                        } else {
                            Toast.makeText(this, "token cannot be null.", Toast.LENGTH_LONG).show();
                        }
                    }, throwable -> {
                        Log.e(TAG, "getToken error.", throwable);
                        Toast.makeText(this, "token error.", Toast.LENGTH_LONG).show();
                    });
        } else {
            Log.d(TAG, IFaceApplication.instance.getApiToken());
        }
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_switch_login_mode:
                if (loginFragment.isAdded()){
                    loginFragment.swapLoginMode();
                }
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }
}
