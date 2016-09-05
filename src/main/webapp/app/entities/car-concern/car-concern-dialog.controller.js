(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarConcernDialogController', CarConcernDialogController);

    CarConcernDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CarConcern'];

    function CarConcernDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CarConcern) {
        var vm = this;

        vm.carConcern = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.carConcern.id !== null) {
                CarConcern.update(vm.carConcern, onSaveSuccess, onSaveError);
            } else {
                CarConcern.save(vm.carConcern, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ccmApp:carConcernUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
