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
	}).state('categories.new', {
		url : '/new',
		views : {
			'sidebar@categories' : {
				templateUrl : 'modules/category/category-new/category-new.html',
				controller : 'category-new'
			}
		}
	}).state('categories.view', {
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
					};

					$scope.currentPage = 1;
					$scope.pageSize = 10;
					$scope.data = GeneralRestService;
					
					$scope.refresh = function() {
						$scope.data.query({
							section : 'category'
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
