//文件上传服务层
app.service("uploadService",function($http){
    this.uploadFile=function(){
        var formData=new FormData();//构建一个formData对象(表单)
        formData.append("file",file.files[0]);
        //参数1：file就是和后台controller.java中的参数名要一致
        //值：file  是 文件类型type为file对应的标签所对应的id的值  document.getElementById("file")
        return $http({
            method:'POST',
            url:"../upload.do",
            data: formData,
            headers: {'Content-Type':undefined},
            transformRequest: angular.identity
        });
    }
});