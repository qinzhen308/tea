package com.becdoor.teanotes.view.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
//import com.baidu.mapapi.search.core.PoiInfo;
//import com.baidu.mapapi.search.core.SearchResult.ERRORNO;
//import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
//import com.baidu.mapapi.search.poi.PoiCitySearchOption;
//import com.baidu.mapapi.search.poi.PoiDetailResult;
//import com.baidu.mapapi.search.poi.PoiResult;
//import com.baidu.mapapi.search.poi.PoiSearch;
//import com.becdoor.teanotes.adapter.AddressAdapter;
//import com.becdoor.teanotes.util.DialogUtils;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;


//public class AddressDialog extends Dialog implements AdapterView.OnItemClickListener {
//    private String Location_Address = "";
//    private String Location_City = "";
//    private AddressAdapter adapter = null;
//    private String address;
//    private List<String> address_list = null;
//    ListView address_lv;
//    private Context context;
//    private ContainsEmojiEditText edtitext;
//    private Dialog mDialog;
//    private PoiSearch mPoiSearch = null;
//    OnGetPoiSearchResultListener poiListener = new OnGetPoiSearchResultListener() {
//        public void onGetPoiDetailResult(PoiDetailResult paramAnonymousPoiDetailResult) {
//            System.out.println("result:" + paramAnonymousPoiDetailResult);
//            if (paramAnonymousPoiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
//                paramAnonymousPoiDetailResult = Toast.makeText(AddressDialog.this.context, "抱歉，未找到结果", 0);
//                paramAnonymousPoiDetailResult.setGravity(17, 0, 0);
//                paramAnonymousPoiDetailResult.show();
//                return;
//            }
//            System.out.println("PoiDetailResult>>>>>>>" + paramAnonymousPoiDetailResult);
//            paramAnonymousPoiDetailResult = Toast.makeText(AddressDialog.this.context, paramAnonymousPoiDetailResult.getName() + ": " + paramAnonymousPoiDetailResult.getAddress(), 0);
//            paramAnonymousPoiDetailResult.setGravity(17, 0, 0);
//            paramAnonymousPoiDetailResult.show();
//        }
//
//        public void onGetPoiResult(PoiResult paramAnonymousPoiResult) {
//            AddressDialog.this.address_list = new ArrayList();
//            if ((paramAnonymousPoiResult == null) || (paramAnonymousPoiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND)) {
//                paramAnonymousPoiResult = Toast.makeText(AddressDialog.this.context, "未找到结果", 1);
//                paramAnonymousPoiResult.setGravity(17, 0, 0);
//                paramAnonymousPoiResult.show();
//                return;
//            }
//            int i;
//            if (paramAnonymousPoiResult.error == SearchResult.ERRORNO.NO_ERROR) {
//                AddressDialog.this.address_list.add("不显示位置");
//                AddressDialog.this.address_list.add(AddressDialog.this.Location_City);
//                i = 0;
//            }
//            while (true) {
//                if (i >= paramAnonymousPoiResult.getAllPoi().size()) {
//                    AddressDialog.this.adapter = new AddressAdapter(AddressDialog.this.address_list, AddressDialog.this.address, AddressDialog.this.context);
//                    AddressDialog.this.address_lv.setAdapter(AddressDialog.this.adapter);
//                    DialogUtils.dismissLoadingDialog(AddressDialog.this.mDialog);
//                    return;
//                }
//                AddressDialog.this.address_list.add(((PoiInfo) paramAnonymousPoiResult.getAllPoi().get(i)).name);
//                i += 1;
//            }
//        }
//    };
//    private Button top_back_btn;
//    private TextView tv;
//
//    public AddressDialog(Context paramContext, String paramString1, String paramString2, String paramString3, TextView paramTextView) {
//        super(paramContext);
//        this.context = paramContext;
//        this.tv = paramTextView;
//        this.Location_City = paramString1;
//        this.Location_Address = paramString2;
//        this.address = paramString3;
//    }
//
//    protected void onCreate(Bundle paramBundle) {
//        super.onCreate(paramBundle);
//        setContentView(2130903044);
//        this.address_lv = ((ListView) findViewById(2131361844));
//        this.address_lv.setOnItemClickListener(this);
//        this.edtitext = ((ContainsEmojiEditText) findViewById(2131361843));
//        this.top_back_btn = ((Button) findViewById(2131361808));
//        this.top_back_btn.setOnTouchListener(new View.OnTouchListener() {
//            public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
//                AddressDialog.this.dismiss();
//                return false;
//            }
//        });
//        this.edtitext.addTextChangedListener(new TextWatcher() {
//            public void afterTextChanged(Editable paramAnonymousEditable) {
//                AddressDialog.this.mDialog = DialogUtils.showLoadingDialog(AddressDialog.this.context);
//                AddressDialog.this.mPoiSearch = PoiSearch.newInstance();
//                AddressDialog.this.mPoiSearch.setOnGetPoiSearchResultListener(AddressDialog.this.poiListener);
//                AddressDialog.this.mPoiSearch.searchInCity(new PoiCitySearchOption().city(AddressDialog.this.Location_City).keyword(paramAnonymousEditable.toString()));
//            }
//
//            public void beforeTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
//            }
//
//            public void onTextChanged(CharSequence paramAnonymousCharSequence, int paramAnonymousInt1, int paramAnonymousInt2, int paramAnonymousInt3) {
//            }
//        });
//        this.mDialog = DialogUtils.showLoadingDialog(this.context);
//        this.mPoiSearch = PoiSearch.newInstance();
//        this.mPoiSearch.setOnGetPoiSearchResultListener(this.poiListener);
//        this.mPoiSearch.searchInCity(new PoiCitySearchOption().city(this.Location_City).keyword(this.Location_Address));
//    }
//
//    public void onItemClick(AdapterView<?> paramAdapterView, View paramView, int paramInt, long paramLong) {
//        if (paramInt == 0) {
//            this.tv.setText("");
//            this.tv.setVisibility(8);
//        }
//        while (true) {
//            dismiss();
//            return;
//            this.tv.setText((CharSequence) this.address_list.get(paramInt));
//        }
//    }
//}