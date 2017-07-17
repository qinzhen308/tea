package com.becdoor.teanotes.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.NoteCommentForRecyclerViewAdapter;
import com.becdoor.teanotes.global.AppStatic;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.global.CustomToast;
import com.becdoor.teanotes.listener.DetailListener;
import com.becdoor.teanotes.listener.INetCallBackListener;
import com.becdoor.teanotes.listener.NoteCommentListener;
import com.becdoor.teanotes.model.NoteCommentBean;
import com.becdoor.teanotes.model.NoteDetailBean;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.CommentDialogView;
import com.becdoor.teanotes.view.RBCallbkRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * Created by Administrator on 2016/11/4.
 * 评论
 */

public class NoteCommentActivity extends BaseRecyclerViewActivity {
    EditText mCommentEdt;
    TextView mNumTv;
    private List<NoteCommentBean> mList;
    private NoteCommentForRecyclerViewAdapter mAdapter;
    private String aid;//笔记的id
    private String pro_id;//产品的id
    private String act_id;//活动的id
    private String url = "note_manage.php?";//笔记的评论
    private String act = "moreComm";//笔记的评论

    private Map<String, String> map;

    //提交评论
    private String submitComment_url = "note_manage.php?";//提交评论默认笔记
    private String submitComment_act = "addComm";//提交评论默认笔记
    private Map<String, String> submitMap;

    private CommentDialogView commentDialogView;
    private Dialog mComDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_notecomment);
        setTitleForRightIamgeView("评论", 0, true, false);

        initView();
        initData();
        DialogUtil.showDialog(mLoaDailog);
        getData1();
        initCommDialog();

    }


    void initCommDialog() {
        commentDialogView = new CommentDialogView(this);
        mComDialog = DialogUtil.getMenuDialog(this, commentDialogView);
        commentDialogView.setCommentListener(new CommentDialogView.CommentListener() {
            @Override
            public void onComment(String content, NoteCommentBean noteCommentBean) {
                if (mComDialog != null && mComDialog.isShowing()) {
                    mComDialog.dismiss();
                }
                submitComment(content, noteCommentBean);
            }
        });

    }

    void initData() {
        String title = getIntent().getStringExtra("title");
        if (getIntent().hasExtra("aid")) {
            aid = getIntent().getStringExtra("aid");
        }
        if (getIntent().hasExtra("pro_id")) {
            pro_id = getIntent().getStringExtra("pro_id");
        }
        if (getIntent().hasExtra("act_id")) {
            act_id = getIntent().getStringExtra("act_id");
        }
        mList = new ArrayList<>();
        mAdapter = new NoteCommentForRecyclerViewAdapter(this, new NoteCommentListener() {
            @Override
            public void commSingleNote(NoteCommentBean noteComment) {
                if (mComDialog != null && !mComDialog.isShowing()) {
                    commentDialogView.updateEdtHintContent(noteComment);
                    mComDialog.show();
                }
            }
        });
        mAdapter.setmList(mList);
        mRecyclerView.setAdapter(mAdapter);

        map = new HashMap<>();
        submitMap = new HashMap<>();

        if (!TextUtils.isEmpty(aid)) {//笔记
            map.put("aid", aid);
            submitMap.put("title", title);
            submitMap.put("aid", aid);
        }
        if (!TextUtils.isEmpty(pro_id)) {//产品
            map.put("pro_id", pro_id);
            act = "morepComm";
            url = "wbo.php?";
            submitMap.put("pro_id", pro_id);
            submitComment_act = "addpComm";
            submitComment_url = "wbo.php?";
        }
        if (!TextUtils.isEmpty(act_id)) {//活动
            map.put("act_id", act_id);
            act = "moreComm";
            url = "activity.php?";

            submitMap.put("act_id", act_id);
            submitMap.put("act_title", title);
            submitComment_act = "addComm";
            submitComment_url = "activity.php?";
        }
    }

    void getData1() {
        setPostFormBuilder(Constant.BASE_URL + url, map, act, new INetCallBackListener() {
            @Override
            public void onFail() {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response) {
                try {
                    DialogUtil.dismissDialog(mLoaDailog);
                    JSONObject object = new JSONObject(response);
                    if (object != null) {
                        JSONObject object1 = object.getJSONObject("data");
                        List<NoteCommentBean> list = null;

                        if (!object1.isNull("act_comm_list")) {
                            list = new Gson().fromJson(object1.getJSONArray("act_comm_list").toString(), new TypeToken<List<NoteCommentBean>>() {
                            }.getType());
                        } else if (!object1.isNull("comm_list")) {
                            list = new Gson().fromJson(object1.getJSONArray("comm_list").toString(), new TypeToken<List<NoteCommentBean>>() {
                            }.getType());
                        }
                        totalPage = object1.optInt("max_page");
                        if (list != null) {
                            mList.addAll(list);
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    /**
     * 提交评论
     *
     * @param content
     */
    void submitComment(String content, NoteCommentBean noteCommentBean) {
        if (TextUtils.isEmpty(content)) {
            CustomToast.showToast(this, "请输入评论内容!", 1500);
            return;
        }
        submitMap.put("content", content);
        if (noteCommentBean != null && !TextUtils.isEmpty(noteCommentBean.getUid()))
            submitMap.put("comm_id", noteCommentBean.getUid());
        AppStatic.submitComment(this, Constant.BASE_URL + submitComment_url, submitMap, submitComment_act, new AppStatic.CommentListener() {
            @Override
            public void commentScuccess() {
                mCommentEdt.setText("");
                refreshData();
            }

            @Override
            public void commentFail() {

            }
        });

    }

    void initView() {
        mRecyclerView = (RBCallbkRecyclerView) findViewById(R.id.recycleview_view);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swip_refresh_widget);
        mCommentEdt = (EditText) findViewById(R.id.noteComment_commentEdt);
        mNumTv = (TextView) findViewById(R.id.noteComment_numTv);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        initListener();
        mCommentEdt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                int num = 0;
                if (!TextUtils.isEmpty(s)) {
                    num = s.toString().length();
                }
                mNumTv.setText(num + "/150");
            }
        });
        mNumTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitComment(mCommentEdt.getText().toString(), null);
            }
        });
        mCommentEdt.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    submitComment(mCommentEdt.getText().toString(), null);
                    return true;
                }

                return false;
            }
        });

    }


    @Override
    void loadNextPage() {
        getData1();
    }

    @Override
    void refreshData() {
        mList.clear();
        curPage = 1;
        getData1();
    }

    /**
     * 笔记评论
     *
     * @param context
     * @param aid
     * @param title
     */
    public static void inVokeForNote(Context context, String aid, String title) {
        Intent intent = new Intent(context, NoteCommentActivity.class);
        intent.putExtra("aid", aid);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    /**
     * 产品的评论
     *
     * @param context
     * @param pro_id
     * @param title
     */
    public static void inVokeForPro(Context context, String pro_id, String title) {
        Intent intent = new Intent(context, NoteCommentActivity.class);
        intent.putExtra("pro_id", pro_id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }

    /**
     * 活动的评论
     *
     * @param context
     * @param act_id
     * @param title
     */
    public static void inVokeForActivity(Context context, String act_id, String title) {
        Intent intent = new Intent(context, NoteCommentActivity.class);
        intent.putExtra("act_id", act_id);
        intent.putExtra("title", title);
        context.startActivity(intent);
    }
}
