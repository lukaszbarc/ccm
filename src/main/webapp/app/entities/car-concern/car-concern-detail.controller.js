(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarConcernDetailController', CarConcernDetailController);

    CarConcernDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CarConcern'];

    function CarConcernDetailController($scope, $rootScope, $stateParams, previousState, entity, CarConcern) {
        var vm = this;

        vm.carConcern = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ccmApp:carConcernUpdate', function(event, result) {
            vm.carConcern = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
