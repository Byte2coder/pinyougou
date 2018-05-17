app.controller('searchController',function ($scope,searchService) {

    //绑定一个变量
    $scope.searchMap={'keywords':'','category':'','brand':'',spec:{}};
    //搜索
    $scope.search=function () {
        searchService.search($scope.searchMap).success(
            function (response) {
            $scope.resultMap=response;
        })
    }

    //点击分类或者品牌或者规格的时候被调用
    $scope.addSearchItem=function (key,value) {
        if(key=='category'||key=='brand'){
            $scope.searchMap[key]=value;
        }else{
            //添加规格的搜索项
           $scope.searchMap.spec[key]=value;

        }

    }

    //移除搜索项
    $scope.removeSearchItem=function (key) {

        //添加普通的搜索项
        if(key=='category'|| key=='brand'){
            $scope.searchMap[key]='';
        }else{
            delete $scope.searchMap.spec[key];//删除对象中的属性
        }
        $scope.search();
    }

})