package com.pinyougou.shop.controller;

import com.pinyougou.util.FastDFSClient;
import entity.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class FileUploadController {

    @Value("${IMAGE_SERVER_URL}")
    private String IMAGE_SERVER_URL;

    @RequestMapping("/upload")
    public Result uploadFile(MultipartFile file){

        try {
            //将接受到的文件流 上传到fastdfs
            //需要字节数组
            byte[] bytes = file.getBytes();
            //需要扩展名
            String originalFilename = file.getOriginalFilename();
            String extName = originalFilename.substring(originalFilename.lastIndexOf(".") + 1);

            FastDFSClient client = new FastDFSClient("classpath:config/fdfs_client.conf");
            String parturl = client.uploadFile(bytes, extName);
            String url=IMAGE_SERVER_URL+parturl;
            return  new Result(true,url);

        } catch (Exception e) {
            e.printStackTrace();
            return new Result(false,"上传失败");
        }



    }

}
