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
                snippet: 'Nichts schmeckt wie eine Chiquita!',
                discountCode: 'BANANA2017',
                showDiscount: false,
                details: [
                    {
                        id: 0,
                        title: 'Unternehmen',
                        imageUrl: '/img/company.svg',
                        contentUrl: '/app/clickstream/chiquita/company.html',
                    }, {
                        id: 1,
                        title: 'Inhaltsstoffe',
                        imageUrl: '/img/ingredients.svg',
                        contentUrl: '/app/clickstream/chiquita/ingredients.html'
                    }, {
                        id: 2,
                        title: 'Rabatte',
                        imageUrl: '/img/discount.svg',
                        contentUrl: '/app/clickstream/discount.html'
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
             snippet: 'kleine Beschreibung',
             showDiscount: false,
             discountCode: 'PHONE2017'
            },{
             id: 2,
             imageUrl: 'http://s7d2.scene7.com/is/image/SamsungUS/SMG930_gs7_102416?$product-details-jpg$',
             name: 'Phone 2',
             snippet: 'kleine Beschreibung',
             showDiscount: false,
             discountCode: 'TEXTIL2017'
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


        $scope.discount = function(text) {
            divolte.signal('discount_'+text);
            sendEvent('discount_'+text, productId, detailId);
            $scope.products[productId].showDiscount = true;
        }

        $scope.outgoingLink = function(linkId) {
            divolte.signal('outgoingLink', linkId);
            sendEvent('outgoingLink', productId, detailId, linkId);
        }
    }])






