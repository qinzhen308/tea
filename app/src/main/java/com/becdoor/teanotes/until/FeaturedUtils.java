package com.becdoor.teanotes.until;


import com.becdoor.teanotes.model.FeauturedBean;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by bobozai09 如果代码写的够快  我就能忘却错误的提示 on 2016/10/21.
 *
 * 将获取到的介绍哦你转换为 对象
 */

public class FeaturedUtils {
    public static List<FeauturedBean> feauturedDataBeans(String res,
                                                         String value){
        List<FeauturedBean>beans=new ArrayList<FeauturedBean>();
        try {
JsonParser parser=new JsonParser();
            JsonObject jsonObject=parser.parse(res).getAsJsonObject();
            JsonElement jsonElement=jsonObject.get(value);
            if (jsonElement==null){

                return null;
            }
            JsonArray jsonArray=jsonElement.getAsJsonArray();
            for (int i=1;i<jsonArray.size();i++){
                JsonObject jo=jsonArray.get(i).getAsJsonObject();
if (jo.has("skipType")&&"special".equals(jo.get("skipType").getAsString()))
continue;
            }

        }catch (Exception e){

        }
return  null;

    }
}
