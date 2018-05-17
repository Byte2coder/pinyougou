var app = angular.module('pinyougou', []);//定义模块

//
/*app.filter('trustHtml',['$sce',function ($sce) {
   return function (data) {
       return $sce.trustAsHtml(data);
   }
}])*/

app.filter('trustHtml',function ($sce) {
    return function (data) {//data就是原来的数据
        return $sce.trustAsHtml(data);
    }
})