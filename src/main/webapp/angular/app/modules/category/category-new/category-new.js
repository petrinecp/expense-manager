/**
 * @module category new
 * @summary category new module
 */

/*globals window, angular, document */

angular.module('category-new', [
    'ui.router'
])
    .controller('category-new', ['$scope', '$state', 'GeneralRestService', function ($scope, $state, GeneralRestService) {
        'use strict';
        
        $scope.data = GeneralRestService;
        $scope.data.errorMessages = [];
        $scope.accordianOpen1 = true;
        $scope.accordianOpen2 = false;

        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.post({section: 'category', id: 0}, function (data) {
                $scope.isDisabled = false;
                $scope.data.category.push(data);
                $state.go('categories');
            }, function (data) {
                $scope.isDisabled = false;
			});
        };
    }]);