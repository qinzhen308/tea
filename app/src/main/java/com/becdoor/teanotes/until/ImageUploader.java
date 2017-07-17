package com.becdoor.teanotes.until;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.os.Looper;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.util.Log;

import com.becdoor.teanotes.global.Constant;
import com.becdoor.teanotes.parser.gson.GsonParser;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.builder.PostFormBuilder;
import com.zhy.http.okhttp.callback.StringCallback;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.logging.Handler;

import okhttp3.Call;
import okhttp3.OkHttpClient;
import okhttp3.Response;

/**
 * Created by paulz on 2016/10/28.
 */

public class ImageUploader {

    public static Uri temp_img_crop_uri;
    public static String temp_img_crop_path;


    public static String note_img_temp_file_path = Environment.getExternalStorageDirectory()
            .getPath() + "/djnote/images/";// 头像存储路径

    static {
        File pathFile = new File(note_img_temp_file_path);
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        temp_img_crop_path=note_img_temp_file_path+"temp_img_crop.png";
        temp_img_crop_uri=Uri.fromFile(new File(temp_img_crop_path));
    }

    public static void init(){

    }

    public static void setTempCrop(String name){
        temp_img_crop_path=note_img_temp_file_path+name;
        temp_img_crop_uri=Uri.fromFile(new File(temp_img_crop_path));

    }



    public static void upload(Context context,File file,final Callback callback){
        final File newFile=compressImage(file);
        File tagFile=newFile==null?file:newFile;
        OkHttpUtils.post().tag(context).url(Constant.BASE_URL+"upload.php?").addParams("file_type","image").addParams("act","upload").addParams("app_key",Constant.DJ_APP_KEY)
                .addParams("access_token",Constant.VALUE_ACCESS_TOKEN)
                .addFile("imgFile",tagFile.getName(),tagFile)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                deleteFile(newFile);
                callback.onFailed(e.toString());
            }

            @Override
            public void onResponse(String response, int id) {
                JSONObject obj= null;
                try {
                    obj = new JSONObject(response);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(obj==null){
                    callback.onFailed("数据格式错误");
                }else {
                    int status=obj.optInt("status");
                    if(status==200){
                        String url=obj.optString("url");
                        if(url.startsWith("/")){
                            url=url.substring(1,url.length());
                        }
                        callback.onSuccess(url);
                    }else {
                        callback.onFailed(obj.optString("message"));
                    }
                }
                deleteFile(newFile);
            }
        });
    }

    public static void uploadBatch(final Context context, final List<File> files, final Callback callback){
        final android.os.Handler handler=new android.os.Handler(Looper.getMainLooper());
        new Thread(){
            @Override
            public void run() {
                PostFormBuilder builder=OkHttpUtils.post().tag(context).url(Constant.BASE_URL+"uploads.php?").addParams("act","uploads").addParams("app_key",Constant.DJ_APP_KEY).addParams("access_token",Constant.VALUE_ACCESS_TOKEN);
                for(int i=0;i<files.size();i++){
                    File file=files.get(i);
                    String name=file.getName();
                    String[] s=name.split("\\.");
                    builder.addFile(s[0],file.getName(),file);
                }
                Response response= null;
                try {
                    response =builder.build().execute();
                    final String result =response.body().string();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            JSONObject obj=null;
                            try {
                                obj = new JSONObject(result);
                                if(obj.optInt("status")==200){
                                    if(callback!=null)callback.onSuccess(obj.opt("img_arr").toString());
                                    return;
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            if(callback!=null)callback.onFailed("图片上传失败");
                        }
                    });
                } catch (final IOException e) {
                    e.printStackTrace();
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if(callback!=null)callback.onFailed(e.toString());
                        }
                    });

                }
            }
        }.start();
    }


    public static void deleteFile(File file){
        if(file!=null){
            file.delete();
        }
    }
    public static final long LIMIT_SIZE=1024*256;
    public static final int LIMIT_WIDTH=500;//压缩的图片均以宽为准，超出这个宽度的，都按宽等比压缩至这么大。
    public static File compressImage(File file){
        if(file==null||!file.exists())return null;
        long size=file.length();
        BitmapFactory.Options options=new BitmapFactory.Options();
        options.inJustDecodeBounds=true;
        Bitmap bm;
        bm=BitmapFactory.decodeFile(file.getPath(),options);
        int W=options.outWidth;
        int H=options.outHeight;
        Log.d("img","start compress ------dimen w="+W+",h="+H);
        int degree = ImageUtil.readPictureDegree(file.getPath());
        //以角度为0或者180的宽为基准宽
        int baseW=0;
        if(degree==0||degree==180){
            baseW=W;
        }else {
            baseW=H;
        }
        if(size>LIMIT_SIZE||baseW>LIMIT_SIZE){
            bm=scaleDimen(file,degree,baseW,options);
            long scale=size/LIMIT_SIZE;
//            double scale=Math.sqrt(((double) size)/(double)LIMIT_SIZE);
            String newPath=note_img_temp_file_path+System.currentTimeMillis()+"_temp_compress.png";

            File newFile=new File(newPath);
            FileOutputStream os=null;
            try {
                os=new FileOutputStream(newFile);
                boolean isSuc=bm.compress(Bitmap.CompressFormat.PNG,size>LIMIT_SIZE?(int)(100.0f/(float) scale):100,os);
                Log.d("img","compress is success?--"+isSuc+"--size="+newFile.length()+"---old size="+size+"----dimen w="+bm.getWidth()+",h="+bm.getHeight());
                return newFile;
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }finally {
                if(os!=null) try {
                    os.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            String suffix=file.getName().split("\\.")[1];
            String newPath=note_img_temp_file_path+System.currentTimeMillis()+"_temp_compress"+(suffix.length()>0?"."+suffix:"");
            File newFile=new File(newPath);
            if(newFile.exists()){
                newFile.delete();
            }
            try {
                newFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            copyFile(file.getAbsolutePath(),newPath);
            return new File(newPath);
        }
        return null;
    }

    //缩放图片至500p
    private static Bitmap scaleDimen(File file,int degree,int W,BitmapFactory.Options options){
        options.inJustDecodeBounds=false;
        Bitmap bm;
        if(W>LIMIT_WIDTH){
            float scale=(float)W/(float)LIMIT_WIDTH;
            options.inMutable=true;
            options.inScaled=true;
            options.inSampleSize=(int)(scale+0.5);//四舍五入
            options.inPurgeable = true;// 同时设置才会有效
            options.inInputShareable = true;//。当系统内存不够时候图片自动被回收
            bm=BitmapFactory.decodeFile(file.getPath(),options);
        }else {
            bm=BitmapFactory.decodeFile(file.getPath());
        }
        //把图片旋转为正方向
        try {
            if (degree != 0) {
                bm = ImageUtil.rotaingImageView(degree, bm);// 传入压缩后的图片进行旋转
            }
        }catch (OutOfMemoryError e){
            e.printStackTrace();
        }
        return bm;
    }

    public static void copyFile(String oldPath, String newPath) {
        Log.d("copy_file","开始复制");
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPath);
            if (oldfile.exists()) { //文件不存在时
                InputStream inStream = new FileInputStream(oldPath); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPath);
                byte[] buffer = new byte[1444];
                int length;
                while ( (byteread = inStream.read(buffer)) != -1) {
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
            Log.d("copy_file",newPath+"复制成功--"+new File(newPath).exists());
        }
        catch (Exception e) {
            System.out.println("复制单个文件操作出错");
            e.printStackTrace();

        }

    }

    public static File saveImag(Bitmap bitmap, String name) {

        // Bitmap bitmap = compressImage(path);
        // // Bitmap bitmap = getimage_(path);

        if (null == bitmap) {
            return null;
        }
        File file = null;
        try {

            file = new File(note_img_temp_file_path, name);
            if (file.exists()) {
                file.delete();
            }

            file.createNewFile();

            FileOutputStream fileOut = new FileOutputStream(file);
            int size = 100;
            if (bitmap.getHeight() > 1000 || bitmap.getWidth() > 1000) {
                size = 80;
            }
            bitmap.compress(Bitmap.CompressFormat.PNG, size, fileOut);

            fileOut.flush();
            fileOut.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return file;
    }

    public static String getRealPathFromURI(Context context,Uri contentUri) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT && DocumentsContract.isDocumentUri(context, contentUri)) {
            if (isExternalStorageDocument(contentUri)) {
                String docId = DocumentsContract.getDocumentId(contentUri);
                String[] split = docId.split(":");
                String type = split[0];
                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }
            } else if (isDownloadsDocument(contentUri)) {
                String id = DocumentsContract.getDocumentId(contentUri);
                Uri contentUri2 = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));
                return getDataColumn(context, contentUri2, null, null);
            } else if (isMediaDocument(contentUri)) {
                String docId = DocumentsContract.getDocumentId(contentUri);
                String[] split = docId.split(":");
                String type = split[0];
                Uri contentUri2 = null;
                if ("image".equals(type)) {
                    contentUri2 = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri2 = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri2 = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }
                String selection = MediaStore.Images.Media._ID + "=?";
                String[] selectionArgs = new String[] { split[1] };
                return getDataColumn(context, contentUri2, selection, selectionArgs);
            }
        }else if(ContentResolver.SCHEME_CONTENT.equals(contentUri.getScheme())){
            String res = null;
            String[] proj = { MediaStore.Images.Media.DATA };
            Cursor cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            if(cursor.moveToFirst()){;
                int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                res = cursor.getString(column_index);
            }
            cursor.close();
            return res;
        }else{
            String path=contentUri.toString();
            if(path.startsWith("file")){
                path=path.replace("file://","");
            }
            return path;
        }
        return null;
    }

    public static String getDataColumn(Context context, Uri uri, String selection, String[] selectionArgs) {
        Cursor cursor = null;
        String column = MediaStore.Images.Media.DATA;
        String[] projection = { column };
        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs, null);
            if (cursor != null && cursor.moveToFirst()) {
                int index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is Google Photos.
     */
    public static boolean isGooglePhotosUri(Uri uri) {
        return "com.google.android.apps.photos.content".equals(uri.getAuthority());
    }

    public static interface Callback{
        public void onSuccess(String url);
        public void onFailed(String msg);
    }
}
