package modules

import "fmt"
import "time"
import "strconv"
import "main/configuration"
import "main/objects"
import "main/readers"
import "main/jsontypes"
import "main/utilities"

type Eleccion = objects.Eleccion
type ResultadoCandidato = objects.ResultadoCandidato
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

	request, _ := utilities.CreateRequest("POST", configuration.UrlElecciones, &eleccion)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func EditarEleccion(){
	eleccion := Eleccion{}
	fmt.Println("EDITAR ELECCION")
	jsontypes.ReadValidLine(&eleccion.Codigo, "Ingrese el codigo: ")
	jsontypes.ReadConfirmationAndValidLine(&eleccion.Nombre, "Modificara el nombre [S/]: ", "Ingrese el nombre: ")
	jsontypes.ReadConfirmationAndValidLine(&eleccion.Descripcion, "Modificara la descripcion [S/]: ", "Ingrese la descripcion: ")
	jsontypes.ReadConfirmationAndValidLine(&eleccion.FechaInicio, "Modificara la fecha de inicio [S/]: ", "Ingrese la fecha de inicio: ")
	jsontypes.ReadConfirmationAndValidLine(&eleccion.FechaFin, "Modificara la fecha de cierre [S/]: ", "Ingrese la fecha de cierre: ")
	zonaHoraria, _ := time.Now().Zone()
	eleccion.ZonaHoraria.Assign(zonaHoraria)

	request, _ := utilities.CreateRequest("PUT", configuration.UrlElecciones, &eleccion)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func FinalizarEleccion(){
	lineReader := readers.NewLineReader()
	fmt.Println("FINALIZAR ELECCION")
	fmt.Println("Ingrese el codigo de la eleccion a finalizar: ")
	codigo, _ := lineReader.ReadLine()
	request, _ := utilities.CreateRequest("PUT", configuration.UrlElecciones + "/" + codigo + "/finalizar", nil)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func CancelarEleccion(){
	lineReader := readers.NewLineReader()
	fmt.Println("CANCELAR ELECCION")
	fmt.Print("Ingrese el codigo de la eleccion a cancelar: ")
	codigo, _ := lineReader.ReadLine()
	request, _ := utilities.CreateRequest("PUT", configuration.UrlElecciones + "/" + codigo + "/cancelar", nil)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func MostrarResultadoEleccion(){
	lineReader := readers.NewLineReader()
	fmt.Println("RESULTADO ELECCION")
	fmt.Print("Ingrese el codigo de la eleccion: ")
	codigoEleccion, _ := lineReader.ReadLine()

	var resultadoCandidatos []ResultadoCandidato
	request, err := utilities.CreateRequest("GET", configuration.UrlElecciones + "/" + codigoEleccion + "/resultado", nil)
	err = utilities.SendRequest(request, &resultadoCandidatos, &ErrorStatusMessage{})

	if err == nil {
		var candidatos []Candidato
		request, _ = utilities.CreateRequest("GET", configuration.UrlCandidatos + "/*/eleccion/" + codigoEleccion, nil)
		err = utilities.SendRequest(request, &candidatos, &ErrorStatusMessage{})

		fmt.Println(resultadoCandidatos)
		fmt.Println(candidatos)

		mapaCandidatos := make(map[string]Candidato)
		for i:=0; i<len(candidatos); i++ {
			mapaCandidatos[candidatos[i].Cedula.Value] = candidatos[i]
		}

		utilities.PrintCenter("CEDULA", 10)
		utilities.PrintCenter("NOMBRE", 10)
		utilities.PrintCenter("APELLIDOS", 15)
		utilities.PrintCenter("VOTOS", 11)
		fmt.Println()
		for i:=0; i<len(resultadoCandidatos); i++ {
			if candidato, ok := mapaCandidatos[resultadoCandidatos[i].CedulaCandidato.Value]; ok {
				utilities.PrintCenter(candidato.Cedula.Value, 10)
				utilities.PrintCenter(candidato.Nombre.Value, 10)
				utilities.PrintCenter(candidato.Apellidos.Value, 15)
				numeroVotos := strconv.FormatInt(resultadoCandidatos[i].NumeroVotos.Value, 10)
				utilities.PrintCenter(numeroVotos, 11)
				fmt.Println()
			}
		}
	}
}


func ListarElecciones(){
	lineReader := readers.NewLineReader()
	fmt.Println("LISTAR ELECCIONES")
	fmt.Println("1. FINALIZADAS")
	fmt.Println("2. ABIERTAS")
	fmt.Println("3. PENDIENTES")
	fmt.Println("4. CANCELADAS")

	fmt.Print("Ingrese la opcion para listarlas: ")
	line, _ := lineReader.ReadLine()

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
		return ;
	}

	var elecciones []Eleccion
	request, _ := utilities.CreateRequest("GET", configuration.UrlElecciones + "/*/filtro/" + filtro + "/zona-horaria/" + zonaHoraria, nil)
	err := utilities.SendRequest(request, &elecciones, &ErrorStatusMessage{})

	if err == nil {
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
}