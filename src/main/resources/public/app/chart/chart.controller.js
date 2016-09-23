(function () {
    'use strict';

    angular
        .module('testhighcharts')
        .controller('ChartController', ChartController);

    function ChartController(HighChartService) {
        var chartVm = this;
        console.log('Inside Home Controller');

        function getAllLeave() {
            console.log('inside get all leave function');
            HighChartService.fetchAllLeaves()
                .then(
                    function (result) {
                        console.log('result: ', result);
                    },
                    function (error) {
                        console.log('error: ', error);
                    }
                );

            HighChartService.fetchLeavesById(2)
                .then(
                    function (result) {
                        console.log('result by id: ', result);
                    },
                    function (error) {
                        console.log('error: ', error);
                    }
                );
        }

        getAllLeave();

        Highcharts.chart('highchartContainer', {

            xAxis: {
                categories: ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun',
                    'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
            },

            series: [{
                data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0, 135.6, 148.5, 216.4, 194.1, 95.6, 54.4]
            }]
        });
    }
}());