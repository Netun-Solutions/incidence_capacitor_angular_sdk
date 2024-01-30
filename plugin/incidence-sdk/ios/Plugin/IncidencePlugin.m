#import <Foundation/Foundation.h>
#import <Capacitor/Capacitor.h>

// Define the plugin using the CAP_PLUGIN Macro, and
// each method the plugin supports using the CAP_PLUGIN_METHOD macro.
CAP_PLUGIN(IncidencePlugin, "Incidence",
           CAP_PLUGIN_METHOD(initConfig, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(addDevice, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(checkDevice, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(deleteDevice, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(getDevice, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(createIncidenceFlow, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(createIncidence, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(closeIncidence, CAPPluginReturnPromise);
           CAP_PLUGIN_METHOD(ecommerce, CAPPluginReturnPromise);
)
