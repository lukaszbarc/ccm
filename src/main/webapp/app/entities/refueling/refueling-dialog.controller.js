(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('RefuelingDialogController', RefuelingDialogController);

    RefuelingDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Refueling', 'User', 'Car'];

    function RefuelingDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Refueling, User, Car) {
        var vm = this;

        vm.refueling = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.users = User.query();
        vm.cars = Car.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.refueling.id !== null) {
                Refueling.update(vm.refueling, onSaveSuccess, onSaveError);
            } else {
                Refueling.save(vm.refueling, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ccmApp:refuelingUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.date = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
