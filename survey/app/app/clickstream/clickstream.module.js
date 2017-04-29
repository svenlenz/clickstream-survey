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
                video: 'RFDOI24RRAE',
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
                        video: 'x4gL6QXhP2I'
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
             video: '73yDRm8KaWY',
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
                        video: '_ZknAd5m8Mg'
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
             video: 'iYLWU_bpKdQ',
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
                        video: 'DBrl356VhqQ'
                    }, {
                        id: 3,
                        title: 'Rabatte',
                        imageUrl: '/img/discount.svg',
                        contentUrl: '/app/clickstream/discount.html'
                    }
                ]
            },{
             id: 3,
             imageUrl: '/img/audi.jpg',
             name: 'Audi A3',
             snippet: 'Vorsprung durch Technik',
             showDiscount: false,
             discountCode: 'AUDI2017',
             video: '34ZwA0DId3o',
             details: [
                    {
                        id: 0,
                        title: 'Unternehmen',
                        imageUrl: '/img/company.svg',
                        contentUrl: '/app/clickstream/audi/company.html',
                    }, {
                        id: 1,
                        title: 'Technische Daten',
                        imageUrl: '/img/ingredients.svg',
                        contentUrl: '/app/clickstream/audi/spec.html'
                    }, {
                        id: 2,
                        title: 'Herkunft',
                        imageUrl: '/img/footprint.svg',
                        contentUrl: '/img/billy.svg',
                        video: 'sqCbYd8O8MU'
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

        $scope.goHome = function() {
            divolte.signal('home');
            $scope.sendEvent('home', productId, detailId);
        }


        $scope.$on('youtube.player.playing', function ($event, player) {
            divolte.signal('playingVideo');
            $scope.sendEvent('playingVideo', productId, detailId);
        });

        $scope.$on('youtube.player.paused', function ($event, player) {
            divolte.signal('pausedVideo');
            $scope.sendEvent('pausedVideo', productId, detailId);
        });


        $scope.$on('youtube.player.ended', function ($event, player) {
            divolte.signal('endedVideo');
            $scope.sendEvent('endedVideo', productId, detailId);
        });


        $scope.sendEvent('open', productId, detailId);
    }])






