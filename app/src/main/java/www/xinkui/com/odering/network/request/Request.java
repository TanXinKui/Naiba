package www.xinkui.com.odering.network.request;

import java.util.ArrayList;
import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import www.xinkui.com.odering.bean.Advertisement;
import www.xinkui.com.odering.bean.Comment;
import www.xinkui.com.odering.bean.DishState;
import www.xinkui.com.odering.bean.Register;
import www.xinkui.com.odering.bean.SellState;
import www.xinkui.com.odering.bean.Transcation;
import www.xinkui.com.odering.bean.User;
import www.xinkui.com.odering.network.response.Response;
/**
*@description
*@author TONXOK
*@time 2019/5/20 16:27
*/
public interface Request {
    /**
     * 填上需要访问的服务器地址
     */
    public static String HOST = "http://www.tanxinkui.cn:8080/ordering/";

    /**
     * 用户登录接口
     */
    @POST("queryUser")
    Observable<Response<String>> userLogin(@Body User user);

    /**
     * 展示商家广告
     */
    @GET("showAdvertisement")
    Observable<Response<ArrayList<Advertisement>>> getAdvertisementInfo();

    /**
     * 展示评论
     */
    @GET("showComment")
    Observable<Response<ArrayList<Comment>>> getComment();

    /**
     * 用户账户接口
     */
    @POST("ShowUserInfo")
    Observable<Response<Integer>> showBalance(@Query("username") String username);

    /**
     * 注册接口
     */
    @POST("UserRegister")
    Observable<Response<String>> userRegister(@Body Register register);

    /**
     * 查询dish状态
     */
    @GET("GetDishState")
    Observable<Response<ArrayList<DishState>>> getDishState();

    /**
     * 提交评论
     */
    @POST("SubmitComment")
    Observable<Response<String>> submitComment(@Body Comment comment);


    /**
     * 设置商家已经查看
     */
    @POST("StateManage")
    Observable<Response<String>> setStateManage(@Query("deskNum") int deskNum,@Query("stateNum") int stateNum,@Query("stateKind") int stateKind);

    /**
     * 注册接口
     */
    @POST("Transcation")
    Observable<Response<String>> transcation(@Body Transcation transcation);

    /**
     * 展示sellState
     */
    @GET("SellState")
    Observable<Response<ArrayList<SellState>>> getSellState();

}
