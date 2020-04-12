package rpc;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class SearchItem
 */
@WebServlet("/search")
public class SearchItem extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public SearchItem() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		         
        // return Json response  --> a Json array   
        JSONArray array = new JSONArray();
        String username = "";
		if(request.getParameter("username") != null) {
			username = request.getParameter("username");
		}
        try {
        	array.put(new JSONObject().put("username", "abcd"));
        	array.put(new JSONObject().put("username", "1234"));
        	array.put(new JSONObject().put("username", username));
        }catch(JSONException e){
			e.printStackTrace();
		}
        RpcHelper.writeJsonArray(response, array);
        
        
/*     
 * return Json Object   -> {username, abc}
		String username = "";
		if(request.getParameter("username") != null) {
			username = request.getParameter("username");
		}
		JSONObject obj = new JSONObject();
		try {
			obj.put("username", username);
		}catch(JSONException e){
			e.printStackTrace();
		}
		out.print(obj);
		out.close(); 
*/		
		
	
		/*return html page -> static page 
		response.setContentType("text/html");
		PrintWriter out = response.getWriter();
		out.println("<html><body>");
		out.println("<h1>This is s HTML page<h1>");
		out.println("</body><html>");
		out.close();*/
		
		/*
		 * return simple response --> hello abc
		PrintWriter out = response.getWriter();
		
		if(request.getParameter("username") != null) {
			String username = request.getParameter("username");
			out.print("Hello " + username);
		}
		
		out.close();*/
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
