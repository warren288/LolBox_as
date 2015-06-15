package com.warren.lolbox;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.Header;

import android.content.Intent;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.warren.lolbox.model.IListener;
import com.warren.lolbox.storage.UserManager;
import com.warren.lolbox.url.URLUtil;
import com.warren.lolbox.util.StringUtils;
import com.warren.lolbox.widget.TitleBar;

/**
 * 搜索召唤师详细信息Activity
 * @author warren
 * @date 2014年12月28日
 */
public class SearchSummonerActivity extends BaseActivity {

	public static final String EXTRA_ISFORSETSUMMONER = "EXTRA_ISFORSETSUMMONER";

	String strUrl = "http://zdl.mbox.duowan.com/phone/playerDetailNew.php?"
				+ "sn=%E7%94%B5%E4%BF%A1%E5%8D%81%E5%9B%9B&pn=%E8%BF%98%E5%9C%A8%E7%AD%89%E5%BE%85";

	private static final String[] arrServerNames = { "艾欧尼亚 电信一", "祖安 电信二", "诺克萨斯 电信三", "班德尔城 电信四",
			"皮尔特沃夫 电信五", "战争学院 电信六", "巨神峰 电信七", "雷瑟守备 电信八", "裁决之地 电信九", "黑色玫瑰 电信十", "暗影岛 电信十一",
			"钢铁烈阳 电信十二", "均衡教派 电信十三", "水晶之痕 电信十四", "影流 电信十五", "守望之海 电信十六", "征服之海 电信十七",
			"卡拉曼达 电信十八", "皮城警备 电信十九", "比尔吉沃特 网通一", "德玛西亚 网通二", "弗雷尔卓德 网通三", "无畏先锋 网通四", "恕瑞玛 网通五",
			"扭曲丛林 网通六", "巨龙之巢 网通七", "教育网专区 教育一" };

	private static final String[] arrServers = { "电信一", "电信二", "电信三", "电信四", "电信五", "电信六", "电信七",
			"电信八", "电信九", "电信十", "电信十一", "电信十二", "电信十三", "电信十四", "电信十五", "电信十六", "电信十七", "电信十八",
			"电信十九", "网通一", "网通二", "网通三", "网通四", "网通五", "网通六", "网通七", "教育一" };

	private TitleBar mTb;
	private EditText mEtSummonerName;
	private Spinner mSpServer;
	private Button mBtnSearch;
	private ListView mLvHistory;
	private List<String> mLstSearchHistory;
	private AdapterList mAdapter;

	private int mServerIndex = 0;

	private boolean mIsForSetSummoner = false;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mIsForSetSummoner = getIntent().getBooleanExtra(EXTRA_ISFORSETSUMMONER, false);
		setContentView(R.layout.activity_searchsummoner);
		initCtrl();
	}

	private void initCtrl() {

		mTb = (TitleBar) findViewById(R.id.titlebar);

		mEtSummonerName = (EditText) findViewById(R.id.et_summonername);
		mSpServer = (Spinner) findViewById(R.id.sp_servername);
		mBtnSearch = (Button) findViewById(R.id.btn_search);
		mLvHistory = (ListView) findViewById(R.id.lv_search);
		mAdapter = new AdapterList(getLayoutInflater());
		mLvHistory.setAdapter(mAdapter);

		if (mIsForSetSummoner) {
			mTb.setText("设置默认召唤师");
			mBtnSearch.setText("设置");
		} else {
			mTb.setText("搜索");
			mBtnSearch.setText("搜索");
		}

		showSearchHistory();

		ArrayAdapter<String> adapterSp = new ArrayAdapter<String>(this, R.layout.textview_spinner,
					arrServerNames);
		mSpServer.setAdapter(adapterSp);

		mSpServer.setOnItemSelectedListener(new OnItemSelectedListener() {

			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
				mServerIndex = position;
			}

			@Override
			public void onNothingSelected(AdapterView<?> parent) {
			}
		});

		mBtnSearch.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				if (mEtSummonerName.getText().toString().trim().length() == 0) {
					Toast.makeText(SearchSummonerActivity.this, "请填写召唤师名称", Toast.LENGTH_SHORT)
								.show();
					return;
				}
				if (mIsForSetSummoner) {
					// 设置默认召唤师
					String strUrl = URLUtil.getURL_SummonerInfo(mEtSummonerName.getText()
								.toString().trim(), arrServers[mServerIndex]);
					httpGet(strUrl, SystemConfig.getIntance().getCommHead(),
								new IListener<String>() {

									@Override
									public void onCall(String strJson) {
										if (StringUtils.isNullOrZero(strJson)) {
											Toast.makeText(SearchSummonerActivity.this, "查询召唤师失败",
														Toast.LENGTH_SHORT).show();
											return;
										}
										jsonParseMap(
													strJson,
													new IListener<Map<String, HashMap<String, Object>>>() {

														@Override
														public void onCall(
																	Map<String, HashMap<String, Object>> map) {
															if (map == null
																		|| !map.containsKey(mEtSummonerName
																					.getText()
																					.toString()
																					.trim())) {
																Toast.makeText(
																			SearchSummonerActivity.this,
																			"查询召唤师失败",
																			Toast.LENGTH_SHORT)
																			.show();
																return;
															}

															SystemConfig.getIntance()
																		.setDefaultSummonerName(
																					mEtSummonerName
																								.getText()
																								.toString()
																								.trim());
															SystemConfig.getIntance()
																		.setDefaultSummonerServer(
																					arrServers[mServerIndex]);

															finish();
														}
													});
									}
								});
				} else {
					// 查询召唤师信息
					startSearch(mEtSummonerName.getText().toString().trim(),
								arrServers[mServerIndex]);
				}
			}
		});

		mLvHistory.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				String searchhistory = mLstSearchHistory.get(position);
				String[] arr = searchhistory.split(",");
				if (mIsForSetSummoner) {

				} else {
					startSearch(arr[1], arr[0]);
				}
			}
		});
	}

	/**
	 * 显示查询历史纪录
	 */
	private void showSearchHistory() {
		if (mIsForSetSummoner) {

		} else {
			UserManager um = UserManager.getInstance();
			mLstSearchHistory = um.getSearchHistory();
			updateHistory();
		}
	}

	/**
	 * 添加一条查询历史纪录
	 * @param strServer
	 * @param strSummoner
	 */
	private void addSearchHistory(String strServer, String strSummoner) {
		if (mLstSearchHistory.contains(strServer + "," + strSummoner)) {
			return;
		}
		if (mIsForSetSummoner) {

		} else {
			UserManager um = UserManager.getInstance();
			um.addSearchHistory(strServer, strSummoner);
			mLstSearchHistory.add(strServer + "," + strSummoner);
			updateHistory();
		}
	}

	/**
	 * 更新查询历史记录ListView
	 */
	private void updateHistory() {
		mAdapter.setList(mLstSearchHistory);
		mAdapter.notifyDataSetChanged();
	}

	/**
	 * 开始搜索召唤师详细信息，成功则打开召唤师详细信息界面，否则提示搜索失败
	 * @param strSummonerName
	 * @param strServer
	 */
	private void startSearch(final String strSummonerName, final String strServer) {

		RequestParams params = new RequestParams();
		params.put("sn", strServer);
		params.put("pn", strSummonerName);
		AsyncHttpClient http = new AsyncHttpClient();

		http.get(strUrl, params, new AsyncHttpResponseHandler() {

			@Override
			public void onStart() {
				super.onStart();
			}

			@Override
			public void onSuccess(int arg0, Header[] arg1, byte[] arg2) {
				super.onSuccess(arg0, arg1, arg2);
				String strContent = new String(arg2, Charset.forName("utf-8"));
				addSearchHistory(strServer, strSummonerName);
				openSumonerDetail(strContent);
			}

			@Override
			public void onFailure(int arg0, Header[] arg1, byte[] arg2, Throwable arg3) {
				super.onFailure(arg0, arg1, arg2, arg3);
				Toast.makeText(SearchSummonerActivity.this, "查找失败，请检查召唤师名称和服务器是否正确",
							Toast.LENGTH_SHORT).show();
			}
		});
	}

	/**
	 * 打开召唤师详情界面
	 * @param strContent 召唤师详情html字符串
	 */
	private void openSumonerDetail(String strContent) {

		Intent it = new Intent(this, SumonerDetailActivity.class);
		it.putExtra(SumonerDetailActivity.EXTRA_CONTENTDATA, strContent);
		startActivity(it);
		overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right);
	}

	@Override
	protected boolean goBack() {
		return false;
	}

	class AdapterList extends BaseAdapter {

		private LayoutInflater mInflater;
		private List<String> mList = new ArrayList<String>();

		public AdapterList(LayoutInflater inflater) {
			this.mInflater = inflater;
		}

		public void setList(List<String> list) {
			this.mList = list;
		}

		@Override
		public int getCount() {
			return mList.size();
		}

		@Override
		public Object getItem(int position) {
			return mList.get(position);
		}

		@Override
		public long getItemId(int position) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

			ViewHolder holder;
			if (convertView == null) {
				convertView = mInflater.inflate(R.layout.activity_baike_listitem, parent, false);
				holder = new ViewHolder();
				holder.img = (ImageView) convertView.findViewById(R.id.img);
				holder.tv = (TextView) convertView.findViewById(R.id.tv);
				holder.tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
				holder.tv.setTextColor(mInflater.getContext().getResources().getColor(android.R.color.darker_gray));
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			holder.img.setVisibility(View.GONE);
			holder.tv.setText(mList.get(position));
			return convertView;
		}

		class ViewHolder {
			public ImageView img;
			public TextView tv;
		}

	}

}
