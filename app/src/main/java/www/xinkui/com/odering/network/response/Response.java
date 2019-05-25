package www.xinkui.com.odering.network.response;

public class Response<T> {
    private int ret; // 返回的code
    private T data; // 具体的数据结果
    private String msg; // message 可用来返回接口的说明
    private long timestamp;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
