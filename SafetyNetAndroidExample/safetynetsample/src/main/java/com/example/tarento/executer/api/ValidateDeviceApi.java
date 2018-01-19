package com.example.tarento.executer.api;


import com.android.volley.Request;
import com.example.tarento.model.BaseModel;
import com.example.tarento.model.ValidateDeviceApiRequest;
import com.example.tarento.model.ValidateDeviceApiResponse;
import com.example.tarento.util.Constant;
import com.example.tarento.util.Utility;

/**
 * ValidateDeviceApi
 * <p>
 * Request:
 * {
 * "nonce" : "5cefa6e7-0da0-44aa-aacb-b26c3be0c8a0",
 * "deviceId" : "fd7659d6046987bd",
 * "attestation" : "dvbdvb"
 * }
 * <p>
 *
 * @author Indraja Machani
 */
public class ValidateDeviceApi extends BaseApi {

    private static final String TAG = ValidateDeviceApi.class.getSimpleName();

    public ValidateDeviceApi(ValidateDeviceApiRequest validateDeviceApiRequest) {
        // set partial url
        setUrl(Constant.API_VALIDATE_DEVICE);
        // set request id
        setRequestId(Constant.REQUESTID_VALIDATE_DEVICE_API);
        //set method
        setMethod(Request.Method.POST);
        //set body data
        setBody(Utility.toJson(validateDeviceApiRequest, ValidateDeviceApiRequest.class));
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
            response = Utility.getGson().fromJson(responseJsonObject, ValidateDeviceApiResponse.class);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

}
