package org.codekit.service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;
import org.codekit.entity.SmsRequest;
import org.codekit.utils.CacheUtils;
import org.codekit.utils.CommonProperties;
import org.codekit.utils.HttpClientUtil;
import org.codekit.utils.MsgCode;
import org.codekit.utils.SignUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import net.sf.json.JSONObject;

@Service
public class SendSMSService {
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
    private CacheUtils cacheUtils;

	/**
	 * 批量发送短信通知
	* @Title: sendGropMsg
	* @Description: TODO(这里用一句话描述这个方法的作用)
	* @param @param smsList
	* @param @return    设定文件
	* @return JSONObject    返回类型
	* @throws
	 */
	public static JSONObject sendGropMsg(List<SmsRequest> smsList) {
   	 	String apiUrl = CommonProperties.serverUrl + "/send/groupsms";
        
   	 	Map<String, String> form = new HashMap<String, String>();
        Map data = new HashMap();
        data.put("data", smsList);
        
        String param = JSONObject.fromObject(data).toString();
        String nonce_str = SignUtil.create_nonce_str();
        String timestamp = SignUtil.create_timestamp();
        String signature = SignUtil.sign(CommonProperties.appId, CommonProperties.appSecret, nonce_str, param,timestamp);
        form.put("appid", CommonProperties.appId);
        form.put("nonce_str", nonce_str);
        form.put("timestamp", timestamp);
        form.put("sign", signature);
        form.put("param", param);
        String res = HttpClientUtil.doPost(apiUrl, form);
        
        JSONObject result = new JSONObject();
        result.accumulate("code", MsgCode.SUCCESS.getCode());
        result.accumulate("msg", MsgCode.SUCCESS.getDescription());
        result.accumulate("data", res);
        return result;
	}
	
	/**
	 *
	* @Title: sendTemplateMessage
	* @Description: 发送短信验证码模板
	* @param @param phone
	* @param @param cid
	* @param @param param
	* @param @return    设定文件
	* @return JSONObject    返回类型
	* @throws
	 */
	public JSONObject sendTemplateMessage(String[] phone, String cid, String[] param) {
        Map<String, String> para = new HashMap<String, String>();
        para.put("mob", StringUtils.join(phone, ','));
        para.put("uid", CommonProperties.WEIMI_CID);
        para.put("pas", CommonProperties.WEIMI_PAS);
        
        // 接口返回类型：json、xml、txt。默认值为txt
        para.put("type", "json");
        para.put("cid", cid);
        if (param != null && param.length > 0) {
            for (int i = 0; i < param.length; i++) {
                para.put("p" + (i + 1), param[i]);
            }
        }

        JSONObject result = new JSONObject();
        try {
            String res = HttpClientUtil.doPost("http://api.weimi.cc/2/sms/send.html", para, "UTF-8");
            result.accumulate("code", MsgCode.SUCCESS.getCode());
            result.accumulate("msg", MsgCode.SUCCESS.getDescription());
            result.accumulate("data", res);
        } catch (Exception e) {
            logger.error("Send Template Message Error", e);
            result.accumulate("code", MsgCode.FAILED.getCode());
            result.accumulate("msg", "短信发送失败!");
        }
        return result;
    }

	/**
	 * 
	* @Title: sendMessage
	* @Description: 发送普通短息
	* @param @param phone
	* @param @param content
	* @param @return    设定文件
	* @return JSONObject    返回类型
	* @throws
	 */
    public JSONObject sendMessage(String[] phone, String content) {
    	JSONObject result = new JSONObject();
    	
        Map<String, String> para = new HashMap<String, String>();
        para.put("mob", StringUtils.join(phone, ','));
        para.put("uid", CommonProperties.WEIMI_CID);
        para.put("pas", CommonProperties.WEIMI_PAS);

        /**
         * 接口返回类型：json、xml、txt。默认值为txt
         */
        para.put("type", "json");
        para.put("con", content);

        try {
            String res = HttpClientUtil.doPost("http://api.weimi.cc/2/sms/send.html", para, "UTF-8");
            JSONObject obj = JSONObject.fromObject(res);
            if (res != null && obj.optInt("code", -1) == 0) {
            	 result.accumulate("code", MsgCode.SUCCESS.getCode());
                 result.accumulate("msg", MsgCode.SUCCESS.getDescription());
                 result.accumulate("data", res);
            }else {
            	result.accumulate("code", MsgCode.FAILED.getCode());
                result.accumulate("msg", "短信发送失败!");
            }
        } catch (Exception e) {
            logger.error("Send Message Error", e);
            result.accumulate("code", MsgCode.FAILED.getCode());
            result.accumulate("msg", "短信发送失败!");
        }
        return result;
    }

    /**
     * 发送验证码
     *   smsService.sendValifyCode(p, 6, 180);
     * @param length
     * @param expire
     * @return
     */
    public boolean sendValifyCode(String phone, int length, int expire) {
        StringBuilder sb = new StringBuilder();

        if (length <= 0) {
            length = 6;
        }

        for (int i = 0; i < length; i++) {
            sb.append(RandomUtils.nextInt(10));
        }
        
        boolean flag = false;
        
        JSONObject result = sendTemplateMessage(new String[] { phone }, CommonProperties.WEIMI_CID,new String[] {sb.toString()});
        if (result != null && result.getInt("code") == 0) {
            if(expire <=0) {
                expire = 60;
            }
            cacheUtils.addCache("code-" + phone, expire, sb.toString());
            flag = true;
        }
        
        return flag;
    }

    /**
     * 验证验证码
     * 
     * @param code
     * @return
     */
    public boolean checkValifyCode(String phone, String code) {
        if (StringUtils.isNotEmpty(phone) && StringUtils.isNotEmpty(code)) {
            String v = cacheUtils.getCache("code-" + phone);
            return code.equals(v);
        }
        return false;
    }

}
