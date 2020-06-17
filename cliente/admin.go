package main

import "fmt"
//import "reflect"
//import "net/http"
import "time"

//import "main/objects"
import "main/modules"
import "main/jsontypes"
import "main/readers"

type JsonString = jsontypes.JsonString
type JsonInt = jsontypes.JsonInt
type JsonFloat = jsontypes.JsonFloat
type JsonBool = jsontypes.JsonBool


func tiempo(){
	//location, err := time.LoadLocation("America/Bogota")
	location, err := time.LoadLocation("-05:00")
	fmt.Println(location, "-", err)
	fmt.Println(time.Now())
	time2 := time.Time{}
	fmt.Println(time2.UTC())
	fmt.Println(time2.Local().UTC())
	name, off := time.Now().Zone()
	fmt.Println(name)
	fmt.Println(off)

	fmt.Println(time.Now().Zone())
}

func leerFoto(){
 	fileReader := readers.NewFileReader(2*1024*1024)
 	path := "/home/usuario/Descargas/pain.jpg"
 	fileString, err := fileReader.ReadAsHexadecimalString(path)

 	if err == nil {
 		fmt.Println(fileString[0:100])
 		fmt.Println(len(fileString))

 	}else{
 		fmt.Println(err)
 	}
}

func menu(){
	lineReader := readers.NewLineReader()
	var option string


	fmt.Println("Funciones:")
	// Partidos
	fmt.Println("1. Crear partido politico")
	fmt.Println("2. Editar partido politico");
	fmt.Println("3. Eliminar partido politico")
	fmt.Println("4. Listar partidos politicos")
	// Candidatos
	fmt.Println("5. Crear un candidato")
	fmt.Println("6. Editar informacion de un candidato")
	fmt.Println("7. Inscribir un candidato en una eleccion")
	fmt.Println("8. Cancelar inscripcion de un candidato en una eleccion")
	fmt.Println("9. Listado de candidatos")
	// Elecciones
	fmt.Println("10. Crear eleccion")
	fmt.Println("11. Editar eleccion")
	fmt.Println("12. Finalizar eleccion")
	fmt.Println("13. Cancelar eleccion")
	fmt.Println("14. [X] Consultar resultado de una eleccion")
	fmt.Println("15. Listar elecciones")
	// Votantes
/*	fmt.Println("16. Ver informacion de votante")
	fmt.Println("17. Bloquear votante")
	fmt.Println("18. Desbloquear votante")
	fmt.Println("19. Registrar votante en una eleccion")
	fmt.Println("20. Cancelar registro de un votante a una eleccion")
	fmt.Println("21. Consultar votos realizados")
	fmt.Println("22. Listar votantes")*/

	fmt.Print("Ingrese una opcion: ")
	option = lineReader.ReadLine()
	switch option {
	case "1":
		modules.CrearPartido()
	case "2":
		modules.EditarPartido()
	case "3":
		modules.EliminarPartido()
	case "4":
		modules.ListarPartidos()
	case "5":
		modules.CrearCandidato()
	case "6":
		modules.EditarCandidato()
	case "7":
		modules.InscribirCandidato()
	case "8":
		modules.CancelarInscripcionCandidato()
	case "9":
		modules.ListarCandidatos()
	case "10":
		modules.CrearEleccion()
	case "11":
		modules.EditarEleccion()
	case "12":
		modules.FinalizarEleccion()
	case "13":
		modules.CancelarEleccion()
	//case "14":
	case "15":
		modules.ListarElecciones()
	default:
		fmt.Println("Codigo incorrecto")
	}
}


func main(){
	menu()
}