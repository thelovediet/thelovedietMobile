package lovedient.com.thelovedietandroid.utils;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by MME on 6/8/2018.
 */

public class HorizontalSpaceItemDecoration extends RecyclerView.ItemDecoration {

    private final int horizontalSpace;

    public HorizontalSpaceItemDecoration(int verticalSpaceHeight) {
        this.horizontalSpace = verticalSpaceHeight;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent,
                               RecyclerView.State state) {
        outRect.left = horizontalSpace;
    }
}
