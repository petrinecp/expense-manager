/**
 * @module user new
 * @summary user new module
 */

/*globals window, angular, document */

angular.module('user-new', [
    'ui.router'
])
    .controller('user-new', ['$scope', '$state', 'GeneralRestService', function ($scope, $state, GeneralRestService) {
        'use strict';
        
        $scope.data = GeneralRestService;
        $scope.accordianOpen1 = true;
        $scope.accordianOpen2 = false;

        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.post({section: 'user', id: 0}, function (data) {
                $scope.isDisabled = false;
				$scope.data.user.push(data);
                $state.go('users');
            }, function (data) {
                $scope.isDisabled = false;
			});
        };
    }]);