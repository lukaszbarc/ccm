(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarDetailController', CarDetailController);

    CarDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Car', 'CarConcern', 'CarMake', 'CarModel', 'CarModelGeneration'];

    function CarDetailController($scope, $rootScope, $stateParams, previousState, entity, Car, CarConcern, CarMake, CarModel, CarModelGeneration) {
        var vm = this;

        vm.car = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ccmApp:carUpdate', function(event, result) {
            vm.car = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
