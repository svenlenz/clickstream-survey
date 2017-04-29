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

        function sendDataToServer(survey) {

          var surveyData = angular.copy(survey.data);
          surveyData.sessionId = divolte.sessionId;
          var resultAsString = JSON.stringify(surveyData);

          console.log(divolte)
          console.log('id: ' + divolte.sessionId)
          console.log(resultAsString)
          divolte.signal('myCustomEvent', survey.data)

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
            divolte.signal('myCustomEvent', { param: 'foo',  otherParam: 'bar' })
            $state.go('clickstream');
        };
    }])