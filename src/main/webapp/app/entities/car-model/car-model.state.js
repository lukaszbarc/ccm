(function() {
    'use strict';

    angular
        .module('ccmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('car-model', {
            parent: 'entity',
            url: '/car-model',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ccmApp.carModel.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-model/car-models.html',
                    controller: 'CarModelController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carModel');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('car-model-detail', {
            parent: 'entity',
            url: '/car-model/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ccmApp.carModel.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-model/car-model-detail.html',
                    controller: 'CarModelDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carModel');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CarModel', function($stateParams, CarModel) {
                    return CarModel.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'car-model',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('car-model-detail.edit', {
            parent: 'car-model-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model/car-model-dialog.html',
                    controller: 'CarModelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarModel', function(CarModel) {
                            return CarModel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-model.new', {
            parent: 'car-model',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model/car-model-dialog.html',
                    controller: 'CarModelDialogController',
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
                    $state.go('car-model', null, { reload: 'car-model' });
                }, function() {
                    $state.go('car-model');
                });
            }]
        })
        .state('car-model.edit', {
            parent: 'car-model',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model/car-model-dialog.html',
                    controller: 'CarModelDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarModel', function(CarModel) {
                            return CarModel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-model', null, { reload: 'car-model' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-model.delete', {
            parent: 'car-model',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model/car-model-delete-dialog.html',
                    controller: 'CarModelDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CarModel', function(CarModel) {
                            return CarModel.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-model', null, { reload: 'car-model' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
