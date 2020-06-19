package modules

import "fmt"
import "main/configuration"
import "main/objects"
import "main/jsontypes"
import "main/readers"
import "main/utilities"


type Votante = objects.Votante
type Voto = objects.Voto


func CrearVotante(){
	votante := Votante{}
	fmt.Println("CREAR VOTANTE")
	jsontypes.ReadValidLine(&votante.Cedula, "Ingrese la cedula: ")
	jsontypes.ReadValidLine(&votante.Email, "Ingrese el email: ")
	jsontypes.ReadValidLine(&votante.Nombre, "Ingrese el nombre: ")
	jsontypes.ReadValidLine(&votante.Apellidos, "Ingrese los apellidos: ")
	jsontypes.ReadValidLine(&votante.FechaNacimiento, "Ingrese la fecha de nacimiento: ")
	jsontypes.ReadValidLine(&votante.Telefono, "Ingrese el telefono: ")
	jsontypes.ReadFileAsHexadecimalString(&votante.Foto, 2*1024*1024, "Ingresara una foto [S/]: ", "Ingrese la ubicacion de la foto: ", "Debe ingresar una ruta correcta de un archivo de maximo 2 MB")

	var votanteRespuesta Votante
	request, _ := utilities.CreateRequest("POST", configuration.UrlVotantes, &votante)
	var err error
	err = utilities.SendRequest(request, &votanteRespuesta, &ErrorStatusMessage{})

	if err == nil { 
		fmt.Println("Password: ", votanteRespuesta.Password.Value)
	}
}

func Votar(){
	lineReader := readers.NewLineReader()
	fmt.Println("VOTAR")
	fmt.Print("Ingrese su cedula: ")
	cedula, _ := lineReader.ReadLine()
	fmt.Print("Ingrese el codigo de la eleccion: ")
	codigoEleccion, _ := lineReader.ReadLine()
	fmt.Print("Ingrese la cedula del candidato: ")
	cedulaCandidato, _ := lineReader.ReadLine()

	request, _ := utilities.CreateRequest("POST", configuration.UrlVotantes + "/" + cedula + "/voto/" + codigoEleccion + "/" + cedulaCandidato, nil)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func BloquearVotante(){
	lineReader := readers.NewLineReader()
	fmt.Println("BLOQUEAR VOTANTE")
	fmt.Print("Ingrese la cedula: ")
	cedula, _ := lineReader.ReadLine()

	request, _ := utilities.CreateRequest("PUT", configuration.UrlVotantes + "/" + cedula + "/bloquear", nil)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func DesbloquearVotante(){
	lineReader := readers.NewLineReader()
	fmt.Println("DESBLOQUEAR VOTANTE")
	fmt.Print("Ingrese la cedula: ")
	cedula, _ := lineReader.ReadLine()

	request, _ := utilities.CreateRequest("PUT", configuration.UrlVotantes + "/" + cedula + "/desbloquear", nil)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func ListarVotos(){
	lineReader := readers.NewLineReader()
	fmt.Println("LISTAR VOTOS")
	fmt.Print("Ingrese la cedula: ")
	cedula, _ := lineReader.ReadLine()

	var votos []Voto
	request, _ := utilities.CreateRequest("GET", configuration.UrlVotantes + "/" + cedula + "/votos", nil)
	err := utilities.SendRequest(request, &votos, &ErrorStatusMessage{})

	if err == nil {
		utilities.PrintCenter("CODIGO", 14)
		utilities.PrintCenter("CANDIDATO", 14)
		fmt.Println()
		for i := 0; i < len(votos); i++ {
			utilities.PrintCenter(votos[i].CodigoEleccion.Value, 14)
			utilities.PrintCenter(votos[i].CedulaCandidato.Value, 14)
			fmt.Println()
		}
	}
}

func ListarVotantes(){
	lineReader := readers.NewLineReader()
	fmt.Println("LISTAR VOTANTES")
	fmt.Println("1. ACTIVOS")
	fmt.Println("2. BLOQUEADOS")
	fmt.Println("3. REGISTRADOS EN UNA ELECCION")

	fmt.Print("Ingrese la opcion para listarlas: ")
	line, _ := lineReader.ReadLine()

	var votantes []Votante
	var err error
	switch line {
	case "1":
		request, _ := utilities.CreateRequest("GET", configuration.UrlVotantes + "/*/ACTIVOS", nil)
		err = utilities.SendRequest(request, &votantes, &ErrorStatusMessage{})
	case "2":
		request, _ := utilities.CreateRequest("GET", configuration.UrlVotantes + "/*/BLOQUEADOS", nil)
		err = utilities.SendRequest(request, &votantes, &ErrorStatusMessage{})
	case "3":
		fmt.Print("Ingrese el codigo de la eleccion: ")
		codigoEleccion, _ := lineReader.ReadLine()
		request, _ := utilities.CreateRequest("GET", configuration.UrlVotantes + "/*/eleccion/" + codigoEleccion, nil)
		err = utilities.SendRequest(request, &votantes, &ErrorStatusMessage{})
	default:
		fmt.Println("Opcion incorrecta")
		return ;
	}
	
	if err == nil {
		utilities.PrintCenter("CEDULA", 10)
		utilities.PrintCenter("EMAIL", 10)
		utilities.PrintCenter("NOMBRE", 10)
		utilities.PrintCenter("APELLIDOS", 10)
		utilities.PrintCenter("FECHA-NACIMIENTO", 10)
		utilities.PrintCenter("TELEFONO", 10)
		utilities.PrintCenter("PASSWORD", 10)
		utilities.PrintCenter("FOTO", 50)
		fmt.Println()
		for i := 0; i < len(votantes); i++ {
			utilities.PrintCenter(votantes[i].Cedula.Value, 10)
			utilities.PrintCenter(votantes[i].Email.Value, 10)
			utilities.PrintCenter(votantes[i].Nombre.Value, 10)
			utilities.PrintCenter(votantes[i].Apellidos.Value, 10)
			utilities.PrintCenter(votantes[i].FechaNacimiento.Value, 10)
			utilities.PrintCenter(votantes[i].Telefono.Value, 10)
			utilities.PrintCenter(votantes[i].Password.Value, 10)
			utilities.PrintCenter(configuration.UrlVotantes + "/" + votantes[i].Cedula.Value + "/foto", 50)
			fmt.Println()
		}
	}
}