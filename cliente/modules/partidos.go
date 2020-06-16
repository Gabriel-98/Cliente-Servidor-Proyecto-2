package modules

import "fmt"
import "main/jsontypes"
import "main/objects"
import "main/readers"


type Partido = objects.Partido

var (
	lineReader readers.LineReader = readers.NewLineReader()
)

//var Url string

//	lineReader := readers.NewLineReader()
//	fileReader := readers.NewFileReader(2*1024*1024)

func CrearPartido(){
	partido := Partido{}

	fmt.Println("CREAR PARTIDO POLITICO")
	jsontypes.ReadValidLine(&partido.Nit, "Ingrese el nit: ")
	jsontypes.ReadValidLine(&partido.Nombre, "Ingrese el nombre: ")
	jsontypes.ReadValidLine(&partido.Telefono, "Ingrese el telefono: ")
	jsontypes.ReadValidLine(&partido.Direccion, "Ingrese la direccion: ")
	jsontypes.ReadValidLine(&partido.Administracion, "Ingrese la administracion: ")
	jsontypes.ReadFileAsHexadecimalString(&partido.Foto, 2*1024*1024, "Ingresara una foto [S/]: ", "Ingrese la ubicacion de la foto: ", "Debe ingresar una ruta correcta de un archivo de maximo 2 MB")

	fmt.Println(partido)
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

	fmt.Println(partido)

	

}

func EliminarPartido(){
	fmt.Println("ELIMINAR PARTIDO POLITICO")

	fmt.Print("Ingrese el nit del partido a eliminar: ")
	nit := lineReader.ReadLine()

	fmt.Println(nit)
}

func ListarPartidos(){
	fmt.Println("LISTAR PARTIDOS POLITICOS")


}