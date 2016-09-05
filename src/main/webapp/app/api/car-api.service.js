(function() {
    'use strict';
    angular
        .module('ccmApp')
        .factory('CarApi', CarApi);

    CarApi.$inject = ['$resource'];

    function CarApi ($resource) {
        var resourceUrl =  'public/api/cars/:id';

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
