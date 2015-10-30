function JiraRestClient () {

    var rootPath = '/jira';


    this.findIssueByKey = function(key) {
        return this._send('GET', "/rest/api/2/issue/" + key);
    }

    this.findAll = function() {

        var params = {
            jql: "project in (RFC)"
        };

        return this._send('POST', "/rest/api/2/search",params);
    }

    this._send = function(method, path, params) {

        var data = {};

        data.url = path;
        data.method = method;
        data.params = params;
        console.log(data.params);

        var promise = $.ajax(
            {
                url: rootPath,
                method: "POST",
                contentType: 'application/json',
                data: JSON.stringify(data),
                dataType: "json"

            })
            .fail((err) => {
                console.log("Se ha producido un error al acceder a la URL: ", rootPath + " (" + method + ":" + path + ") ,", err);
            });

        return promise;
    }

}
