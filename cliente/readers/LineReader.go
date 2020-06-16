package readers

import "bufio"
import "os"

type LineReader struct {
	reader *bufio.Reader
}

func NewLineReader() LineReader{
	lineReader := LineReader{}
	lineReader.reader = bufio.NewReader(os.Stdin)
	return lineReader
}

func (o *LineReader) ReadLine() string {
	line, _ := o.reader.ReadString('\n')
	line = line[0:len(line)-1]
	return line
}