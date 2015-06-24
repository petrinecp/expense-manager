/**
 * @module Users
 * @summary Users module
 */

/* globals window, angular, document */

angular.module('users', [ 'ngResource', 'ui.bootstrap', 'ui.router' ])

.config([ '$stateProvider', function($stateProvider) {
	'use strict';

	$stateProvider.state('users', {
		url : '/users',
		views : {
			'main@' : {
				templateUrl : 'modules/userManagement/users/users.html'
			}
		}
	}).state('users.new', {
		url : '/new',
		views : {
			'sidebar@users' : {
				templateUrl : 'modules/userManagement/user-new/user-new.html',
				controller : 'user-new'
			}
		}
	}).state('users.view', {
		url : '/:id',
		views : {
			'sidebar@users' : {
				templateUrl : 'modules/userManagement/user/user.html',
				controller : 'user'
			}
		}
	});
} ])

.controller(
		'users',
		[ '$scope', '$stateParams', '$filter', 'GeneralRestService', '$log',
				function($scope, $stateParams, $filter, GeneralRestService, $log) {
					'use strict';

					$scope.filters = {
//						id : '',	
//						name : '',
//						forname: '',
//						userName: '',
					    authRole : '',
//					    passwd : ''
					};

					$scope.currentPage = 1;
					$scope.pageSize = 10;
					$scope.data = GeneralRestService;

					$scope.refresh = function() {
						$scope.data.query({
							section : 'user'
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
