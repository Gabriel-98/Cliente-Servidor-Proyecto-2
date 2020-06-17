package modules

import "fmt"
import "main/configuration"
import "main/objects"
import "main/readers"
import "main/jsontypes"
import "main/utilities"

type Partido = objects.Partido


func CrearPartido(){
	partido := Partido{}
	fmt.Println("CREAR PARTIDO POLITICO")
	jsontypes.ReadValidLine(&partido.Nit, "Ingrese el nit: ")
	jsontypes.ReadValidLine(&partido.Nombre, "Ingrese el nombre: ")
	jsontypes.ReadValidLine(&partido.Telefono, "Ingrese el telefono: ")
	jsontypes.ReadValidLine(&partido.Direccion, "Ingrese la direccion: ")
	jsontypes.ReadValidLine(&partido.Administracion, "Ingrese la administracion: ")
	jsontypes.ReadFileAsHexadecimalString(&partido.Foto, 2*1024*1024, "Ingresara una foto [S/]: ", "Ingrese la ubicacion de la foto: ", "Debe ingresar una ruta correcta de un archivo de maximo 2 MB")

	request, _ := utilities.CreateRequest("POST", configuration.UrlPartidos, &partido)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func EditarPartido(){
	fmt.Println("EDITAR PARTIDO POLITICO")
	partido := Partido{}
	jsontypes.ReadValidLine(&partido.Nit, "Ingrese el nit: ")
	jsontypes.ReadConfirmationAndValidLine(&partido.Nombre, "Modificara el nombre [S/]: ", "Ingrese el nombre: ")
	jsontypes.ReadConfirmationAndValidLine(&partido.Telefono, "Modificara el telefono [S/]: ", "Ingrese el telefono: ")
	jsontypes.ReadConfirmationAndValidLine(&partido.Direccion, "Modificara la direccion [S/]: ", "Ingrese la direccion: ")
	jsontypes.ReadConfirmationAndValidLine(&partido.Administracion, "Modificara la administracion [S/]: ", "Ingrese la administracion: ")
	jsontypes.ReadFileAsHexadecimalString(&partido.Foto, 2*1024*1024, "Modificara la foto [S/]: ", "Ingrese la ubicacion de la foto: ", "Debe ingresar una ruta correcta de un archivo de maximo 2 MB")

	request, _ := utilities.CreateRequest("PUT", configuration.UrlPartidos, &partido)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func EliminarPartido(){
	lineReader := readers.NewLineReader()
	fmt.Println("ELIMINAR PARTIDO POLITICO")
	fmt.Print("Ingrese el nit del partido a eliminar: ")
	nit := lineReader.ReadLine()
	request, _ := utilities.CreateRequest("DELETE", configuration.UrlPartidos + "/" + nit, nil)
	utilities.SendRequest(request, nil, &ErrorStatusMessage{})
}

func ListarPartidos(){
	fmt.Println("LISTAR PARTIDOS POLITICOS")
	var partidos []Partido
	request, _ := utilities.CreateRequest("GET", configuration.UrlPartidos + "/*", nil)
	err := utilities.SendRequest(request, &partidos, &ErrorStatusMessage{})

	if err == nil {
		utilities.PrintCenter("NIT", 10)
		utilities.PrintCenter("NOMBRE", 10)
		utilities.PrintCenter("TELEFONO", 15)
		utilities.PrintCenter("DIRECCION", 23)
		utilities.PrintCenter("ADMINISTRACION", 20)
		utilities.PrintCenter("FOTO", 50)
		fmt.Println()
		for i := 0; i < len(partidos); i++ {
			utilities.PrintCenter(partidos[i].Nit.Value, 10)
			utilities.PrintCenter(partidos[i].Nombre.Value, 10)
			utilities.PrintCenter(partidos[i].Telefono.Value, 15)
			utilities.PrintCenter(partidos[i].Direccion.Value, 23)
			utilities.PrintCenter(partidos[i].Administracion.Value, 20)
			utilities.PrintCenter(configuration.UrlPartidos + "/" + partidos[i].Nit.Value + "/foto", 50)
			fmt.Println()
		}
	}
}