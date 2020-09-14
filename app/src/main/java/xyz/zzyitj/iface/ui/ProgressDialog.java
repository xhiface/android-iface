package xyz.zzyitj.iface.ui;

import android.app.Dialog;
import android.content.Context;
import androidx.annotation.NonNull;
import xyz.zzyitj.iface.R;

/**
 * xyz.zzyitj.iface.ui
 *
 * @author intent zzy.main@gmail.com
 * @date 2020/9/14 15:41
 * @since 1.0
 */
public class ProgressDialog extends Dialog {
    public ProgressDialog(@NonNull Context context) {
        super(context);
        setContentView(R.layout.dialog_progress);
        // 按空白处不能取消动画
        setCanceledOnTouchOutside(false);
    }
}
