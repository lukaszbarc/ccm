'use strict';

describe('Controller Tests', function() {

    describe('Refueling Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockRefueling, MockUser, MockCar;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockRefueling = jasmine.createSpy('MockRefueling');
            MockUser = jasmine.createSpy('MockUser');
            MockCar = jasmine.createSpy('MockCar');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Refueling': MockRefueling,
                'User': MockUser,
                'Car': MockCar
            };
            createController = function() {
                $injector.get('$controller')("RefuelingDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'ccmApp:refuelingUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
