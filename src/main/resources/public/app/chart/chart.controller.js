(function () {
    'use strict';

    angular
        .module('testhighcharts')
        .controller('ChartController', ChartController);

    function ChartController(HighChartService) {
        var chartVm = this;
        console.log('Inside Home Controller');
        console.log('inside get all leave function');

        HighChartService
            .fetchAllEmployees()
            .then(function (result) {
                    chartVm.employees = _.map(result, 'firstName');
                    chartVm.ids = _.map(result, 'id');
                    _.forEach(chartVm.ids, function(id) {
                       HighChartService.fetchLeavesByEmployeeId(id)
                           .then(function (result) {
                               console.log('id: ', id);
                               console.log('Leave Result for id: ', result);
                           })
                    });
                    console.log('Employees', chartVm.employees);
                    Highcharts.chart('highchartContainer', {
                        xAxis: {
                            categories: chartVm.employees
                        },
                        series: [{
                            data: [29.9, 71.5, 106.4, 129.2, 144.0, 176.0]
                        }]
                    });
                }
            )
    }
}());