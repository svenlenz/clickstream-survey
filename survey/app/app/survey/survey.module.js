angular.module("app.survey", ['constants', 'app.common'])
    .controller("surveyController", ['$scope', 'surveyFactory', 'shortSurvey', 'fullSurvey', '$state', function ($scope, surveyFactory, shortSurvey, fullSurvey, $state) {


    //var surveyJSON = shortSurvey;
    var surveyJSON = fullSurvey;

    Survey.defaultBootstrapCss.navigationButton = "btn btn-primary";
    Survey.Survey.cssType = "bootstrap";
    var myCss = {
            matrix: {root: "table table-striped"},
            navigationButton: "button btn-lg"
       };

        function guid() {
          function s4() {
            return Math.floor((1 + Math.random()) * 0x10000)
              .toString(16)
              .substring(1);
          }
          return s4() + s4() + '-' + s4() + '-' + s4() + '-' +
            s4() + '-' + s4() + s4() + s4();
        }

        try {
            var test = divolte;
        }
        catch(e) {
            divolte = {
              sessionId: guid(),
              signal: function() {}
            };
        }

        function sendDataToServer(survey) {

          var surveyData = angular.copy(survey.data);
          if(angular.isDefined(divolte)) {
            surveyData.sessionId = divolte.sessionId;
          } else {
            surveyData.sessionId = guid();
          }

          var resultAsString = JSON.stringify(surveyData);

          if(angular.isDefined(divolte)) {
            console.log(divolte)
            console.log('id: ' + divolte.sessionId)
            console.log(resultAsString)
            divolte.signal('myCustomEvent', survey.data)
          }

          surveyFactory.send(function (response) {
              console.log("login response -=====>>" + response);
              // alert("response.length "+response.length);
              if (response.length = 1) {
                  $state.go('clickstream');
              }
              else {
                  $('.ErrorMsg').css('display', 'block');
              }
          }, surveyData);
        }

        var survey = new Survey.Model(surveyJSON);
        survey.showProgressBar = "bottom";
        survey.onComplete.add(sendDataToServer);
        Survey.SurveyNG.render("surveyElement", {model:survey, css: myCss});


        $scope.submit = function () {
            $state.go('clickstream');
        };
    }])