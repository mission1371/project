/**
 * Created by umut.taherzadeh on 2016-12-26.
 */
comparatorApp.config(function ($routeProvider, $locationProvider) {
    $locationProvider.html5Mode(true);
    var PREFIX = "/views/";
    $routeProvider
        .when('/', {
            templateUrl: PREFIX + 'index.view.html',
            controller: 'indexCtrl'
        })
        .when('/java', {
            templateUrl: PREFIX + 'java.view.html',
            controller: 'javaCtrl'
        })
        .when('/node', {
            templateUrl: PREFIX + 'node.view.html',
            controller: 'nodeCtrl'
        })
        .when('/php', {
            templateUrl: PREFIX + 'php.view.html',
            controller: 'phpCtrl'
        })
        .when('/python', {
            templateUrl: PREFIX + 'python.view.html',
            controller: 'pythonCtrl'
        })
        .otherwise({
            redirectTo : "/"
        })
});