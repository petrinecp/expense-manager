/**
 * @module user
 * @summary user module
 */

/*globals window, angular, document */

angular.module('category', [
    'ui.router'
])
    .controller('category', ['$rootScope', '$scope', '$stateParams', 'DataFromServer', function ($rootScope, $scope, $stateParams, DataFromServer) {
        'use strict';
        
        $scope.data = DataFromServer;
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
            $scope.data.post({section: 'user', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
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