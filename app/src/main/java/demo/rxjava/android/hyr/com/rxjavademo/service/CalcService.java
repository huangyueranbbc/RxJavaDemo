package demo.rxjava.android.hyr.com.rxjavademo.service;

import android.os.SystemClock;

/**
 * Created by huangyueran on 2017/6/18.
 */
public class CalcService {

    int result = 0;

    private OnResultListener onResultListener;


    /**
     * 计算每个亲戚分多少套房子
     *
     * @param total
     * @param count
     * @return
     */
    public void calc(final int total, final int count) {
        // 耗时操作
        // 如果比较长 可以造成ANR

        // 开启异步请求 获取数据
        new Thread() {

            @Override
            public void run() {
                try {
                    SystemClock.sleep(2000);
                    result = total / count;
                    if (onResultListener != null) {
                        onResultListener.onSuccess(result);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    if (onResultListener != null) {
                        onResultListener.onFaild();
                    }
                }
            }
        }.start();
    }

    /**
     * 异步
     * 回调机制
     *
     * @param total
     * @param count
     * @param listener
     * @return
     */
    public void calc(final int total, final int count, final OnResultListener listener) {
        // 耗时操作
        // 如果比较长 可以造成ANR

        // 开启异步请求 获取数据
        new Thread() {

            @Override
            public void run() {
                try {
                    SystemClock.sleep(3000);
                    result = total / count;
                    listener.onSuccess(result);
                } catch (Exception e) {
                    e.printStackTrace();
                    listener.onFaild();
                }
            }
        }.start();
    }

    public void setOnResultListener(OnResultListener onResultListener) {
        this.onResultListener = onResultListener;
    }

    // 面向接口编程 AOP
    public interface OnResultListener {

        void onSuccess(int result);

        void onFaild();
    }
}
