(function () {
    'use strict';

    angular
        .module('testhighcharts')
        .controller('ChartController', ChartController);

    function ChartController(HighChartService) {
        var chartVm = this;
        chartVm.input = [];
        chartVm.names = [];
        chartVm.totalLeaves = [];

        HighChartService
            .fetchAllEmployees()
            .then(function (result) {
                _.forEach(result, function (value) {
                    HighChartService
                        .fetchLeavesByEmployeeId(value.id)
                        .then(function (result) {
                            _.forEach(result, function (value) {
                                createInput(value.employee.firstName, value.noOfDays);
                            });
                            Highcharts.chart('highchartContainer', {
                                xAxis: {
                                    categories: chartVm.names
                                },
                                series: [{
                                    data: chartVm.totalLeaves
                                }]
                            });
                        }, function (error) {

                        });
                });
            }, function (error) {

            });

        function createInput(firstName, noOfDays) {
            var key = _.findKey(chartVm.input, {'name': firstName});
            if (key) {
                chartVm.input[key].totalLeaves += noOfDays;
                chartVm.totalLeaves[key] += noOfDays;
            } else {
                chartVm.input.push({name: firstName, totalLeaves: noOfDays});
                chartVm.names.push(firstName);
                chartVm.totalLeaves.push(noOfDays);
            }
        }
    }
}());