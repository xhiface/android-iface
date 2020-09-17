package xyz.zzyitj.iface.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.IFaceApplication;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.api.ApiClockService;
import xyz.zzyitj.iface.model.ApiClockDto;
import xyz.zzyitj.iface.ui.ClockRecordAdapter;

import java.util.*;

/**
 * xyz.zzyitj.iface.fragment
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/15 19:04
 * @since 1.0
 */
public class ClockRecordFragment extends Fragment {
    private static final String TAG = ClockRecordFragment.class.getSimpleName();

    private View rootView;
    private RecyclerView recyclerView;
    private ClockRecordAdapter clockRecordAdapter;
    private List<ApiClockDto> apiClockDtoList;

    @Nullable
    @org.jetbrains.annotations.Nullable
    @Override
    public View onCreateView(@NonNull @NotNull LayoutInflater inflater,
                             @Nullable @org.jetbrains.annotations.Nullable ViewGroup container,
                             @Nullable @org.jetbrains.annotations.Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_clock_record, container, false);
        initViews(rootView);
        initAdapter();
        return rootView;
    }

    private void initAdapter() {
        ApiClockService.getAttendRecordList(IFaceApplication.instance.getUserDto().getPhoneNumber())
                .subscribe(apiClockDtoList -> {
                    clockRecordAdapter.setApiClockDtoList(apiClockDtoList);
                }, throwable -> {
                    Log.e(TAG, "initAdapter: ", throwable);
                    Toast.makeText(getActivity(), R.string.get_attend_record_fail, Toast.LENGTH_LONG).show();
                });
    }

    private void initViews(View rootView) {
        apiClockDtoList = new ArrayList<>();
        clockRecordAdapter = new ClockRecordAdapter(apiClockDtoList, getActivity());
        recyclerView = rootView.findViewById(R.id.fragment_clock_record_recycler_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(clockRecordAdapter);
    }
}
