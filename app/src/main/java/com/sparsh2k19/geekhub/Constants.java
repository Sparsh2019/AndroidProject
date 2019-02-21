package com.sparsh2k19.geekhub;

class Constants {

    private Constants(){}

    static final int LOGIN_REQUEST = 1;
    static final int CAMERA_REQUEST = 2;
    static final int CODE_REQUEST = 3;

    /**
     * Evaluator's Note:
     * More Languages can be added over here as mentioned on the Hackerearth API Documentation at
     *
     * https://www.hackerearth.com/docs/wiki/developers/v3/
     *
     */
    static final String[] languages = {"C", "CPP", "JAVA", "JAVASCRIPT", "PYTHON"};

    /**
     * Evaluator's Note:
     * As for hackerearth API client secret, you can use the current provided client secret instead of obtaining a new key.
     * In case while using hackerearth api, if you get response "Error code: 1200", that is an error on server side. Errors like those would be neglected during evaluation
     * Although handling those errors would fetch you more points
     */
    static final String client_secret = "f9b249df2141bbcc45658e036898a613af787765";
    static final String server_url = "https://api.hackerearth.com/code/run/";

}
