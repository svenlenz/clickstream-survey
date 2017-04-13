angular.module('app.common', [])

.factory('surveyFactory',['$http', function ($http){

    var returned= null;
    var send = function(callBack, data){
        $http.post('/survey', data).
            then(function(respond){
                returned = respond.data;
                callBack(returned);
            }, function(respond) {
            })
    }

    return {
        send: send
    }

}])

.factory('clickstreamFactory',['$http', function ($http){

    var returned= null;
    var send = function(callBack, data){
        $http.post('/clickstream', data).
            then(function(respond){
                returned = respond.data;
                callBack(returned);
            }, function(respond) {
            })
    }

    return {
        send: send
    }

}]);
