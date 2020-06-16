package modules

import "fmt"
import "encoding/json"
import "net/http"
import "time"
import "bytes"
import "main/configuration"
import "main/objects"
import "main/readers"
import "main/jsontypes"
import "main/utilities"

type Eleccion = objects.Eleccion
type ErrorStatusMessage = objects.ErrorStatusMessage


func CrearEleccion(){
	eleccion := Eleccion{}
	fmt.Println("CREAR ELECCION")
	jsontypes.ReadValidLine(&eleccion.Codigo, "Ingrese el codigo: ")
	jsontypes.ReadValidLine(&eleccion.Nombre, "Ingrese el nombre: ")
	jsontypes.ReadValidLine(&eleccion.Descripcion, "Ingrese la descripcion: ")
	jsontypes.ReadValidLine(&eleccion.FechaInicio, "Ingrese la fecha de inicio: ")
	jsontypes.ReadValidLine(&eleccion.FechaFin, "Ingrese la fecha de cierre: ")
	zonaHoraria, _ := time.Now().Zone()
	eleccion.ZonaHoraria.Assign(zonaHoraria)

	eleccionBytes, _ := json.Marshal(eleccion)
	request, _ := http.NewRequest("POST", configuration.UrlElecciones, bytes.NewBuffer(eleccionBytes))
	request.Header.Add("Content-Type", "application/json")

	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func EditarEleccion(){
	eleccion := Eleccion{}
	fmt.Println("EDITAR ELECCION")
	jsontypes.ReadValidLine(&eleccion.Codigo, "Ingrese el codigo: ")
	jsontypes.ReadConfirmationAndValidLine(&eleccion.Nombre, "Modificara el nombre [S/]", "Ingrese el nombre: ")
	jsontypes.ReadConfirmationAndValidLine(&eleccion.Descripcion, "Modificara la descripcion [S/]", "Ingrese la descripcion: ")
	jsontypes.ReadConfirmationAndValidLine(&eleccion.FechaInicio, "Modificara la fecha de inicio [S/]", "Ingrese la fecha de inicio: ")
	jsontypes.ReadConfirmationAndValidLine(&eleccion.FechaFin, "Modificara la fecha de cierre [S/]", "Ingrese la fecha de cierre: ")
	zonaHoraria, _ := time.Now().Zone()
	eleccion.ZonaHoraria.Assign(zonaHoraria)
}

func FinalizarEleccion(){
	reader := readers.NewLineReader()
	fmt.Println("Ingrese el codigo de la eleccion a finalizar: ")
	codigo := reader.ReadLine()
	request, _ := http.NewRequest("PUT", configuration.UrlElecciones + "/" + codigo + "/finalizar", nil)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func CancelarEleccion(){
	reader := readers.NewLineReader()
	fmt.Println("Ingrese el codigo de la eleccion a cancelar: ")
	codigo := reader.ReadLine()
	request, _ := http.NewRequest("PUT", configuration.UrlElecciones + "/" + codigo + "/cancelar", nil)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}


func ListarElecciones(){
	reader := readers.NewLineReader()
	fmt.Println("LISTAR ELECCIONES")
	fmt.Println("1. FINALIZADAS")
	fmt.Println("2. ABIERTAS")
	fmt.Println("3. PENDIENTES")
	fmt.Println("4. CANCELADAS")

	fmt.Print("Ingrese la opcion para listarlas: ")
	line := reader.ReadLine()

	var filtro string
	zonaHoraria, _ := time.Now().Zone()

	switch line {
	case "1":
		filtro = "FINALIZADAS"
	case "2":
		filtro = "ABIERTAS"
	case "3":
		filtro = "PENDIENTES"
	case "4":
		filtro = "CANCELADAS"
	default:
		fmt.Println("Opcion incorrecta")
		break;
	}

	request, _ := http.NewRequest("GET", configuration.UrlElecciones + "/*/filtro/" + filtro + "/zona-horaria/" + zonaHoraria, nil)
	var elecciones []Eleccion

	err := utilities.SendRequest(request, &elecciones, &ErrorStatusMessage{})
	fmt.Println(err)
	fmt.Println(elecciones)

	utilities.PrintCenter("CODIGO", 10)
	utilities.PrintCenter("NOMBRE", 10)
	utilities.PrintCenter("DESCRIPCION", 15)
	utilities.PrintCenter("FECHA-INICIO", 23)
	utilities.PrintCenter("FECHA-FIN", 23)
	fmt.Println()
	for i := 0; i < len(elecciones); i++ {
		utilities.PrintCenter(elecciones[i].Codigo.Value, 10)
		utilities.PrintCenter(elecciones[i].Nombre.Value, 10)
		utilities.PrintCenter(elecciones[i].Descripcion.Value, 15)
		utilities.PrintCenter(elecciones[i].FechaInicio.Value, 23)
		utilities.PrintCenter(elecciones[i].FechaFin.Value, 23)
		fmt.Println()
	}
}