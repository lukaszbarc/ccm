(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarMakeDetailController', CarMakeDetailController);

    CarMakeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CarMake', 'CarConcern'];

    function CarMakeDetailController($scope, $rootScope, $stateParams, previousState, entity, CarMake, CarConcern) {
        var vm = this;

        vm.carMake = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ccmApp:carMakeUpdate', function(event, result) {
            vm.carMake = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
