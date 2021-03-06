/**
 * @module payment new
 * @summary payment new module
 */

/*globals window, angular, document */

angular.module('payment-new', [
    'ui.router',
    'ui.bootstrap',
    'ui.bootstrap.datepicker',
    'ngFileUpload'
])
    .controller('payment-new', ['$scope', '$state', 'GeneralRestService', '$log', '$timeout', 'Upload', function ($scope, $state, GeneralRestService, $log, $timeout, Upload) {
        'use strict';
        
        $scope.data = GeneralRestService;
        $scope.accordianOpen1 = true;
        $scope.accordianOpen2 = false;
              
		$scope.data.query({section : 'bank'}, function (data) {
			$scope.setDefaultBank();
		});
		
        //Empty new payment
        $scope.data.payment_new = {
        		bank : null,
        		paymentDate: new Date()
        };
        
		//Set default value to 'bank' choose box
        $scope.setDefaultBank = function() {
        	if ($scope.data.bank !== undefined && $scope.data.payment_new !== undefined){
        		var arrayLength = $scope.data.bank.length;
        		if (arrayLength > 0){
        	    	$scope.data.payment_new.bank = $scope.data.bank[0];
        	    }
        	}
        };

        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.post({section: 'payment', id: 0}, function (data) {
                $scope.isDisabled = false;
                $scope.data.payment.push(data);
                $state.go('payments');
                $scope.data.query({section : 'payment'});
            }, function (data) {
                $scope.isDisabled = false;
                $scope.editable = true;
			});
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
      	  
        //Upload xml file and import new payments
      	$scope.upload = function () {
      		$scope.isDisabled = true;
      		var files = document.getElementById('paymentsXmlFile').files;
      		$log.info("files: "+ files)
            if (files && files.length) {
                for (var i = 0; i < files.length; i++) {
                    var file = files[i];
                    Upload.upload({
                        url: 'rest/payment/upload',
                        fields: {
                            'bank': $scope.data.payment_new.bank.identifier
                        },
                        file: file
                    }).progress(function (evt) {
                    	//Progress logging
                    	/*
                        var progressPercentage = parseInt(100.0 * evt.loaded / evt.total);
                        $scope.log = 'progress: ' + progressPercentage + '% ' +
                                    evt.config.file.name + '\n' + $scope.log;
                        */
                    }).success(function (data, status, headers, config) {
                        $scope.log = 'file ' + config.file.name + 'uploaded. Response: ' + JSON.stringify(data) + '\n' + $scope.log;
                        //$scope.$apply();
                        $scope.isDisabled = false;
                        //Refresh paymets list
                        $scope.data.query({
        					section : 'payment'
        				});
                        $state.go('payments');
                    });
                }
            }
        };
    }]);