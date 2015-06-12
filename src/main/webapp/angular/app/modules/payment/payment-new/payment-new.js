/**
 * @module payment new
 * @summary payment new module
 */

/*globals window, angular, document */

angular.module('payment-new', [
    'ui.router',
    'ui.bootstrap',
    'ui.bootstrap.datepicker'
])
    .controller('payment-new', ['$scope', '$state', 'GeneralRestService', '$log', '$timeout', function ($scope, $state, GeneralRestService, $log, $timeout) {
        'use strict';
        
        $scope.data = GeneralRestService;
        $scope.accordianOpen1 = true;
        $scope.accordianOpen2 = false;
              
		$scope.data.query({
			section : 'bank'
		});

        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.post({section: 'payment', id: 0}, function (data) {
                $scope.isDisabled = false;
                $scope.data.payment.push(data);
                $state.go('payments');
            });
        };

        //Empty new payment
        $scope.data.payment_new = {
        		bank : null,
        		paymentDate: new Date()
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
    }]);