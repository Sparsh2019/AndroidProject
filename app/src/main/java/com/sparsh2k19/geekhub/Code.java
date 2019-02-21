package com.sparsh2k19.geekhub;

public class Code {
    private String filename = null;
    private String referenceid;
    private String source = null;
    private String lang = null;

    Code() {}

    Code(String referenceid) {
        this.referenceid = referenceid;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getReferenceid() {
        return referenceid;
    }

    public String getLang() {
        return lang;
    }

    public String getSource() {
        return source;
    }

    public void setLang(String lang) {
        this.lang = lang;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
