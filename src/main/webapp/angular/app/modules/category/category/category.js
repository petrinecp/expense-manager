/**
 * @module category
 * @summary category module
 */

/*globals window, angular, document */

angular.module('category', [
    'ui.router'
])
    .controller('category', ['$rootScope', '$scope', '$stateParams', 'GeneralRestService', '$state', 'authFactory', function ($rootScope, $scope, $stateParams, GeneralRestService, $state, authFactory) {
        'use strict';
        
        $scope.data = GeneralRestService;
        $scope.editable = false;
        $scope.authFactory = authFactory;
        $scope.data.errorMessages = [];
        
    	if ($stateParams.id) {
            $rootScope.id = Number($stateParams.id);
            $scope.data.get({section: 'category', id: $rootScope.id}, function (data) {
                $scope.category = data;
            });
        }
        
        $scope.edit = function () {
            $scope.editable = true;
        };
        
        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.update({section: 'category', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
                $state.go('categories');
                $scope.data.query({
					section : 'category'
				});
            }, function (data) {
                $scope.isDisabled = false;
                $scope.editable = true;
			});
        };
        
        $scope.remove = function () {
            $scope.isDisabled = true;
            $scope.data.remove({section: 'category', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
                $state.go('categories');
                $scope.data.query({
					section : 'category'
				});
            });
        };
        
        $scope.isMyCategory = function (category) {
        	if (category !== undefined && category != null){
        		var isMyCategory = false;
        		try {
            		if(category.user !== undefined && category.user != null && category.user.username == authFactory.getUserName()){
                		isMyCategory = true;
            		}
            	}
            	catch(err) {
            	}
            	
    	    	if(isMyCategory || authFactory.isAdmin()){
    	    		return true;
    	    	} else {
    	    		return false;
    	    	}
        	} else {
        		return false;
        	}
        };
        
        $scope.addSection = function () {
            var copy = angular.copy($scope.user.sections[$scope.user.sections.length - 1]);
            $scope.user.sections.push(copy);
        };
        
        $scope.removeSection = function () {
            $scope.user.sections.splice($scope.user.sections.length - 1, $scope.user.sections.length);
        };
    }])