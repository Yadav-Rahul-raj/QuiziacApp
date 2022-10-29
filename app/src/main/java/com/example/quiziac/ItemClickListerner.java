package com.example.quiziac;

import android.view.View;

public interface ItemClickListerner {
    void onClick(View view, int position, boolean isLongClick);
}
