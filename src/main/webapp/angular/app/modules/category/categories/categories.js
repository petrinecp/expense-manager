/**
 * @module categories
 * @summary categories module
 */

/* globals window, angular, document */

angular.module('categories', [ 'ngResource', 'ui.bootstrap', 'ui.router' ])

.config([ '$stateProvider', function($stateProvider) {
	'use strict';

	$stateProvider.state('categories', {
		url : '/categories',
		views : {
			'main@' : {
				templateUrl : 'modules/category/categories/categories.html'
			}
		}
	}).state('category.new', {
		url : '/new',
		views : {
			'sidebar@categories' : {
				templateUrl : 'modules/category/category-new/category-new.html',
				controller : 'category-new'
			}
		}
	}).state('category.view', {
		url : '/:id',
		views : {
			'sidebar@categories' : {
				templateUrl : 'modules/category/category/category.html',
				controller : 'category'
			}
		}
	});
} ])

.controller(
		'categories',
		[ '$scope', '$stateParams', '$filter', 'GeneralRestService', '$log',
				function($scope, $stateParams, $filter, GeneralRestService, $log) {
					'use strict';

					$scope.filters = {
						title : ''
					};

					$scope.currentPage = 1;
					$scope.pageSize = 10;
					$scope.data = GeneralRestService;
					$scope.data.query({
						section : 'category'
					});
					
			        //refresh users list
//			        $scope.refreshUsers = function () {
//			        	$scope.data.query({section : 'members'},
//			                    function (data, status, headers, config) {
//			        				$scope.data.query = data;
//			                        $log.info("List of users loaded.");
//			                    }, function (data, status, headers, config) {
//			                $log.error("An error occurred on server! List of users cannot be loaded.");
//			            });
//			        };
//			        $scope.refreshCities();

					$scope.filter = function(key, value) {
						$scope.filters[key] = value;
					};

					$scope.test = function() {
						$scope.data.query({
							section : 'category'
						});
						$log.info($scope.data);
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
