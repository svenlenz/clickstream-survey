angular.module("app.clickstream", ['app.common', 'ngStomp'])
    .controller("clickstreamController", ['$scope', 'GetJSonMI', 'wsConstant', '$stomp', '$stateParams', function ($scope, GetJSonMI, wsConstant, $stomp, $stateParams) {

        var productId = $stateParams.productId; //getting fooVal

        $scope.productId = productId;

          $scope.phones = [
            {
             id: 1,
             orderProp: 1,
             imageUrl: 'https://support.apple.com/library/content/dam/edam/applecare/images/en_US/iphone/iphone7/iphone7-colors.jpg',
             name: 'Phone 1',
             snippet: 'kleine Beschreibung'
            },{
             id: 2,
             orderProp: 2,
             imageUrl: 'http://s7d2.scene7.com/is/image/SamsungUS/SMG930_gs7_102416?$product-details-jpg$',
             name: 'Phone 2',
             snippet: 'kleine Beschreibung'
            },{
             id: 3,
             orderProp: 2,
             imageUrl: 'http://s7d2.scene7.com/is/image/SamsungUS/SMG930_gs7_102416?$product-details-jpg$',
             name: 'Phone 2',
             snippet: 'kleine Beschreibung'
            }
          ];

          $scope.setImage = function(imageUrl) {
            $scope.mainImageUrl = imageUrl;
          }

    }])






