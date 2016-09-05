(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('RefuelingController', RefuelingController);

    RefuelingController.$inject = ['$scope', '$state', 'Refueling'];

    function RefuelingController ($scope, $state, Refueling) {
        var vm = this;
        
        vm.refuelings = [];

        loadAll();

        function loadAll() {
            Refueling.query(function(result) {
                vm.refuelings = result;
            });
        }
    }
})();
