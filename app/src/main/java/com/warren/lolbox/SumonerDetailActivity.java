package com.warren.lolbox;

import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.warren.lolbox.util.LogTool;
import com.warren.lolbox.widget.TitleBar;

/**
 * 召唤师详情Activity
 * @author yangsheng
 * @date 2015年3月9日
 */
public class SumonerDetailActivity extends BaseActivity {

	public static final String EXTRA_CONTENTDATA = "CONTENTDATA";
	public static final String EXTRA_URL = "FILEPATH";

	private TitleBar mTb;
	private WebView mWvContent;

	private String mStrContent;
	private String mStrURL;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_summonerdetail);

		mStrContent = getIntent().getStringExtra(EXTRA_CONTENTDATA);
		mStrURL = getIntent().getStringExtra(EXTRA_URL);

		initCtrl();
	}

	private void initCtrl() {
		mTb = (TitleBar) findViewById(R.id.titlebar);
		mWvContent = (WebView) findViewById(R.id.wv_content);

		WebSettings wbSet = mWvContent.getSettings();
		wbSet.setDefaultTextEncodingName("utf-8");
		wbSet.setJavaScriptEnabled(true);
		wbSet.setJavaScriptCanOpenWindowsAutomatically(false);
		wbSet.setBuiltInZoomControls(true);
		wbSet.setDisplayZoomControls(false);

		mWvContent.setWebViewClient(new SummonerWebClient());

		if (mStrContent != null) {
			mWvContent.loadDataWithBaseURL(null, mStrContent, "text/html", "utf-8", null);
		} else if (mStrURL != null) {
			mWvContent.loadUrl(mStrURL);
		} else {
			mWvContent.loadData("没有数据", "text/html", "utf-8");
		}

		// mWvContent.setWebViewClient();

	}

	class SummonerWebClient extends WebViewClient {
		@Override
		public boolean shouldOverrideUrlLoading(WebView view, String strUrl) {
			LogTool.i("SummonerDetailActivity", "召唤师比赛URL: " + strUrl);

			BaseKitManager.openUrl(SumonerDetailActivity.this, strUrl);
			return true;
		}
	}

	@Override
	protected boolean goBack() {
		return false;
	}

}
