var webApp = angular.module('webApp', ['ngRoute']);				

webApp.config(function ($routeProvider) {
    
	 $routeProvider
     .when('/login', {
         controller: 'loginController',
         templateUrl: 'views/login.html',
         controllerAs: 'vm'
     })
      .when('/signup', {
         controller: 'signupController',
         templateUrl: 'views/signup.html',
         controllerAs: 'vm'
     })
     .when('/profile', {
         controller: 'profileController',
         templateUrl: 'views/profile.html',
         controllerAs: 'vm'
     })
     .when('/about', {
         controller: 'aboutController',
         templateUrl: 'views/about.html',
         controllerAs: 'vm'
     })
	  .when('/logout', {
         controller: 'logoutController',
         controllerAs: 'vm'
     })
     

     .otherwise({ redirectTo: '/login' })
    ;
	})
	.run(function($rootScope,$location,$http) {
            $rootScope.$on('$routeChangeStart', function (event, next) {
            	
				if($location.$$path === '/about') {
            		//Do nothing
            	} else if($location.$$path === '/profile' && (null == localStorage.getItem("userId") || null == localStorage.getItem("userEmail"))) {
            		//If user is not logged in then move user to login
					event.preventDefault();
            		$rootScope.isLoggedIn = false;
            		$rootScope.removeLocalStorageVariables(true);
            	} else if($location.$$path === '/login') { 
					//If user is logged in then move user to profile page
            		if(null != localStorage.getItem("userId") && null != localStorage.getItem("userEmail")) {
            			event.preventDefault();
            			$rootScope.isLoggedIn = true;
            			$location.path('/profile').replace();
            		} else {
            			//Just remove local variable if any, and then proceed to redirect
            			$rootScope.removeLocalStorageVariables(false);
            		}
            	}
            });
            
            $rootScope.removeLocalStorageVariables = function(shouldRedirectToLogin) {
        		localStorage.removeItem("userId");
          	  	localStorage.removeItem("userEmail");
          	  	$rootScope.isLoggedIn = false;
          	  	if(shouldRedirectToLogin) {
          	  		$location.path('/login').replace();	
          	  	}
          	  	
        	}

	});