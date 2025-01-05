<!DOCTYPE html>
<% 
    String tableResults = (String) session.getAttribute("tableResults");
    String sqlStatement = (String) session.getAttribute("sql");
%>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Root Home - AMT</title>
    <style>
        h1 {
            color: yellow;
            font-size: 28pt;
        }
        h2 {
            color: lime;
            font-size: 24pt;
        }
        body {
            background: black;
            color: white;
            text-align: center;
            font-family: Arial, sans-serif;
        }

        textarea {
            background: blue;
            color: white;
            font-family: Verdana;
            font-size: 15pt;
            width: 900px;
            height: 300px;
        }

        input {
            color: lime;
            background: #665D1E;
            font-weight: bold;
            font-size: 16pt;
        }
        input[type="button"] {
            color: red;
        }

        span {
            color: yellow;
            font-size: 20pt;
        }
        table {
            font-family: Verdana;
            border: 4px solid grey;
            margin-left: auto;
            margin-right: auto;
            color: black;
        }
        th {
            padding: 5px;
            border: 3px solid black;
            background: red;
            color: black;
        }
        td {
            padding: 5px;
            border: 3px solid black;
            color: black;
        }
        tr:nth-child(even) {
            background-color: lightgray;
        }
        tr:nth-child(odd) {
            background-color: white;
        }
    </style>
    <script>
        function resetForm() {
            document.getElementById("sql").value = "";
        }
        
        function clearResults() {
            document.getElementById("tableResults").innerHTML = "";
        }
    </script>
</head>
<body>
    <h1>Welcome to the Fall 2024 Project 4 Enterprise System</h1>
    <h2>A Servlet/JSP-based Multi-tiered Enterprise Application Using a Tomcat Container</h2>
    <hr>
    <div>
        You are connected to the Project 4 Enterprise System database as a 
        <span style="color:red; font-size: 16px;">root-level</span> user.
    </div>
    <div>Please enter any SQL query or update command in the box below.</div>
    <br>
    <form action="RootUser" method="post">
        <textarea id="sql" name="sql"><%= sqlStatement != null ? sqlStatement : "" %></textarea>
        <br><br>
        <input type="submit" value="Execute Command">
        <input type="button" value="Reset Form" onclick="resetForm()">
        <input type="button" value="Clear Results" style="color: yellow;" onclick="clearResults()">
    </form>
    <br><br>
    <div>
        All execution results will appear below this line.
    </div>
    <hr>
    <div style="font-weight: bold;">
        Execution Results:
    </div>
    <br>
    <div id="tableResults">
        <%= tableResults != null ? tableResults : "" %>
    </div>
</body>
</html>
    