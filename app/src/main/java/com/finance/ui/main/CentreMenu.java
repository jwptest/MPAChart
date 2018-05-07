package com.finance.ui.main;

import android.view.View;
import android.widget.ImageView;

import com.finance.R;
import com.finance.base.BaseViewHandle;
import com.finance.widget.roundview.RoundLinearLayout;
import com.finance.widget.roundview.RoundTextView;

import butterknife.BindView;

/**
 * 中间菜单栏
 */
public class CentreMenu extends BaseViewHandle {

    @BindView(R.id.ivZST)
    ImageView ivZST;
    @BindView(R.id.ivZXT)
    ImageView ivZXT;
    @BindView(R.id.ivKXT)
    ImageView ivKXT;
    @BindView(R.id.ivOther)
    ImageView ivOther;
    @BindView(R.id.rllUZST)
    RoundLinearLayout rllUZST;
    @BindView(R.id.rtvTip)
    RoundTextView rtvTip;
    private ICentreMenu mCentreMenu;

    public CentreMenu(ICentreMenu centreMenu) {
        mCentreMenu = centreMenu;
    }

    @Override
    public CentreMenu onInit(View rootView) {
        super.onInit(rootView);
//        R.layout.home_layout_centre_menu
        return this;
    }


//    @OnClick({R.id.ivZST, R.id.ivZXT, R.id.ivKXT})
//    public void onViewClicked(View view) {
//        if (BtnClickUtil.isFastDoubleClick(view.getId())) {
//            //防止双击
//            return;
//        }
//        switch (view.getId()) {
//            case R.id.ivZST:
//                if (mCentreMenu != null)
//                    mCentreMenu.checkedChart(Constants.CHART_LINEFILL);
//                break;
//            case R.id.ivZXT:
//                if (mCentreMenu != null)
//                    mCentreMenu.checkedChart(Constants.CHART_LINE);
//                break;
//            case R.id.ivKXT:
//                if (mCentreMenu != null)
//                    mCentreMenu.checkedChart(Constants.CHART_CANDLE);
//                break;
//        }
//    }


}
