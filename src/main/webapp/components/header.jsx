class Header extends React.Component {

    render() {

        return (
            <header>
                <nav className="navbar navbar-inverse">
                    <div className="container-fluid">
                        <div className="navbar-header">
                            <button type="button" className="navbar-toggle collapsed" data-toggle="collapse" data-target="#bs-example-navbar-collapse-9" aria-expanded="false">
                                <span className="sr-only">Toggle navigation</span>
                                <span className="icon-bar"></span>
                                <span className="icon-bar"></span>
                                <span className="icon-bar"></span>
                            </button>
                            <a className="navbar-brand" href="/">Projectplan</a>
                        </div>

                        <div className="collapse navbar-collapse" id="bs-example-navbar-collapse-9">
                            <ul className="nav navbar-nav">
                                <li className="active"><a href="/proyectos">Proyectos</a></li>
                                <li><a href="/recursos">Recursos</a></li>
                                <li><a href="/interesados">Interesados</a></li>
                                <li><a href="/calendario">Calendario</a></li>
                            </ul>
                        </div>
                    </div>
                </nav>
            </header> 
        );
        
    }
}

