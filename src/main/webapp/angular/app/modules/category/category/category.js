/**
 * @module category
 * @summary category module
 */

/*globals window, angular, document */

angular.module('category', [
    'ui.router'
])
    .controller('category', ['$rootScope', '$scope', '$stateParams', 'GeneralRestService', '$state', function ($rootScope, $scope, $stateParams, GeneralRestService, $state) {
        'use strict';
        
        $scope.data = GeneralRestService;
        $scope.editable = false;
        $scope.num = false;
        
    	if ($stateParams.id) {
            $rootScope.id = Number($stateParams.id);
            $scope.data.get({section: 'category', id: $rootScope.id}, function (data) {
                $scope.category = data;
            });
        }
        
        $scope.edit = function () {
            $scope.editable = true;
        };
        
        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.update({section: 'category', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
                $state.go('categories');
                $scope.data.query({
					section : 'category'
				});
            });
        };
        
        $scope.remove = function () {
            $scope.isDisabled = true;
            $scope.data.remove({section: 'category', id: $rootScope.id}, function (data) {
                $scope.isDisabled = false;
                $scope.editable = false;
                $state.go('categories');
                $scope.data.query({
					section : 'category'
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