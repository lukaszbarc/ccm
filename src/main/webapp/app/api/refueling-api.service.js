(function() {
    'use strict';
    angular
        .module('ccmApp')
        .factory('RefuelingApi', RefuelingApi);

    RefuelingApi.$inject = ['$resource'];

    function RefuelingApi ($resource) {
        var resourceUrl =  'public/api/refueling/:id';

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
