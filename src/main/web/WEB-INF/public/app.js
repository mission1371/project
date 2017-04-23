/**
 * Created by umut.taherzadeh on 2016-12-26.
 */
var comparatorApp = angular.module('comparatorModule', ['ngRoute']);


comparatorApp.controller("indexCtrl", function ($scope, $http) {

    $scope.type = "index";

    $scope.asd = function () {
        debugger;

        // method: 'POST',
        //     url: 'https://admin.example.com/api/v1/service',
        //
        // data: { test: 'test' }


        $http(
            {
                method: 'GET',
                url : "v1/service/database/createOperation",
                headers: {
                    'Content-Type': 'application/json'
                }
            }
        ).success(function(resp) {
            debugger;
        }).error(function(err) {

        })


        //
        // $http.get("v1/service/database/createOperation", {
        //     headers : {
        //         'Content-Type': 'application/json'
        //     }
        // }).success(function(resp) {
        //     debugger;
        // }).error(function(err) {
        //
        // })

    }
});
