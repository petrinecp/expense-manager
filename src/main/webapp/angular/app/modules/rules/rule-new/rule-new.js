/**
 * @module rule new
 * @summary rule new module
 */

/*globals window, angular, document */

angular.module('rule-new', [
    'ui.router'
])
    .controller('rule-new', ['$scope', '$state', 'GeneralRestService', function ($scope, $state, GeneralRestService) {
        'use strict';
        
        $scope.data = GeneralRestService;
        $scope.data.errorMessages = [];
        $scope.accordianOpen1 = true;
        $scope.accordianOpen2 = false;
        
        $scope.data.query({section : 'category'});

        $scope.save = function () {
            $scope.isDisabled = true;
            $scope.data.post({section: 'rule', id: 0}, function (data) {
                $scope.isDisabled = false;
                $scope.data.rule.push(data);
                $state.go('rules');
            }, function (data) {
                $scope.isDisabled = false;
			});
        };
    }]);