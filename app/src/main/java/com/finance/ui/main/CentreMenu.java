package com.finance.ui.main;

import android.os.CountDownTimer;
import android.view.View;
import android.widget.ImageView;

import com.finance.R;
import com.finance.base.BaseViewHandle;
import com.finance.common.Constants;
import com.finance.event.EventBus;
import com.finance.event.UserLoginEvent;
import com.finance.interfaces.ICallback;
import com.finance.model.ben.NoteMessage;
import com.finance.model.ben.NotesMessage;
import com.finance.utils.BtnClickUtil;
import com.finance.widget.roundview.RoundLinearLayout;
import com.finance.widget.roundview.RoundTextView;

import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 中间菜单栏
 */
public class CentreMenu extends BaseViewHandle implements ICallback<NotesMessage> {

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
    private MainContract.Presenter presenter;
    private ArrayList<NoteMessage> noteMessages;

    public CentreMenu(ICentreMenu centreMenu, MainContract.Presenter presenter) {
        mCentreMenu = centreMenu;
        this.presenter = presenter;
        EventBus.register(this);
    }

    @Override
    public CentreMenu onInit(View rootView) {
        super.onInit(rootView);
//        R.layout.home_layout_centre_menu
        return this;
    }

    @Override
    public void onCallback(int code, NotesMessage notesMessage, String message) {
        ArrayList<NoteMessage> noteMessages = notesMessage == null ? null : notesMessage.getTrends();
        if (noteMessages == null || noteMessages.isEmpty()) return;
        this.noteMessages = noteMessages;
        rtvTip.setText("开启倒计时");
        if (noteMessages.size() > 1)
            startCountDown((noteMessages.size() - 1) * 5000);
    }

    private CountDownTimer timer;//倒计时

    public void stopCountDown() {
        if (timer == null) return;
        timer.cancel();
        timer = null;
    }

    public void startCountDown(long l) {
        stopCountDown();
        timer = new CountDownTimer(l, 5000) {
            @Override
            public void onTick(long millisUntilFinished) {
                rtvTip.setText("开启倒计时：" + millisUntilFinished);
            }

            @Override
            public void onFinish() {
                presenter.notesMessage(CentreMenu.this);
            }
        };
        timer.start();
    }

    @OnClick({R.id.ivZST, R.id.ivZXT, R.id.ivKXT, R.id.ivOther})
    public void onViewClicked(View view) {
        if (BtnClickUtil.isFastDoubleClick(view.getId())) {
            //防止双击
            return;
        }
        switch (view.getId()) {
            case R.id.ivZST:
                if (mCentreMenu != null)
                    mCentreMenu.checkedChart(Constants.CHART_LINEFILL);
                break;
            case R.id.ivZXT:
                if (mCentreMenu != null)
                    mCentreMenu.checkedChart(Constants.CHART_LINE);
                break;
//            case R.id.ivKXT:
//                if (mCentreMenu != null)
//                    mCentreMenu.checkedChart(Constants.CHART_CANDLE);
//                break;
//            case R.id.ivOther:
//                if (mCentreMenu != null)
//                    mCentreMenu.checkedChart(Constants.CHART_STICK);
//                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopCountDown();
        EventBus.unregister(this);
    }

    @Subscribe
    public void onEvent(UserLoginEvent event){
        presenter.notesMessage(this);
    }

}
