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
						name : '',
						role : ''
					};

					$scope.currentPage = 1;
					$scope.pageSize = 10;
					$scope.data = GeneralRestService;
					$scope.data.query({
						section : 'members'
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
							section : 'members'
						});
						$log.info($scope.data);
						$log.info($scope.data.query);
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
