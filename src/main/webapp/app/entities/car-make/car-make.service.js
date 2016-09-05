(function() {
    'use strict';
    angular
        .module('ccmApp')
        .factory('CarMake', CarMake);

    CarMake.$inject = ['$resource'];

    function CarMake ($resource) {
        var resourceUrl =  'api/car-makes/:id';

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
