'use strict';

export class JiraRestClient {

    constructor() {
        this.rootPath = '/jira';
    }

    findIssueByKey(key) {
        return this._send('GET', "/rest/api/2/issue/" + key);
    }


    _send(method, path, params) {

        var data = {};

        data.url = path;
        data.method = method;
        data.params = params;

        var promise = $.ajax(
            {
                url: this.rootPath,
                method: "POST",
                contentType: 'application/json',
                data: JSON.stringify(data),
                dataType: "json"

            })
            .fail((err) => {
                console.log("Se ha producido un error al acceder a la URL: ", this.rootPath + " (" + method + ":" + path + ") ,", err);
            });

        return promise;
    }

}
