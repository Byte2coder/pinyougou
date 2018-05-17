//服务层
app.service('contentService',function($http){
	    	

	//查询content
	this.findContentListByCategoryId=function (categoryId) {
		return $http.get('content/findContentListByCategoryId.do?categoryId='+categoryId);
    }
});
