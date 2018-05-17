 //控制层 
app.controller('goodsController' ,function($scope,$controller   ,$location,goodsService,uploadService,itemCatService,typeTemplateService){
	
	$controller('baseController',{$scope:$scope});//继承
	
    //读取列表数据绑定到表单中  
	$scope.findAll=function(){
		goodsService.findAll().success(
			function(response){
				$scope.list=response;
			}			
		);
	}    
	
	//分页
	$scope.findPage=function(page,rows){			
		goodsService.findPage(page,rows).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}
	
	//查询实体 
	$scope.findOne=function(id){
		var id=$location.search()['id'];
		if(id==null || id==undefined){
			return ;
		}
		goodsService.findOne(id).success(
			function(response){
				$scope.entity= response;

				//将查询到的数据存到kindeditor
				editor.html($scope.entity.goodsDesc.introduction)
			    //将查询到的图片从字符串转换为json
				$scope.entity.goodsDesc.itemImages=angular.fromJson($scope.entity.goodsDesc.itemImages);
			   //将查询的自定义属性从字符串转换为json
				$scope.entity.goodsDesc.customAttributeItems=angular.fromJson($scope.entity.goodsDesc.customAttributeItems);
               //将查询到的规格属性从字符串转换为json
				$scope.entity.goodsDesc.specificationItems=angular.fromJson($scope.entity.goodsDesc.specificationItems);
                  for(var i=0;i<$scope.entity.itemList.length;i++){
                  	$scope.entity.itemList[i].spec=angular.fromJson($scope.entity.itemList[i].spec);
				  }


			}
		);				
	}
	
	//保存 
	$scope.save=function(){
		$scope.entity.goodsDesc.introduction=editor.html();
		var serviceObject;//服务层对象  				
		if($scope.entity.goods.id!=null){//如果有ID
			serviceObject=goodsService.update( $scope.entity ); //修改  
		}else{
			serviceObject=goodsService.add( $scope.entity  );//增加 
		}				
		serviceObject.success(
			function(response){
				if(response.success){
					alert("保存成功!")
					$scope.entity={};
					editor.html("");
					//重新查询 
		        	//$scope.reloadList();//重新加载
					window.location.href="goods.html";
				}else{
					alert(response.message);
				}
			}		
		);				
	}
	
	//增加
	/*
	$scope.add=function () {
		//将商品介绍的富文本编辑器绑定到要保存的字段中
		$scope.entity.goodsDesc.introduction=editor.html();
        goodsService.add( $scope.entity).success(
        	function (response) {
        		if(response.success){
        			$scope.entity={};
        			//清空富文本编辑器里面的内容
					editor.html('');
				}else{
        			alert(response.message);
				}
        })


		
    }*/
	 
	//批量删除 
	$scope.dele=function(){			
		//获取选中的复选框			
		goodsService.dele( $scope.selectIds ).success(
			function(response){
				if(response.success){
					$scope.reloadList();//刷新列表
				}						
			}		
		);				
	}

	//定义商品审核状态信息
	$scope.status=["未审核","已审核","审核未通过","关闭"]
	
	$scope.searchEntity={};//定义搜索对象
	
	//搜索
	$scope.search=function(page,rows){			
		goodsService.search(page,rows,$scope.searchEntity).success(
			function(response){
				$scope.list=response.rows;	
				$scope.paginationConf.totalItems=response.total;//更新总记录数
			}			
		);
	}


	//定义一个新数组
	$scope.itemCatList=[];
	//加载商品分类列表
	$scope.findItemList=function () {
		itemCatService.findAll().success(
			function (response) {//List<TbItemCat>
			for(var i=0;i<response.length;i++){
				$scope.itemCatList[response[i].id]=response[i].name;
			}

        })
    }
	
    /**
	 * 上传图片
     */
    $scope.uploadFile=function () {
        uploadService.uploadFile().success(
            function (response) {//result :包含里面的图片的URL
                if(response.success){
                    $scope.image_entity.url=response.message;
                }else{
                    alert("失败");
                }

            }
        )
    }

    $scope.entity={goods:{},goodsDesc:{itemImages:[],specificationItems:[]},itemList:[]}

    /**
	 * 将图片对象添加到数组中
     */
    $scope.add_image_entity=function () {
		$scope.entity.goodsDesc.itemImages.push($scope.image_entity);
    }

    //列表中移除图片
    $scope.remove_image_entity=function(index){
        $scope.entity.goodsDesc.itemImages.splice(index,1);
    }

    //查询一级分类的列表
	$scope.selectCat1List=function () {
		itemCatService.findByParentId(0).success(
			function (response) {//list<tbitemcat>
				$scope.itemCat1List=response;
            }
		)
    }

    //监控一级分类ID变化触发的变化
	$scope.$watch('entity.goods.category1Id',function (newValue,oldValue) {
		if (newValue!=undefined){
            itemCatService.findByParentId(newValue).success(
                function (response) {//list<tbitemcat>
                    $scope.itemCat2List=response;
                }
            )

		}
    })
//监控二级分类ID变化触发的变化
    $scope.$watch('entity.goods.category2Id',function (newValue,oldValue) {
        if (newValue!=undefined){
            itemCatService.findByParentId(newValue).success(
                function (response) {//list<tbitemcat>
                    $scope.itemCat3List=response;
                }
            )

        }
    })

    //监控三级分类ID变化,触发以下逻辑,根据三级分类的ID 自己的对象的数据 获取到模板的ID 展示到页面
    $scope.$watch('entity.goods.category3Id',function (newValue,oldValue) {
        if (newValue!=undefined){
            itemCatService.findOne(newValue).success(
                function (response) {//tbitemcat
                    $scope.entity.goods.typeTemplateId=response.typeId;//展示模板的id
                }
            );

        }
    })

    /**
	 * 监控模板ID的变化,查询模板的对象,查询关联到的品牌列表,展示到下拉框
     */

    $scope.$watch("entity.goods.typeTemplateId",function (newValue,oldValue) {
    	if(newValue!=null){
    		//查询模板数据,查询品牌和扩展属性的展示
    	typeTemplateService.findOne(newValue).success(
    		function (response) {//TbTypeTemplate
				$scope.typeTemplate=response;
                $scope.typeTemplate.brandIds=angular.fromJson( $scope.typeTemplate.brandIds);
               // $scope.typeTemplate.brandIds= JSON.parse( $scope.typeTemplate.brandIds);//品牌列表
                //$scope.entity.goodsDesc.customAttributeItems=angular.fromJson($scope.typeTemplate.customAttributeItems);
				if($location.search()['id']!=null||$location.search()['id']==undefined){
					//要编辑,不需要从模板中获取属性

				}else{
                    $scope.entity.goodsDesc.customAttributeItems=angular.fromJson( $scope.typeTemplate.customAttributeItems);//扩展属性
				}


        }
        )
			//查询模板对应的规格列表
			typeTemplateService.findSpecList(newValue).success(
				function (response) {//list<Map>
					$scope.specList=response;

            })


		}

    })


    /**
	 * [{"attributeName":"网络制式","attributeValue":"联通4G"}]
	 * 当点击复选框时调用去影响变量,$scope.entity.goodsDesc.specificationItems的值
     * @param specName    点击选项所对应的规格名称,--网络制式
     * @param specValue    点击选项的值,--联通4G
     */
    $scope.updateSpecAttribute=function ($event,specName,specValue) {
		var object=$scope.searchObjectByKey($scope.entity.goodsDesc.specificationItems,'attributeName',specName);
		if(object!=null){
            //有对象
			if($event.target.checked){

                object.attributeValue.push(specValue);
			}else{
                object.attributeValue.splice(object.attributeValue.indexOf(specValue),1);
                //判断数组的attributeValue长度是否为0,若是,删除该对象
				if(object.attributeValue.length==0){
                    $scope.entity.goodsDesc.specificationItems.splice($scope.entity.goodsDesc.specificationItems.indexOf(object),1);
				}

			}

		}else{
			//没有对象
            $scope.entity.goodsDesc.specificationItems.push({'attributeName':specName,'attributeValue':[specValue]});
		}
    }


    /**
     * 重新构建SKU列表  深克隆
     */
    $scope.createItemList=function () {
        $scope.entity.itemList=[{spec:{},price:0,num:9999,status:'0',isDefault:'0'}];

        //循环遍历$scope.entity.goodsDesc.specificationItems ---》 [{"attributeName":"网络制式","attributeValue":["移动3G"]}]

        var items = $scope.entity.goodsDesc.specificationItems;

        for (var i=0;i<items.length;i++){
            var object = items[i];
            $scope.entity.itemList=addColumn($scope.entity.itemList,object.attributeName,object.attributeValue);
        }
    }

    /**
     *
     * @param list   [{spec:{},price:0,num:9999,status:'0',isDefault:'0'}];
     * @param column 网络制式
     * @param clumnValues ["移动3G"]
     * @returns {Array}
     */
    addColumn=function (list,column,columnValues) {
        var newList = [];
        for(var i=0;i<list.length;i++){
            var oldRow =  list[i];//{spec:{'网路制式'“：3g},price:0,num:9999,status:'0',isDefault:'0'}
            for (var j = 0;j<columnValues.length;j++){
                var newRow = angular.fromJson(angular.toJson(oldRow));
                newRow.spec[column]=columnValues[j];//加规格属性
                newList.push(newRow);
            }
        }
        return newList;
    }

    //根据规格名称和选项名称返回是否被勾选
    $scope.checkAttributeValue=function(specName,specValue){
        var items= $scope.entity.goodsDesc.specificationItems;
        var object= $scope.searchObjectByKey(items,'attributeName',specName);
        if(object!=null){
            if(object.attributeValue.indexOf(specValue)!=-1){//说明找到
                return true;
            }else{
                return false;
            }
        }else{
            return false;
        }
    }

});	
