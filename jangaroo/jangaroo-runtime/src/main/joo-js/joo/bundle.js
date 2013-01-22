/**
 * This plugin handles bundle! prefixed modules. It does the following:
 *
 * A regular module can have a dependency on a bundle, but the regular
 * module does not want to specify what locale to load. So it just specifies
 * the bundle master, like "bundle!Colors".
 *
 * This plugin will load the i18n bundle at locale/Colors as a bundle master.
 * It will then try to find all locales available in that master bundle that
 * are part of the globally defined localeChain, and request all the
 * locale pieces for these locales. For instance, if the localeChain is
 * ["en_US", "en"] then the plugin will load locale/en_US/Colors.js and
 * locale/en/Colors.js (but only if they are specified on the bundle master).
 *
 * Once all the bundles for the locales are loaded, it joins all their
 * key/value pairs under the corresponding locale and bundle name, then
 * finally sets the context.defined value for the locale/Colors bundle
 * to be these joined locale bundles.
 *
 * This value is also written to the global mx.resources.ResourceManager
 * to be used by cross-compiled Flex classes.
 */
(function () {
  'use strict';

  define(['module', 'classes/mx/resources/ResourceManager'], function (module, ResourceManager) {
    var masterConfig = module.config();

    function findLocalesToLoad(master) {
      var localesToLoad;
      localesToLoad = [];
      for (var locale in master) {
        if (masterConfig.localeChain.indexOf(locale) !== -1) {
          localesToLoad.push(locale);
        }
      }
      return localesToLoad;
    }

    var LOCALE_MODULES_PATH_PREFIX = "classes/locale/";

    return {
      version: '0.0.1',
      /**
       * Called when a dependency needs to be loaded.
       */
      load: function (name, req, onLoad, config) {
        function localeToModuleName(locale) {
          return LOCALE_MODULES_PATH_PREFIX + locale + "/" + name;
        }

        config = config || {};

        if (config.localeChain) {
          masterConfig.localeChain = config.localeChain;
        }
        ResourceManager._.getInstance().setLocaleChain(masterConfig.localeChain);

        var masterName = LOCALE_MODULES_PATH_PREFIX + name;
        if (config.isBuild) {
          var toLoad = [masterName];
          //Check for existence of all locale possible files and
          //require them if exist.
          var maybeLoad = masterConfig.localeChain.map(localeToModuleName);
          for (var i = 0; i < maybeLoad.length; i++) {
            var moduleName = maybeLoad[i];
            console.log("module name: " + moduleName + " / url " + req.toUrl(moduleName));
            if (require._fileExists(req.toUrl(moduleName))) {
              toLoad.push(moduleName);
            }
          }

          req(toLoad, onLoad);
        } else {
          //First, fetch the master bundle, it knows what locales are available.
          req([masterName], function (master) {
            var localesToLoad = findLocalesToLoad(master);
            var toLoad = localesToLoad.map(localeToModuleName);

            //Load all the parts missing.
            req(toLoad, function () {
              var value = {};
              for (var i = 0; i < localesToLoad.length; i++) {
                var locale = localesToLoad[i];
                value[locale] = arguments[i];
              }
              //All done, store in resource manager and notify the loader.
              ResourceManager._.getInstance().addBundle(name, value);
              onLoad(value);
            });
          });
        }
      }
    };
  });
}());
