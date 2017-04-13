angular.module("app.clickstream", ['app.common'])
    .controller("clickstreamController", ['$scope', '$stateParams', 'clickstreamFactory', function ($scope, $stateParams, clickstreamFactory) {

        var productId = $stateParams.productId;
        var detailId = $stateParams.detailId;

        if(!$scope.lastEvent) {
            $scope.lastEvent = new Date();
        }

        function sendEvent(eventId, productId, detailId) {

            var currentdate = new Date();
            var duration = currentdate.getTime() -  $scope.lastEvent.getTime();
            $scope.lastEvent = currentdate;

            var event = {
                datetime: currentdate,
                duration:  duration,
                event: eventId,
                sessionId: divolte.sessionId,
                productId: productId || 'undefined',
                detail: detailId || 'undefined',
            };

          clickstreamFactory.send(function (response) {
              console.log("clickstream response -=====>>" + response);
          }, event);
        }

        $scope.productId = productId;
        $scope.detailId = detailId;

          $scope.products = [
            {
                id: 0,
                imageUrl: 'https://support.apple.com/library/content/dam/edam/applecare/images/en_US/iphone/iphone7/iphone7-colors.jpg',
                name: 'Phone 1',
                snippet: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.',
                details: [
                    {
                        id: 0,
                        title: 'Unternehmen',
                        imageUrl: 'http://www.pixys.de/files/unternehmen-icon.png',
                    }, {
                        id: 1,
                        title: 'Inhaltsstoffe',
                        imageUrl: 'https://image.flaticon.com/icons/svg/53/53421.svg',
                    }, {
                        id: 2,
                        title: 'Rabatte',
                        imageUrl: 'https://cdn3.iconfinder.com/data/icons/shopping-icons-1/512/Price_Tag-512.png',
                    }, {
                        id: 3,
                        title: 'Herkunft',
                        imageUrl: 'http://www.free-icons-download.net/images/right-footprint-icon-27808.png',
                    }
                ]
            },{
             id: 1,
             imageUrl: 'http://s7d2.scene7.com/is/image/SamsungUS/SMG930_gs7_102416?$product-details-jpg$',
             name: 'Phone 2',
             snippet: 'kleine Beschreibung'
            },{
             id: 2,
             imageUrl: 'http://s7d2.scene7.com/is/image/SamsungUS/SMG930_gs7_102416?$product-details-jpg$',
             name: 'Phone 2',
             snippet: 'kleine Beschreibung'
            }
          ];


        if(productId) {
            $scope.product = $scope.products[productId];

            if(detailId) {
                $scope.detail = $scope.product.details[detailId];
            }
        }

        $scope.setImage = function(imageUrl) {
            $scope.mainImageUrl = imageUrl;
        }
        divolte.signal('myCustomEvent', event);
        sendEvent('open', productId, detailId);

        $scope.setFav = function() {
            $scope.favorite = !$scope.favorite;
            divolte.signal('favorite', $scope.favorite);
            sendEvent('favorite_'+$scope.favorite, productId, detailId);
        }

        $scope.backToProduct = function() {
            divolte.signal('backToProductButton', "tbd");
            sendEvent('backToProduct', productId, detailId);
        }

        $scope.backToOverviewButton = function() {
            divolte.signal('backToOverviewButton', "tbd");
            sendEvent('backToOverviewButton', productId, detailId);
        }
    }])






