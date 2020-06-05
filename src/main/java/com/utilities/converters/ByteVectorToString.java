package com.utilities.converters;

import java.util.Vector;

import org.modelmapper.AbstractConverter;

public class ByteVectorToString  extends AbstractConverter<Vector<Byte>,String> {

	@Override
	protected String convert(Vector<Byte> source){
		char[] alp = {'0','1','2','3','4','5','6','7','8','9','a','b','c','d','e','f'};
		int[] div = new int[256];
		int[] mod = new int[256];
		for(int i=0,id=0; i<16; i++){
			for(int j=0; j<16; j++,id++){
				div[id] = i;
				mod[id] = j;
			}
		}
		
		char[] array = new char[2 * source.size()];
		for(int i=0; i<source.size(); i++){
			int c = source.get(i);
			if(c < 0)
			c += 256;
			array[i+i] = alp[div[c]];
			array[i+i+1] = alp[mod[c]]; 
		}
		return new String(array);
	}
}
