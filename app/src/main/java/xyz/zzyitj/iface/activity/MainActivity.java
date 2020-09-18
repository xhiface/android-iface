package xyz.zzyitj.iface.activity;

import android.content.Intent;
import android.view.Menu;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.fragment.app.FragmentTransaction;
import com.roughike.bottombar.BottomBar;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.ApiConst;
import xyz.zzyitj.iface.fragment.ClockFragment;
import xyz.zzyitj.iface.fragment.ClockRecordFragment;
import xyz.zzyitj.iface.fragment.RegisterFragment;

/**
 * @author intent
 */
public class MainActivity extends AppCompatActivity {
    private static final String TAG = MainActivity.class.getSimpleName();
    public static final int CLOCK_FRAGMENT = 0;
    public static final int REGISTER_FRAGMENT = 1;
    public static final int CLOCK_RECORD_FRAGMENT = 2;

    private ClockFragment clockFragment;
    private ClockRecordFragment clockRecordFragment;
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
                IFaceApplication.instance.removeUser();
                startActivity(new Intent(this, LoginActivity.class));
                finish();
                break;
            default:
        }
        return super.onOptionsItemSelected(item);
    }

    private void initViews() {
        bottomBar = findViewById(R.id.main_bottom_bar);
        clockFragment = new ClockFragment(this);
        if (ApiConst.DEFAULT_ROLE_ADMIN.equals(IFaceApplication.instance.getUserDto().getRole())) {
            registerFragment = new RegisterFragment(this);
            bottomBar.setItems(R.xml.bottombar_tabs);
        } else {
            bottomBar.setItems(R.xml.bottombar_tabs_user);
        }
        clockRecordFragment = new ClockRecordFragment();
        bottomBar.setOnTabSelectListener(tabId -> {
            switch (tabId) {
                case R.id.tab_clock:
                    changeFragment(CLOCK_FRAGMENT);
                    break;
                case R.id.tab_input:
                    changeFragment(REGISTER_FRAGMENT);
                    break;
                case R.id.tab_clock_record:
                    changeFragment(CLOCK_RECORD_FRAGMENT);
                    break;
                default:
            }
        });
    }

    public void changeBottomTab(int pos) {
        bottomBar.selectTabAtPosition(pos, true);
    }

    public void changeFragment(int fragmentId) {
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        switch (fragmentId) {
            case CLOCK_FRAGMENT:
                if (registerFragment != null && registerFragment.isAdded()) {
                    fragmentTransaction.remove(registerFragment);
                }
                if (clockRecordFragment.isAdded()) {
                    fragmentTransaction.remove(clockRecordFragment);
                }
                if (!clockFragment.isAdded()) {
                    fragmentTransaction.add(R.id.main_content, clockFragment).commit();
                }
                break;
            case REGISTER_FRAGMENT:
                if (clockFragment.isAdded()) {
                    fragmentTransaction.remove(clockFragment);
                }
                if (clockRecordFragment.isAdded()) {
                    fragmentTransaction.remove(clockRecordFragment);
                }
                if (registerFragment != null && !registerFragment.isAdded()) {
                    fragmentTransaction.add(R.id.main_content, registerFragment).commit();
                }
                break;
            case CLOCK_RECORD_FRAGMENT:
                if (clockFragment.isAdded()) {
                    fragmentTransaction.remove(clockFragment);
                }
                if (registerFragment != null && registerFragment.isAdded()) {
                    fragmentTransaction.remove(registerFragment);
                }
                if (!clockRecordFragment.isAdded()) {
                    fragmentTransaction.add(R.id.main_content, clockRecordFragment).commit();
                }
                break;
            default:
        }
    }

}