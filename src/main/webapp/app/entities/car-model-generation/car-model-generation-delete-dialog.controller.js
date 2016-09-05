(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarModelGenerationDeleteController',CarModelGenerationDeleteController);

    CarModelGenerationDeleteController.$inject = ['$uibModalInstance', 'entity', 'CarModelGeneration'];

    function CarModelGenerationDeleteController($uibModalInstance, entity, CarModelGeneration) {
        var vm = this;

        vm.carModelGeneration = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CarModelGeneration.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
