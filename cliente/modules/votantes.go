package modules

import "fmt"
import "main/configuration"
import "main/objects"
import "main/jsontypes"
import "main/utilities"


type Votante = objects.Votante


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
	utilities.SendRequest(request, &votanteRespuesta, &ErrorStatusMessage{})
	
	fmt.Println("Password: ", votanteRespuesta)
}