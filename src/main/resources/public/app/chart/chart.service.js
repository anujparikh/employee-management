(function () {
    'use strict';

    angular
        .module('testhighcharts')
        .factory('HighChartService', HighChartService);

    function HighChartService($q, $http) {
        return {
            fetchAllLeaves: fetchAllLeaves,
            fetchLeavesById: fetchLeavesById
        };

        function fetchLeavesById(id) {
            var defer = $q.defer();
            $http.get('/leave/' + id)
                .then(
                    function (response) {
                        console.log(response);
                        defer.resolve(response.data);
                    },
                    function (error) {
                        defer.reject(error.status);
                    }
                );
            return defer.promise;
        }

        function fetchAllLeaves() {
            var defer = $q.defer();
            $http.get('/leave/')
                .then(
                    function (response) {
                        console.log(response);
                        defer.resolve(response.data);
                    },
                    function (error) {
                        defer.reject(error.status);
                    }
                );
            return defer.promise;
        }
    }
}());
