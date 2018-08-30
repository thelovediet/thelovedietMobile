package lovedient.com.thelovedietandroid.utils;

import android.content.Context;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import lovedient.com.thelovedietandroid.R;

public class HandleDialog extends ParentDialog {
    public HandleDialog(Context context, String message) {
        super(context);
        View view = LayoutInflater.from(context).inflate(R.layout.message_popup, null);
        TextView msg = view.findViewById(R.id.message);
        ImageView close = view.findViewById(R.id.close);
        msg.setText(message);
        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

    }
}
