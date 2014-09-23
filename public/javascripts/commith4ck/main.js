define(function (require) {
    "use strict";

    var $ = require("jquery");
    var foo = require("./foo");

    $(function () {
        foo.initialize($('body'));
    });
});