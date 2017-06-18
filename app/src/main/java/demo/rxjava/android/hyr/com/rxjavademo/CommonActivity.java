package demo.rxjava.android.hyr.com.rxjavademo;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.io.Serializable;

import demo.rxjava.android.hyr.com.rxjavademo.service.CalcService;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;

public class CommonActivity extends AppCompatActivity implements CalcService.OnResultListener {

    private final CompositeDisposable disposables = new CompositeDisposable();
    //.subscribeWith(subscriber)//在1.x中此方法返回Subscription，而在2.x中是没有返回值的
    //所以增加subscribeWith()方法，用来返回一个Disposable对象
    //使得用户可以CompositeDisposable.add()方法添加对象。1.x为CompositeSubscription
    //其他subscribe()重载方法返回Disposable

    private CalcService calcService;
    private static final String TAG = "CommonActivity";
    private Observer<String> observer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_common);

        calcService = new CalcService();
    }

    public void clickListener(View view) {
        // 计算 10套房子  分给5个亲戚 每人分几套
        getResult();
        // 异步 回调机制
//        getResultAsync();
    }

    public void getResultAsync() {
        calcService.calc(10, 5, this);
    }

    // 设置监听接口
    private void getResult() {
        calcService.setOnResultListener(new CalcService.OnResultListener() {
            @Override
            public void onSuccess(int result) {
                Log.i(TAG, "setOnResultListener onSuccess------result:" + result);
            }

            @Override
            public void onFaild() {
                Log.i(TAG, "计算出错");
            }
        });
        calcService.calc(10, 2);
    }

    public void onSuccess(int result) {
        Log.i(TAG, "onSuccess------result:" + result);
    }


    public void onFaild() {
        Log.i(TAG, "计算出错");
    }


    /**
     * 基本实现
     *
     * @param view
     */
    public void onclik(View view) {
        // 想象工程中的流水线
        // 创建被观察的对象 Observable
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<String> e) throws Exception {
                // 可以调用多次onNext()
                e.onNext("Hello 黄跃然 RxJava");
                e.onNext("Hello 黄跃然 RxJava1");
                e.onNext("Hello 黄跃然 RxJava2");
                e.onNext("Hello 黄跃然 RxJava3");
                e.onNext("Hello 黄跃然 RxJava4");
                e.onNext("Hello 黄跃然 RxJava5");
                e.onNext("Hello 黄跃然 RxJava6");
                e.onNext("Hello 黄跃然 RxJava7");

                e.onComplete(); // 事件序列结束调用

                //事件错误的标记
                // e.onError();
            }
        }).subscribe(new Observer<String>() { //订阅事件 指定一个观察者,被观察者必须指定观察者(订阅者)，整个事件流程菜可以被启动
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: ");
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.i(TAG, "onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError: 错误");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: 结束");
            }
        });
    }

    /**
     * 变换1
     *
     * @param view
     */
    public void onclik1(View view) {
        Observable.just(1, "aaa").subscribe(new Observer<Serializable>() {// 订阅事件
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                Log.i(TAG, "onSubscribe: ");
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull Serializable serializable) {
                Log.i(TAG, "onNext: " + serializable.toString());
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        });
    }

    /**
     * 变换2
     *
     * @param view
     */
    public void onclik2(View view) {

        String[] array = {"url1", "url2"};
        // 相当于rxjava1的onStart
        observer = new Observer<String>() {
            // 相当于rxjava1的onStart
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                //可用于取消订阅
                //    d.dispose();
                //还可以判断是否处于取消状态
                //boolean b=d.isDisposed();
                Log.i(TAG, "onSubscribe: ");
                disposables.add(d);
            }

            @Override
            public void onNext(@NonNull String s) {
                Log.i(TAG, "onNext: " + s);
            }

            @Override
            public void onError(@NonNull Throwable e) {
                Log.i(TAG, "onError: ");
            }

            @Override
            public void onComplete() {
                Log.i(TAG, "onComplete: ");
            }
        };

        Observable.fromArray(array).subscribe(observer); // 订阅事件
        // Observable.OnSubscribe 变成 ObservableOnSubscribe
    }

    /**
     * 变换3
     * action1
     * 在2.0后命名规则有了改变
     * 0后命名规则有了改变
     * Action1--------Consumer一个参数的
     * Action2--------BiConsumer两个参数的
     * 而Function的也一样
     *
     * @param view
     */
    public void onclik3(View view) {

        String[] array = {"url1", "url2"};

        Observable.fromArray(array).subscribe(new Consumer<String>() {// 订阅事件
            @Override
            public void accept(@NonNull String s) throws Exception {
                //这里接收数据项
                Log.i(TAG, "accept: " + s);
            }
        }, new Consumer<Throwable>() {
            @Override
            public void accept(@NonNull Throwable throwable) throws Exception {
                //这里接收onError
                Log.i(TAG, "onError: ");
            }
        }, new Action() {
            @Override
            public void run() throws Exception {
                //这里接收onComplete。 结束
                Log.i(TAG, "onComplete: ");
            }
        });
    }

    /**
     * 变换4
     * @param view
     */
    public void onclik4(View view) {

    }

        @Override
        protected void onDestroy () {
            super.onDestroy();
            if (disposables != null) {
                disposables.dispose();
                disposables.clear();
            }
        }
    }
