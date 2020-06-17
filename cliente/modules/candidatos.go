package modules

import "fmt"
import "main/configuration"
import "main/objects"
import "main/readers"
import "main/jsontypes"
import "main/utilities"


type Candidato = objects.Candidato


func CrearCandidato(){
	candidato := Candidato{}
	fmt.Println("CREAR CANDIDATO")
	jsontypes.ReadValidLine(&candidato.Cedula, "Ingrese la cedula: ")
	jsontypes.ReadValidLine(&candidato.Nombre, "Ingrese el nombre: ")
	jsontypes.ReadValidLine(&candidato.Apellidos, "Ingrese los apellidos: ")
	jsontypes.ReadValidLine(&candidato.Telefono, "Ingrese el telefono: ")
	jsontypes.ReadFileAsHexadecimalString(&candidato.Foto, 2*1024*1024, "Ingresara una foto [S/]: ", "Ingrese la ubicacion de la foto: ", "Debe ingresar una ruta correcta de un archivo de maximo 2 MB")
	jsontypes.ReadValidLine(&candidato.NitPartido, "Ingrese el nit del partido: ")

	request, _ := utilities.CreateRequest("POST", configuration.UrlCandidatos, &candidato)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func EditarCandidato(){
	fmt.Println("EDITAR CANDIDATO")
	candidato := Candidato{}
	jsontypes.ReadValidLine(&candidato.Cedula, "Ingrese la cedula: ")
	jsontypes.ReadConfirmationAndValidLine(&candidato.Nombre, "Modificara el nombre [S/]: ", "Ingrese el nombre: ")
	jsontypes.ReadConfirmationAndValidLine(&candidato.Apellidos, "Modificara los apellidos [S/]: ", "Ingrese los apellidos: ")
	jsontypes.ReadConfirmationAndValidLine(&candidato.Telefono, "Modificara el telefono [S/]: ", "Ingrese el telefono: ")
	jsontypes.ReadFileAsHexadecimalString(&candidato.Foto, 2*1024*1024, "Modificara la foto [S/]: ", "Ingrese la ubicacion de la foto: ", "Debe ingresar una ruta correcta de un archivo de maximo 2 MB")
	jsontypes.ReadConfirmationAndValidLine(&candidato.NitPartido, "Cambiara de partido [S/]: ", "Ingrese el nit del partido: ")

	request, _ := utilities.CreateRequest("PUT", configuration.UrlCandidatos, &candidato)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func InscribirCandidato(){
	lineReader := readers.NewLineReader()
	fmt.Println("INSCRIBIR CANDIDATO EN ELECCION")
	fmt.Print("Ingrese la cedula del candidato: ")
	cedulaCandidato := lineReader.ReadLine()
	fmt.Print("Ingrese el codigo de la eleccion: ")
	codigoEleccion := lineReader.ReadLine()
	request, _ := utilities.CreateRequest("POST", configuration.UrlCandidatos + "/" + cedulaCandidato + "/eleccion/" + codigoEleccion, nil)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func CancelarInscripcionCandidato(){
	lineReader := readers.NewLineReader()
	fmt.Println("CANCELAR INSCRIPCION DE CANDIDATO EN ELECCION")
	fmt.Print("Ingrese la cedula del candidato: ")
	cedulaCandidato := lineReader.ReadLine()
	fmt.Print("Ingrese el codigo de la eleccion: ")
	codigoEleccion := lineReader.ReadLine()
	request, _ := utilities.CreateRequest("DELETE", configuration.UrlCandidatos + "/" + cedulaCandidato + "/eleccion/" + codigoEleccion, nil)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func ListarCandidatos(){
	lineReader := readers.NewLineReader()
	fmt.Println("LISTAR CANDIDATOS")
	fmt.Println("1. TODOS")
	fmt.Println("2. POR PARTIDO")
	fmt.Println("3. POR ELECCION")

	fmt.Print("Ingrese la opcion para listarlas: ")
	line := lineReader.ReadLine()

	var candidatos []Candidato
	var err error
	switch line {
	case "1":
		request, _ := utilities.CreateRequest("GET", configuration.UrlCandidatos + "/*", nil)
		err = utilities.SendRequest(request, &candidatos, &ErrorStatusMessage{})
	case "2":
		fmt.Print("Ingrese el nit de la partido: ")
		nitPartido := lineReader.ReadLine()
		request, _ := utilities.CreateRequest("GET", configuration.UrlCandidatos + "/*/partido/" + nitPartido, nil)
		err = utilities.SendRequest(request, &candidatos, &ErrorStatusMessage{})
	case "3":
		fmt.Print("Ingrese el codigo de la eleccion: ")
		codigoEleccion := lineReader.ReadLine()
		request, _ := utilities.CreateRequest("GET", configuration.UrlCandidatos + "/*/eleccion/" + codigoEleccion, nil)
		err = utilities.SendRequest(request, &candidatos, &ErrorStatusMessage{})
	default:
		fmt.Println("Opcion incorrecta")
		return ;
	}
	
	if err == nil {
		utilities.PrintCenter("CEDULA", 10)
		utilities.PrintCenter("NOMBRE", 10)
		utilities.PrintCenter("APELLIDOS", 15)
		utilities.PrintCenter("TELEFONO", 14)
		utilities.PrintCenter("FOTO", 50)
		utilities.PrintCenter("NIT-PARTIDO", 10)
		fmt.Println()
		for i := 0; i < len(candidatos); i++ {
			utilities.PrintCenter(candidatos[i].Cedula.Value, 10)
			utilities.PrintCenter(candidatos[i].Nombre.Value, 10)
			utilities.PrintCenter(candidatos[i].Apellidos.Value, 15)
			utilities.PrintCenter(candidatos[i].Telefono.Value, 14)
			utilities.PrintCenter(configuration.UrlCandidatos + "/" + candidatos[i].Cedula.Value + "/foto", 50)
			utilities.PrintCenter(candidatos[i].NitPartido.Value, 10)	
			fmt.Println()
		}
	}
}
