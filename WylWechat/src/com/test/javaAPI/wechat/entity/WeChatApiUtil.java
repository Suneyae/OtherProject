package com.test.javaAPI.wechat.entity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.servlet.http.Part;

import sun.net.www.http.HttpClient;
/**
 * 参考：http://ask.csdn.net/questions/176216
 * @author Wei
 * @time  2016年10月17日 下午9:04:00
 */
public class WeChatApiUtil {
	 // token 接口(GET)
//    private static final String ACCESS_TOKEN = "https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid=%s&secret=%s";
//    // 素材上传(POST)
//    private static final String UPLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/upload";
//    // 素材下载:不支持视频文件的下载(GET)
//    private static final String DOWNLOAD_MEDIA = "http://file.api.weixin.qq.com/cgi-bin/media/get?access_token=%s&media_id=%s";
//
//    public static String getTokenUrl(String appId, String appSecret) {
//        return String.format(ACCESS_TOKEN, appId, appSecret);
//    }
//
//    public static String getDownloadUrl(String token, String mediaId) {
//        return String.format(DOWNLOAD_MEDIA, token, mediaId);
//    }
//
//    /**
//     * 通用接口获取Token凭证
//     * @param appId
//     * @param appSecret
//     * @return
//     */
//    public static String getToken(String appId, String appSecret) {
//        if(appId==null||appSecret==null){
//            return null;
//        }
//
//        String token = null;
//        String tockenUrl = WeChatApiUtil.getTokenUrl(appId, appSecret);
//        String response = httpsRequestToString(tockenUrl, "GET", null);
//        JSONObject jsonObject = JSONObject.fromObject(response);
//        if (null != jsonObject) {
//            try {
//                token =jsonObject.getString("access_token");
//            } catch (JSONException e) {
//                token = null;// 获取token失败
//                logger.error(e);
//            }
//        }
//        return token;
//    }
//
//    /**
//     * 微信服务器素材上传
//     * @param file  表单名称media
//     * @param token access_token
//     * @param type  type只支持四种类型素材(video/image/voice/thumb)
//     */
//    public static JSONObject uploadMedia(File file, String token, String type) {
//        if(file==null||token==null||type==null){
//            return null;
//        }
//
//        if(!file.exists()){
//            logger.info("上传文件不存在,请检查!");
//            return null;
//        }
//
//        String url = UPLOAD_MEDIA;
//        JSONObject jsonObject = null;
//        PostMethod post = new PostMethod(url);
//        post.setRequestHeader("Connection", "Keep-Alive");
//        post.setRequestHeader("Cache-Control", "no-cache");
//        FilePart media = null;
//        HttpClient httpClient = new HttpClient();
//        //信任任何类型的证书
//        Protocol myhttps = new Protocol("https", new MySSLProtocolSocketFactory(), 443); 
//        Protocol.registerProtocol("https", myhttps);
//
//        try {
//            media = new FilePart("media", file);
//            Part[] parts = new Part[] { new StringPart("access_token", token),
//                    new StringPart("type", type), media };
//            MultipartRequestEntity entity = new MultipartRequestEntity(parts,
//                    post.getParams());
//            post.setRequestEntity(entity);
//            int status = httpClient.executeMethod(post);
//            if (status == HttpStatus.SC_OK) {
//                String text = post.getResponseBodyAsString();
//                jsonObject = JSONObject.fromObject(text);
//            } else {
//                logger.info("upload Media failure status is:" + status);
//            }
//        } catch (FileNotFoundException execption) {
//            logger.error(execption);
//        } catch (HttpException execption) {
//            logger.error(execption);
//        } catch (IOException execption) {
//            logger.error(execption);
//        }
//        return jsonObject;
//    }
//
//    /**
//     * 多媒体下载接口
//     * @comment 不支持视频文件的下载
//     * @param fileName  素材存储文件路径
//     * @param token     认证token
//     * @param mediaId   素材ID（对应上传后获取到的ID）
//     * @return 素材文件
//     */
//    public static File downloadMedia(String fileName, String token,
//            String mediaId) {
//        String url = getDownloadUrl(token, mediaId);
//        return httpRequestToFile(fileName, url, "GET", null);
//    }
//
//    /**
//     * 以http方式发送请求,并将请求响应内容输出到文件
//     * @param path    请求路径
//     * @param method  请求方法
//     * @param body    请求数据
//     * @return 返回响应的存储到文件
//     */
//    public static File httpRequestToFile(String fileName,String path, String method, String body) {
//        if(fileName==null||path==null||method==null){
//            return null;
//        }
//
//        File file = null;
//        HttpURLConnection conn = null;
//        InputStream inputStream = null;
//        FileOutputStream fileOut = null;
//        try {
//            URL url = new URL(path);
//            conn = (HttpURLConnection) url.openConnection();
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            conn.setRequestMethod(method);
//            if (null != body) {
//                OutputStream outputStream = conn.getOutputStream();
//                outputStream.write(body.getBytes("UTF-8"));
//                outputStream.close();
//            }
//
//            inputStream = conn.getInputStream();
//            if(inputStream!=null){
//                file = new File(fileName);
//            }else{
//                return file;
//            }
//
//            //写入到文件
//            fileOut = new FileOutputStream(file);
//            if(fileOut!=null){
//                int c = inputStream.read();
//                while(c!=-1){
//                    fileOut.write(c);
//                    c = inputStream.read();
//                }
//            }
//        } catch (Exception e) {
//            logger.error(e);
//        }finally{
//            if(conn!=null){
//                conn.disconnect();
//            }
//
//            /*
//             * 必须关闭文件流
//             * 否则JDK运行时，文件被占用其他进程无法访问
//             */
//            try {
//                inputStream.close();
//                fileOut.close();
//            } catch (IOException execption) {
//                logger.error(execption);
//            }
//        }
//        return file;
//    }
//
//    public static void main(String[] args) {
//        File f = new File("D:/test.png");
//        String appId = "";
//        String appSecret = "";
//        String token = WeChatApiUtil.getToken(appId, appSecret);
//        JSONObject o = WeChatApiUtil.uploadMedia(f, token.getAccessToken(), "image");
//        System.out.println(o.toString());
//
//        //下载刚刚上传的图片以id命名
//        String media_id = o.getString("media_id");
//        File t = WeChatApiUtil.downloadMedia("D:/"+media_id+".png", token.getAccessToken(), media_id);
//
//    }
}
