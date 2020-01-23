package backup;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

@WebServlet(urlPatterns = "/UploadServlet", loadOnStartup = 1)
@MultipartConfig(maxFileSize = 99999999999l)
public class UploadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		Part parts = request.getPart("file");
		InputStream fileInputStream = parts.getInputStream();
		BufferedReader br = new BufferedReader(new InputStreamReader(fileInputStream));
		ArrayList<String> a = new ArrayList<>();
		try {
			String nextLine;
			while (null != (nextLine = br.readLine())) {
				a.add(nextLine);
			}
		} catch (IOException ex) {
		}
		
		ArrayList<Job> jobs = new ArrayList<>();
		for (String x : a) {
			String[] xx = x.split(",");
			jobs.add(new Job(xx[0], xx[1], xx[2], xx[3], xx[4], xx[5], xx[6], xx[8], xx[10], xx[16], xx[17]));
		}
		
		ServletContext sc = this.getServletContext();
		sc.setAttribute("out", jobs);
		sc.setAttribute("lastUpdate", new Date());
		response.sendRedirect("/ATI/listbackups");;
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		doPost(request, response);
	}
}