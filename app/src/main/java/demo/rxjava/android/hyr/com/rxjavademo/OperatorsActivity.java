package demo.rxjava.android.hyr.com.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.annotations.NonNull;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;

public class OperatorsActivity extends AppCompatActivity {
    private static final String TAG = "OperatorsActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operators);
    }

    /**
     * Map转换,将一个对象,转换为另一个对象
     *
     * @param view
     */
    public void clickMap(View view) {
        Observable.just(6).map(new Function<Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                return integer + "";
            } // Integer:转换前类型 String:转换后类型
        }).map(new Function<String, Long>() {
            @Override
            public Long apply(@NonNull String s) throws Exception {
                return Long.parseLong(s);
            } // Integer:转换前类型 String:转换后类型
        }).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long result) throws Exception {
                Log.i(TAG, "result: " + result);
            }
        });
    }
}
