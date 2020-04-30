module.exports = {
    open: function (type, allowMultiple, success, failure) {
        if (!type) type = '*/*';

        allowMultiple = !!allowMultiple;

        cordova.exec(success, failure, "CustomFileChooser", "open", [type, allowMultiple.toString()]);
    }
};
