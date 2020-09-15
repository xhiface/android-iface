package xyz.zzyitj.iface.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;
import org.jetbrains.annotations.NotNull;
import xyz.zzyitj.iface.R;
import xyz.zzyitj.iface.model.ApiClockDto;

import java.util.List;

/**
 * xyz.zzyitj.iface.ui
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/15 19:38
 * @since 1.0
 */
public class ClockRecordAdapter extends RecyclerView.Adapter<ClockRecordAdapter.ViewHolder> {

    private final List<ApiClockDto> apiClockDtoList;

    public List<ApiClockDto> getApiClockDtoList() {
        return apiClockDtoList;
    }

    public void setApiClockDtoList(List<ApiClockDto> apiClockDtoList) {
        if (this.apiClockDtoList != null) {
            this.apiClockDtoList.clear();
            this.notifyDataSetChanged();
        }
        this.apiClockDtoList.addAll(apiClockDtoList);
        this.notifyDataSetChanged();
    }

    public ClockRecordAdapter(List<ApiClockDto> apiClockDtoList) {
        this.apiClockDtoList = apiClockDtoList;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.clock_record_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull ViewHolder holder, int position) {
        ApiClockDto apiClockDto = apiClockDtoList.get(position);
        holder.usernameTextView.setText(apiClockDto.getUsername());
        holder.phoneNumberTextView.setText(apiClockDto.getPhoneNumber());
        holder.checkTimeTextView.setText(apiClockDto.getCheckTime());
    }

    @Override
    public int getItemCount() {
        return apiClockDtoList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        AppCompatTextView usernameTextView;
        AppCompatTextView phoneNumberTextView;
        AppCompatTextView checkTimeTextView;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            usernameTextView = itemView.findViewById(R.id.clock_record_item_user_name);
            phoneNumberTextView = itemView.findViewById(R.id.clock_record_item_phone_number);
            checkTimeTextView = itemView.findViewById(R.id.clock_record_item_check_time);
        }
    }
}
