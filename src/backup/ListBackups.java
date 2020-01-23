package backup;

import java.io.IOException;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet("/listbackups")
public class ListBackups extends HttpServlet {
	private static final long serialVersionUID = 1L;
	SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");

	public ListBackups() {
		super();
	}

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		response.setContentType("text/html; charset=utf-8");
		Writer w = response.getWriter();
		w.append("<!DOCTYPE html><html><head><link href=\"css/bootstrap.min.css\" rel=\"stylesheet\">"
				+ "<link href=\"css/style.css\" rel=\"stylesheet\"></head><body>");
		w.append("<header><nav><a href=\"/ATI\">Main</a><a href=\"/ATI/listbackups/json\">JSON</a></nav><div>"
				+ "Добро пожаловать!" + "</div>");

		if (this.getServletContext().getAttribute("lastUpdate") != null) {
			w.append("<div>Последнее обновление:" + sdf.format(this.getServletContext().getAttribute("lastUpdate"))
					+ "</div>");
		}
		w.append("</header>");
		w.append("<div align=\"center\"><h3>Backup Jobs:<h3></div>");
		ArrayList<Job> out = (ArrayList<Job>) this.getServletContext().getAttribute("out");
		if (out != null) {
			w.append(
					"<section style=\"max-width: 1600px; margin: 0 auto;\"><table class=\"table table-striped table-hover\">"
							+ "<thead class=\"thead-dark\">" + "<tr><th>Job ID</th>" + "<th>Job Type</th>"
							+ "<th>Job State</th>" + "<th>Оperation Status</th>" + "<th>Job Policy</th>"
							+ "<th>Job Shedule</th>" + "<th>Client</th>" + "<th>Start Time</th>" + "<th>End Time</th>"
							+ "<th>Volume,KB</th>" + "<th>Files</th>" + "</tr></thead>");

			if (request.getParameter("servers") == null || request.getParameter("servers").equals("all")) {
				printArray(out, w);
			} else if (request.getParameter("servers").equals("vlg")) {
				ArrayList<Job> vlg = new ArrayList<>();
				for (Job job : out) {
					if (job.getPolicy().contains("app411") || job.getClient().contains("app172")) {
						vlg.add(job);
					}
				}
				printArray(vlg, w);
			}
		} else {
			w.append("<div align=\"center\">no data</div>");
		}
		w.append("</section></body></html>");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doGet(request, response);
	}

	private void printArray(ArrayList<Job> list, Writer writer) throws IOException {
		for (Job job : list) {

			if (job.getOperationStatus().equals("0")) {
				writer.append("<tr>" + "<td>" + job.getId() + "</td>");
			} else if (job.getOperationStatus().equals("2850")) {
				writer.append("<tr>" + "<td>" + job.getId() + "</td>");
			} else if (job.getState().equals("1")) {
				writer.append("<tr class=\"active\">" + "<td>" + job.getId() + "</td>");
			} else {
				writer.append("<tr class=\"danger\">" + "<td>" + job.getId() + "</td>");
			}
			switch (job.getType()) {
			case "0":
				if (job.getShedule().equals("-")) {
					writer.append("<td>Snapshot</td>");
				} else {
					writer.append("<td>Backup</td>");
				}
				break;
			case "2":
				writer.append("<td>Restore</td>");
				break;
			case "6":
				writer.append("<td>HotCatalog</td>");
				break;
			case "17":
				writer.append("<td>Image Cleanup</td>");
				break;
			default:
				writer.append("<td>" + job.getType() + "</td>");
				break;
			}

			switch (job.getState()) {
			case "3":
				writer.append("<td>Done</td>");
				break;
			case "1":
				writer.append("<td>Active</td>");
				break;
			case "5":
				writer.append("<td>Incomlete</td>");
				break;
			default:
				writer.append("<td>" + job.getState() + "</td>");
				break;
			}

			switch (job.getOperationStatus()) {
			case "0":
				writer.append("<td>OK</td>");
				break;
			case "13":
				writer.append("<td>File read failed</td>");
				break;
			case "24":
				writer.append("<td>Socket write failed</td>");
				break;
			case "25":
				writer.append("<td>Can't connect on socket</td>");
				break;
			case "41":
				writer.append("<td>Network connection timed out</td>");
				break;
			case "58":
				writer.append("<td>Can't connect to client</td>");
				break;
			case "150":
				writer.append("<td>Termination requested<br>by administrator</td>");
				break;
			case "156":
				writer.append("<td>Snapshot error encountered</td>");
				break;
			case "2850":
				writer.append("<td>Restore error</td>");
				break;
			default:
				writer.append("<td>" + job.getOperationStatus() + "</td>");
				break;
			}

			writer.append("<td>" + job.getPolicy() + "</td>" + "<td>" + job.getShedule() + "</td>" + "<td>"
					+ job.getClient() + "</td>" + "<td>"
					+ sdf.format(Date.from(Instant.ofEpochSecond(Long.parseLong(job.getStartTime())))) + "</td>");
			if (!(job.getEndTime().equals("0000000000"))) {
				writer.append("<td>" + sdf.format(Date.from(Instant.ofEpochSecond(Long.parseLong(job.getEndTime()))))
						+ "</td>");
			} else {
				writer.append("<td></td>");
			}
			writer.append("<td>" + job.getVolume() + "</td>" + "<td>" + job.getCountFiles() + "</td>");
		}
	}
}