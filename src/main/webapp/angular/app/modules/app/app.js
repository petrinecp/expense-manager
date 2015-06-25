/**
 * @module App
 * @summary First module loaded
 */

/*globals window, angular, document */
var app = angular.module('app', [
    'ngResource',
    'ui.router',
    'ui.bootstrap',
    'ngWebSocket',
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

    .controller('app', ['$rootScope', '$scope', '$state', 'authFactory', 'GeneralRestService', 'webSocketFactory', function ($rootScope, $scope, $state, authFactory, GeneralRestService, webSocketFactory) {
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
        
        $scope.webSocketFactory = webSocketFactory;

        $scope.sendWebSocket = function (message) {
        	var userName = authFactory.getUserName();
    		var authToken = authFactory.getAuthData().authToken;
    		webSocketFactory.send("{\"timestamp\":\"" + new Date() + "\",\"userId\":\""+userName+"\",\"token\":\""+authToken+"\",\"action\":\"Click on login button\"}");
        };     		
		
        $scope.login = function (authLoginElement) {
            authFactory.login(authLoginElement).success(function (data) {
                authFactory.setAuthData(data);
                $rootScope.user = data;
                $scope.loggedIn = true;
                $scope.sendWebSocket();
                if ($rootScope.user.image === undefined || $rootScope.user.image == null){
                	$rootScope.user.image = "img/blank.jpg";
                }
                $scope.data.errorMessages = [];
                $state.go('payments');
            }).error(function () {
            	$scope.loggedIn = false;
            	$scope.data.errorMessages = [{"field":"Login fail","message":"please check your name and password"}];
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
            return $http.post('rest/auth/login', authLoginElement);
        };
		
		authFactory.logout = function () {
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
    

    .factory(
		'webSocketFactory',
		function($websocket, $log, $injector) {
			// Open a WebSocket connection
			var ws = $websocket("wss://" + document.location.host + "/expense-manager/websocket");
			var wsc = [];

			ws.onMessage(function(event) {
				$log.info('message: ', event.data);
				var response;
				try {
					response = angular.fromJson(event.data);
				} catch (e) {
					$log.error('error: ', e);
					response = {
						'error' : e
					};
				}
			});
			ws.onError(function(event) {
				$log.error('connection Error', event);
			});
			ws.onClose(function(event) {
				$log.info('connection closed', event);
			});
			ws.onOpen(function() {
				$log.info('connection open');
				var authFactory = $injector.get('authFactory');
				var userName = authFactory.getUserName();
				var authToken = authFactory.getAuthData().authToken;
				ws.send("{\"timestamp\":\"" + new Date() + "\",\"userId\":\""+userName+"\",\"token\":\""+authToken+"\",\"action\":\"New webSocket connection opened.\"}");
			});

			return {
				wsc : wsc,
				status : function() {
					return ws.readyState;
				},
				send : function(message) {
					if (angular.isString(message)) {
						ws.send(message);
					} else if (angular.isObject(message)) {
						ws.send(JSON.stringify(message));
					}
				}
			};
		})

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
;
