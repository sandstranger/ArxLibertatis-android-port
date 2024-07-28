package com.arxlibertatis.ui.activity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import androidx.annotation.Nullable;
import com.arxlibertatis.engine.EngineKt;

public class MainActivity extends android.app.Activity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Button button= new Button (this);
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(
                FrameLayout.LayoutParams.WRAP_CONTENT,
                FrameLayout.LayoutParams.WRAP_CONTENT);
        params.topMargin = 0;
        params.gravity = Gravity.TOP | Gravity.CENTER_HORIZONTAL;

        button.setText("Start Game");
        addContentView(button, params);
        // setContentView(tv);
        button.setOnClickListener(new Button.OnClickListener(){
            @Override
            public void onClick(View v) {
                EngineKt.startEngine(MainActivity.this);
            }

        });
    }
}
