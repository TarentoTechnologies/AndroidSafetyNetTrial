package com.example.tarento.executer.api;


import com.android.volley.Request;
import com.example.tarento.model.BaseModel;
import com.example.tarento.model.GetNonceApiRequest;
import com.example.tarento.model.GetNonceApiResponse;
import com.example.tarento.util.Constant;
import com.example.tarento.util.Utility;

/**
 * GetNonceApi
 * <p>
 * Request:
 * {"deviceId":"fd7659d6046987bd"}
 * <p>
 * Response:
 * {"responseStatus":true,
 * "nonce":"5cefa6e7-0da0-44aa-aacb-b26c3be0c8a0"}
 *
 * @author Indraja Machani
 */
public class GetNonceApi extends BaseApi {

    private static final String TAG = GetNonceApi.class.getSimpleName();

    public GetNonceApi(GetNonceApiRequest getNonceApiRequest) {
        // set partial url
        setUrl(Constant.API_GET_NONCE);
        // set request id
        setRequestId(Constant.REQUESTID_GETNONCE_API);
        //set method
        setMethod(Request.Method.POST);

        //set body data
        setBody(Utility.toJson(getNonceApiRequest, GetNonceApiRequest.class));
    }

    /**
     * serialize response
     *
     * @param responseJsonObject
     * @return
     * @throws Exception
     */
    @Override
    public BaseModel serialize(String responseJsonObject) {
        BaseModel response = null;
        try {
            // parse the response data
            response = Utility.getGson().fromJson(responseJsonObject, GetNonceApiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
