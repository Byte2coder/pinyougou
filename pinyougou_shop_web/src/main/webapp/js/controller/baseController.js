 //品牌控制层 
app.controller('baseController' ,function($scope){	
	
    //重新加载列表 数据
    $scope.reloadList=function(){
    	//切换页码  
    	$scope.search( $scope.paginationConf.currentPage, $scope.paginationConf.itemsPerPage);	   	
    }
    
	//分页控件配置 
	$scope.paginationConf = {
         currentPage: 1,
         totalItems: 10,
         itemsPerPage: 10,
         perPageOptions: [10, 20, 30, 40, 50],
         onChange: function(){
        	 $scope.reloadList();//重新加载
     	 }
	}; 
	
	$scope.selectIds=[];//选中的ID集合 

	//更新复选
	$scope.updateSelection = function($event,id) {
		if($event.target.checked){//如果是被选中,则增加到数组
			$scope.selectIds.push( id);			
		}else{
			var idx = $scope.selectIds.indexOf(id);
            $scope.selectIds.splice(idx, 1);//删除 
		}
	}

    /**
	 * [{"id":2,"text":"华为"},{"id":3,"text":"三星"}]
     * @param jsonString
     * @param key
	 *
	 *
     */
	$scope.jsonToString=function (jsonString,key) {
		//将字符串数据转为json
		let fromJson = angular.fromJson(jsonString);
		//进行遍历添加
		let str="";
        for (var i=0;i<fromJson.length;i++) {
			  str += fromJson[i][key]+",";
        }
        if (str.length>1){
        	str=str.substring(0,str.length-1);
		}
		return str;
    }

    /**
	 * 从集合中按照key查询对象
     */
	
    $scope.searchObjectByKey=function (list,key,keyvalue) {
		for(var i=0;i<list.length;i++){
			var object=list[i];
			if(object[key]==keyvalue){
				return object;
			}
		}
		return null;
    }
	
});	