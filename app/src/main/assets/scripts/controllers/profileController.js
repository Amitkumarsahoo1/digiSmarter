webApp.controller('profileController', function($scope,service, $location,$rootScope) {
    
	 var userId = localStorage.getItem("userId") ;
	 $scope.userData = {};
	 if(null != userId) {
		 service.getUserData(userId).then(function(data){
			 $scope.userData = data.data;
		 },function(error){
			 alert(error.data.data);
			 $rootScope.removeLocalStorageVariables(true);
		 })
	 } else {
		 $rootScope.removeLocalStorageVariables(true);
	 }

	 if(null != localStorage.getItem("userId") && null != localStorage.getItem("userEmail")) {
		 $rootScope.isLoggedIn = true;
	 }
});