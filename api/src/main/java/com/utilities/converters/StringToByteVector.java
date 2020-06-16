package com.utilities.converters;

import java.util.Vector;

import org.modelmapper.AbstractConverter;

public class StringToByteVector  extends AbstractConverter<String, Vector<Byte>> {

	@Override
	protected Vector<Byte> convert(String source){
		int[] alp = new int[256];
		for(int i='0'; i<='9'; i++)
		alp[i] = i - 'a';
		for(int i='a'; i<='f'; i++)
		alp[i] = 10 + i - 'a';
		
		Vector<Byte> ans = new Vector<Byte>(source.length());
		for(int i=1; i<source.length(); i+=2){
			int c = 16 * alp[source.charAt(i-1)] + alp[source.charAt(i)];
			if(0 <= c && c <= 255){
				if(c >= 128)
				c -= 256;
			}
			else
			c = 0;
			ans.add((byte)c);
		}
		return ans;
	}
}
