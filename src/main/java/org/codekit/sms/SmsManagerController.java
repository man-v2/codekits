/**  
* @Title: SmsManagerController.java
* @Package org.codekit.sms
* @Description: TODO(用一句话描述该文件做什么)
* @author Administrator  
* @date 2018年7月27日 下午4:31:40
* @version V1.0  
*/ 
package org.codekit.sms;

import java.util.ArrayList;
import java.util.List;

import org.codekit.entity.SmsRequest;
import org.codekit.service.SendSMSService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import net.sf.json.JSONObject;

/**
* @ClassName: SmsManagerController
* @Description: TODO(发送短信)
* @author Administrator
* @date 2018年7月27日 下午4:31:40
*
*/
@RestController
@RequestMapping("/api/sms")
public class SmsManagerController {
	
	@Autowired
	private SendSMSService sendSmsService;
	
	@GetMapping("/test")
	public JSONObject testSendSmsGroup() throws Exception {
   	 	SmsRequest sms = new SmsRequest();
        sms.setMobile("13279376496");
        sms.setContent("这只是一条测试短信");
        List<SmsRequest> smsList = new ArrayList<SmsRequest>();
        smsList.add(sms);
        
        return sendSmsService.sendGropMsg(smsList);        
   }

}
