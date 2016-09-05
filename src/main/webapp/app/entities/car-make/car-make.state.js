(function() {
    'use strict';

    angular
        .module('ccmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('car-make', {
            parent: 'entity',
            url: '/car-make',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ccmApp.carMake.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-make/car-makes.html',
                    controller: 'CarMakeController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carMake');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('car-make-detail', {
            parent: 'entity',
            url: '/car-make/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ccmApp.carMake.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-make/car-make-detail.html',
                    controller: 'CarMakeDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carMake');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CarMake', function($stateParams, CarMake) {
                    return CarMake.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'car-make',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('car-make-detail.edit', {
            parent: 'car-make-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-make/car-make-dialog.html',
                    controller: 'CarMakeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarMake', function(CarMake) {
                            return CarMake.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-make.new', {
            parent: 'car-make',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-make/car-make-dialog.html',
                    controller: 'CarMakeDialogController',
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
                    $state.go('car-make', null, { reload: 'car-make' });
                }, function() {
                    $state.go('car-make');
                });
            }]
        })
        .state('car-make.edit', {
            parent: 'car-make',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-make/car-make-dialog.html',
                    controller: 'CarMakeDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarMake', function(CarMake) {
                            return CarMake.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-make', null, { reload: 'car-make' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-make.delete', {
            parent: 'car-make',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-make/car-make-delete-dialog.html',
                    controller: 'CarMakeDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CarMake', function(CarMake) {
                            return CarMake.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-make', null, { reload: 'car-make' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
