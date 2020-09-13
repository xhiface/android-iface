package xyz.zzyitj.iface.activity;

import android.util.Log;
import android.widget.Toast;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.roughike.bottombar.BottomBar;
import com.roughike.bottombar.OnTabSelectListener;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.AuthService;
import xyz.zzyitj.iface.fragment.ClockFragment;
import xyz.zzyitj.iface.fragment.InputFragment;

/**
 * @author intent
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();

    private ClockFragment clockFragment;
    private InputFragment inputFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.d(TAG, "onCreate");
        initViews();
        initToken();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    private void initViews() {
        clockFragment = new ClockFragment();
        inputFragment = new InputFragment();
        if (!clockFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, clockFragment)
                    .commit();
        }
        BottomBar bottomBar = findViewById(R.id.main_bottom_bar);
        bottomBar.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelected(@IdRes int tabId) {
                switch (tabId) {
                    case R.id.tab_clock:
                        Log.d(TAG, "tab_clock");
                        if (inputFragment.isAdded()) {
                            getSupportFragmentManager().beginTransaction()
                                    .remove(inputFragment)
                                    .add(R.id.main_content, clockFragment)
                                    .commit();
                        }
                        break;
                    case R.id.tab_input:
                        Log.d(TAG, "tab_input");
                        if (clockFragment.isAdded()) {
                            getSupportFragmentManager().beginTransaction()
                                    .remove(clockFragment)
                                    .add(R.id.main_content, inputFragment)
                                    .commit();
                        }
                        break;
                    default:
                }
            }
        });
    }

    /**
     * 初始化token
     */
    private void initToken() {
        if (IFaceApplication.instance.getApiToken() == null) {
            AuthService.getToken()
                    .subscribe(apiTokenDto -> {
                        if (apiTokenDto != null && apiTokenDto.getAccessToken() != null) {
                            IFaceApplication.instance.setApiToken(apiTokenDto);
                            Log.d(TAG, apiTokenDto.toString());
                        } else {
                            Toast.makeText(MainActivity.this,
                                    "token cannot be null.", Toast.LENGTH_LONG).show();
                        }
                    }, throwable -> {
                        Log.e(TAG, "getToken error.", throwable);
                        Toast.makeText(MainActivity.this,
                                "token error.", Toast.LENGTH_LONG).show();
                    });
        } else {
            Log.d(TAG, IFaceApplication.instance.getApiToken());
        }
    }
}