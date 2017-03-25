// app.js
 angular.module('routerApp', ['ui.router','app.survey','app.clickstream'])

.config(function($stateProvider, $urlRouterProvider) {

    $stateProvider
        .state('survey', {
            url: '/survey',
            templateUrl: 'app/survey/survey.view.html',
            controller: 'surveyController'
        })

        .state('clickstream', {
            url: '/clickstream',
            templateUrl: 'app/clickstream/clickstream.view.html',
            controller: 'clickstreamController'
        })

        .state('product', {
            url: '/product/:productId',
            templateUrl: 'app/clickstream/product.view.html',
            controller: 'clickstreamController'
        })

        .state('detail', {
            url: '/product/:productId/detail/:detailId',
            templateUrl: 'app/clickstream/detail.view.html',
            controller: 'clickstreamController'
        });

    $urlRouterProvider.otherwise('/survey');

});
