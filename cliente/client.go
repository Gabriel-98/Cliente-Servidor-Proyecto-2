package main

import "fmt"
import "main/modules"
import "main/readers"

func menuVotante(){
	lineReader := readers.NewLineReader()
	var option string = "1"

	for option != "0" {
		fmt.Println("Funciones: ")
		fmt.Println("1. Registrarse")
		fmt.Println("2. [X] Votar")

		fmt.Print("Ingrese una opcion: ")
		option = lineReader.ReadLine()
		switch option {
		case "1":
			modules.CrearVotante()
		case "2":
			//modules.()
		case "0":
			//
		default:
			fmt.Println("Codigo incorrecto")
		}
	}
}