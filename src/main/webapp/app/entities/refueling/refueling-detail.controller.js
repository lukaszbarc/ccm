(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('RefuelingDetailController', RefuelingDetailController);

    RefuelingDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Refueling', 'User', 'Car'];

    function RefuelingDetailController($scope, $rootScope, $stateParams, previousState, entity, Refueling, User, Car) {
        var vm = this;

        vm.refueling = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('ccmApp:refuelingUpdate', function(event, result) {
            vm.refueling = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
