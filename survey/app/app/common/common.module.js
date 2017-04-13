angular.module('app.common', [])

    .factory('GetJSonMI',['$http', function ($http){
    var allData= null;
    var getallData = function(callBack){

        $http.get('/poc-backend/MI?').
            then(function(respond){
                allData = angular.fromJson(respond.data);
                console.log(allData);
                callBack(allData);
            }, function(respond) {
            })
    }

    return {
        getAllLocations: getallData
    }

}])
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

}]);
