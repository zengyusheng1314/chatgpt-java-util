package com.sean.chatgpt.pojo.chat;

/**
 * 接口请求失败返回
 *
 * @author zengyusheng
 * @date 2023/04/11 15:46
 */
public class ErrorResponse {

    private Error error;


    public class Error {
        private String message;
        private String type;
        private String param;
        private String code;

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getParam() {
            return param;
        }

        public void setParam(String param) {
            this.param = param;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }
    }

    public Error getError() {
        return error;
    }

    public void setError(Error error) {
        this.error = error;
    }
}
