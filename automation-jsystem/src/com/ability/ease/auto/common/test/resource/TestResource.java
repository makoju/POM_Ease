package com.ability.ease.auto.common.test.resource;

import java.io.File;

import jsystem.framework.JSystemProperties;
import jsystem.framework.ParameterProperties;

public class TestResource { 
	private String os;
	public enum resourceLocation {
		Imag_Automation_Home_Dir , Imag_DB_Home_Dir
	}
	public String relativePath;
	public File source;
	public resourceLocation destinationEntity;
	private boolean shouldCopy =true;

	public String getConstructedPath() throws Exception{
		String resourcePrefix ="";
		switch (destinationEntity) {
		 case Imag_Automation_Home_Dir :
			 os = "windows";
			 resourcePrefix = "c:\\automation\\";
			 break;
		 case Imag_DB_Home_Dir :
			 os = "windows";
			 resourcePrefix = "c:\\db\\";
			 break;
		}

		if (relativePath.startsWith("/") || relativePath.startsWith("\\")){ // remove leading slash if existed
			relativePath = relativePath.substring(1);
		}
		
		String sourceFileName = "";
		File srcFile = null;
		if (source!=null) {
			srcFile = new File(JSystemProperties.getCurrentTestsPath() + source.getPath().replaceAll("classes", ""));
		} else {
			srcFile = new File("");
		}
		
		if (os.toLowerCase().contains("windows")){// remove trailing slash if existed
			if ( (relativePath.endsWith("\\") || relativePath.endsWith("/") ) && relativePath.length()>2){
				relativePath = relativePath.substring(0, relativePath.length()-1);
			}
			//next check verify path NOT end with file, disregarding cases like ..\a\b\c
			if ( (relativePath.lastIndexOf("/") >= relativePath.lastIndexOf(".")) || 
				 (relativePath.lastIndexOf("\\")>= relativePath.lastIndexOf(".")) ) {
				relativePath = relativePath + "\\";
				
				if (srcFile.isFile()) {
					sourceFileName = source.getName();
				}
			}
		}
		else {
			relativePath = relativePath.replaceAll("\\\\", "/");
			if ( relativePath.lastIndexOf("/") >= relativePath.lastIndexOf(".")){
				relativePath = relativePath + "/";
			}
			
			if (srcFile.isFile()) {
				sourceFileName = source.getName();
			}
		
		}
		
		return resourcePrefix + relativePath + sourceFileName;


	}

	public resourceLocation getDestinationEntity() {
		return destinationEntity;
	}
	@ParameterProperties(description = "Destination Entity")
	public void setDestinationEntity(resourceLocation destinationEntity) {
		this.destinationEntity = destinationEntity;
	}
	public String getRelativePath() {
		return relativePath;
	}
	@ParameterProperties(description = "Relative Path")
	public void setRelativePath(String relativePath) {
		this.relativePath = relativePath;
	}
	public File getSource() {
		return source;
	}
	@ParameterProperties(description = "Source File or Folder")
	public void setSource(File source) {
		this.source = source;
	}
	public boolean isShouldCopy() {
		return shouldCopy;
	}

	public void setShouldCopy(boolean shouldCopy) {
		this.shouldCopy = shouldCopy;
	}
	public String getConstructedPathUnixSperator() throws Exception {
		if ("windows".equalsIgnoreCase(os)){
			return getConstructedPath().replaceAll("\\\\", "/");
		}else{
			return getConstructedPath();
		}
	}
}
