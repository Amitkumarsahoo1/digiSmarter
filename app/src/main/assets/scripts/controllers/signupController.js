webApp.controller('signupController', function($scope,service,$rootScope,$location) {
    
    $scope.user = {};
    $scope.signup = function(user) {
        var obj = {};
        obj.name = user.name;
        obj.companyName = user.companyName;
        obj.email = user.email;
        obj.password = user.password;

        service.signup(obj).then(function(data) {
			 alert(data.data.message);
             $location.path('/login').replace();
		 },function(error) {
			alert(error.data.message);
        	$rootScope.removeLocalStorageVariables(false);
		 })
    }
});