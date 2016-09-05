(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('RefuelingDeleteController',RefuelingDeleteController);

    RefuelingDeleteController.$inject = ['$uibModalInstance', 'entity', 'Refueling'];

    function RefuelingDeleteController($uibModalInstance, entity, Refueling) {
        var vm = this;

        vm.refueling = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Refueling.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
