package utilities

import "io/ioutil"
import "encoding/json"
import "net/http"
import "fmt"

type ErrorMessage interface {
	ReadErrorMessage() string
}

func SendRequest(request *http.Request, data interface{}, errorBody ErrorMessage) error {
	client := http.Client{}
	var err error = nil

	response, err := client.Do(request)
	if err != nil {
		fmt.Println("Error al conectarse")
	}else{
		switch response.StatusCode {
		case 200:
			fmt.Println("Correcto")
			barr, err := ioutil.ReadAll(response.Body)
			if err != nil{
				fmt.Println("Error al leer el body de la respuesta")
				return err;
			}

			if data != nil {
				err = json.Unmarshal(barr, &data)
				if err != nil {
					fmt.Println("Error al realizar unmarshal de la estructura")
					fmt.Println(err)
				}else{
					fmt.Println(data)
				}
			}
		case 400:
			body := response.Body
			barr,err := ioutil.ReadAll(body)
			if err != nil {
				fmt.Println(err)
				return err;
			}
			err = json.Unmarshal(barr, &errorBody)
			if err != nil {
				fmt.Println(err)
				return err;
			}
			fmt.Println(errorBody.ReadErrorMessage())
		default:
			fmt.Println(response.Status)
		}
	}
	return nil
}