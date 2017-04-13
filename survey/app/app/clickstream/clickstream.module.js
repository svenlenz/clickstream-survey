angular.module("app.clickstream", ['app.common'])
    .controller("clickstreamController", ['$scope', '$stateParams', 'clickstreamFactory', function ($scope, $stateParams, clickstreamFactory) {

        var productId = $stateParams.productId;
        var detailId = $stateParams.detailId;

        if(!$scope.lastEvent) {
            $scope.lastEvent = new Date();
        }

        function sendEvent(eventId, productId, detailId, linkId) {

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
                linkId: linkId || 'undefined',
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
                imageUrl: '/img/Chiquita.png',
                name: 'Chiquita Banana',
                snippet: 'Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata sanctus est Lorem ipsum dolor sit amet.',
                details: [
                    {
                        id: 0,
                        title: 'Unternehmen',
                        imageUrl: '/img/company.svg',
                        contentUrl: '/app/clickstream/chiquita.html'
                    }, {
                        id: 1,
                        title: 'Inhaltsstoffe',
                        imageUrl: '/img/ingredients.svg',
                    }, {
                        id: 2,
                        title: 'Rabatte',
                        imageUrl: '/img/discount.svg',
                    }, {
                        id: 3,
                        title: 'Herkunft',
                        imageUrl: '/img/footprint.svg',
                        contentUrl: '/img/avocado.svg',
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

        $scope.outgoingLink = function(linkId) {
            divolte.signal('outgoingLink', linkId);
            sendEvent('outgoingLink', productId, detailId, linkId);
        }
    }])






