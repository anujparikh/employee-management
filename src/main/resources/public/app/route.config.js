(function () {
    'use strict';

    angular
        .module('testhighcharts')
        .config(moduleConfig);

    moduleConfig.$inject = ['$routeProvider'];

    function moduleConfig($routeProvider) {

        console.log('Inside App Config');

        $routeProvider
            .when('/chart', {
                templateUrl: 'app/chart/chart.html',
                controller: 'ChartController',
                controllerAs: 'chartVm'
            })
            .otherwise({
                redirectTo: '/chart'
            });
    }
}());