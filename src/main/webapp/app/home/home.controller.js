(function () {
    'use strict';

    angular
        .module('ccmApp')
        .controller('HomeController', HomeController);

    HomeController.$inject = ['$scope', 'Principal', 'LoginService', '$state', 'CarApi', 'RefuelingApi'];

    function HomeController($scope, Principal, LoginService, $state, CarApi, RefuelingApi) {
        var vm = this;

        vm.account = null;
        vm.isAuthenticated = null;
        vm.login = LoginService.open;
        vm.register = register;
        $scope.$on('authenticationSuccess', function () {
            getAccount();
            getCars();
            getRefuelings();
        });
        vm.cars = null;
        vm.refuelings = null;


        getAccount();
        getCars();
        getRefuelings();


        function getAccount() {
            Principal.identity().then(function (account) {
                vm.account = account;
                vm.isAuthenticated = Principal.isAuthenticated;
            });
        }

        function getCars() {
            CarApi.query(function (data) {
                vm.cars = data;
            });
        }

        function getRefuelings() {
            RefuelingApi.query(function (data) {
                vm.refuelings = data;
            });
        }

        function register() {
            $state.go('register');
        }
    }
})();
