'use strict';

export class JiraRestClient {

    constructor() {
        this.server = document.createElement('iron-request');
        console.log(this.server);
        this.rootPath = '/jira';
    }

    findIssueByKey(key) {
        return this._send('GET',"/rest/api/2/issue/"+key);
    }


    _send(method,path,params) {

        var data = {};

        data.url = path;
        data.method = method;
        data.params = params;

        var promise = this.server.send({
            url: this.rootPath,
            method:"post",
            headers: {
                'content-type':'application/json'
            }
        });

        promise.catch(() => {
            console.log("Se ha producido un error al acceder a la URL: ",this.rootPath +"("+method+":"+path+")");
        });

        return promise;
    }

}
