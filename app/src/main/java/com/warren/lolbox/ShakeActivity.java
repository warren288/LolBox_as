package com.warren.lolbox;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.warren.lolbox.model.IListener;
import com.warren.lolbox.url.URLUtil;
import com.warren.lolbox.util.StringUtils;
import com.warren.lolbox.widget.TitleBar;


/**
 * 摇一摇查看当前进行的对战信息
 * @author yangsheng
 * @date 2015/6/6
 */
public class ShakeActivity extends BaseActivity {

	private TitleBar mTb;
	private TextView mTvServer;
	private TextView mTvSummoner;
	private ImageView mImgViberate;
	private ImageView mImgVoice;
	private ImageView mImgShake;

	private String mStrSummoner;
	private String mStrServer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_shake);
		mStrSummoner = SystemConfig.getIntance().getDefaultSummonerName();
		mStrServer = SystemConfig.getIntance().getDefaultSummonerServer();
		if (StringUtils.isNullOrZero(mStrServer) || StringUtils.isNullOrZero(mStrSummoner)) {
			Toast.makeText(this, "请先设置默认召唤师", Toast.LENGTH_SHORT).show();
			finish();
		} else {
			initCtrl();
		}
	}

	private void initCtrl(){
		mTb = (TitleBar) findViewById(R.id.tb);
		mTvServer = (TextView) findViewById(R.id.tv_server);
		mTvSummoner = (TextView) findViewById(R.id.tv_summoner);
		mImgVoice = (ImageView) findViewById(R.id.img_voice);
		mImgViberate = (ImageView) findViewById(R.id.img_viberate);
		mImgShake = (ImageView) findViewById(R.id.img_shake);

		mTvServer.setText(mStrServer);
		mTvSummoner.setText(mStrSummoner);

		mTb.setLeftClick(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				finish();
			}
		});
		mImgShake.setOnLongClickListener(new View.OnLongClickListener() {
			@Override
			public boolean onLongClick(View v) {
				requestForCurBattle();
				return false;
			}
		});
	}

	private void requestForCurBattle(){

		httpGet(URLUtil.getURL_CurrentMatch(mStrServer, mStrSummoner), null, new IListener<String>() {
			@Override
			public void onCall(String s) {
				if(StringUtils.isNullOrZero(s)){
					Toast.makeText(ShakeActivity.this, "当前没有对战", Toast.LENGTH_LONG).show();
					return;
				}
				final String strJson = s;
//				final String strJson = "{\"playerInfo\":{\"Luc1us\\u4e36Vming\":{\"zdl\":5944,\"winRate\":48,\"total\":1297,\"tierDesc\":\"\\u9ec4\\u91d1\\/V\"},\"\\u6a31\\u4e95\\u7fbd\\u4e0a\\u7a7a\":{\"zdl\":7936,\"winRate\":53,\"total\":1540,\"tierDesc\":\"\\u9ec4\\u91d1\\/II\"},\"\\u70bc\\u72f1\\u82f1\\u96c4\\u80dc\":{\"zdl\":2480,\"winRate\":44,\"total\":385,\"tierDesc\":\"\\u65e0\\u8bc4\\u7ea7\"},\"\\u5b83\\u543b\\u5b83\\u7684\\u5507\":{\"zdl\":4634,\"winRate\":51,\"total\":510,\"tierDesc\":\"\\u767d\\u94f6\\/II\"},\"CardiacDeath\":{\"zdl\":8794,\"winRate\":53,\"total\":2684,\"tierDesc\":\"\\u767d\\u91d1\\/V\"},\"\\u53e3\\u5f84\\u597d\\u5927\":{\"zdl\":3976,\"winRate\":49,\"total\":674,\"tierDesc\":\"\\u767d\\u94f6\\/IV\",\"sexInfo\":\"0\"},\"\\u8fd8\\u5728A\\u7b49\\u5f85\":{\"zdl\":6217,\"winRate\":51,\"total\":1662,\"tierDesc\":\"\\u767d\\u94f6\\/II\"},\"\\u522b\\u6233\\u6211\\u706c\":{\"zdl\":7643,\"winRate\":51,\"total\":1662,\"tierDesc\":\"\\u767d\\u91d1\\/V\"},\"\\u522b\\u9017\\u6211\\u706c\":{\"zdl\":5442,\"winRate\":49,\"total\":1000,\"tierDesc\":\"\\u9ec4\\u91d1\\/IV\"},\"\\u54b1\\u771f\\u83dc\":{\"zdl\":4905,\"winRate\":52,\"total\":466,\"tierDesc\":\"\\u767d\\u94f6\\/II\"}},\"gameInfo\":{\"timeStamp\":1433563989,\"gameMode\":\"CLASSIC\",\"gameType\":\"NORMAL_GAME\",\"queueTypeName\":\"NORMAL\",\"100\":{\"Luc1us\\u4e36Vming\":\"Chogath\",\"\\u6a31\\u4e95\\u7fbd\\u4e0a\\u7a7a\":\"Mordekaiser\",\"\\u70bc\\u72f1\\u82f1\\u96c4\\u80dc\":\"Garen\",\"\\u5b83\\u543b\\u5b83\\u7684\\u5507\":\"Sivir\",\"CardiacDeath\":\"Yasuo\"},\"200\":{\"\\u53e3\\u5f84\\u597d\\u5927\":\"Tryndamere\",\"\\u8fd8\\u5728A\\u7b49\\u5f85\":\"Vayne\",\"\\u522b\\u6233\\u6211\\u706c\":\"Ziggs\",\"\\u522b\\u9017\\u6211\\u706c\":\"LeeSin\",\"\\u54b1\\u771f\\u83dc\":\"Katarina\"},\"queueTypeCn\":\"\\u5339\\u914d\\u8d5b\",\"100_sort\":[\"CardiacDeath\",\"\\u6a31\\u4e95\\u7fbd\\u4e0a\\u7a7a\",\"Luc1us\\u4e36Vming\",\"\\u5b83\\u543b\\u5b83\\u7684\\u5507\",\"\\u70bc\\u72f1\\u82f1\\u96c4\\u80dc\"],\"200_sort\":[\"\\u522b\\u6233\\u6211\\u706c\",\"\\u8fd8\\u5728A\\u7b49\\u5f85\",\"\\u522b\\u9017\\u6211\\u706c\",\"\\u54b1\\u771f\\u83dc\",\"\\u53e3\\u5f84\\u597d\\u5927\"],\"sn\":\"\\u7535\\u4fe1\\u5341\\u56db\",\"pn\":\"\\u8fd8\\u5728A\\u7b49\\u5f85\"}}" ;
				Intent it = new Intent(ShakeActivity.this, MatchInfoActivity.class);
				it.putExtra(MatchInfoActivity.EXTRA_MATCHJSON, strJson);
				startActivity(it);

			}
		});

	}

	@Override
	protected boolean goBack() {
		return false;
	}

}
