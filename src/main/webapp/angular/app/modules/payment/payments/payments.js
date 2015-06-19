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
								'title': ''
							}
					};

					$scope.currentPage = 1;
					$scope.pageSize = 10;
					$scope.data = GeneralRestService;
					
					$scope.refresh = function() {
						$scope.data.query({
							section : 'payment'
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
