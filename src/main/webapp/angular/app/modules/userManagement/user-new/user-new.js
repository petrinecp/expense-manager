/**
 * @module user new
 * @summary user new module
 */

/*globals window, angular, document */

angular.module('user-new', [
    'ui.router'
])
    .controller('user-new', ['$scope', '$state', 'Data', function ($scope, $state, Data) {
        'use strict';
        
        $scope.data = Data;
        $scope.data.users_new = {
            date: new Date()
        };
        $scope.accordianOpen1 = true;
        $scope.accordianOpen2 = false;

        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.post({section: 'users', id: 0}, function (data) {
                $scope.isDisabled = false;
                $state.go('users');
            });
        };
    }]);