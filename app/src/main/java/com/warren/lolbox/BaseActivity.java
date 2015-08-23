package com.warren.lolbox;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.warren.lolbox.model.IListener;
import com.warren.lolbox.util.StringUtils;
import com.warren.lolbox.widget.TitleBar;

/**
 * 基础Activity，所有Activity均应继承该类
 * @update 2015.08.23 在网络请求和Json解析回调中添加判断activity是否已被关闭，以避免出现
 * activity已被关闭而回调仍在执行更新界面等操作导致报错。
 * @author yangsheng
 * @date 2015年2月24日
 */
public abstract class BaseActivity extends Activity {

	private static Map<String, Object> arguments = new HashMap<String, Object>();
    private TitleBar mTb;
    private boolean mIsFinished = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}

	/**
	 * Activity间传递参数用。添加参数，为避免覆盖，使用该方法时应确保参数 strExtraName 的唯一性
	 * @param strExtraName
	 * @param objExtra
	 * @return
	 */
	protected static Object putArgument(String strExtraName, Object objExtra) {
		return arguments.put(strExtraName, objExtra);
	}

	/**
	 * Activity间传递参数用。取参数，一旦成功取出参数对应的对象，该键值对将从参数Map中移除
	 * @param strExtraName
	 * @param objDefault
	 * @return
	 */
	protected static Object getArgument(String strExtraName, Object objDefault) {
		Object obj = arguments.get(strExtraName);
		if (obj != null) {
			arguments.remove(strExtraName);
		} else {
			obj = objDefault;
		}
		return obj;
	}

	/**
	 * 返回操作。
	 * @description {@link BaseActivity} 的 {@link #onKeyDown(int, KeyEvent)}
	 *              中重写了返回键的事件处理，先判断
	 *              本方法的返回值，true 表示返回操作处理完毕；false表示返回操作尚未处理完毕，将继续执行
	 *              {@link Activity}的返回键事件。
	 * @return
	 */
	protected abstract boolean goBack();

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {

        boolean handled = false;
        switch (keyCode){
            case KeyEvent.KEYCODE_BACK:
                handled = goBack();
                break;
            case KeyEvent.KEYCODE_MENU:
                handled = this.mTb != null ? this.mTb.executeRightClick() : false;
                break;
            default:
                break;
        }
        return handled ? true : super.onKeyDown(keyCode, event);
	}

    @Override
    protected void onDestroy() {
        this.mIsFinished = true;
        super.onDestroy();
    }

    /**
     * 设置标题栏
     * @param tb
     */
    protected void setTitleBar(TitleBar tb){
        this.mTb = tb;
    }

    /**
     * 取自定义的标题栏
     * @return
     */
    protected TitleBar getTitleBar(){
        return this.mTb;
    }

    /**
     * activity是否已经被关闭
     * @return
     */
    protected boolean isFinished(){
        return this.mIsFinished;
    }

	/**
	 * 默认使用切换动画 android.R.anim.slide_in_left, android.R.anim.slide_out_right
	 */
	@Override
	public void startActivity(Intent intent) {
		super.startActivity(intent);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}

	/**
	 * 使用指定的切换动画
	 * @param intent
	 * @param animIn
	 * @param animOut
	 */
	public void startActivity(Intent intent, int animIn, int animOut) {
		super.startActivity(intent);
		if (animIn < 0) {
			animIn = android.R.anim.slide_in_left;
		}
		if (animOut < 0) {
			animOut = android.R.anim.slide_out_right;
		}
		overridePendingTransition(animIn, animOut);
	}

	/**
	 * 异步访问指定路径，然后再异步解析Json
	 * @param strUrl
	 * @param headers
	 * @param cls
	 * @param listener
	 */
	public <T> void httpGetAndParse(final String strUrl, final Map<String, String> headers,
				final Class<T> cls, final IListener<T> listener) {
		httpGet(strUrl, headers, new IListener<String>() {

			@Override
			public void onCall(String strResult) {
				if (StringUtils.isNullOrZero(strResult)) {
					listener.onCall(null);
					return;
				}
				jsonParse(strResult, cls, listener);
			}
		});
	}

	/**
	 * 异步访问指定路径，然后再异步解析Json，解析为列表
	 * @param strUrl
	 * @param headers
	 * @param cls
	 * @param listener
	 */
	public <T> void httpGetAndParseList(final String strUrl, final Map<String, String> headers,
				final Class<T> cls, final IListener<List<T>> listener) {
		httpGet(strUrl, headers, new IListener<String>() {

            @Override
            public void onCall(String strResult) {
                if (StringUtils.isNullOrZero(strResult)) {
                    listener.onCall(null);
                    return;
                }

                jsonParseList(strResult, cls, listener);
            }
        });
	}

	/**
	 * 异步访问指定路径
	 * @param strUrl
	 * @param headers
	 * @param listener
	 */
	public void httpGet(final String strUrl, final Map<String, String> headers,
				final IListener<String> listener) {
		AppContext.getApp().getNetManager().get(strUrl, headers, new IListener<String>() {
            @Override
            public void onCall(String s) {
                if( ! isFinished()){
                    listener.onCall(s);
                }
            }
        });
	}

	/**
	 * 异步解析指定Json
	 * @param strJson
	 * @param cls
	 * @param listener
	 */
	public <T> void jsonParse(final String strJson, final Class<T> cls, final IListener<T> listener) {
		AppContext.getApp().getJsonManager().parse(strJson, cls, new IListener<T>() {
            @Override
            public void onCall(T t) {
                if( ! isFinished()){
                    listener.onCall(t);
                }
            }
        });
	}

	/**
	 * 异步解析指定Json
	 * @param strJson
	 * @param cls
	 * @param listener
	 */
	public <T> void jsonParseList(final String strJson, final Class<T> cls,
				final IListener<List<T>> listener) {
		AppContext.getApp().getJsonManager().parseList(strJson, cls, new IListener<List<T>>() {
            @Override
            public void onCall(List<T> ts) {
                if( ! isFinished()){
                    listener.onCall(ts);
                }
            }
        });
	}

	/**
	 * 异步解析指定Json
	 * @param strJson
	 * @param listener
	 */
	public <T> void jsonParseMap(final String strJson,
				final IListener<Map<String, HashMap<String, Object>>> listener) {

		AppContext.getApp().getJsonManager().parseMap(strJson, listener);
	}
}
