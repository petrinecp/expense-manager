/**
 * @module user
 * @summary user module
 */

/*globals window, angular, document */

angular.module('user', [
    'ui.router'
])
    .controller('user', ['$rootScope', '$scope', '$stateParams', 'GeneralRestService', '$state', function ($rootScope, $scope, $stateParams, GeneralRestService, $state) {
        'use strict';
        
        $scope.data = GeneralRestService;
        $scope.editable = false;
        $scope.num = false;
        
        if ($stateParams.id) {
            $rootScope.id = Number($stateParams.id);
            $scope.data.get({section: 'user', id: $rootScope.id}, function (data) {
                $scope.user = data;
            });
        }
        
        $scope.edit = function () {
            $scope.editable = true;
        };
        
        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.update({section: 'user', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
				$state.go('users');
                $scope.data.query({
					section : 'user'
				});
            });
        };

        $scope.remove = function () {
            $scope.isDisabled = true;
            $scope.data.remove({section: 'user', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
                $state.go('users');
                $scope.data.query({
					section : 'user'
				});
            });
        };
        
        $scope.addSection = function () {
            var copy = angular.copy($scope.user.sections[$scope.user.sections.length - 1]);
            $scope.user.sections.push(copy);
        };
        
        $scope.removeSection = function () {
            $scope.user.sections.splice($scope.user.sections.length - 1, $scope.user.sections.length);
        };
    }])