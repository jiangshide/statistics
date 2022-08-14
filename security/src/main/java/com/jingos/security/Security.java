package com.jingos.security;

public class Security {
    private static volatile Security instance;

    private Security() {
    }

    public static Security getInstance() {
        if (instance == null) {
            synchronized (Security.class) {
                if (instance == null) {
                    instance = new Security();
                }
            }
        }
        return instance;
    }

    /**
     * the validate with res
     *
     * @return boolean
     */
    public boolean isValidate(){
        return isValidate(true);
    }

    /**
     * the validate with res
     *
     * @param isValidateModel
     * @return boolean
     */
    public boolean isValidate(boolean isValidateModel) {
        return Constants.validate(Constants.getDeviceInfo(isValidateModel));
    }
}
