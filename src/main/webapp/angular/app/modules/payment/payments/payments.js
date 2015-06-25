/**
 * @module payments
 * @summary payments module
 */

/* globals window, angular, document */

angular.module('payments', [ 'ngResource', 'ui.bootstrap', 'ui.router' ])

.config([ '$stateProvider', function($stateProvider) {
	'use strict';

	$stateProvider.state('payments', {
		url : '/payments',
		views : {
			'main@' : {
				templateUrl : 'modules/payment/payments/payments.html'
			}
		}
	}).state('payments.new', {
		url : '/new',
		views : {
			'sidebar@payments' : {
				templateUrl : 'modules/payment/payment-new/payment-new.html',
				controller : 'payment-new'
			}
		}
	}).state('payments.view', {
		url : '/:id',
		views : {
			'sidebar@payments' : {
				templateUrl : 'modules/payment/payment/payment.html',
				controller : 'payment'
			}
		}
	});
} ])

.controller(
		'payments',
		[ '$scope', '$stateParams', '$filter', 'GeneralRestService', '$log',
				function($scope, $stateParams, $filter, GeneralRestService, $log) {
					'use strict';

					$scope.filters = {
							'bank': {
								'identifier': ''
							}
					};

					$scope.currentPage = 1;
					$scope.pageSize = 10;
					$scope.data = GeneralRestService;
					$scope.graph2d;
					
					$scope.refresh = function() {
						$scope.data.query({
							section : 'payment'
						}, function (data) {
							$scope.refreshPaymentsChart();
			            });
						
					};

					$scope.refresh();
					
					$scope.filter = function(key, value) {
						if(key == 'bank.title'){
							$scope.filters.bank.title = value;
						} else {
							$scope.filters[key] = value;
						}
						$log.debug("Filters: "+$scope.filters);
					};
					
					//Prepare data for chart
					$scope.prepareDataForChart = function() {
						var dataArray = [];
						var filteredData = $filter('filter')($scope.data.payment, $scope.filters);
						if (filteredData !== undefined) {
							var paymentsArrayLength = filteredData.length;
							for (var i = 0; i < paymentsArrayLength; i++) {
								//TODO: Upravit, aby zobrazovalo stavy účtů (sumu přírůstků/úbytků)
								var newData = {
										y: filteredData[i].amount,
										x: filteredData[i].paymentDate
								}
								dataArray.push(newData);
							}
						}
						return dataArray;
					};
					
					$scope.refreshPaymentsChart = function() {
						var container = document.getElementById('paymentsChart');
						  var items = $scope.prepareDataForChart();

						  var dataset = new vis.DataSet(items);
						  var options = {
//						    start: '2014-06-10',
//						    end: '2014-06-18'
						  };
						  if ($scope.graph2d === undefined){
							  $scope.graph2d = new vis.Graph2d(container, dataset, options);
						  } else {
							  $scope.graph2d.setItems(dataset);
						  }
						  
					};

				} ])


.filter('startFrom', [ function() {
	'use strict';
	return function(input, value) {
		if (!input || !input.length) { 
			return; 
		}
		return input ? input.slice(value) : input;
	};
} ])
;
