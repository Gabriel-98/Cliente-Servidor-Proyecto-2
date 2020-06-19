package main

import "fmt"
import "main/modules"
import "main/readers"

func menuVotante(){
	lineReader := readers.NewLineReader()
	var option string = "1"

	for option != "0" {
		fmt.Println("\nFunciones: ")
		fmt.Println("1. Registrarse")
		fmt.Println("2. Votar")

		fmt.Print("Ingrese una opcion: ")
		option, _ = lineReader.ReadLine()
		switch option {
		case "1":
			modules.CrearVotante()
		case "2":
			modules.Votar()
		case "0":
			//
		default:
			fmt.Println("Codigo incorrecto")
		}
	}
}