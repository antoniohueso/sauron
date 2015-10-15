/***
 *
 * TODO:
 *
 * - Poner PropTypes en todas las clases
 * - Hacer MultiSelect la tabla
 * - Hacer métodos para borrar, añadir, modificar, cargar y filtrar
 * - Quitar la propiedad data para el datatable, será una propiedad interna de la tabla que se alimentará
 *   de la base de datos
 * - Poner como propiedades el endpoint CRUD, mejor que me pasen una url y a partir de ahi, le añadimos all, add, delete, update
 *
 */

function checkValidChildren(clase,children,componentes) {

    children.map((child) => {

        if(!child.type || componentes.indexOf(child.type) < 0) {
            throw new Error(clase+": Solo se permiten componentes de tipo "+componentes)
        }
    });
}
/**
 * Propiedades:
 *
 * data
 * EditAction
 * DeleteAction
 *
 */
class DataTable extends React.Component {

    constructor(props) {
        super(props);

        if(props.data) this.state={data:props.data};
        else this.state={data:[]};

    }

    getDataTableRow(props) {
        return React.cloneElement(this.dataTableRow, props);
    }

    getHeaderElements(children) {
        return React.Children.toArray(this.dataTableRow.props.children).map((child) => {
            return <th key={child.props.label}>{child.props.label}</th>
        });
    }

    getBodyElemens() {
        return this.state.data.map((d) =>{
            return this.getDataTableRow({key:d[this.props.keyProperty], data:d})
        });
    }

    render() {
        return (
            <table className="table">
                <thead>
                <tr>
                    {this.getHeaderElements()}
                </tr>
                </thead>
                <tbody>
                {this.getBodyElemens()}
                </tbody>
            </table>
        );

    }
}

/**
 * Fila que contiene varias DataTableColumn y Una barra de toolbar con acciones
 */
class DataTableRow extends React.Component {

    constructor() {
        this.state = {
            data:this.props.data,
            editing:false,
            cancelEditing: false
        }
    }

    render() {

        if(this.state.editing) {
            return this.props.children.map(child => {
                return <td>{React.cloneElement(child,{data:data})}</td>
            });
        }
        else {

        }



        return (
            <tr>
                {this.getChildren(React.Children.toArray(this.props.children))}
            </tr>
        )
    }
}

/**
 * Acción del toolbar de acciones de la tabla
 *
 * Propiedades:
 * onClick: handler
 * label: Texto del link
 *
 * Opcionales:
 * className -> Con el icono de glypicon
 */
class DataTableAction extends React.Component {

    render() {
        return <a onClick={this.props.onClick}><span>{this.props.label}</span><i className={this.props.className}></i></a>
    }
}

/**
 * Toolbar de acciones
 *
 * Propiedades:
 *
 * label: Nombre de la columna
 */
class DataTableToolbar extends React.Component {

    constructor(props) {
        super(props);
    }

    render() {
        return <td {...this.props}>{this.props.children}</td>;
    }
}

/**
 * DataTableColumm
 * Columna de tipo texto.
 *
 * Propiedades:
 * value -> Valor del campo a mostrar
 */
class DataTableColumn extends React.Component {

    getElement() {
        return <span>{this.props.value}</span>;
    }

    render() {
        return <td {...this.props}>{this.getElement()}</td>
    }
}



class PrjDataTable extends React.Component {

    constructor(props) {
        super(props);
        this.state={data:props.data};
    }

    handleSave(e,oldRow,newRow) {
        console.log("Implementar Guardar en BD!");
        console.log(this.state.data);
    }

    handleDelete(e,row) {

        console.log("Implementar Borrar de la BD! y un mensaje de confirmación");
        var newData = this.state.data;
        var index = newData.indexOf(row);
        newData.splice(index,1);
        this.setState({data:newData});

    }

    add(row) {
        console.log("Add!");
    }

    render() {

        var header = React.Children.toArray(this.props.children).map((child) => {
            return <th key={child.props.label}>{child.props.label}</th>
        });


        return (
            <table className="table">
                <thead>
                <tr>
                    {header}
                    <th key="accion">Acción</th>
                </tr>
                </thead>
                <tbody>
                {
                    this.state.data.map((d) =>{
                        return (
                            <PrjDataTableRow key={d[this.props.keyProperty]} data={d}
                                             onSave={this.handleSave.bind(this)}
                                             onDelete={this.handleDelete.bind(this)}
                                             keyProperty={this.props.keyProperty}>
                                {this.props.children}
                            </PrjDataTableRow>
                        );
                    })
                }
                </tbody>
            </table>
        );

    }

}
PrjDataTable.propTypes = {
    ref: React.PropTypes.string,
    data: React.PropTypes.array,
    keyProperty: React.PropTypes.string,
};

class PrjDataTableRow extends React.Component {

    constructor(props) {
        super(props);
        this.state = {
            editing:false,
            cancelEditing:false,
            data: props.data
        };

        this.getValue = function() {
            var newData = this.state.data;
            for(var p in this.refs) {
                newData[p] = this.refs[p].inputValue;
            }
            return newData;
        }
    }

    handleEdit(e) {
        this.setState({editing:true,cancelEditing:false});
    }

    handleCancel(e) {
        this.setState({editing:false,cancelEditing:true});
    }

    handleSave(e) {
        this.setState({editing:false , cancelEditing:false , data:this.getValue()});
        this.props.onSave(e,this.props.data,this.state.data);
    }

    handleDelete(e) {
        this.props.onDelete(e,this.state.data);
    }

    render() {

        var children = React.Children.toArray(this.props.children);

        var actionButtons = null;

        if (this.state.editing == true) {
            actionButtons = <td>
                <a onClick={this.handleCancel.bind(this)}><i className="glyphicon glyphicon-remove"></i></a>
                <a onClick={this.handleSave.bind(this)}><i className="glyphicon glyphicon-ok"></i></a>
            </td>
        }
        else {
            actionButtons = <td>
                <a onClick={this.handleEdit.bind(this)}><i className="glyphicon glyphicon-edit"></i></a>
                <a onClick={this.handleDelete.bind(this)}><i className="glyphicon glyphicon-trash"></i></a>
            </td>
        }

        return (
            <tr>
                {
                    children.map((child) => {

                        if(!child.type || [PrjDataTableTextarea,PrjDataTableInput
                                ,PrjDataTableText,PrjDataTableSelect].indexOf(child.type) < 0) {
                            throw new Error("Solo se permiten componentes de tipo "+['PrjDataTableInput',
                                    'PrjDataTableTextarea','PrjDataTableText','PrjDataTableSelect'])
                        }

                        return React.cloneElement(child, {
                            ref: child.props.property,
                            editing: this.state.editing,
                            cancelEditing: this.state.cancelEditing,
                            value: this.state.data[child.props.property]
                        });

                    })
                }
                {actionButtons}
            </tr>
        )


    }

}


class AbstractPrjDataTableField extends React.Component {

    constructor(props) {
        super(props);
        this.inputValue = this.props.value;
    }

    handleChange(e) {
        this.inputValue = e.target.value;
    }

    transferProps() {
        var props = {};

        for(var prop in this.props) {
            if(['value','width'].indexOf(prop) < 0 ) {
                props[prop] = this.props[prop];
            }
        }

        return props;
    }

    getFieldElement() {
        throw new Error("Clase abtracta hay que implementar este método");
    }

    renderEditableField(field) {
        return <td width={this.props.width}>{field}</td>
    }

    renderField() {
        return <td width={this.props.width}>{this.inputValue}</td>
    }

    render() {

        if (this.props.editing) {

            //--- Obtiene el elemento
            var field = this.getFieldElement();

            //--- Copia las propiedades del elemento padre
            var props = this.transferProps();
            //--- Añade el default value , el evento de onChange y la clase de BootStrap
            if(!field.props.defaultValue) props.defaultValue=this.inputValue;
            props.onChange=this.handleChange.bind(this);
            props.className="form-control";


            //--- Transfiere las propiedades al elemento creado
            field = React.cloneElement(field, props);

            return this.renderEditableField(field);
        }

        if(this.props.cancelEditing) {
            this.inputValue = this.props.value;
        }

        return this.renderField();
    }

}

class PrjDataTableText extends AbstractPrjDataTableField {

    render() {
        return this.renderField();
    }

}

class PrjDataTableInput extends AbstractPrjDataTableField {

    getFieldElement() {
        return <input />
    }

}

class PrjDataTableTextarea extends AbstractPrjDataTableField {

    getFieldElement() {
        return <textarea />
    }
}

/**
 * El campo de los datos debe ser un objeto tipo con un código y una descripción y
 * las opciones deben tener el mismo formato y llamarse igual los campos que los que vienen en el objeto
 */
class PrjDataTableSelect extends AbstractPrjDataTableField {

    constructor(props) {
        super(props);
        this.indexOption = {};
        props.options.map(option => {
            this.indexOption[option[props.optionKey]] = option;
        });
    }

    handleChange(e) {
        this.inputValue = this.indexOption[e.target.value];
    }

    getFieldElement() {

        var options = this.props.options.map(option => {
            return <option key={option[this.props.optionKey]} value={option[this.props.optionKey]}>
                {option[this.props.optionText]}
            </option>
        });

        return <select defaultValue={this.inputValue[this.props.optionKey]}>
                {options}
               </select>

    }

    renderField() {
        return <td width={this.props.width}>{this.inputValue[this.props.optionText]}</td>
    }

}

