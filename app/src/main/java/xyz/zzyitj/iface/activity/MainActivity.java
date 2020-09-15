package xyz.zzyitj.iface.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.roughike.bottombar.BottomBar;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.ApiConst;
import xyz.zzyitj.iface.fragment.ClockFragment;
import xyz.zzyitj.iface.fragment.RegisterFragment;

/**
 * @author intent
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int CLOCK_FRAGMENT = 0;
    public static final int REGISTER_FRAGMENT = 1;

    private ClockFragment clockFragment;
    private RegisterFragment registerFragment;

    private BottomBar bottomBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initViews();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.action_login_out:
                IFaceApplication.instance.removeLocalStorage(ApiConst.SHARED_PREFS_NAME, ApiConst.SHARED_PREFS_USER);
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        clockFragment = new ClockFragment(this);
        registerFragment = new RegisterFragment(this);
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
                    changeFragment(REGISTER_FRAGMENT);
                    break;
                default:
            }
        });
    }

    public void changeBottomTab(int pos) {
        bottomBar.selectTabAtPosition(pos, true);
    }

    public void changeFragment(int fragmentId) {
        switch (fragmentId) {
            case CLOCK_FRAGMENT:
                if (registerFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(registerFragment)
                            .add(R.id.main_content, clockFragment)
                            .commit();
                }
                break;
            case REGISTER_FRAGMENT:
                if (clockFragment.isAdded()) {
                    getSupportFragmentManager().beginTransaction()
                            .remove(clockFragment)
                            .add(R.id.main_content, registerFragment)
                            .commit();
                }
                break;
            default:
        }
    }
}