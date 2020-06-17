package com.utilities;

public class FileBytes {

	private byte[] byteArray;
	
	public FileBytes(byte[] byteArray){
		this.byteArray = new byte[byteArray.length];
		for(int i=0; i<byteArray.length; i++)
		this.byteArray[i] = byteArray[i];
	}
	
	public FileBytes(String fileString){
		if(fileString == null)
		byteArray = new byte[0];
		else{		
			int size = fileString.length() / 2;
			byteArray = new byte[size];
			
			int[] alp = new int[256];
			for(int i=0; i<256; i++)
			alp[i] = 0;
			for(int i='0'; i<='9'; i++)
			alp[i] = i - '0';
			for(int i='A'; i<='F'; i++)
			alp[i] = 10 + i - 'A';
			
			for(int i=0,j=0; i<size; i++,j+=2){
				int ascii = 16 * alp[fileString.charAt(j)] + alp[fileString.charAt(j+1)];
				if(ascii >= 128)
				ascii -= 256;
				byteArray[i] = (byte)ascii;
			}
		}
	}
	
	public int size(){
		return byteArray.length;
	}
	
	public byte get(int i){
		return byteArray[i];
	}
	
	public String toString(){
		char[] alp = {'0','1','2','3','4','5','6','7','8','9','A','B','C','D','E','F'};
		int[] div = new int[256];
		int[] mod = new int[256];
		for(int i=0,id=0; i<16; i++){
			for(int j=0; j<16; j++,id++){
				div[id] = i;
				mod[id] = j;
			}
		}
		
		char[] array = new char[2 * byteArray.length];
		for(int i=0; i<byteArray.length; i++){
			int ascii = byteArray[i];
			if(ascii < 0)
			ascii += 256;
			array[i+i] = alp[div[ascii]];
			array[i+i+1] = alp[mod[ascii]]; 
		}
		return new String(array);
	}
	
	public byte[] getByteArray(){
		byte[] ans = new byte[byteArray.length];
		for(int i=0; i<byteArray.length; i++)
		ans[i] = byteArray[i];
		return ans;
	}
}
