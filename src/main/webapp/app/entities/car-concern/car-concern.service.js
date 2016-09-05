(function() {
    'use strict';
    angular
        .module('ccmApp')
        .factory('CarConcern', CarConcern);

    CarConcern.$inject = ['$resource'];

    function CarConcern ($resource) {
        var resourceUrl =  'api/car-concerns/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
