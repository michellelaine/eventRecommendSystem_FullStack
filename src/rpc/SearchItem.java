package rpc;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Item;

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
		double lat = Double.parseDouble(request.getParameter("lat"));
		double lon = Double.parseDouble(request.getParameter("lon"));
		String term = request.getParameter("term");
		
		DBConnection connection = DBConnectionFactory.getConnection();//create DB connection
		List<Item> items = connection.searchItems(lat, lon, term);
		connection.close();
		
		List<JSONObject> list = new ArrayList<>();
		try {
			for (Item item : items) {
				JSONObject obj = item.toJSONObject();
				list.add(obj);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		JSONArray array = new JSONArray(list);		
		RpcHelper.writeJsonArray(response, array);
		         
        // return Json response  --> a Json array   
//        JSONArray array = new JSONArray();
//        String username = "";
//		if(request.getParameter("username") != null) {
//			username = request.getParameter("username");
//		}
//        try {
//        	array.put(new JSONObject().put("username", "abcd"));
//        	array.put(new JSONObject().put("username", "yhr"));
//        	array.put(new JSONObject().put("username", username));
//        }catch(JSONException e){
//			e.printStackTrace();
//		}
//        RpcHelper.writeJsonArray(response, array);
        
        
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
