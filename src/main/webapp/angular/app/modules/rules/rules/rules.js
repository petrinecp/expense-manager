/**
 * @module rules
 * @summary rules module
 */

/* globals window, angular, document */

angular.module('rules', [ 'ngResource', 'ui.bootstrap', 'ui.router' ])

.config([ '$stateProvider', function($stateProvider) {
	'use strict';

	$stateProvider.state('rules', {
		url : '/rules',
		views : {
			'main@' : {
				templateUrl : 'modules/rules/rules/rules.html'
			}
		}
	}).state('rules.new', {
		url : '/new',
		views : {
			'sidebar@rules' : {
				templateUrl : 'modules/rules/rule-new/rule-new.html',
				controller : 'rule-new'
			}
		}
	}).state('rules.view', {
		url : '/:id',
		views : {
			'sidebar@rules' : {
				templateUrl : 'modules/rules/rule/rule.html',
				controller : 'rule'
			}
		}
	});
} ])

.controller(
		'rules',
		[ '$scope', '$stateParams', '$filter', 'GeneralRestService', '$log',
				function($scope, $stateParams, $filter, GeneralRestService, $log) {
					'use strict';
					
					$scope.data = GeneralRestService;
					$scope.data.errorMessages = [];
					
					$scope.filters = {
					};

					$scope.currentPage = 1;
					$scope.pageSize = 10;
					
					
					$scope.refresh = function() {
						$scope.data.query({
							section : 'rule'
						});
					};

					$scope.refresh();
					
					$scope.filter = function(key, value) {
						$scope.filters[key] = value;
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
