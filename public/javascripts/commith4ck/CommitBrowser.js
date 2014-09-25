define(function (require) {
    "use strict";

    var ko = require('ko');
    var template = require('text!./templates/table.html');
    var $ = require("jquery");

    return {
        initialize: function ($container) {
            var viewModel = {
                commits: ko.observableArray()
            };

            $.ajax({
                url: '/commits/',
                type: "GET",
                dataType: "json"
            }).done(function (x) {
                viewModel.commits(x);
            });

            var t = $(template);
            ko.applyBindings(viewModel, t[0]);
            $container.append(t);
        }
    }
});