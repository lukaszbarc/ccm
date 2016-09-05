(function() {
    'use strict';

    angular
        .module('ccmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('car-model-generation', {
            parent: 'entity',
            url: '/car-model-generation',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ccmApp.carModelGeneration.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-model-generation/car-model-generations.html',
                    controller: 'CarModelGenerationController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carModelGeneration');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('car-model-generation-detail', {
            parent: 'entity',
            url: '/car-model-generation/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ccmApp.carModelGeneration.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/car-model-generation/car-model-generation-detail.html',
                    controller: 'CarModelGenerationDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('carModelGeneration');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CarModelGeneration', function($stateParams, CarModelGeneration) {
                    return CarModelGeneration.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'car-model-generation',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('car-model-generation-detail.edit', {
            parent: 'car-model-generation-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model-generation/car-model-generation-dialog.html',
                    controller: 'CarModelGenerationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarModelGeneration', function(CarModelGeneration) {
                            return CarModelGeneration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-model-generation.new', {
            parent: 'car-model-generation',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model-generation/car-model-generation-dialog.html',
                    controller: 'CarModelGenerationDialogController',
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
                    $state.go('car-model-generation', null, { reload: 'car-model-generation' });
                }, function() {
                    $state.go('car-model-generation');
                });
            }]
        })
        .state('car-model-generation.edit', {
            parent: 'car-model-generation',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model-generation/car-model-generation-dialog.html',
                    controller: 'CarModelGenerationDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CarModelGeneration', function(CarModelGeneration) {
                            return CarModelGeneration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-model-generation', null, { reload: 'car-model-generation' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('car-model-generation.delete', {
            parent: 'car-model-generation',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/car-model-generation/car-model-generation-delete-dialog.html',
                    controller: 'CarModelGenerationDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CarModelGeneration', function(CarModelGeneration) {
                            return CarModelGeneration.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('car-model-generation', null, { reload: 'car-model-generation' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
