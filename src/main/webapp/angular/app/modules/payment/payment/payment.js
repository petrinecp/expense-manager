/**
 * @module payment
 * @summary payment module
 */

/*globals window, angular, document */

angular.module('payment', [
    'ui.router'
])
    .controller('payment', ['$rootScope', '$scope', '$stateParams', 'GeneralRestService', '$state', '$timeout', function ($rootScope, $scope, $stateParams, GeneralRestService, $state, $timeout) {
        'use strict';
        
        $scope.data = GeneralRestService;
        $scope.editable = false;
        
		$scope.data.query({section : 'bank'}, function (data) {
			$scope.setDefaultBank();
		});
        
    	if ($stateParams.id) {
            $rootScope.id = Number($stateParams.id);
            $scope.data.get({section: 'payment', id: $rootScope.id}, function (data) {
                $scope.payment = data;
                $scope.setDefaultBank();
            });
        }
        
        $scope.edit = function () {
            $scope.editable = true;
        };
        
        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.payment.user = null;
            $scope.data.update({section: 'payment', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
                $state.go('payments');
                $scope.data.query({
					section : 'payment'
				});
            }, function (data) {
                $scope.isDisabled = false;
                $scope.editable = true;
			});
        };
        
        $scope.remove = function () {
            $scope.isDisabled = true;
            $scope.data.remove({section: 'payment', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
                $state.go('payments');
                $scope.data.query({
					section : 'payment'
				});
            }, function (data) {
                $scope.isDisabled = false;
                $scope.editable = true;
			});
        };
        
        $scope.addSection = function () {
            var copy = angular.copy($scope.user.sections[$scope.user.sections.length - 1]);
            $scope.user.sections.push(copy);
        };
        
        $scope.removeSection = function () {
            $scope.user.sections.splice($scope.user.sections.length - 1, $scope.user.sections.length);
        };
        
        // Remember if datepicker is open
        $scope.datePicker = {
        		isOpen: false
        };
        
        //Open date picker popup
        $scope.open = function($event) {
            $event.preventDefault();
            $event.stopPropagation();

            $timeout( function(){
                $scope.datePicker.isOpen = true;  
             }, 50);
        };
        
        //Set default value to 'bank' choose box
        $scope.setDefaultBank = function() {
        	if ($scope.data.bank !== undefined && $scope.payment !== undefined && $scope.payment.bank !== undefined){
        		var arrayLength = $scope.data.bank.length;
            	for (var i = 0; i < arrayLength; i++) {
            	    if ($scope.data.bank[i].id == $scope.payment.bank.id){
            	    	$scope.payment.bank = $scope.data.bank[i];
            	    	break;
            	    }
            	}
        	}
        };
    }])