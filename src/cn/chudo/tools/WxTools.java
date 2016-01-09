package cn.chudo.tools;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;
import net.sf.json.JSONObject;
public class WxTools {
	// 与接口配置信息中的Token要一致
	private static String token="zxcasdqwe123";
    public static String getJsApiTicket(String token){
    	String result=HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/ticket/getticket","access_token="+token+"&type=jsapi");
		JSONObject obj=JSONObject.fromObject(result);
		String ticket=obj.getString("ticket");
		if(ticket!=null&!ticket.equals(""))
			return ticket;
		else
			return "获取凭证出错";
    }
    
    public static String getAccessToken(String AppID,String AppSecret){
    	String jsonStr=HttpRequest.sendGet("https://api.weixin.qq.com/cgi-bin/token","grant_type=client_credential&appid="+AppID+"&secret="+AppSecret);
		JSONObject jObj=JSONObject.fromObject(jsonStr);
		String token=jObj.getString("access_token");
		if(token!=null&&!token.equals(""))
			return token;
		else
			return "获取令牌出错";
    }
    public static String create_nonce_str() {
        return UUID.randomUUID().toString();
    }
    public static String create_timestamp() {
        return Long.toString(System.currentTimeMillis() / 1000);
    }
    public static String getSignature(String AppID,String AppSecret, String timestamp, String nonce, String jsurl) throws IOException {
    /****
     * 对 jsapi_ticket、 timestamp 和 nonce 按字典排序 对所有待签名参数按照字段名的 ASCII
     * 码从小到大排序（字典序）后，使用 URL 键值对的格式（即key1=value1&key2=value2…）拼接成字符串
     * string1。这里需要注意的是所有参数名均为小写字符。 接下来对 string1 作 sha1 加密，字段名和字段值都采用原始值，不进行
     * URL 转义。即 signature=sha1(string1)。
     * **如果没有按照生成的key1=value&key2=value拼接的话会报错
     */
    	String token=getAccessToken(AppID,AppSecret);
    	String jsapi_ticket=getJsApiTicket(token);
    	System.out.println("jsapi_ticket:"+jsapi_ticket);
	    String[] paramArr = new String[] { "jsapi_ticket=" + jsapi_ticket,"timestamp=" + timestamp, "noncestr=" + nonce, "url=" + jsurl };
	    Arrays.sort(paramArr);
	    // 将排序后的结果拼接成一个字符串
	    String content = paramArr[0].concat("&"+paramArr[1]).concat("&"+paramArr[2]).concat("&"+paramArr[3]);
	    String gensignature = null;
	    try {
	        MessageDigest md = MessageDigest.getInstance("SHA-1");
	        // 对拼接后的字符串进行 sha1 加密
	        byte[] digest = md.digest(content.toString().getBytes());
	        gensignature = byteToStr(digest);
	    } catch (NoSuchAlgorithmException e) {
	        e.printStackTrace();
	    }
	    // 将 sha1 加密后的字符串与 signature 进行对比
	    if (gensignature != null) {
	        return gensignature.toLowerCase();// 返回signature
	    } else {
	        return "false";
	    }
	    // return (String) (ciphertext != null ? ciphertext: false);
     }
	/**
	 * 验证签名
	 * 
	 * @param signature
	 * @param timestamp
	 * @param nonce
	 * @return
	 */
	public static boolean checkSignature(String signature, String timestamp,String nonce) {
		String[] arr = new String[] { token, timestamp, nonce };
		// 将token、timestamp、nonce三个参数进行字典序排序
		Arrays.sort(arr);
		StringBuilder content = new StringBuilder();
		for (int i = 0; i < arr.length; i++) {
			content.append(arr[i]);
		}
		MessageDigest md = null;
		String tmpStr = null;
		try {
			md = MessageDigest.getInstance("SHA-1");
			// 将三个参数字符串拼接成一个字符串进行sha1加密
			byte[] digest = md.digest(content.toString().getBytes());
			tmpStr = byteToStr(digest);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		content = null;
		// 将sha1加密后的字符串可与signature对比，标识该请求来源于微信
		return tmpStr != null ? tmpStr.equals(signature.toUpperCase()) : false;
	}
    /** 
     * 将字节数组转换为十六进制字符串 
     *  
     * @param byteArray 
     * @return 
     */  
    private static String byteToStr(byte[] byteArray) {  
        String strDigest = "";  
        for (int i = 0; i < byteArray.length; i++) {  
            strDigest += byteToHexStr(byteArray[i]);  
        }  
        return strDigest;  
    }  
  
    /** 
     * 将字节转换为十六进制字符串 
     *  
     * @param mByte 
     * @return 
     */  
    private static String byteToHexStr(byte mByte) {  
        char[] Digit = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
        char[] tempArr = new char[2];  
        tempArr[0] = Digit[(mByte >>> 4) & 0X0F];  
        tempArr[1] = Digit[mByte & 0X0F];  
        String s = new String(tempArr);  
        return s;  
    } 
}
