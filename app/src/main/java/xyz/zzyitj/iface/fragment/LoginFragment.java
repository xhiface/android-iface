package xyz.zzyitj.iface.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.R;

/**
 * xyz.zzyitj.iface.fragment
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 11:13
 * @since 1.0
 */
public class LoginFragment extends Fragment {
    private View rootView;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_login, container, false);
        initViews(rootView);
        return rootView;
    }

    private void initViews(View rootView) {

    }
}
