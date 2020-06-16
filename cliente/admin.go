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
	fmt.Println("14. Consultar resultado de una eleccion")
	fmt.Println("15. Listar elecciones")
	// Votantes
	fmt.Println("16. Ver informacion de votante")
	fmt.Println("17. Bloquear votante")
	fmt.Println("18. Desbloquear votante")
	fmt.Println("19. Registrar votante en una eleccion")
	fmt.Println("20. Cancelar registro de un votante a una eleccion")
	fmt.Println("21. Consultar votos realizados")
	fmt.Println("22. Listar votantes")

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
	case "10":
		modules.CrearEleccion()
	case "11":
		modules.EditarEleccion()
	default:
		fmt.Println("Codigo incorrecto")
	}
}


func main(){
	menu()
	//leerFoto()
	//listarElecciones()
	//crearEleccion()
	//modules.CrearPartido()

	//convert2()
	//convert()
	//tiempo()
	// string
	/*var x string
	var lim int = 1e6
	for i:=0; i<lim; i++ {
		x += "a"
	}
	fmt.Println(len(x))*/
	// slice
	//var x []int
	/*var lim int = 1e6
	for i:=0; i<lim; i++ {
		x = append(x, 1)
	}*/
	//fmt.Println(len(x))


	/*partido := Partido{
		Nit: 			JsonString{"8",true},
		Nombre: 		JsonString{"fgfdgdfs",true},
		Telefono: 		JsonString{"3456465741",true},
		Direccion: 		JsonString{"cra x #8-15",true},
		Administracion: JsonString{"asfas",true},
		Foto:			JsonString{"0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff0aff",true},
	}
	partidoBytes, _ := json.Marshal(partido)

	fmt.Println(partido)
	fmt.Println(partidoBytes)
	fmt.Println(string(partidoBytes))

	client := http.Client{}

	request, err := http.NewRequest("POST", "http://localhost:8080/partidos", bytes.NewBuffer(partidoBytes))
	request.Header.Add("Content-Type", "application/json")
	fmt.Println()
	fmt.Println(request)
	fmt.Println(err)

	response, err := client.Do(request)
	fmt.Println()
	fmt.Println(response)
	fmt.Println(err)*/


	//var a string = "35465456"
	//x := &a
	/*p := Partido{Nit:"5656", Nombre:"verde", Telefono:"315156657", Direccion:"cra 10 #5-15"}
	fmt.Println(p)

	arr, err := json.Marshal(p)
	if err != nil {
		fmt.Println("error")
	}else {
		jsonString := string(arr)
		fmt.Println(jsonString)
	}

	fmt.Println(p.Administracion)*/

	/*
	u := User{
			Nombre:JsonString{"dfsd", true},
			Telefonos:[]JsonString{JsonString{"as",true}},
		}
	fmt.Println(u)
	arr2, err := json.Marshal(u)
	if err != nil {
		fmt.Println("error")
	}else{
		fmt.Println(string(arr2))
	}

	var x int
	fmt.Println(reflect.ValueOf(x))

	j := JsonInt{Value:56, Set:true}
	fmt.Println(j)

	m, err := json.Marshal(j)
	if err != nil {
		fmt.Println(err)
	}else {
		fmt.Println(string(m))
	}

	var y []int64 = []int64{}
	fmt.Println(reflect.ValueOf(y))
	if y == nil {
		fmt.Println(".")
	}*/
}