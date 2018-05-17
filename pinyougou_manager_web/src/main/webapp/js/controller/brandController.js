//视图控制器
app.controller("brandController",function ($scope,brandService) {
    //读取列表数据绑定到表单中
    $scope.findAll=function () {
        brandService.findAll().success(
            function (response) {
                $scope.list=response;

            })
    }

    //分页配置
    $scope.paginationConf = {
        currentPage: 1,
        totalItems: 10,
        itemsPerPage: 10,
        perPageOptions: [10, 20, 30, 40, 50],
        onChange: function(){
            $scope.reloadList();//重新加载
        }
    };

    //根据当前页码和每页显示数量查询调用
    $scope.reloadList=function () {
        //切换页码
        $scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);
    }


    //按ID查询
    $scope.findOne=function (id) {
        brandService.findOne(id).success(
            function (response) {
                $scope.entity=response;
            })
    }

    //分页
    $scope.findPage=function (page,page) {
        brandService.findPage(page,rows).success(
            function (response) {
                $scope.list=response.rows;//每页显示的行
                $scope.paginationConf.totalItems=response.total//总记录数
            })
    }
    //添加品牌
    /*$scope.save=function () {
        var methodName="add";
        if ($scope.entity.id!=null){
            methodName="update";
        }
        $http.post("../brand/"+methodName+".do",$scope.entity).success(
            function (response) {
                if (response.success){
                    //重新查询
                    $scope.reloadList;
                }else{
                    alert(response.message);
                }

        })
    }*/
    $scope.save=function () {
        var serviceObject={};//服务层对象
        if ($scope.entity.id!=null){
            serviceObject=brandService.update($scope.entity);
        }else{
            serviceObject=brandService.add($scope.entity);
        }
        serviceObject.success(
            function (response) {
                if (response.success){
                    //重新查询
                    $scope.reloadList;
                }else{
                    alert(response.message);
                }

            })
    }

    $scope.selectIds=[];//选中的id

    //更新复选框
    $scope.updateSelection=function ($event,id) {
        if ($event.target.checked){
            $scope.selectIds.push(id);

        }else{
            $scope.selectIds.splice($scope.selectIds.indexOf(id),1);
        }
    }

    //删除操作
    $scope.dele=function () {
        brandService.dele($scope.selectIds).success(
            function (response) {//返回的是result
                if(response.success){
                    $scope.reloadList();//刷新列表
                }else{
                    alert(response.message);
                }
            }

        )


    }

    $scope.searchEntity={};//定义搜索对象为空对象防止传递给null的情况
    //搜索

    $scope.search=function (page,rows) {
        brandService.search(page,rows,$scope.searchEntity).success(
            function (response) {
                $scope.list=response.rows;
                $scope.paginationConf.totalItems=response.total;
            })
    }


})