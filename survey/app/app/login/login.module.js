angular.module("app.login", ['webServicesConstant', 'app.common'])
    .controller("loginController", ['$scope', 'checkLogin', 'wsConstant', '$state', function ($scope, checkLogin, wsConstant, $state) {


        var surveyJSON = {
 completedHtml: "<p><h4>Besten Dank für das Ausfüllen des ersten Teil.</h4></p><p>Die nächste Aufgabe ist das Klicken durch den Prototyp: <button type='button' class='btn btn-info btn-block' ng-click='submit()' >Zum Prototypen</button></p>",
 pageNextText: "Weiter",
 pagePrevText: "Zurück",
 pages: [
  {
   name: "Statistik",
   questions: [
    {
     type: "radiogroup",
     name: "Statistik",
     title: "Wie oft haben Sie schon an diesem Test teilgenommen?",
     isRequired: true,
     choices: [
      {
       value: "1",
       text: "Das ist mein erstes Mal"
      },
      {
       value: "2",
       text: "Das ist mein zweites Mal"
      },
      {
       value: "3",
       text: "Mehr als zwei Mal"
      }
     ]
    },
    {
     type: "radiogroup",
     name: "Ihr Geschlecht:",
     isRequired: true,
     choices: [
      {
       value: "W",
       text: "weiblich"
      },
      {
       value: "M",
       text: "männlich"
      }
     ]
    },
    {
     type: "radiogroup",
     name: "Wie alt sind Sie?",
     isRequired: true,
     choices: [
      {
       value: "1",
       text: "jünger als 20 Jahre"
      },
      {
       value: "2",
       text: "20 bis 30 Jahre"
      },
      {
       value: "3",
       text: "31 bis 40 Jahre"
      },
      {
       value: "4",
       text: "41 bis 50 Jahre"
      },
      {
       value: "5",
       text: "älter als 50 Jahre"
      }
     ]
    },
    {
     type: "radiogroup",
     name: "Ihre Berufsausbildung / Studium?",
     choices: [
      {
       value: "1",
       text: "Lehre"
      },
      {
       value: "2",
       text: "Meister"
      },
      {
       value: "3",
       text: "Diplom"
      },
      {
       value: "4",
       text: "Bachelor"
      },
      {
       value: "5",
       text: "Master"
      },
      {
       value: "6",
       text: "Promotion"
      },
      {
       value: "7",
       text: "anderes"
      }
     ]
    },
    {
     type: "radiogroup",
     name: "Was trifft auf Sie zu?",
     choices: [
      {
       value: "1",
       text: "Ich bin Student"
      },
      {
       value: "2",
       text: "Ich bin Arbeiter"
      },
      {
       value: "3",
       text: "Ich bin Angestellter"
      },
      {
       value: "4",
       text: "Ich bin Beamter"
      },
      {
       value: "5",
       text: "Ich bin selbständig"
      },
      {
       value: "6",
       text: "Ich bin arbeitslos"
      },
      {
       value: "7",
       text: "anderes"
      }
     ]
    }
   ],
   title: "Für statistische Zwecke"
  },
  {
   name: "Psycho",
   questions: [
    {
     type: "matrix",
     name: "Inwieweit treffen die folgenden Aussagen auf Sie zu? Antworten Sie möglichst spontan. Es gibt keine richtigen oder falschen Antworten.",
     isRequired: true,
     columns: [
      {
       value: "0",
       text: "trifft gar nicht zu"
      },
      {
       value: "1",
       text: "trifft eher nicht zu"
      },
      {
       value: "2",
       text: "trifft eher zu"
      },
      {
       value: "3",
       text: "trifft genau zu"
      }
     ],
     rows: [
      {
       value: "1",
       text: "Ich bin ein ängstlicher Typ."
      },
      {
       value: "2",
       text: "Im privaten Bereich habe ich schon mal Dinge gemacht, die besser nicht an die Öffentlichkeit kommen sollten."
      }
     ],
     isAllRowRequired: true
    }
   ],
   title: "Anonymer Persönlichkeitstest"
  },
  {
   name: "Feedback",
   questions: [
    {
     type: "comment",
     name: "about",
     title: "Feedback"
    }
   ]
  }
 ],
 title: "Umfrage Masterarbeit Sven"
}

    Survey.defaultBootstrapCss.navigationButton = "btn btn-primary";
    Survey.Survey.cssType = "bootstrap";
    var myCss = {
            matrix: {root: "table table-striped"},
            navigationButton: "button btn-lg"
       };

        function sendDataToServer(survey) {
          var resultAsString = JSON.stringify(survey.data);
          alert(resultAsString); //send Ajax request to your web server.
            checkLogin.checkLoginSuccess(function (response) {
                console.log("login response -=====>>" + response);
                // alert("response.length "+response.length);
                if (response.length = 1) {
                    $state.go('profile');
                }
                else {
                    $('.ErrorMsg').css('display', 'block');
                }
            });
        }

        var survey = new Survey.Model(surveyJSON);
        survey.showProgressBar = "bottom";
        survey.onComplete.add(sendDataToServer);
        Survey.SurveyNG.render("surveyElement", {model:survey, css: myCss});


        $scope.submit = function () {
            divolte.signal('myCustomEvent', { param: 'foo',  otherParam: 'bar' })
            //$state.go('profile');
        };
    }])