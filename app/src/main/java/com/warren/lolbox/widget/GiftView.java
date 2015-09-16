package com.warren.lolbox.widget;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.warren.lolbox.AppContext;
import com.warren.lolbox.R;
import com.warren.lolbox.model.bean.GiftDot;
import com.warren.lolbox.url.URLUtil;
import com.warren.lolbox.util.DeviceUtil;
import com.warren.lolbox.util.LogTool;
import com.warren.lolbox.util.StringUtils;

import java.util.List;

/**
 * 天赋图视图
 * @author:yangsheng
 * @date:2015/8/23
 */
public class GiftView extends GridView implements AdapterView.OnItemClickListener{

    private static final int NUM_COLUMN = 4;
    private static final int NUM_ROW = 6;

    private AdapterGrid mAdapter;
    private List<GiftDot> mLstGd;
    private GiftDot[][] mArrDots;
    private int[][] mArrState;

    private ImageLoader mImgLoader;

    public GiftView(Context context) {
        super(context);
        init();
    }

    public GiftView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GiftView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init(){
        mArrDots = new GiftDot[NUM_ROW][NUM_COLUMN];
        mArrState = new int[NUM_ROW][NUM_COLUMN];

        setNumColumns(NUM_COLUMN);
        mImgLoader = AppContext.getApp().getImgLoader();
        mAdapter = new AdapterGrid(getContext());
        setAdapter(mAdapter);

        setOnItemClickListener(this);
    }

    public boolean setData(List<GiftDot> lstGd){
        if(lstGd == null || lstGd.size() == 0){
            return false;
        }
        this.mLstGd = lstGd;
        for(GiftDot gd : lstGd){
            int row = Integer.valueOf(gd.getId().substring(1, 2)) - 1;
            int column = Integer.valueOf(gd.getId().substring(2)) - 1;
            mArrDots[row][column] = gd;
        }
        mAdapter.notifyDataSetChanged();
        return true;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        showGiftDetail(position);
    }

    private void showGiftDetail(final int position){

        final int row = position / NUM_COLUMN;
        final int column = position % NUM_COLUMN;
        final GiftDot gd = mArrDots[row][column];
        if(gd == null){
            return;
        }
        MessageDialog md = new MessageDialog(getContext()).setMessage(StringUtils.createStringFromStrList(gd.getLevel(), "\n"))
                .setTitle(gd.getName()).setCancelableOutside(true).setCancelableOnButton(false);

        if(isNeedEnabled(position)){
            md.setNegativeButton("-", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    //需要保证操作不会导致下一行的天赋激活条件失效
                    if(mArrState[row][column] > 0 && canMinus(position)){
                        mArrState[row][column] --;
                        mAdapter.notifyDataSetChanged();
                    }
                }
            }).setPositiveButton("+", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    if (mArrState[row][column] < gd.getLevel().size()) {
                        mArrState[row][column] ++;
                        mAdapter.notifyDataSetChanged();
                    }
                }
            });
        }
        md.show();

    }

    /**
     * 取某个位置的天赋对象
     * @param position
     * @return
     */
    private GiftDot getGiftDot(int position){
        if(mArrDots == null){
            return null;
        }
        int row = position / NUM_COLUMN;
        int column = position % NUM_COLUMN;
        GiftDot gd = mArrDots[row][column];
        return gd;
    }

    /**
     * 某个位置的天赋是否可以减少点数
     * @description 判断依据如下几点：
     * 1，本行下方如果没有天赋加点，则可以减少；
     * 2，本行下方有天赋加点，如果下方所需上方点数不高于已有上方已有点数，则不可减少；
     * 3，本行下方有天赋加点而且下方所需上方点数高于已有上方点数，但下方有依赖于本位置天赋的天赋已被点开，
     *      则本天赋是否可以减少点数取决于该依赖是否能被满足
     * @param position
     * @return
     */
    private boolean canMinus(int position){
        int row = position / NUM_COLUMN;
        int column = position % NUM_COLUMN;
        boolean hasNext = false;
        for(int i=NUM_ROW - 1; i > row; i--){
            for(int j = 0; j < NUM_COLUMN; j++){
                if(mArrState[i][j] > 0){
                    hasNext = true;
                    break;
                }
            }
        }

        if(! hasNext){
            return true;
        }

        if(getUpExist(row + 1) <= (row + 1) * NUM_COLUMN){
            return false;
        }
        if(mArrState[row + 1][column] > 0 && mArrDots[row + 1][column] != null &&
                ! StringUtils.isNullOrZero(mArrDots[row + 1][column].getNeed())){
            String strNeed = mArrDots[row + 1][column].getNeed();
            int rowNeed = Integer.valueOf(strNeed.substring(1, 2)) - 1;
            int colNeed = Integer.valueOf(strNeed.substring(2, 3)) -1;
            int stateNeed = Integer.valueOf(strNeed.substring(4));
            if(rowNeed == row && colNeed == column){
                return stateNeed < mArrState[row][column];
            }
        }
        return true;
    }

    private int getUpExist(int row){
        int numUpExist = 0;
        for(int i = 0; i < row; i ++){
            for (int j = 0; j < NUM_COLUMN; j++){
                numUpExist += mArrState[i][j];
            }
        }
        return numUpExist;
    }

    /**
     * 某个位置的天赋是否可激活
     * @param position
     * @return
     */
    private boolean isNeedEnabled(int position){
        int row = position / NUM_COLUMN;
        int column = position % NUM_COLUMN;
        GiftDot gd = mArrDots[row][column];
        String strNeed = gd.getNeed();
        int numUpExist = 0;
        int numUp = row * NUM_COLUMN;
        if(numUp <= 0){
            return true;
        }
        boolean enabled = false;
        for(int i = 0; i < row; i ++){
            for (int j = 0; j < NUM_COLUMN; j++){
                numUpExist += mArrState[i][j];
            }
        }
        if (numUp > numUpExist){
            return false;
        }
        if(strNeed == null) {
            return true;
        }
        int rowNeed = Integer.valueOf(strNeed.substring(1, 2)) - 1;
        int colNeed = Integer.valueOf(strNeed.substring(2, 3)) -1;
        int stateNeed = Integer.valueOf(strNeed.substring(4));

        return mArrState[rowNeed][colNeed] >= stateNeed;
    }

    class AdapterGrid extends BaseAdapter{

        private Context context;
        public AdapterGrid(Context context){
            this.context = context;
        }

        @Override
        public int getCount() {
            return NUM_COLUMN * NUM_ROW;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {

            if(convertView == null){
                convertView = LayoutInflater.from(this.context).inflate(R.layout.gift_img_item, parent, false);
            }
            AbsListView.MarginLayoutParams param = new AbsListView.MarginLayoutParams(
                    (parent.getWidth()) / NUM_COLUMN, (parent.getWidth()) / NUM_COLUMN);
            convertView.setPadding(DeviceUtil.dp2Px(getContext(), 10), DeviceUtil.dp2Px(getContext(), 10),
                    DeviceUtil.dp2Px(getContext(), 10), DeviceUtil.dp2Px(getContext(), 10));
            convertView.setLayoutParams(param);

            StatefulImageView simg = (StatefulImageView) convertView.findViewById(R.id.simg);
            TextView tv = (TextView) convertView.findViewById(R.id.tv);
            simg.setStateEnable(true);
            int row = position / NUM_COLUMN;
            int column = position % NUM_COLUMN;
            GiftDot gd = mArrDots[row][column];

            if(gd == null){
                convertView.setVisibility(View.INVISIBLE);
                return convertView;
            }

            if(position == 0){
                LogTool.i("GiftView", gd.getId() + "," + gd.getName());
            }
            convertView.setVisibility(View.VISIBLE);
            mImgLoader.displayImage(URLUtil.getUrl_GiftImg(gd.getId()), simg);
            if(mArrState[row][column] > 0 || isNeedEnabled(position)){
                simg.setShowState(false);
                tv.setText(mArrState[row][column] + "/" + gd.getLevel().size());
            } else {
                simg.setShowState(true);
            }

            return convertView;
        }
    }
}
