'use strict';

import {JiraRestClient} from "../components/jirarest.js"

export class HomeComponent {

    beforeRegister() {
        this.is = 'sauron-home';

        this.data = {
            data: []
        };
    }

    ready() {
        var j = new JiraRestClient();

        for(var i = 0; i < 100; i++ ) {
            j.findIssueByKey('RFC-1'+i).then((response) => {
                console.log("llega ", response);
            });
        }
    }

}

export class AppComponent {
    beforeRegister() {
        this.is = 'sauron-app';
    }

    isCurrentPage(page) {
        console.log("PAGE: ", page, this.currentPage);
        return this.currentPage == page;
    }

    handleClick() {
        this.color = (this.color == "color-rojo" ? "color-azul" : "color-rojo");
    }

    ready() {
        this.color = "color-rojo";

    }

    attached() {
        /*
        this.currentPage = "home";

        var self = this;

        var AppRouter = Backbone.Router.extend({
            routes: {
                "": "home",
                "/": "home",
                "recursos": "recursos",
                "*actions": "defaultRoute"

            }
        });

        var app_router = new AppRouter;

        app_router.on('route:home',() => {
            console.log("Home");
            self.currentPage = "home";

            var child = this.$.container.firstChild;
            if (child) this.$.container.removeChild(child);
            var el = document.createElement('sauron-home');
            this.$.container.appendChild(el);


        });

        app_router.on('route:recursos', () => {
            self.currentPage = "recursos";

            var child = this.$.container.firstChild;
            if (child) this.$.container.removeChild(child);


            var el = document.createElement('sauron-header');
            el.setAttribute("nombre", "@Recursos");
            this.$.container.appendChild(el);
            return false;
        });

        app_router.on('route:defaultRoute', (actions) => {
            alert(actions);
        });

        Backbone.history.start();
        */
    }
}

Polymer(AppComponent);
Polymer(HomeComponent);

