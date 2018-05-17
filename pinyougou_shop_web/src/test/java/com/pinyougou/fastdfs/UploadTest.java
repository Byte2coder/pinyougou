package com.pinyougou.fastdfs;

import com.pinyougou.util.FastDFSClient;
import org.csource.fastdfs.*;
import org.junit.Test;

public class UploadTest {

    @Test
    public void test01() throws Exception{
        ClientGlobal.init("F:\\coding\\pinyougou\\pinyougou_parent\\pinyougou_shop_web\\src\\main\\resources\\config\\fdfs_client.conf");

        //3.先创建一个trackerClient对象  直接nEW一个即可
        TrackerClient trackerClient=new TrackerClient();
        //4.通过trackerClient对象获取trackerServer对象
        TrackerServer trackerServer=trackerClient.getConnection();
        //5.定义一个storageServer对象 赋值为null 就可以了
        StorageServer storageServer=null;
        //6.构建一个storageClient对象
        StorageClient storageClient=new StorageClient(trackerServer,storageServer);
        //7.上传图片
        //参数1：本地文件的路径
        //参数2：文件的扩展名  不要带“.”
        //参数3：元数据（图片的像素 大小 高度 时间戳）
        String[] jpgs = storageClient.upload_file("G:\\pics\\th.jpg", "jpg", null);

       // byte[] file = storageClient.download_file("group1", "M00/00/00/wKgZhVrnr7iAHbIQAAAdhJuP02U802.jpg");


        for (String jpg : jpgs) {
            System.out.println(jpg);
        }

    }

    @Test
    public void  testFASTClient() throws Exception{
        FastDFSClient fastDFSClient = new FastDFSClient("F:\\coding\\pinyougou\\pinyougou_parent\\pinyougou_shop_web\\src\\main\\resources\\config\\fdfs_client.conf");
        String jpg = fastDFSClient.uploadFile("G:\\pics\\蒲公英.jpg", "jpg");
        System.out.println(jpg);
    }

}
