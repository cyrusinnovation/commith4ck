require.config({
    baseUrl: "/assets/javascripts/",
    paths: {
        lodash: "lib/lodash/dist/lodash.compat",
        requirejs: "lib/requirejs/require",
        jquery: "lib/jquery/dist/jquery"
    },
    packages: [
        {
            name: "commith4ck"
        }
    ]
});

require(['commith4ck'], function (commith4ck) {
    "use strict";

});