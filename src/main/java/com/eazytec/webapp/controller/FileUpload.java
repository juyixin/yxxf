package com.eazytec.webapp.controller;


/**
 * Command class to handle uploading of a file.
 *
 * @author madan
 */
public class FileUpload {
    private String name;
    private byte[] file;
    private String description;
    private String classificationId;

    

	/**
     * @return Returns the name.
     */
    public String getName() {
        return name;
    }

    /**
     * @param name The name to set.
     */
    public void setName(String name) {
        this.name = name;
    }

    public void setFile(byte[] file) {
        this.file = file;
    }

    public byte[] getFile() {
        return file;
    }

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}
	
	public String getClassificationId() {
		return classificationId;
	}

	public void setClassificationId(String classificationId) {
		this.classificationId = classificationId;
	}
}
