package org.codekit.entity;


/**
 * 
* @ClassName: SmsRequest
* @Description: 短信发送请求实体
* @author Administrator
* @date 2018年7月27日 下午4:38:09
*
 */
public class SmsRequest {
    /**
     * 发送号码
     */
    private String mobile;

    /**
     * 发送内容
     */
    private String content;

	public String getMobile() {
		return mobile;
	}

	public void setMobile(String mobile) {
		this.mobile = mobile;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

    
}
