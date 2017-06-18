package demo.rxjava.android.hyr.com.rxjavademo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);
    }

    @OnClick({R.id.btn_common, R.id.btn_operators, R.id.btn_scheduler})
    public void onActionClick(View view) {
        switch (view.getId()) {
            case R.id.btn_common: // 基本实现界面
                startActivity(new Intent(this, CommonActivity.class));
                break;
            case R.id.btn_operators: // 操作符界面
                startActivity(new Intent(this, OperatorsActivity.class));
                break;
            case R.id.btn_scheduler: // 线程调度器
                startActivity(new Intent(this, SchedulerActivity.class));
                break;
            default:
                break;
        }
    }

}
