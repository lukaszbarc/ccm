(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarMakeDialogController', CarMakeDialogController);

    CarMakeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CarMake', 'CarConcern'];

    function CarMakeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CarMake, CarConcern) {
        var vm = this;

        vm.carMake = entity;
        vm.clear = clear;
        vm.save = save;
        vm.carconcerns = CarConcern.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.carMake.id !== null) {
                CarMake.update(vm.carMake, onSaveSuccess, onSaveError);
            } else {
                CarMake.save(vm.carMake, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ccmApp:carMakeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
