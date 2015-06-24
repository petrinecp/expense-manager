/**
 * @module rule
 * @summary rule module
 */

/*globals window, angular, document */

angular.module('rule', [
    'ui.router'
])
    .controller('rule', ['$rootScope', '$scope', '$stateParams', 'GeneralRestService', '$state', 'authFactory', function ($rootScope, $scope, $stateParams, GeneralRestService, $state, authFactory) {
        'use strict';
        
        $scope.data = GeneralRestService;
        $scope.editable = false;
        $scope.authFactory = authFactory;
        $scope.data.errorMessages = [];
        
        $scope.data.query({section : 'category'}, function (data) {
			$scope.setDefaultCategory();
		});
        
        //Set default value to 'category' choose box
        $scope.setDefaultCategory = function() {
        	if ($scope.data.category !== undefined && $scope.rule !== undefined && $scope.rule.category !== undefined){
        		var arrayLength = $scope.data.category.length;
            	for (var i = 0; i < arrayLength; i++) {
            	    if ($scope.data.category[i].id == $scope.rule.category.id){
            	    	$scope.rule.category = $scope.data.category[i];
            	    	break;
            	    }
            	}
        	}
        };
        
    	if ($stateParams.id) {
            $rootScope.id = Number($stateParams.id);
            $scope.data.get({section: 'rule', id: $rootScope.id}, function (data) {
                $scope.rule = data;
                $scope.setDefaultCategory();
            });
        }
        
        $scope.edit = function () {
            $scope.editable = true;
        };
        
        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.update({section: 'rule', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
                $state.go('rules');
                $scope.data.query({
					section : 'rule'
				});
            }, function (data) {
                $scope.isDisabled = false;
                $scope.editable = true;
			});
        };
        
        $scope.remove = function () {
            $scope.isDisabled = true;
            $scope.data.remove({section: 'rule', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
                $state.go('rules');
                $scope.data.query({
					section : 'rule'
				});
            });
        };
        
        $scope.isMyRule = function (rule) {
        	if (rule !== undefined && rule != null){
        		var isMyRule = false;
        		try {
            		if(rule.user !== undefined && rule.user != null && rule.user.username == authFactory.getUserName()){
                		isMyRule = true;
            		}
            	}
            	catch(err) {
            	}
            	
    	    	if(isMyRule || authFactory.isAdmin()){
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