angular.module('sauronApp').controller('WorklogCtrl'
    , function ($scope, $rootScope, $routeParams, $location, RESTService, $filter) {

        init();

        function init() {
            setVisible(false);
            refresh();
        }

        function setVisible(visible) {
            $scope.visible = visible;
        }

        function refresh() {

            var fdesde = moment('2014/12/15');
            var fhasta = moment('2014/12/19');

            var cal = {};
            var fecha = moment(fdesde);

            while(!fecha.isAfter(fhasta)) {
                if(fecha.weekday() < 5) cal[fecha.format('YYYYDDMM')] = 0;
                fecha.add(1,'days');
            }

            RESTService.users().then(function (users) {

                var data = {};

                angular.forEach(users,function(u){
                    data[u.displayName] = angular.copy(cal);
                });

                RESTService.worklogs(fdesde,fhasta).then(function (issues) {

                    angular.forEach(issues,function(i){
                        var issue = i.fields;
                        issue.issuekey = i.key;
                        if(issue.project.key == 'GI') {
                            console.log(issue.worklog);
                        }
                        var worklogs = issue.worklog.worklogs;

                        //console.log(worklogs);
                        angular.forEach(worklogs,function(w){
                            var fecha = moment(new Date(w.created));

                            if(!fecha.isBefore(fdesde) && !fecha.isAfter(fhasta) && fecha.weekday() < 5) {

                                if(w.author.displayName == 'Carlos Prieto') {
                                    console.log("Siii ", w);
                                }

                                if(data.hasOwnProperty(w.author.displayName)) {
                                    var ss = data[w.author.displayName][fecha.format('YYYYDDMM')];
                                    ss = ss + w.timeSpentSeconds;
                                    data[w.author.displayName][fecha.format('YYYYDDMM')] = ss;
                                }
                            }
                            else {
                                if(w.author.displayName == 'Carlos Prieto') {
                                    //console.log("Noooo ", issue.issuekey, " ", w.created, " ", w.updated, " ", w.started);
                                }
                            }
                        });

                    });

                    console.log(data);

                    setVisible(true);
                });

            });
        };


    });
