angular.module("app.clickstream", ['app.common'])
    .controller("clickstreamController", ['$scope', '$stateParams', 'clickstreamFactory', function ($scope, $stateParams, clickstreamFactory) {

        var productId = $stateParams.productId;
        var detailId = $stateParams.detailId;

        if(!$scope.lastEvent) {
            $scope.lastEvent = new Date();
        }

        $scope.sendEvent = function sendEvent(eventId, productId, detailId, linkId) {

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
                        title: 'Herkunft',
                        imageUrl: '/img/footprint.svg',
                        contentUrl: '/img/banana.svg',
                    }, {
                        id: 3,
                        title: 'Rabatte',
                        imageUrl: '/img/discount.svg',
                        contentUrl: '/app/clickstream/discount.html'
                    }
                ]
            },{
             id: 1,
             imageUrl: '/img/wii.png',
             name: 'Nintendo Wii',
             snippet: 'move you',
             showDiscount: false,
             discountCode: 'PHONE2017',
                details: [
                    {
                        id: 0,
                        title: 'Unternehmen',
                        imageUrl: '/img/company.svg',
                        contentUrl: '/app/clickstream/wii/company.html',
                    }, {
                        id: 1,
                        title: 'Technische Daten',
                        imageUrl: '/img/ingredients.svg',
                        contentUrl: '/app/clickstream/wii/spec.html'
                    }, {
                        id: 2,
                        title: 'Herkunft',
                        imageUrl: '/img/footprint.svg',
                        contentUrl: '/img/wii.svg',
                    }, {
                        id: 3,
                        title: 'Rabatte',
                        imageUrl: '/img/discount.svg',
                        contentUrl: '/app/clickstream/discount.html'
                    }
                ]
            },{
             id: 2,
             imageUrl: '/img/billy.jpg',
             name: 'IKEA Billy',
             snippet: 'Ein Traum für Bücherwürmer',
             showDiscount: false,
             discountCode: 'BILLY2017',
             details: [
                    {
                        id: 0,
                        title: 'Unternehmen',
                        imageUrl: '/img/company.svg',
                        contentUrl: '/app/clickstream/billy/company.html',
                    }, {
                        id: 1,
                        title: 'Produktmasse',
                        imageUrl: '/img/ingredients.svg',
                        contentUrl: '/app/clickstream/billy/spec.html'
                    }, {
                        id: 2,
                        title: 'Herkunft',
                        imageUrl: '/img/footprint.svg',
                        contentUrl: '/img/billy.svg',
                    }, {
                        id: 3,
                        title: 'Rabatte',
                        imageUrl: '/img/discount.svg',
                        contentUrl: '/app/clickstream/discount.html'
                    }
                ]
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

        $scope.setFav = function() {
            $scope.favorite = !$scope.favorite;
            divolte.signal('favorite', $scope.favorite);
            $scope.sendEvent('favorite_'+$scope.favorite, productId, detailId);
        }

        $scope.backToProduct = function() {
            divolte.signal('backToProductButton', "tbd");
            $scope.sendEvent('backToProduct', productId, detailId);
        }

        $scope.backToOverviewButton = function() {
            divolte.signal('backToOverviewButton', "tbd");
            $scope.sendEvent('backToOverviewButton', productId, detailId);
        }


        $scope.discount = function(text) {
            divolte.signal('discount_'+text);
            $scope.sendEvent('discount_'+text, productId, detailId);
            $scope.products[productId].showDiscount = true;
        }

        $scope.outgoingLink = function(linkId) {
            divolte.signal('outgoingLink', linkId);
            $scope.sendEvent('outgoingLink', productId, detailId, linkId);
        }

        $scope.sendEvent('open', productId, detailId);
    }])






