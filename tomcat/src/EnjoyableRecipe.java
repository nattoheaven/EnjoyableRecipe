

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.Locale;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;

/**
 * Servlet implementation class McDonalds
 */
@WebServlet("/")
@MultipartConfig(location = "/nginx", maxFileSize = 20 * 1024 * 1024, maxRequestSize = 20 * 1024 * 1024)
public class EnjoyableRecipe extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	static {
		try {
			Class.forName("org.postgresql.Driver");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}
	
	static private abstract class Query {
		protected final PreparedStatement statement;
		Query(PreparedStatement s) {
			statement = s;
		}
		public abstract String executeQuery() throws SQLException;
	}
	
	static final class SelectQuery extends Query{
		SelectQuery(PreparedStatement s) {
			super(s);
		}
		SelectQuery(Connection c, String q) throws SQLException {
			this(c.prepareStatement(q));
		}
		@Override
		public String executeQuery() throws SQLException {
			ResultSet resultSet = statement.executeQuery();
			StringBuilder sb = new StringBuilder();
			try {
				while (resultSet.next()) {
					sb.append(resultSet.getString("json_agg"));
				}
			} catch (SQLException e) {
				throw e;
			} finally {
				resultSet.close();
			}
			return sb.toString();
		}
	}
       
	static final class UpdateQuery extends Query{
		UpdateQuery(PreparedStatement s) {
			super(s);
		}
		UpdateQuery(Connection c, String q) throws SQLException {
			this(c.prepareStatement(q));
		}
		@Override
		public String executeQuery() throws SQLException {
			statement.executeUpdate();
			return "";
		}
	}
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public EnjoyableRecipe() {
        super();
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String url = "jdbc:postgresql://localhost/testdb";
		int id = -1;
		try {
			id = Integer.parseUnsignedInt(request.getParameter("id"));
		} catch (NumberFormatException e) {
		}
		String method = request.getParameter("method");
		String q = request.getParameter("q");
		try {
			Connection connection = DriverManager.getConnection(url, "nishihei", "123456");
			try {
				Query[] queries;
				String head, foot;
				if (id == -1) {
					if (q != null) {
						StringBuilder sb = new StringBuilder();
						sb.append("WITH cte AS ( SELECT testtable.id, title, address, latitude, longitude FROM (testtable INNER JOIN footprinttable ON testtable.id = footprinttable.id) WHERE visible = TRUE AND");
						boolean isFirstToken = true;
						for (StringTokenizer st = new StringTokenizer(q); st.hasMoreTokens();) {
							st.nextToken();
							if (!isFirstToken) {
								sb.append(" AND");
							}
							isFirstToken = false;
							sb.append(" (type_cuisine ILIKE ? OR area ILIKE ?)");
						}
						sb.append(" ORDER BY footprint DESC ) SELECT json_agg(cte.*) FROM cte;");
						PreparedStatement statement = connection.prepareStatement(sb.toString());;
						int iStatementString = 1;
						for (StringTokenizer st = new StringTokenizer(q); st.hasMoreTokens();) {
							String token = st.nextToken();
							statement.setString(iStatementString++, "%" + token + "%");
							statement.setString(iStatementString++, "%" + token + "%");
						}
						queries = new Query[] {
								new SelectQuery(statement),
						};
						head = "<html><head><title>EnjoyableRecipe</title><script src=\"/static/js.js\" type=\"text/javascript\"></script><script src=\"/static/list.js\" type=\"text/javascript\"></script><script>\ndocument.addEventListener(\"DOMContentLoaded\",function(e){f(";
						foot =
						")});\n</script>" +
						"<link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css.css\">" +
						"</head>" +
						"<body style=\"background-image:url(/static/l_101.png);\">" +
						"<h1 style=\"text-align:center;font-style:italic;\">" +
						"<a href=\"/\" style=\"color:black;\">EnjoyableRecipe</a></h1>" +
						"<p>Click to show the restaurant's overview.</p>" +
						"<form action=\"./\" method=\"get\">" +
						"<h2 class=\"header\">" +
						"Search results: " +
						"<input type=\"text\" name=\"q\" value=\"" + q +"\" placeholder=\"Genre / area.\" size=\"10\">" +
						"<input type=\"submit\" value=\"Search\">" +
						"</h2>" +
						"</form>" +
						"<dl id=\"list\"></dl>" +
						"<p style=\"text-align:center;\">< 1/5 2 3 4 5 ></p>" +
						"<p class=\"copyright\">© 2016 EnjoyableRecipe</p>" +
						"</body>" +
						"</html>";
					} else /*if ("top".equals(method))*/ {
						queries = new Query[] {};
						head = "<html><head><title>EnjoyableRecipe</title><script src=\"/static/js.js\" type=\"text/javascript\"></script>";
						foot =
						"<link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css.css\">" +
						"</head><body style=\"background-image:url(/static/l_101.png);\">" +
						"<h1 style=\"text-align:center;font-style:italic;\">EnjoyableRecipe</h1>" +
						"<div class=\"left\">" +
						"<p>Choose a type of cuisine to search.</p>" +
						"<ul>" +
						"<li><a href=\"./?q=japanese\">Japanese</a></li>" +
						"<li><a href=\"./?q=french\">French</a></li>" +
						"<li><a href=\"./?q=italian\">Italian</a></li>" +
						"<li><a href=\"./?q=cafe\">Cafe</a></li>" +
						"</ul>" +
						"</div>" +
						"<div class=\"right\">" +
						"<p>Choose an area to search.</p>" +
						"<ul>" +
						"<li><a href=\"./?q=shinjuku\">Shinjuku</a></li>" +
						"<li><a href=\"./?q=kagurazaka\">Kagurazaka</a></li>" +
						"<li><a href=\"./?q=hongo\">Hongo</a></li>" +
						"<li><a href=\"./?q=koenji\">Koenji</a></li>" +
						"</ul>" +
						"</div>" +
						"<br class=\"clear\">" +
						"<form action=\"./\" method=\"get\">" +
						"<p>" +
						"<input type=\"text\" name=\"q\" value=\"\" placeholder=\"Genre / area.\">" +
						"<input type=\"submit\" value=\"Search\">" +
						"</p>" +
						"</form>" +
						"<p class=\"copyright\">© 2016 EnjoyableRecipe</p>" +
						"</body>" +
						"</html>";
						/*
					} else {
						queries = new Query[] {
								new SelectQuery(connection, "WITH cte AS ( SELECT id, title, address, latitude, longitude FROM testtable ORDER BY footprint DESC ) SELECT json_agg(cte.*) FROM cte;")
								};
						head = "<html><head><title>EnjoyableRecipe</title><script src=\"/static/js.js\" type=\"text/javascript\"></script><script src=\"/static/list.js\" type=\"text/javascript\"></script><script>\ndocument.addEventListener(\"DOMContentLoaded\",function(e){f(";
						foot = ")});\n</script><link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css.css\"></head><body style=\"background-image:url(/static/l_101.png);\"><h1 style=\"text-align:center;font-style:italic;\">EnjoyableRecipe</h1><p>Click to show the restaurant's overview.</p><h2 class=\"header\">Tokyo Minato area:</h2><dl id=\"list\"></dl><p style=\"text-align:center;\">< 1/5 2 3 4 5 ></p></body></html>";
						*/
					}
				} else if ("details".equals(method)) {
					PreparedStatement statement0 = connection.prepareStatement("UPDATE footprinttable SET footprint=footprint+1 WHERE id=?;");
					statement0.setInt(1, id);
					PreparedStatement statement1 = connection.prepareStatement("WITH cte AS ( SELECT title, num_seats, smoke, wifi, english_menu, credit_card, genre, hours FROM testtable WHERE id=? ) SELECT json_agg(cte.*) FROM cte;");
					statement1.setInt(1, id);
					queries = new Query[] {
							new UpdateQuery(statement0),
							new SelectQuery(statement1)
							};
					String overviewUrl = "./?id=" + id + "&method=overview";
					head = "<html><head><title id=\"title\"></title><script src=\"/static/js.js\" type=\"text/javascript\"></script><script src=\"/static/swipe.js\" type=\"text/javascript\"></script><script src=\"/static/details.js\" type=\"text/javascript\"></script><script>\nsetSwipeUrl(null,\"" + overviewUrl +"\");\ndocument.addEventListener(\"DOMContentLoaded\",function(e){f(";
					foot = ")});\n</script><link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css.css\"></head><body style=\"background-image:url(/static/details" + id + ".jpg);\"><h1 id=\"h1\" class=\"header\"></h1><table><tr><th>Number of seats:</th><td id=\"num_seats\"></td></tr><tr><th>Smoking:</th><td id=\"smoke\"></td></tr><tr><th>Wi-Fi:</th><td id=\"wifi\"></td></tr><tr><th>English menus:</th><td id=\"english_menu\"></td></tr><tr><th>Credit cards:</th><td id=\"credit_card\"></td></tr><tr><th>Genre:</th><td id=\"genre\"></td></tr><tr><th>Hours:</th><td id=\"hours\"></td></tr></table><p><a href=\"" + overviewUrl + "\">Back to the overview.</a></p><p><a href=\"./\">Back to the top page.</a></p><p class=\"copyright\">© 2016 EnjoyableRecipe</p></body></html>";
				} else if ("reviews".equals(method)) {
					PreparedStatement statement0 = connection.prepareStatement("WITH cte AS ( SELECT review, nationality, gender, age, photo, timestamp FROM reviewtable WHERE restaurant_id=? AND confirmed=TRUE AND deleted=FALSE ORDER BY timestamp DESC ) SELECT json_agg(cte.*) FROM cte;");
					statement0.setInt(1, id);
					queries = new Query[] {
							new SelectQuery(statement0)
							};
					String overviewUrl = "./?id=" + id + "&method=overview";
					head = "<html><head><title id=\"title\"></title><script src=\"/static/js.js\" type=\"text/javascript\"></script><script src=\"/static/swipe.js\" type=\"text/javascript\"></script><script src=\"/static/review.js\" type=\"text/javascript\"></script><script>\nsetSwipeUrl(null,\"" + overviewUrl +"\");\ndocument.addEventListener(\"DOMContentLoaded\",function(e){f(";
					foot = ")});\n</script><link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css.css\"></head><body style=\"background-image:url(/static/reviews" + id + ".jpg);\"><h1 class=\"header\">User reviews</h1><div id=\"reviews\"></div>" +
					"<form action=\"/\" enctype=\"multipart/form-data\" method=\"post\">" +
					"<input type=\"hidden\" name=\"id\" value=\"" + id + "\">" +
					"<input type=\"hidden\" name=\"method\" value=\"review_submit\">" +
					"<p>Nationality (optional): <input type=\"text\" name=\"nationality\"></p>" +
					"<p>Gender (optional): <select name=\"gender\">" +
					"<option value=\"\"></option>" +
					"<option value=\"male\">Male</option>" +
					"<option value=\"female\">Female</option>" +
					"</select>" +
					"<p>Age (optional): <select name=\"age\">" +
					"<option value=\"\"></option>" +
					"<option value=\"- 14\">- 14</option>" +
					"<option value=\"15 - 24\">15 - 24</option>" +
					"<option value=\"25 - 34\">25 - 34</option>" +
					"<option value=\"35 - 44\">35 - 44</option>" +
					"<option value=\"45 - 54\">45 - 54</option>" +
					"<option value=\"55 - 64\">55 - 64</option>" +
					"<option value=\"65 -\">65 -</option>" +
					"</select>" +
					"</p>" +
					"<p>" +
					"Review:" +
					"<br>" +
					"<textarea name=\"review\" value=\"\">" +
					"</textarea>" +
					"</p>" +
					"<p>" +
					"If you have a photo taken in this restaurant and you want to let it shown on EnjoyableRecipe, you can upload it." +
					"</p>" +
					"<p>" +
					"Photo (optional): <span id=\"photoSpan\"></span><input type=\"button\" value=\"Select...\" onclick=\"document.getElementById('photo').click();\"><input type=\"button\" value=\"Reset\" onclick=\"createPhotoForm(); document.getElementById('photoImg').removeAttribute('src');\">" +
					"</p>" +
					"<p>" +
					"<img id=\"photoImg\" style=\"width:100%;\">" +
					"</p>" +
					"<p><br>*Your review will be confirmed by us before published.</p>" +
					"<p><input type=\"Submit\" value=\"submit\"></p>" +
					"</form>" +
					"<p><a href=\"" + overviewUrl + "\">Back to the overview.</a></p><p><a href=\"./\">Back to the top page.</a></p><p class=\"copyright\">© 2016 EnjoyableRecipe</p></body></html>";
				} else if ("review_submit".equals(method)) {
					long timestamp = new Date().getTime();
					String review = request.getParameter("review");
					String nationality = request.getParameter("nationality");
					String gender = request.getParameter("gender");
					String age = request.getParameter("age");
					Part part = request.getPart("photo");
					String photoName = part.getSubmittedFileName();
					String photoFile;
					if ("".equals(photoName)) {
						photoFile = null;
					} else {
						photoFile = "review" + timestamp + photoName.substring(photoName.lastIndexOf('.')).toLowerCase(Locale.ENGLISH);
					}
					if (photoFile != null) {
						part.write(photoFile);
					}
					PreparedStatement statement0 = connection.prepareStatement("INSERT INTO reviewtable (restaurant_id, review, nationality, gender, age, photo, timestamp) VALUES (?, ?, ?, ?, ?, ?, ?);");
					statement0.setInt(1, id);
					statement0.setString(2, review);
					if (nationality.isEmpty()) {
						statement0.setString(3, null);
					} else {
						statement0.setString(3, nationality);
					}
					if (gender.isEmpty()) {
						statement0.setString(4, null);
					} else {
						statement0.setString(4, gender);
					}
					if (age.isEmpty()) {
						statement0.setString(5, null);
					} else {
						statement0.setString(5, age);
					}
					statement0.setString(6, photoFile);
					statement0.setTimestamp(7, new Timestamp(timestamp));
					statement0.executeUpdate();
					try {
				        InternetAddress toAddress = new InternetAddress("enjoyablerecipe@gmail.com");
						Properties property = new Properties();
				        property.put("mail.smtp.host","smtp.gmail.com");
				        property.put("mail.smtp.auth", "true");
				        property.put("mail.smtp.starttls.enable", "true");
				        property.put("mail.smtp.host", "smtp.gmail.com");
				        property.put("mail.smtp.port", "587");
				        property.put("mail.smtp.debug", "true");
				        Session session = Session.getInstance(property, new javax.mail.Authenticator() {
				        	@Override
				        	protected PasswordAuthentication getPasswordAuthentication(){
				                return new PasswordAuthentication("enjoyablerecipe@gmail.com", "mikan4133");
				            }
				        });
				        MimeMessage	mimeMessage = new MimeMessage(session);
				        mimeMessage.setRecipient(Message.RecipientType.TO, toAddress);
				        mimeMessage.setFrom(toAddress);
				        mimeMessage.setSubject("ユーザーレビュー投稿");
				        mimeMessage.setText("ナショナリティ: " + nationality + "\n" +
				        "ジェンダー: " + gender + "\n" +
				        "年齢: " + age + "\n" +
				        "写真: " + (photoFile == null ? "" : "http://www.enjoyablerecipe.com/static/" + photoFile) + "\n" +
				        "本文: \n" + review + "\n");
				        Transport.send(mimeMessage);
					} catch (AddressException e) {
						e.printStackTrace();
					} catch (MessagingException e) {
						e.printStackTrace();
					}
					response.setCharacterEncoding("UTF-8");
					response.setContentType("text/html");
					String reviewsUrl = "./?id=" + id + "&method=reviews";
					response.setHeader("Refresh", "3;URL=" + reviewsUrl);
					PrintWriter aWriter = response.getWriter();
					aWriter.print("<html><head><title>Thank you</title><script src=\"/static/js.js\" type=\"text/javascript\"></script><link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css.css\"></head><body style=\"background-image:url(/static/reviews" + id + ".jpg);line-height:200%;\"><p>Thank you for your contributing!</a><p>*Your review will be confirmed by us before published.</p><p class=\"copyright\">© 2016 EnjoyableRecipe</p></body></html>");
					aWriter.flush();
					return;
				} else {
					PreparedStatement statement0 = connection.prepareStatement("UPDATE overviewfootprinttable SET footprint=footprint+1 WHERE id=?;");
					statement0.setInt(1, id);
					PreparedStatement statement1 = connection.prepareStatement("WITH cte AS ( SELECT title, address, gaiyo, latitude, longitude, overview, coupon, message, access, prices FROM testtable WHERE id=? ) SELECT json_agg(cte.*) FROM cte;");
					statement1.setInt(1, id);
					queries = new Query[] {
							new UpdateQuery(statement0),
							new SelectQuery(statement1),
							};
					String detailsUrl = "./?id=" + id + "&method=details";
					String reviewsUrl = "./?id=" + id + "&method=reviews";
					head = "<html><head><title id=\"title\"></title><script src=\"/static/js.js\" type=\"text/javascript\"></script><script src=\"/static/swipe.js\" type=\"text/javascript\"></script><script src=\"/static/content.js\" type=\"text/javascript\"></script><script>\nsetSwipeUrl(\"" + detailsUrl +"\",null);\ndocument.addEventListener(\"DOMContentLoaded\",function(e){f(";
					foot = ")});\n</script><link rel=\"stylesheet\" type=\"text/css\" href=\"/static/css.css\"></head><body style=\"background-image:url(/static/" + id + ".jpg);line-height:200%;\"><h1 id=\"h1\" class=\"header\"></h1><p><a href=\"" + detailsUrl + "\" id=\"gaiyo\"></a></p><p id=\"overview\"></p><p id=\"google-maps\"></p><ul id=\"access\"></ul><div id=\"prices\"></div><div id=\"coupon\"></div><div id=\"message\"></div><p><a href=\"" + reviewsUrl + "\">User reviews.</a></p><a href=\"./\">Back to the top page.</a></p><p class=\"copyright\">© 2016 EnjoyableRecipe</p></body></html>";
				}
				response.setCharacterEncoding("UTF-8");
				response.setContentType("text/html");
				PrintWriter aWriter = response.getWriter();
				aWriter.print(head);
				for (int iQuery = 0; iQuery < queries.length; iQuery++) {
					Query query = queries[iQuery];
					try {
						aWriter.print(query.executeQuery());
					} catch (SQLException e) {
						e.printStackTrace();
					}
				}
				aWriter.print(foot);
				aWriter.flush();
			} catch (SQLException e) {
				e.printStackTrace();
			} finally {
				connection.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
