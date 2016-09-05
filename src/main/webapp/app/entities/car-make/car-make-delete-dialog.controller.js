(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarMakeDeleteController',CarMakeDeleteController);

    CarMakeDeleteController.$inject = ['$uibModalInstance', 'entity', 'CarMake'];

    function CarMakeDeleteController($uibModalInstance, entity, CarMake) {
        var vm = this;

        vm.carMake = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CarMake.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
