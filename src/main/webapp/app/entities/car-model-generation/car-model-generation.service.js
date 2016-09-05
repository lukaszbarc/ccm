(function() {
    'use strict';
    angular
        .module('ccmApp')
        .factory('CarModelGeneration', CarModelGeneration);

    CarModelGeneration.$inject = ['$resource'];

    function CarModelGeneration ($resource) {
        var resourceUrl =  'api/car-model-generations/:id';

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
