define(function (require) {
    "use strict";

    var $ = require("jquery");
    var commitBrowser = require("./CommitBrowser");

    $(function () {
        commitBrowser.initialize($('body'));
    });
});