package readers

import "errors"
import "io/ioutil"
import "os"

type FileReader struct {
	maxSize int64
}

func NewFileReader(maxSize int64) FileReader {
	fileReader := FileReader{}
	fileReader.maxSize = maxSize
	return fileReader
}

func (o *FileReader) ToHexadecimalString(bytes []byte) string {
	var alp []int = []int{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'}
	var imgArr []byte = make([]byte, 2 * len(bytes))

 	for i := 0; i<len(bytes); i++ {
 		var ascii int = int(bytes[i])
 		imgArr[2*i] = byte(alp[ascii / 16])
 		imgArr[2*i+1] = byte(alp[ascii % 16])
 	}
 	return string(imgArr)
}

func (o *FileReader) ReadAsHexadecimalString(path string) (string, error) {
	var err error
	var bytes []byte
	file, err := os.Open(path)

	if err != nil {
		return "", err;
	}
	fileInfo, err := file.Stat()
 	if err != nil {
 		return "", err;
 	}

 	imgSize := fileInfo.Size()
 	if imgSize > o.maxSize {
 		return "", errors.New("File size exceeded")
 	}

	bytes, err = ioutil.ReadFile(path)
	if err != nil {
		return "", err
	}

	return o.ToHexadecimalString(bytes), nil
}