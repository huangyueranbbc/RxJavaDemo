package demo.rxjava.android.hyr.com.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

public class SchedulerActivity extends AppCompatActivity {

    private static final String TAG = "SchedulerActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scheduler);
    }

    /**
     * 线程调度器
     * subscribeOn 可执行多次，切换操作符的线程
     * observeOn 只需要执行一次，指定订阅者执行的线程
     * <p/>
     * Schedulers.immediate() 默认线程 2.0改成Schedulers.single()
     * Schedulers.newThread() 每次都创建新的线程
     * Schedulers.io() 包含线程池的机制，个数无线，可以重复利用空闲线程
     * Schedulers.computation() CPU密集计算线程，线程池线程数和CPU数量一致
     * AndroidSchedulers.mainThread() Android更新主界面线程
     *
     * @param view
     */
    public void click(View view) {
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                // 在子线程处理
                Log.i(TAG, "subscribe: " + Thread.currentThread().getName());
                int result = 10 / 5;
                e.onNext(result + "");
                e.onComplete();
            }
        }).subscribeOn(Schedulers.io())// 让ObservableOnSubscribe操作执行在异步线程
                .observeOn(AndroidSchedulers.mainThread()) // 让订阅者代码执行在UI主线程
                .subscribe(new Observer<String>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        // 回调主线程
                        Log.i(TAG, "onNext: " + Thread.currentThread().getName());
                        Log.i(TAG, "onNext: ");
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                    }

                    @Override
                    public void onComplete() {
                    }
                });
    }

    public void click2(View view) {

        Observable.just(6).subscribeOn(Schedulers.single()).map(new Function<Integer, String>() {
            @Override
            public String apply(@NonNull Integer integer) throws Exception {
                Log.i(TAG, "thread 1 : " + Thread.currentThread().getName());
                return integer + "";
            } // Integer:转换前类型 String:转换后类型
        }).subscribeOn(Schedulers.newThread()).map(new Function<String, Long>() {
            @Override
            public Long apply(@NonNull String s) throws Exception {
                Log.i(TAG, "thread 2 : " + Thread.currentThread().getName());
                return Long.parseLong(s);
            } // Integer:转换前类型 String:转换后类型
        }).subscribeOn(Schedulers.computation()).map(new Function<Long, Long>() {
            @Override
            public Long apply(@NonNull Long s) throws Exception {
                Log.i(TAG, "thread 3 : " + Thread.currentThread().getName());
                return s;
            } // Integer:转换前类型 String:转换后类型
        }).observeOn(AndroidSchedulers.mainThread()).subscribe(new Consumer<Long>() {
            @Override
            public void accept(@NonNull Long result) throws Exception {
                Log.i(TAG, "thread 4 : " + Thread.currentThread().getName());
                Log.i(TAG, "result: " + result);
            }
        });

    }
}
