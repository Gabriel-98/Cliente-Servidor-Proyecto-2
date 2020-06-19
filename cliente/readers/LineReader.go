package readers

import "bufio"
import "os"
import "runtime"

type LineReader struct {
	reader *bufio.Reader
}

func NewLineReader() LineReader{
	lineReader := LineReader{}
	lineReader.reader = bufio.NewReader(os.Stdin)
	return lineReader
}

func (o *LineReader) ReadLine() (string, error) {
	var line string
	var err error
	line, err = o.reader.ReadString('\n')
	if err == nil {
		if runtime.GOOS == "linux" {
			line = line[0:len(line)-1]
		}else{
			line = line[0:len(line)-2]
		}
	}
	return line, err
}