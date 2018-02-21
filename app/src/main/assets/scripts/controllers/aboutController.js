webApp.controller('aboutController', function($scope,service,$http ,$location,$rootScope) {
  
	 if(null != localStorage.getItem("userId") && null != localStorage.getItem("userEmail")) {
		 $rootScope.isLoggedIn = true;
	 }
});