package utilities

import "bytes"
import "errors"
import "io/ioutil"
import "encoding/json"
import "net/http"
import "fmt"

type ErrorMessage interface {
	ReadErrorMessage() string
}

func CreateRequest(method string, url string, body interface{}) (*http.Request, error) {
	var request *http.Request
	var err error
	if body == nil {
		request, err = http.NewRequest(method, url, nil)
		
	}else {
		bodyBytes, _ := json.Marshal(body)
		request, err = http.NewRequest(method, url, bytes.NewBuffer(bodyBytes))
	}
	if err == nil {
		request.Header.Add("Content-Type", "application/json")
	}
	return request, err
}

func SendRequest(request *http.Request, data interface{}, errorBody ErrorMessage) error {
	client := http.Client{}
	var err error = nil

	response, err := client.Do(request)
	if err != nil {
		fmt.Println("[*] Error al conectarse")
		return err
	}else{
		switch response.StatusCode {
		case 200:
			fmt.Println("[*] Correcto")
			barr, err := ioutil.ReadAll(response.Body)
			if err != nil{
				fmt.Println("[*] Error al leer el body de la respuesta")
				return err
			}

			if data != nil {
				err = json.Unmarshal(barr, &data)
				if err != nil {
					fmt.Println("[*] Error al realizar unmarshal de la estructura")
					fmt.Println(err)
					return err
				}
			}
		case 400:
			body := response.Body
			barr, err := ioutil.ReadAll(body)
			if err != nil {
				fmt.Println("[*]", err)
				return err;
			}
			err = json.Unmarshal(barr, &errorBody)
			if err != nil {
				fmt.Println("[*]", err)
				return err;
			}
			fmt.Println(errorBody.ReadErrorMessage())
			return errors.New("Status Code: 400 " + errorBody.ReadErrorMessage())
		default:
			fmt.Println("[*]", response.Status)
			return errors.New("Invalid Status Code")
		}
	}
	return nil
}