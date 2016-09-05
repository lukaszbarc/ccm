(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarModelGenerationDetailController', CarModelGenerationDetailController);

    CarModelGenerationDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CarModelGeneration', 'CarModel'];

    function CarModelGenerationDetailController($scope, $rootScope, $stateParams, previousState, entity, CarModelGeneration, CarModel) {
        var vm = this;

        vm.carModelGeneration = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ccmApp:carModelGenerationUpdate', function(event, result) {
            vm.carModelGeneration = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
