(function() {
    'use strict';

    angular
        .module('ccmApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('refueling', {
            parent: 'entity',
            url: '/refueling',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ccmApp.refueling.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/refueling/refuelings.html',
                    controller: 'RefuelingController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('refueling');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('refueling-detail', {
            parent: 'entity',
            url: '/refueling/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'ccmApp.refueling.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/refueling/refueling-detail.html',
                    controller: 'RefuelingDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('refueling');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Refueling', function($stateParams, Refueling) {
                    return Refueling.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'refueling',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('refueling-detail.edit', {
            parent: 'refueling-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/refueling/refueling-dialog.html',
                    controller: 'RefuelingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Refueling', function(Refueling) {
                            return Refueling.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('refueling.new', {
            parent: 'refueling',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/refueling/refueling-dialog.html',
                    controller: 'RefuelingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                date: null,
                                milage: null,
                                trip: null,
                                quantity: null,
                                cost: null,
                                comment: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('refueling', null, { reload: 'refueling' });
                }, function() {
                    $state.go('refueling');
                });
            }]
        })
        .state('refueling.edit', {
            parent: 'refueling',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/refueling/refueling-dialog.html',
                    controller: 'RefuelingDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Refueling', function(Refueling) {
                            return Refueling.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('refueling', null, { reload: 'refueling' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('refueling.delete', {
            parent: 'refueling',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/refueling/refueling-delete-dialog.html',
                    controller: 'RefuelingDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Refueling', function(Refueling) {
                            return Refueling.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('refueling', null, { reload: 'refueling' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
