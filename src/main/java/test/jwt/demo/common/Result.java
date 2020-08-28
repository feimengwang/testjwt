package test.jwt.demo.common;


import lombok.Data;

/**
 * @author Java猿
 */
@Data
public class Result<T> {

    public static final int SUCCESS_CODE = 200;
    public static final String SUCCESS_MSG = "SUCCESS";
    public static final int FAIL_CODE = -1;
    public static final String FAIL_MSG = "FAIL";
    private T data;
    private int code;

    public Result(T data, int code) {
        this.data = data;
        this.code = code;
    }

    public Result(T data) {
        this.data = data;
    }

    public static <T> Result SUCCESS(T data) {
        return new Result(data, SUCCESS_CODE);
    }

    public static <T> Result FAIL(T data) {
        return new Result(data, FAIL_CODE);
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }
}
