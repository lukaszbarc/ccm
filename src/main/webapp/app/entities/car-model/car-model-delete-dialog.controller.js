(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarModelDeleteController',CarModelDeleteController);

    CarModelDeleteController.$inject = ['$uibModalInstance', 'entity', 'CarModel'];

    function CarModelDeleteController($uibModalInstance, entity, CarModel) {
        var vm = this;

        vm.carModel = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CarModel.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
