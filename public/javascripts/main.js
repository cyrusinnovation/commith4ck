require.config({
    baseUrl: "/assets/javascripts/",
    paths: {
        lodash: "lib/lodash/dist/lodash",
        requirejs: "lib/requirejs/require",
        jquery: "lib/jquery/dist/jquery",
        knockoutjs: "lib/knockoutjs/dist/knockout",
        "requirejs-text": "lib/requirejs-text/text"
    },
    packages: [
        {
            name: "commith4ck"
        }
    ],
    map: {
        "*": {
            underscore: "lodash",
            ko: "knockoutjs",
            text: "requirejs-text"
        }
    }
});

require(['commith4ck']);