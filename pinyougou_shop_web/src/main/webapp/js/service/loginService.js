app.service("loginService",function ($http) {
    this.getLoginName=function () {
        return $http.get("../login/getUserInfo.do");
    }
})