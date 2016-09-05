(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarModelGenerationDialogController', CarModelGenerationDialogController);

    CarModelGenerationDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CarModelGeneration', 'CarModel'];

    function CarModelGenerationDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CarModelGeneration, CarModel) {
        var vm = this;

        vm.carModelGeneration = entity;
        vm.clear = clear;
        vm.save = save;
        vm.carmodels = CarModel.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.carModelGeneration.id !== null) {
                CarModelGeneration.update(vm.carModelGeneration, onSaveSuccess, onSaveError);
            } else {
                CarModelGeneration.save(vm.carModelGeneration, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ccmApp:carModelGenerationUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
