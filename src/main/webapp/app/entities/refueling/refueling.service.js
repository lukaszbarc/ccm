(function() {
    'use strict';
    angular
        .module('ccmApp')
        .factory('Refueling', Refueling);

    Refueling.$inject = ['$resource', 'DateUtils'];

    function Refueling ($resource, DateUtils) {
        var resourceUrl =  'api/refuelings/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
