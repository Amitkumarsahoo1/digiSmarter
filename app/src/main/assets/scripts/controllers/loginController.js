webApp.controller('loginController', function($scope,service,$http ,$location,$rootScope) {
    
  $scope.login = function(user) {
      var obj={};
        obj.email = user.email;
        obj.password = user.password;
        service.login(obj).then(function(data){
        	var userData = data.data;
        	if(undefined != userData) {
        		localStorage.setItem("userId",userData.id);
        		localStorage.setItem("userEmail",userData.email);
        		$location.path('/profile').replace();
        	}
      },function(error) {
    	  alert(error.data.message)
    	  $rootScope.removeLocalStorageVariables(true);
      });
  }
    
    
    $scope.signup = function() {
    	$location.path('/signup').replace();	
    }
    
});

webApp.controller('logoutController', function($rootScope) {
  $rootScope.removeLocalStorageVariables(true);
});


