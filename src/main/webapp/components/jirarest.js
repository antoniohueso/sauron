'use strict';

var assert = require("assert");
var http = require('http');

class JiraRestClient {

    constructor(ip,port) {

        assert.notEqual(ip,null);
        assert.notEqual(port,null);

        this.host = ip;
        this.port = port;
        this.session = null;
        this.rootPath = '/jira/rest';

    }

    login(user,pwd) {
        var promise = new Promise((resolve,reject) => {

            this._post('/auth/1/session',{username:user,password:pwd}).then((result) => {
                this.session = result.session.name + "=" + result.session.value;
                resolve(this.session);
            }).catch((e) => {
                reject(e);
            });

        });

        return promise;
    }

    findIssueByKey(key) {

        assert.notEqual(key,null);

        var promise = new Promise((resolve,reject) => {
            this._get("/api/2/issue/"+key).then((result) => {
                resolve(result);
            }).catch(function(e){
                reject(e);
            });
        });

        return promise;

    }

    _post(path,data) {
        return this._send("POST",path,data);
    }

    _get(path) {
        return this._send("GET",path);
    }

    _send(type,path,data) {

        assert.notEqual(type,null);
        assert.notEqual(path,null);

        path = this.rootPath + path;

        var result = "";

        if(data) data = JSON.stringify(data);
        else data = "";

        var options = {
            hostname: this.host,
            port: this.port,
            path: path,
            method: type,
            headers: {
                'Content-Type': 'application/json',
                'Content-Length': data.length,
                'Cookie' : this.session
            }
        };

        var promise = new Promise((resolve,reject) => {

            var req = http.request(options, (res) => {
                res.setEncoding('utf8');

                res.on('data',(chunk) => {
                    result += chunk;
                });

                res.on('end', () => {
                    resolve(JSON.parse(result));
                });

                res.on('error', (err) => {
                    reject(err);

                })

            }).on('error', (e) => {
               reject(e);
            });

            if(type == 'POST') {
                req.write(data);
            }

            req.end();

        });


        return promise;
    }

}
/*
var c = new JiraRestClient('10.0.100.118', 8085);
c.login("ahg","ahg191907")
    .then((result) => {
        console.log(result);

        return Promise.all([c.findIssueByKey('PPS-3')]);
    }).then(function(result){

        result = result[0];

        console.log(result);

        for( var p in result.fields)  {

            if(p.startsWith('custom') && result.fields[p]) {
                console.log(p, " -> ",result.fields[p]);

            }
        }


    } )
    .catch(console.log);

*/

