/**
 * @module App
 * @summary First module loaded
 */

/*globals window, angular, document */
var app = angular.module('app', [
    'ngResource',
    'ui.router',
    'ui.bootstrap',
    'item',
    'item-new',
    'items',
    'overlay',
    'users',
    'user',
    'user-new',
    'categories',
    'category',
    'category-new',
    'rules',
    'rule',
    'rule-new',
    'payments',
    'payment',
    'payment-new'
])

    .config(['$stateProvider', function ($stateProvider) {
        'use strict';

        $stateProvider
            .state('app', {
                url: '',
                views: {
                    'main': {
                        templateUrl: 'modules/app/app.html'
                    }
                }
            });
    }])
    
    .config(function ($httpProvider) {
	    $httpProvider.interceptors.push('authHttpRequestInterceptor');
	    $httpProvider.interceptors.push('httpErrorInterceptor');
	})

    .controller('app', ['$rootScope', '$scope', '$state', 'authFactory', 'GeneralRestService', function ($rootScope, $scope, $state, authFactory, GeneralRestService) {
        'use strict';
        
        $scope.$state = $state;
        $scope.loggedIn = false;
        $scope.navbarCollapsed = true;
        $scope.authFactory = authFactory;
        $scope.authLoginElement = {
        	"username": "",
        	"password": ""
        };
        $scope.data = GeneralRestService;
        
        $scope.login = function (authLoginElement) {
            authFactory.login(authLoginElement).success(function (data) {
                authFactory.setAuthData(data);
                $rootScope.user = data;
                $scope.loggedIn = true;
                $state.go('payments');
            }).error(function () {
            	$scope.loggedIn = false;
            	$state.go('app');
            });
        };
        
        $scope.logout = function () {
            authFactory.logout().success(function (data) {
                authFactory.cleanAuthData();
                $rootScope.user = {};
                $scope.loggedIn = false;
                $state.go('app');
            }).error(function () {
            });
        };
    }])

	.factory('GeneralRestService', ['$rootScope', '$resource', function ($rootScope, $resource) {
        'use strict';
        return {
            resource: $resource('rest/:section', null, {
                get: { method: 'GET', isArray: false, params: { section: '@section', id: '@id' }, url: 'rest/:section/:id' },
                post: { method: 'POST', isArray: false, params: { section: '@section', id: '@id' }, url: 'rest/:section' },
                query: { method: 'GET', isArray: true, params: { section: '@section', id: '@id' } },
                update: { method: 'POST', isArray: false, params: { section: '@section', id: '@id' }, url: 'rest/:section/:id' },
                remove: { method: 'DELETE', isArray: false, params: { section: '@section', id: '@id' }, url: 'rest/:section/:id' }
            }),
            /**
             * @method get
             * Single item
             */
            get: function (params, callback, error) {
                var me = this;
                me.deleteErrors();
                //console.log('get', params);
                me.resource.get(params, null, function (value) {
                    me.setData(value, params.section, params.id);
                    if (callback) { callback(value); }
                }, error);
            },
            /**
             * @method post
             * Submit data
             */
            post: function (params, callback, error) {
                var me = this;
                me.deleteErrors();
                //console.log('post', params);
                me.resource.post(params, me.getData(params.section, params.id), function (value) {
                    //me.setData(value, params.section, params.id); // do not save post data
                    if (callback) { callback(value); }
                }, error);
            },
            /**
             * @method update
             * Update data (send post to address <url>/<id>)
             */
            update: function (params, callback, error) {
                var me = this;
                me.deleteErrors();
                //console.log('post', params);
                me.resource.update(params, me.getData(params.section, params.id), function (value) {
                    //me.setData(value, params.section, params.id); // do not save post data
                    if (callback) { callback(value); }
                }, error);
            },
            /**
             * @method remove
             * Delete data (send "delete" to address <url>/<id>)
             */
            remove: function (params, callback, error) {
                var me = this;
                me.deleteErrors();
                //console.log('delete', params);
                me.resource.remove(params, null, function (value) {
                    //me.setData(value, params.section, params.id); // do not save post data
                    if (callback) { callback(value); }
                }, error);
            },
            /**
             * @method query
             * List of items
             */
            query: function (params, callback, error) {
                var me = this;
                me.deleteErrors();
                //console.log('query', params);
                me.resource.query(params, null, function (value) {
                    me.setData(value, params.section, params.id);
                    if (callback) { callback(value); }
                }, error);
            },
            /**
             * @method setData
             * Cache the items for sharing between controllers
             */
            setData: function (value, section, id) {
                if (typeof id === 'number') {
                    if (!this[section + '_detail']) {
                        this[section + '_detail'] = {};
                    }
                    this[section + '_detail'][id] = value;
                } else {
                    this[section] = value;
                }
            },
            /**
             * @method getData
             * Get cached data object
             */
            getData: function (section, id) {
                if (typeof id === 'number') {
                    if (id === 0) {
                        return this[section + '_new'];
                    } else {
                        return this[section + '_detail'][id];
                    }
                } else {
                    return this[section];
                }
            },
            /**
             * Delete error messages
             */
            deleteErrors: function (section, id) {
                this.errorMessages = [];
            }
        };
    }])
    
    .factory('authFactory', ['$rootScope', '$http', function ($rootScope, $http) {

        var authFactory = {
            authData: undefined
        };

        authFactory.setAuthData = function (authData) {
            this.authData = {
                authId: authData.authId,
                authToken: authData.authToken,
                authPermission: authData.authPermission
            };
            $rootScope.$broadcast('authChanged');
        };
        
        authFactory.cleanAuthData = function () {
            this.authData = undefined;
        };

        authFactory.getAuthData = function () {
            return this.authData;
        };
        
        authFactory.getUserName = function () {
        	if (this.isAuthenticated()){
        		return this.getAuthData().authId;
        	} else {
        		return null;
        	}
        };

        authFactory.isAuthenticated = function () {
            return !angular.isUndefined(this.getAuthData());
        };
        
        authFactory.isAdmin = function () {
        	if (this.isAuthenticated() && this.getAuthData().authPermission == 'ADMIN'){
        		return true
        	} else {
        		return false
        	}
        };
        
        authFactory.isBasicUser = function () {
        	if (this.isAuthenticated() && this.getAuthData().authPermission == 'BASIC_USER'){
        		return true
        	} else {
        		return false
        	}
        };
        
        authFactory.isPrivilegedUser = function () {
        	if (this.isAuthenticated() && this.getAuthData().authPermission == 'PRIVILEGED_USER'){
        		return true
        	} else {
        		return false
        	}
        };
        
        authFactory.login = function (authLoginElement) {
            var webSocket = new WebSocket('wss://localhost:8443/expense-manager/websocket');
    		webSocket.send("{\"timestamp\":"+new Date()+",\"userId\":2,\"token\":\"test\",\"action\":\"Click on login button\"}");
    		
            return $http.post('rest/auth/login', authLoginElement);
        };
		
		authFactory.logout = function () {
			var webSocket = new WebSocket('wss://localhost:8443/expense-manager/websocket');
    		webSocket.send("{\"timestamp\":"+new Date()+",\"userId\":2,\"token\":\"test\",\"action\":\"Click on logout button\"}");
    		
            return $http.post('rest/auth/logout');
        };

        return authFactory;
    }])
    
    
    .factory('authHttpRequestInterceptor', ['$rootScope', '$injector', function ($rootScope, $injector) {
    	var authHttpRequestInterceptor = {
	        request: function ($request) {
	            var authFactory = $injector.get('authFactory');
	            if (authFactory.isAuthenticated()) {
	                $request.headers['auth-id'] = authFactory.getAuthData().authId;
	                $request.headers['auth-token'] = authFactory.getAuthData().authToken;
	            }
	            return $request;
	        }
        };
    	return authHttpRequestInterceptor;
    }])
    
    .factory('httpErrorInterceptor', ['$rootScope', '$injector', '$q', function ($rootScope, $injector, $q) {
    	var errorHttpRequestInterceptor = {
    		responseError: function ($rejection) {
	            var data = $injector.get('GeneralRestService');
	            data.errorMessages = $rejection.data.fieldErrors;
	            return $q.reject($rejection);
	        }
        };
    	return errorHttpRequestInterceptor;
    }])
    
//    .factory('Data', ['$rootScope', '$resource', function ($rootScope, $resource) {
//        'use strict';
//        return {
//            resource: $resource('data/:section.json', null, {
//                get: { method: 'GET', isArray: false, params: { section: '@section', id: '@id' }, url: 'data/:section/:id.json' },
//                post: { method: 'POST', isArray: false, params: { section: '@section', id: '@id' }, url: 'data/:section/:id.json' },
//                query: { method: 'GET', isArray: true, params: { section: '@section', id: '@id' } }
//            }),
//            /**
//             * @method get
//             * Single item
//             */
//            get: function (params, callback, error) {
//                var me = this;
//                //console.log('get', params);
//                me.resource.get(params, null, function (value) {
//                    me.setData(value, params.section, params.id);
//                    if (callback) { callback(value); }
//                }, error);
//            },
//            /**
//             * @method post
//             * Submit data
//             */
//            post: function (params, callback, error) {
//                var me = this;
//                //console.log('post', params);
//                me.resource.post(params, me.getData(params.section, params.id), function (value) {
//                    //me.setData(value, params.section, params.id); // do not save post data
//                    if (callback) { callback(value); }
//                }, error);
//            },
//            /**
//             * @method query
//             * List of items
//             */
//            query: function (params, callback, error) {
//                var me = this;
//                //console.log('query', params);
//                me.resource.query(params, null, function (value) {
//                    me.setData(value, params.section, params.id);
//                    if (callback) { callback(value); }
//                }, error);
//            },
//            /**
//             * @method setData
//             * Cache the items for sharing between controllers
//             */
//            setData: function (value, section, id) {
//                if (typeof id === 'number') {
//                    if (!this[section + '_detail']) {
//                        this[section + '_detail'] = {};
//                    }
//                    this[section + '_detail'][id] = value;
//                } else {
//                    this[section] = value;
//                }
//            },
//            /**
//             * @method getData
//             * Get cached data object
//             */
//            getData: function (section, id) {
//                if (typeof id === 'number') {
//                    if (id === 0) {
//                        return this[section + '_new'];
//                    } else {
//                        return this[section + '_detail'][id];
//                    }
//                } else {
//                    return this[section];
//                }
//            }
//        };
//    }])

    .filter('trusted', ['$sce', function ($sce) {
        'use strict';
        
        return function (url) {
            return $sce.trustAsResourceUrl(url);
        };
    }])

    .directive('selectOnClick', function () {
        'use strict';
        
        return {
            restrict: 'A',
            link: function (scope, element, attrs) {
                element.on('click', function () {
                    this.select();
                });
            }
        };
    })

//    .directive('focusPoint', function () {
//        'use strict';
//        
//        return {
//            restrict: 'A',
//            replace: true,
//            scope: {
//                model: '=ngModel'
//            },
//            template: '<div class="focus-point">' +
//                        '<div class="focus-area">' +
//                            '<span class="target" style="left: {{ x }}%; top: {{ y }}%"></span>' +
//                            '<img src="{{ src }}" alt="" ng-mousemove="onMouseMove($event)" ng-mousedown="onMouseDown($event)" ng-mouseup="onMouseUp($event)" draggable="false" class="source" />' +
//                        '</div>' +
//                        '<span style="background-image: url(\'{{ src }}\'); background-position: {{ x }}% {{ y }}%" class="preview-portrait"></span>' +
//                        '<span style="background-image: url(\'{{ src }}\'); background-position: {{ x }}% {{ y }}%" class="preview"></span>' +
//                        '<span style="background-image: url(\'{{ src }}\'); background-position: {{ x }}% {{ y }}%" class="preview-landscape"></span>' +
//                        '<span style="background-image: url(\'{{ src }}\'); background-position: {{ x }}% {{ y }}%" class="preview-wide"></span>' +
//                        '<div class="info">{{ x }}% / {{ y }}%</div>' +
//                       '</div>',
//            link: function (scope, element, attr) {
//                var dragging = false;
//                scope.src = attr.src;
//
//                scope.onMouseDown = function (e) {
//                    scope.update(e);
//                    dragging = true;
//                };
//
//                scope.onMouseMove = function (e) {
//                    if (dragging === true) {
//                        scope.update(e);
//                    }
//                };
//
//                scope.onMouseUp = function (e) {
//                    e.preventDefault();
//                    dragging = false;
//                };
//                
//                scope.update = function (e) {
//                    e.preventDefault();
//                    var offset = scope.offset(e.target);
//                    scope.x = Math.round(((e.pageX - offset.left) / e.target.clientWidth) * 100);
//                    scope.y = Math.round(((e.pageY - offset.top) / e.target.clientHeight) * 100);
//                };
//                
//                scope.offset = function (elm) {
//                    try { return elm.offset(); } catch (e) { }
//                    var body = document.documentElement || document.body;
//                    return {
//                        left: elm.getBoundingClientRect().left + (window.pageXOffset || body.scrollLeft),
//                        top: elm.getBoundingClientRect().top + (window.pageYOffset || body.scrollTop)
//                    };
//                };
//            }
//        };
//    })
    ;
