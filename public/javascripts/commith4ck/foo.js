define(function (require) {
    "use strict";

    var $ = require("jquery");

    return {
        initialize: function ($container) {
            $container.html("<h1>Hello World!</h1>");
        }
    }
});