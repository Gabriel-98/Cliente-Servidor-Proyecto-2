package jsontypes

import "strconv"
import "errors"
import "bufio"
import "os"
import "fmt"
import "main/readers"


type JsonType interface {
	MarshalJSON() ([]byte, error)
	UnmarshalJSON(data []byte) error
	ReadLine() error
}


type JsonString struct {
	Value string
	Set bool
}

type JsonInt struct {
	Value int64
	Set bool
}

type JsonFloat struct {
	Value float64
	Set bool
}

type JsonBool struct {
	Value bool
	Set bool
}

func (o *JsonString) MarshalJSON() ([]byte, error) {
	if o.Set {
		return []byte("\"" + o.Value + "\""), nil
	}else {
		return []byte("null"), nil
	}
}

func (o *JsonInt) MarshalJSON() ([]byte, error) {
	if o.Set {
		return []byte(strconv.FormatInt(o.Value, 10)), nil
	}else {
		return []byte("null"), nil
	}
}

func (o *JsonFloat) MarshalJSON() ([]byte, error) {
	if o.Set {
		return []byte(strconv.FormatFloat(o.Value,'f',-1, 64)), nil
	}else {
		return []byte("null"), nil
	}
}

func (o *JsonBool) MarshalJSON() ([]byte, error) {
	if o.Set {
		return []byte(strconv.FormatBool(o.Value)), nil
	} else {
		return []byte("null"), nil
	}
}


func (o *JsonString) UnmarshalJSON(data []byte) error {
	if string(data) == "null" {
		return nil
	} else {
		str := string(data)
		if len(str) >= 2 && str[0] == '"' && str[len(str)-1] == '"' {
			o.Value = str[1:len(str)-1]
			o.Set = true
		} else {
			return errors.New("Error no es una cadena")
		}
		return nil
	}
}

func (o *JsonInt) UnmarshalJSON(data []byte) error {
	if string(data) == "null" {
		return nil
	} else {
		var err error
		o.Value, err = strconv.ParseInt(string(data), 10, 64)
		o.Set = true
		return err
	}
}

func (o *JsonFloat) UnmarshalJSON(data []byte) error {
	var err error
	if string(data) == "null" {
		return nil
	} else {
		o.Value, err = strconv.ParseFloat(string(data), 64)
		o.Set = true
	}
	return err
}

func (o *JsonBool) UnmarshalJSON(data []byte) error {
	var err error
	if string(data) == "null" {
		return nil
	} else {
		o.Value, err = strconv.ParseBool(string(data))
		o.Set = true
	}
	return err
}


// Asignaciones
func (o *JsonString) Assign(data string) {
	o.Value = data
	o.Set = true
}

func (o *JsonInt) Assign(data int64) {
	o.Value = data
	o.Set = true
}

func (o *JsonFloat) Assign(data float64) {
	o.Value = data
	o.Set = true
}

func (o *JsonBool) Assign(data bool) {
	o.Value = data
	o.Set = true
}


// Test -> por corregir

func (o *JsonString) ReadLine() error {
	reader := bufio.NewReader(os.Stdin)
	line, err := reader.ReadString('\n')
	if err == nil {
		line = line[0:len(line)-1]
		if line != "" {
			o.Value = line
			o.Set = true
		}
	}
	return err
}


func (o *JsonInt) ReadLine() error {
	reader := bufio.NewReader(os.Stdin)
	line, err := reader.ReadString('\n')
	if err == nil {
		line = line[0:len(line)-1]
		if line != "" {
			value, err := strconv.ParseInt(line, 10, 64)
			if err == nil {
				o.Value = value
				o.Set = true
			}
		}
	}
	return err
}

// Metodos para leer hasta que obtenga un valor corecto

func ReadValidLine(target JsonType, message string) {
	for true {
		fmt.Print(message)
		err := target.ReadLine()
		if err == nil {
			break
		}else {
			fmt.Println("[*] error")
		}
	}
}

func ReadConfirmationAndValidLine(target JsonType, confirmationMessage string, message string) {
	reader := bufio.NewReader(os.Stdin)
	fmt.Print(confirmationMessage)
	line, _ := reader.ReadString('\n')
	line = line[0:len(line)-1]
	if line == "S" {
		ReadValidLine(target, message)
	}
}

// Lectura de archivos
func ReadFileAsHexadecimalString(target *JsonString, maxSize int64, confirmationMessage string, message string, errorMessage string) {
	fmt.Println(confirmationMessage)
	lineReader := readers.NewLineReader()
	fileReader := readers.NewFileReader(maxSize)
	line := lineReader.ReadLine()
	if line == "S" {
		var foto string
		var err error 
		for true {
			fmt.Print(message)
			ubicacion := lineReader.ReadLine()
			if ubicacion == "" {
				break
			}
			foto, err = fileReader.ReadAsHexadecimalString(ubicacion)
			if err != nil {
				fmt.Println("[*]", errorMessage)
			}else{
				target.Assign(foto)
				break
			}
		}
	}
}
