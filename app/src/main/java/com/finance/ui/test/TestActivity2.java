package com.finance.ui.test;

import android.widget.TextView;

import com.finance.R;
import com.finance.base.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

/**
 *
 */
public class TestActivity2 extends BaseActivity {

    @BindView(R.id.tvTest)
    TextView tvTest;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_test;
    }

    @Override
    protected void onCreated() {

    }

    @OnClick(R.id.tvTest)
    public void onClick() {
//        beginConnect();
    }

    private final static String TAG = "KING";
    private final static String HUB_URL = "https://i.api789.top:8080/";

//    /**
//     * hub链接
//     */
//    private HubConnection conn = new HubConnection(HUB_URL, this, new LongPollingTransport()) {
//        @Override
//        public void OnError(Exception exception) {
//            Log.d(TAG, "OnError=" + exception.getMessage());
//        }
//
//        @Override
//        public void OnMessage(String message) {
//            Log.d(TAG, "message=" + message);
//        }
//
//        @Override
//        public void OnStateChanged(StateBase oldState, StateBase newState) {
//            Log.d(TAG, "OnStateChanged=" + oldState.getState() + " -> " + newState.getState());
//        }
//    };
//
//    private IHubProxy hub = null;
//
//    /**
//     * 开启推送服务 panderman 2013-10-25
//     */
//    private void beginConnect() {
//        try {
//            //服务器端的HUB为ChatHub
//            hub = conn.CreateHubProxy("ChatHub");
//        } catch (OperationApplicationException e) {
//            e.printStackTrace();
//        }
//        hub.On("addNewMessageToPage", new HubOnDataCallback() {
//            @Override
//            public void OnReceived(JSONArray args) {
////                EditText chatText = (EditText) findViewById(R.id.chat_text);
////                //chatText.setText(args.toString());
////                for (int i = 0; i < args.length(); i++) {
////                    chatText.append(args.opt(i).toString());
////                }
//                Log.d("123", "OnReceived: " + args);
//            }
//        });
//        conn.Start();
//    }

}
