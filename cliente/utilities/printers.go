package utilities

import "fmt"

func PrintCenter(data string, length int) {
	fmt.Print(" ")
	if len(data) >= length {
		fmt.Print(data[0:length])
	}else {
		var free, left, right int
		free = length -  len(data)
		left = (free + 1) / 2
		right = free / 2
		for i := 0; i<left; i++ {
			fmt.Print(" ")
		}
		fmt.Print(data)
		for i := 0; i<right; i++ {
			fmt.Print(" ")
		}
	}
	fmt.Print(" ")
}