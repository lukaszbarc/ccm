(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarModelDetailController', CarModelDetailController);

    CarModelDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CarModel', 'CarMake'];

    function CarModelDetailController($scope, $rootScope, $stateParams, previousState, entity, CarModel, CarMake) {
        var vm = this;

        vm.carModel = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ccmApp:carModelUpdate', function(event, result) {
            vm.carModel = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
