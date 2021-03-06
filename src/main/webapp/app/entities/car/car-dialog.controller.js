(function() {
    'use strict';

    angular
        .module('ccmApp')
        .controller('CarDialogController', CarDialogController);

    CarDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Car', 'CarConcern', 'CarMake', 'CarModel', 'CarModelGeneration', 'User'];

    function CarDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Car, CarConcern, CarMake, CarModel, CarModelGeneration, User) {
        var vm = this;

        vm.car = entity;
        vm.clear = clear;
        vm.save = save;
        vm.carconcerns = CarConcern.query();
        vm.carmakes = CarMake.query();
        vm.carmodels = CarModel.query();
        vm.carmodelgenerations = CarModelGeneration.query();
        vm.users = User.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.car.id !== null) {
                Car.update(vm.car, onSaveSuccess, onSaveError);
            } else {
                Car.save(vm.car, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('ccmApp:carUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
