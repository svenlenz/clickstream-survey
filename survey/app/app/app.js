// app.js
 angular.module('routerApp', ['ui.router','app.survey','app.mi'])

.config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
        .state('survey', {
            url: '/survey',
            templateUrl: 'app/survey/survey.view.html',
            controller: 'surveyController'
        })

        .state('profile', {
            url: '/profile',
            templateUrl: 'app/mi/mi.view.html',
            controller: 'miController'
        });
    $urlRouterProvider.otherwise('/survey');

});
