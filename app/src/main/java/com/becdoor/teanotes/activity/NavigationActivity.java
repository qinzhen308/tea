package com.becdoor.teanotes.activity;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.becdoor.teanotes.R;
import com.becdoor.teanotes.adapter.NavigationAdapter;
import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.global.CustomToast;
import com.becdoor.teanotes.model.CateInfo;
import com.becdoor.teanotes.until.DialogUtil;
import com.becdoor.teanotes.until.NetUtil;
import com.becdoor.teanotes.view.ReListView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.Call;

/**
 * Created by jjj on 2016/11/10.
 * 栏目管理
 */

public class NavigationActivity extends TitleActivity {
    @Bind(R.id.navigation_listView)
    ReListView mListView;
    @Bind(R.id.navigation_addLayout)
    RelativeLayout mAddLayout;

    private NavigationAdapter mAdapter;
    private List<CateInfo> mList;

    private Dialog loadDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContextView(R.layout.activity_navigation);
        setTitleForRightIamgeView("栏目管理", 0, true, false);
        ButterKnife.bind(this);
        loadDialog = DialogUtil.getLoadingDialog(this);
        initData();
        mAddLayout.requestLayout();
        mAddLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (TextUtils.isEmpty(mList.get(mList.size() - 1).getCat_id())) {
                    CustomToast.showToast(NavigationActivity.this, "每次最多可以创建一个栏目！", 1000);
                    return;
                }
                CateInfo cateInfo = new CateInfo();
                cateInfo.setCat_name("");
                cateInfo.setCat_type("-1");
                mList.add(cateInfo);
                mAdapter.notifyDataSetChanged();
            }
        });
    }

    void initData() {
        mList = new ArrayList<>();
        List<CateInfo> cateInfos = (List<CateInfo>) getIntent().getExtras().getSerializable("cateinfos");
        if (cateInfos != null) {
            mList.addAll(cateInfos);
        }
        mAdapter = new NavigationAdapter(this, mList);
        mListView.setAdapter(mAdapter);
        if (cateInfos == null || cateInfos.isEmpty()) {
            loadNav();
        }
        mAdapter.setNavigationListener(new NavigationAdapter.NavigationListener() {
            @Override
            public void deteleNavigation(int position) {
                if (TextUtils.isEmpty(mList.get(mList.size() - 1).getCat_id())) {
                    mList.remove(mList.size() - 1);
                    mAdapter.notifyDataSetChanged();
                } else {
                    deNavigation(position);
                }
            }

            @Override
            public void updateOrAddNavigation(int position, String name) {
                CateInfo info = mList.get(position);
                if ("-1".equals(info.getCat_type())) {
                    //保存
                    if (TextUtils.isEmpty(name)) {
                        CustomToast.showToast(NavigationActivity.this, "请输入栏目名称", 1000);
                        return;
                    }
                    if (TextUtils.isEmpty(info.getCat_id())) {
                        //创建
                        addNavigation(name, position);
                    } else {
                        //更新
                        updateNavigation(info.getCat_id(), name, position);
                    }

                } else {
                    //编辑
                    mList.get(position).setCat_type("-1");
                    mAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * 删除栏目
     *
     * @param po
     */
    void deNavigation(final int po) {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("cat_id", mList.get(po).getCat_id());
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "doDelNav", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    DialogUtil.dismissDialog(mLoaDailog);
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Toast.makeText(NavigationActivity.this, "删除成功!", Toast.LENGTH_SHORT).show();
                        mList.remove(po);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(NavigationActivity.this, object == null ? "删除失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(NavigationActivity.this, "删除失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * 添加栏目
     *
     * @param cat_name
     */
    void addNavigation(final String cat_name, final int position) {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("cat_name", cat_name);
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "doAddNav", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    DialogUtil.dismissDialog(mLoaDailog);
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Toast.makeText(NavigationActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
                        getDataForOP();
                    } else {
                        Toast.makeText(NavigationActivity.this, object == null ? "添加失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(NavigationActivity.this, "添加失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    void getDataForOP() {
        DialogUtil.showDialog(mLoaDailog);
        Map<String, String> map = new HashMap<>();
        map.put("op", "nav");
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "spa_manage", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    DialogUtil.dismissDialog(mLoaDailog);
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        List<CateInfo> cateInfos = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<CateInfo>>() {
                        }.getType());
                        if (cateInfos != null) {
                            mList.clear();
                            mList.addAll(cateInfos);
                            mAdapter.notifyDataSetChanged();
                        }

                    } else {
                        Toast.makeText(NavigationActivity.this, object == null ? "获取失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(NavigationActivity.this, "获取失败!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    /**
     * 编辑栏目
     *
     * @param cat_id
     * @param cat_name
     */
    void updateNavigation(String cat_id, final String cat_name, final int position) {
        DialogUtil.showDialog(mLoaDailog);
        final Map<String, String> map = new HashMap<>();
        map.put("cat_name", cat_name);
        map.put("cat_id", cat_id);
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "doEditNav", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                DialogUtil.dismissDialog(mLoaDailog);
            }

            @Override
            public void onScuccess(String response, int id) {
                try {
                    DialogUtil.dismissDialog(mLoaDailog);
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        Toast.makeText(NavigationActivity.this, "添加成功!", Toast.LENGTH_SHORT).show();
                        mList.get(position).setCat_name(cat_name);
                        mList.get(position).setCat_type("3");
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(NavigationActivity.this, object == null ? "添加失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(NavigationActivity.this, "添加失败!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void loadNav() {
        DialogUtil.showDialog(loadDialog);
        Map<String, String> map = new HashMap<>();
        map.put("op", "nav");
        NetUtil.postData(this, Constant.BASE_URL + "center.php?", map, "spa_manage", new NetUtil.NetUtilCallBack() {
            @Override
            public void onFail(Call call, Exception e, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
            }

            @Override
            public void onScuccess(String response, int id) {
                if (!isFinishing()) DialogUtil.dismissDialog(loadDialog);
                try {
                    JSONObject object = new JSONObject(response);
                    if (object != null && 200 == object.optInt("status")) {
                        List<CateInfo> cateInfos = new Gson().fromJson(object.getJSONArray("data").toString(), new TypeToken<List<CateInfo>>() {
                        }.getType());
                        mList.addAll(cateInfos);
                        mAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(NavigationActivity.this, object == null ? "获取失败!" : object.optString("message"), Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    Toast.makeText(NavigationActivity.this, "获取失败!", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    public static void inVoke(Context context, List<CateInfo> list) {
        Intent intent = new Intent(context, NavigationActivity.class);
        Bundle bundle = new Bundle();
        if (list != null) {
            bundle.putSerializable("cateinfos", (Serializable) list);
        }
        intent.putExtras(bundle);
        context.startActivity(intent);
    }
}
