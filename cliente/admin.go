package main

import "fmt"
import "main/modules"
import "main/readers"

func menu(){
	lineReader := readers.NewLineReader()
	var option string

	fmt.Println("\nFunciones:")
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
	fmt.Println("16. Bloquear votante")
	fmt.Println("17. Desbloquear votante")
	fmt.Println("18. Consultar votos realizados")
	fmt.Println("19. Listar votantes")

	fmt.Print("Ingrese una opcion: ")
	option, _ = lineReader.ReadLine()
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
	case "14":
		modules.MostrarResultadoEleccion()
	case "15":
		modules.ListarElecciones()
	case "16":
		modules.BloquearVotante()
	case "17":
		modules.DesbloquearVotante()
	case "18":
		modules.ListarVotos()
	case "19":
		modules.ListarVotantes()
	default:
		fmt.Println("Codigo incorrecto")
	}
}


func main(){
	lineReader := readers.NewLineReader()
	fmt.Println("admin[S/], votante: ")
	line, _ := lineReader.ReadLine()
	if line == "S" {
		menu()
	}else {
		menuVotante()
	}
}