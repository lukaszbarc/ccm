(function() {
    'use strict';

    angular
        .module('ccmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('car-concern', {
            parent: 'entity',
            url: '/car-concern',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ccmApp.carConcern.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-concern/car-concerns.html',
                    controller: 'CarConcernController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carConcern');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('car-concern-detail', {
            parent: 'entity',
            url: '/car-concern/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ccmApp.carConcern.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-concern/car-concern-detail.html',
                    controller: 'CarConcernDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carConcern');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CarConcern', function($stateParams, CarConcern) {
                    return CarConcern.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'car-concern',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('car-concern-detail.edit', {
            parent: 'car-concern-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-concern/car-concern-dialog.html',
                    controller: 'CarConcernDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarConcern', function(CarConcern) {
                            return CarConcern.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-concern.new', {
            parent: 'car-concern',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-concern/car-concern-dialog.html',
                    controller: 'CarConcernDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('car-concern', null, { reload: 'car-concern' });
                }, function() {
                    $state.go('car-concern');
                });
            }]
        })
        .state('car-concern.edit', {
            parent: 'car-concern',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-concern/car-concern-dialog.html',
                    controller: 'CarConcernDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarConcern', function(CarConcern) {
                            return CarConcern.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-concern', null, { reload: 'car-concern' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-concern.delete', {
            parent: 'car-concern',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-concern/car-concern-delete-dialog.html',
                    controller: 'CarConcernDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CarConcern', function(CarConcern) {
                            return CarConcern.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-concern', null, { reload: 'car-concern' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
