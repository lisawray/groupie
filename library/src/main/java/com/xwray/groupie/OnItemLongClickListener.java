package com.xwray.groupie;

import androidx.annotation.NonNull;
import android.view.View;

public interface OnItemLongClickListener {

    boolean onItemLongClick(@NonNull Item item, @NonNull View view);

}
