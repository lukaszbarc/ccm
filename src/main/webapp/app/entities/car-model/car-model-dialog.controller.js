(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarModelDialogController', CarModelDialogController);

    CarModelDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CarModel', 'CarMake'];

    function CarModelDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CarModel, CarMake) {
        var vm = this;

        vm.carModel = entity;
        vm.clear = clear;
        vm.save = save;
        vm.carmakes = CarMake.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.carModel.id !== null) {
                CarModel.update(vm.carModel, onSaveSuccess, onSaveError);
            } else {
                CarModel.save(vm.carModel, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ccmApp:carModelUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
