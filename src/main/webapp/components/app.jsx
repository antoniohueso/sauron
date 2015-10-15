class App extends React.Component {

    constructor() {

        var arr = [];
        for(var i = 0 ; i < 5000; i++ ) {
            arr.push({id:i, nombre:'Antonio Hueso García '+i,edad:i});
        }
        this.state = { data: arr };

    }

    handlerClick(e,index) {
        console.log("Siiiiii",index);

        this.setState({
            data: React.addons.update(this.state.data, {$splice: [[index, 1]]})
        });
       /*
        this.setState({
            data: this.state.data.filter((_, i) => i !== index)
        });*/
        //console.log(this.state.data.length);
    }

    render() {

        console.log("Otra VEz!");

        var tableData = this.state.data.map((d,index) =>{
           return (
               <tr key={d.id}>
                    <td>{d.nombre}</td>
                    <td>{d.edad}</td>
                    <td><a><i onClick={e => this.handlerClick(e,index)} className="glyphicon glyphicon-book"></i></a></td>
                </tr>
           );
        });



        return (
            <div>
                <Header />

                <table className="table">
                    <thead>
                        <tr>
                            <td>Nombre</td>
                            <td>Edad</td>
                            <td>Acción</td>
                        </tr>
                    </thead>
                    <tbody>
                    {tableData}
                    </tbody>

                </table>


            </div>
        );
    }
}


App.defaultProps = { initialCount: 0 };

ReactDOM.render(
    <App />,
    document.getElementById('container')
);

