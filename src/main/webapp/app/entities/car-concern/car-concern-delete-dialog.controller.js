(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarConcernDeleteController',CarConcernDeleteController);

    CarConcernDeleteController.$inject = ['$uibModalInstance', 'entity', 'CarConcern'];

    function CarConcernDeleteController($uibModalInstance, entity, CarConcern) {
        var vm = this;

        vm.carConcern = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CarConcern.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
