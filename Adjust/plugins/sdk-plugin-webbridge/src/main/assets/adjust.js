// generate random callback ID with prefix (similar to iOS implementation)
window.randomCallbackIdWithPrefix = function(prefix) {
    const randomString = (Math.random() + 1).toString(36).substring(7);
    return prefix + "_" + randomString;
};

var Adjust = {
    // map to store callbacks by their unique callback ID
    _callbackMap: {},
    
    _handleGetterCallback: function(callback, callbackId) {
        // store callback in map
        this._callbackMap[callbackId] = callback;

        // create a function on window with the callbackId name
        // this function will be called by the native side
        window[callbackId] = (function(adjustInstance, storedCallbackId) {
            return function(value) {
                var callback = adjustInstance._callbackMap[storedCallbackId];
                if (callback) {
                    // for attribution, the native side passes a JSON object that's already parsed
                    // for other values, they're passed as strings
                    if (storedCallbackId.includes("adjust_getAttribution")) {
                        // value is already a JavaScript object (parsed from JSON)
                        // only parse if it's actually a string (shouldn't happen)
                        if (typeof value === 'string') {
                            callback(JSON.parse(value));
                        } else {
                            callback(value);
                        }
                    } else {
                        callback(value);
                    }
                    // clean up: remove callback and delete the function
                    delete adjustInstance._callbackMap[storedCallbackId];
                    delete window[storedCallbackId];
                } else {
                    // callback was already cleaned up (teardown was called)
                    // safely remove the window function to prevent memory leaks
                    delete window[storedCallbackId];
                }
            };
        })(this, callbackId);
    },

    initSdk: function (adjustConfig) {
        if (adjustConfig && !adjustConfig.getSdkPrefix()) {
            adjustConfig.setSdkPrefix(this.getSdkPrefix());
        }
        this.adjustConfig = adjustConfig;
        if (AdjustBridge) {
            AdjustBridge.initSdk(JSON.stringify(adjustConfig));
        }
    },

    getConfig: function () {
        return this.adjustConfig;
    },

    trackEvent: function (adjustEvent) {
        if (AdjustBridge) {
            AdjustBridge.trackEvent(JSON.stringify(adjustEvent));
        }
    },

    onResume: function () {
        if (AdjustBridge) {
            AdjustBridge.onResume();
        }
    },

    onPause: function () {
        if (AdjustBridge) {
            AdjustBridge.onPause();
        }
    },

    enable: function () {
        if (AdjustBridge) {
            AdjustBridge.enable();
        }
    },

    disable: function () {
        if (AdjustBridge) {
            AdjustBridge.disable();
        }
    },

    isEnabled: function (callback) {
        if (!AdjustBridge) {
            return undefined;
        }
        // supports legacy return with callback
        if (arguments.length === 1) {
            // generate unique callback ID
            const callbackId = window.randomCallbackIdWithPrefix("adjust_isEnabled");
            this._handleGetterCallback(callback, callbackId);
            AdjustBridge.isEnabled(callbackId);
        } else {
            return AdjustBridge.isEnabled();
        }
    },

    setReferrer: function (referrer) {
        if (AdjustBridge) {
            AdjustBridge.setReferrer(referrer);
        }
    },

    switchToOfflineMode: function() {
        if (AdjustBridge) {
            AdjustBridge.switchToOfflineMode();
        }
    },

    switchBackToOnlineMode: function() {
        if (AdjustBridge) {
            AdjustBridge.switchBackToOnlineMode();
        }
    },

    addGlobalCallbackParameter: function(key, value) {
        if (AdjustBridge) {
            if (typeof key !== 'string' || typeof value !== 'string') {
                console.log('[Adjust]: Passed key or value is not of string type');
                return;
            }
            AdjustBridge.addGlobalCallbackParameter(key, value);
        }
    },

    addGlobalPartnerParameter: function(key, value) {
        if (AdjustBridge) {
            if (typeof key !== 'string' || typeof value !== 'string') {
                console.log('[Adjust]: Passed key or value is not of string type');
                return;
            }
            AdjustBridge.addGlobalPartnerParameter(key, value);
        }
    },

    removeGlobalCallbackParameter: function(key) {
        if (AdjustBridge) {
            if (typeof key !== 'string') {
                console.log('[Adjust]: Passed key is not of string type');
                return;
            }
            AdjustBridge.removeGlobalCallbackParameter(key);
        }
    },

    removeGlobalPartnerParameter: function(key) {
        if (AdjustBridge) {
            if (typeof key !== 'string') {
                console.log('[Adjust]: Passed key is not of string type');
                return;
            }
            AdjustBridge.removeGlobalPartnerParameter(key);
        }
    },

    removeGlobalCallbackParameters: function() {
        if (AdjustBridge) {
            AdjustBridge.removeGlobalCallbackParameters();
        }
    },

    removeGlobalPartnerParameters: function() {
        if (AdjustBridge) {
            AdjustBridge.removeGlobalPartnerParameters();
        }
    },

    gdprForgetMe: function() {
        if (AdjustBridge) {
            AdjustBridge.gdprForgetMe();
        }
    },

    trackThirdPartySharing: function(adjustThirdPartySharing) {
        if (AdjustBridge) {
            AdjustBridge.trackThirdPartySharing(JSON.stringify(adjustThirdPartySharing));
        }
    },

    trackMeasurementConsent: function(consentMeasurement) {
        if (AdjustBridge) {
            AdjustBridge.trackMeasurementConsent(consentMeasurement);
        }
    },

    endFirstSessionDelay: function() {
        if (AdjustBridge) {
            AdjustBridge.endFirstSessionDelay();
        }
    },

    enableCoppaComplianceInDelay: function() {
        if (AdjustBridge) {
            AdjustBridge.enableCoppaComplianceInDelay();
        }
    },

    disableCoppaComplianceInDelay: function() {
        if (AdjustBridge) {
            AdjustBridge.disableCoppaComplianceInDelay();
        }
    },

    enablePlayStoreKidsComplianceInDelay: function() {
        if (AdjustBridge) {
            AdjustBridge.enablePlayStoreKidsComplianceInDelay();
        }
    },

    disablePlayStoreKidsComplianceInDelay: function() {
        if (AdjustBridge) {
            AdjustBridge.disablePlayStoreKidsComplianceInDelay();
        }
    },

    setExternalDeviceIdInDelay: function(externalDeviceId) {
        if (AdjustBridge) {
            AdjustBridge.setExternalDeviceIdInDelay(externalDeviceId);
        }
    },

    getGoogleAdId: function (callback) {
        if (AdjustBridge) {
            const callbackId = window.randomCallbackIdWithPrefix("adjust_getGoogleAdId");
            this._handleGetterCallback(callback, callbackId);
            AdjustBridge.getGoogleAdId(callbackId);
        }
    },

    getAdid: function (callback) {
        if (AdjustBridge) {
            const callbackId = window.randomCallbackIdWithPrefix("adjust_getAdid");
            this._handleGetterCallback(callback, callbackId);
            AdjustBridge.getAdid(callbackId);
        }
    },

    getAdidWithTimeout: function (timeoutInMilliSec, callback) {
        if (AdjustBridge) {
            const callbackId = window.randomCallbackIdWithPrefix("adjust_getAdidWithTimeout");
            this._handleGetterCallback(callback, callbackId);
            AdjustBridge.getAdidWithTimeout(timeoutInMilliSec, callbackId);
        }
    },

    getAmazonAdId: function (callbackSuccess, callbackFail) {
        if (AdjustBridge) {
            const callbackId = window.randomCallbackIdWithPrefix("adjust_getAmazonAdId");
            this._handleGetterCallback(callbackSuccess, callbackId);
            // note: Java side only supports success callback, callbackFail is ignored
            AdjustBridge.getAmazonAdId(callbackId);
        }
    },

    getAttribution: function (callback) {
        if (AdjustBridge) {
            const callbackId = window.randomCallbackIdWithPrefix("adjust_getAttribution");
            this._handleGetterCallback(callback, callbackId);
            AdjustBridge.getAttribution(callbackId);
        }
    },

    getAttributionWithTimeout: function (timeoutInMilliSec, callback) {
        if (AdjustBridge) {
            const callbackId = window.randomCallbackIdWithPrefix("adjust_getAttributionWithTimeout");
            this._handleGetterCallback(callback, callbackId);
            AdjustBridge.getAttributionWithTimeout(timeoutInMilliSec, callbackId);
        }
    },

    getSdkVersion: function (callback) {
        if (AdjustBridge) {
            const callbackId = window.randomCallbackIdWithPrefix("adjust_getSdkVersion");
            // wrap callback to add SDK prefix before passing to _handleGetterCallback
            const wrappedCallback = function(sdkVersion) {
                callback(Adjust.getSdkPrefix() + '@' + sdkVersion);
            };
            this._handleGetterCallback(wrappedCallback, callbackId);
            AdjustBridge.getSdkVersion(callbackId);
        }
    },

    getSdkPrefix: function () {
        if (this.adjustConfig) {
            return this.adjustConfig.getSdkPrefix();
        } else {
            return 'web-bridge5.5.0';
        }
    },

    teardown: function() {
        if (AdjustBridge) {
            AdjustBridge.teardown();
        }
        this.adjustConfig = undefined;
        this._callbackMap = {};
    },
};
