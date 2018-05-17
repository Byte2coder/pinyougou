 //控制层 
app.controller('contentController' ,function($scope,$controller   ,contentService){
	
    $scope.contentList=[];
    $scope.findContentListByCategoryId=function (categoryId) {
		contentService.findContentListByCategoryId(categoryId).success(
			function (response) {
                $scope.contentList[categoryId]=response;
        })
    }

});	
