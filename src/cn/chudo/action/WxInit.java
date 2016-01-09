package cn.chudo.action;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import cn.chudo.tools.WxTools;
@WebServlet("/init.do")
public class WxInit extends HttpServlet {
	private String AppID="wx24776bc023d019e7";
	private String AppSecret="18fdfe4222e1c1c7df01b9788253f364";
	private static final long serialVersionUID = 1L;
	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		HttpSession session = request.getSession();
		String s=(String) session.getAttribute("signature");
		if(s==null){
		    String url="http://chudo.cn/WxPlatform/init.do";
		    String nonce_str = WxTools.create_nonce_str();
		    String timestamp = WxTools.create_timestamp();
		    String signature=WxTools.getSignature(AppID, AppSecret,timestamp,nonce_str,url);		    
		    session.setAttribute("nonce_str",nonce_str);
		    session.setAttribute("timestamp",timestamp);
		    session.setAttribute("signature",signature);
		}
        getServletContext().getRequestDispatcher("/index.jsp").forward(request, response);
	}
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {	
	}
}
