package xyz.zzyitj.iface.activity;

import android.util.Log;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.roughike.bottombar.BottomBar;
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
    public static final int CLOCK_FRAGMENT = 0;
    public static final int INPUT_FRAGMENT = 1;

    private ClockFragment clockFragment;
    private InputFragment inputFragment;

    private BottomBar bottomBar;

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
        clockFragment = new ClockFragment(this);
        inputFragment = new InputFragment(this);
        if (!clockFragment.isAdded()) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.main_content, clockFragment)
                    .commit();
        }
        bottomBar = findViewById(R.id.main_bottom_bar);
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_clock:
                    changeFragment(CLOCK_FRAGMENT);
                    break;
                case R.id.tab_input:
                    changeFragment(INPUT_FRAGMENT);
                    break;
                default:
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

    public void changeBottomTab(int pos) {
        bottomBar.selectTabAtPosition(pos, true);
    }

    public void changeFragment(int fragmentId) {
        switch (fragmentId) {
            case CLOCK_FRAGMENT:
                if (inputFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(inputFragment)
                            .add(R.id.main_content, clockFragment)
                            .commit();
                }
                break;
            case INPUT_FRAGMENT:
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
}