package rpc;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet(name = "recommendation", urlPatterns = "/recommendation")
public class Recommendation extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter writer = response.getWriter();

        writer.println("<html><body>");
        writer.println("<h1>Hello World</h1>");
        writer.println("</body></html>");

        writer.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        JSONArray array = new JSONArray();
        try {
            array.put(new JSONObject().put("name", "abcd").put("address", "San Francisco").put("time", "01/01/2017"));
            array.put(new JSONObject().put("name", "1234").put("address", "San Jose").put("time", "01/01/2017"));
        } catch (JSONException e) {
            e.printStackTrace();
        }
        RpcHelper.writeJsonArray(response, array);
    }
}
