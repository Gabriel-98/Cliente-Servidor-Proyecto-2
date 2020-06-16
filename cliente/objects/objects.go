package objects

import "main/jsontypes"

type JsonString = jsontypes.JsonString
type JsonInt = jsontypes.JsonInt
type JsonFloat = jsontypes.JsonFloat
type JsonBool = jsontypes.JsonBool


type Partido struct {
	Nit 			JsonString 		"json:\"nit\""
	Nombre 			JsonString 		"json:\"nombre\""
	Telefono 		JsonString 		"json:\"telefono\""
	Direccion 		JsonString 		"json:\"direccion\""
	Administracion 	JsonString 		"json:\"administracion\""
	Foto			JsonString 		"json:\"foto\""
}

type Candidato struct {
	Cedula 			JsonString 		"json:\"cedula\""
	Nombre 			JsonString 		"json:\"nombre\""
	Apellidos 		JsonString 		"json:\"apellidos\""
	Telefono 		JsonString 		"json:\"celular\""
	Foto 			JsonString 		"json:\"foto\""
	NitPartido		JsonString 		"json:\"nitPartido\""
}

type Eleccion struct {
	Codigo			JsonString 		"json:\"codigo\""
	Nombre 			JsonString 		"json:\"nombre\""
	Descripcion 	JsonString 		"json:\"descripcion\""
	ZonaHoraria 	JsonString 		"json:\"zonaHoraria\""
	FechaInicio		JsonString 		"json:\"fechaInicio\""
	FechaFin 		JsonString 		"json:\"fechaFin\""
}

type Votante struct {
	Cedula 			JsonString 		"json:\"cedula\""
	Email  			JsonString 		"json:\"email\""
	Nombre 			JsonString 		"json:\"nombre\""
	Apellidos 		JsonString  	"json:\"apellidos\""
	FechaNacimiento JsonString 		"json:\"fechaNacimiento\""
	Telefono 		JsonString 		"json:\"telefono\""
	Password 		JsonString 		"json:\"password\""
	Foto 			JsonString 		"json:\"foto\""
}

type Voto struct {
	CodigoEleccion 	JsonString 		"json:\"codigoEleccion\""
	CedulaVotante 	JsonString 		"json:\"cedulaVotante\""
	CedulaCandidato	JsonString 		"json:\"cedulaCandidato\""
}

type ResultadoCandidato struct {
	CodigoEleccion 	JsonString 		"json:\"codigoEleccion\""
	CedulaCandidato JsonString 		"json:\"cedulaCandidato\""
	NumeroVotos 	JsonInt 		"json:\"numeroVotos\""
}

type ErrorStatusMessage struct {
	Timestamp 		JsonString 		"json:\"timeStamp\""
	Status 			JsonInt 		"json:\"status\""
	Error 			JsonString 		"json:\"error\""
	Message			JsonString 		"json:\"message\""
	Path 			JsonString 		"json:\"path\""
}

func (o *ErrorStatusMessage) ReadErrorMessage() string {
	if o.Message.Set == false {
		return ""
	}else{
		return o.Message.Value
	}
}