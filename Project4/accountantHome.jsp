<!DOCTYPE html>

<%
    // Retrieve session attributes
    String tableResults = (String) session.getAttribute("tableResults");
%>

<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Accountant Home - AMT</title>
    <style>
        h1 {
            color: lime;
            font-size: 28pt;
        }
        h2 {
            color: orange;
            font-size: 24pt;
        }
        body {
            background: black;
            color: white;
            text-align: center;
            font-family: Arial, sans-serif;
        }

        .multiple-choice {
            display: inline-block;
            background-color: gray;
            padding: 20px;
            width: 95%;
            text-align: left;
            color: blue;
        }
        .radio-option {
            margin: 30px 10px;
            font-size: 16pt;
        }
        input[type="radio"] {
            margin-right: 10px;
            cursor: pointer;
        }

        input[type="submit"], input[type="button"] {
            font-size: 16pt;
            font-weight: bold;
            padding: 10px 20px;
            border: none;
            border-radius: 5px;
            cursor: pointer;
        }
        input[type="submit"] {
            color: lime;
            background-color: #175c3d7a;
        }
        input[type="button"] {
            color: red;
            background-color: #175c3d7a;
        }
        
        
        .table-results {
            margin-top: 30px;
        }
        .results-container {
            margin-top: 30px;
        }
        .results-header {
            font-size: 18pt;
            font-weight: bold;
        }
        table {
            margin: 20px auto;
            width: 80%;
            border-collapse: collapse;
        }
        th, td {
            border: 1px solid white;
            padding: 10px;
            text-align: left;
        }
        th {
            background-color: #444;
        }
        tr:nth-child(even) {
            background-color: #666;
        }
        tr:nth-child(odd) {
            background-color: #555;
        }
    </style>
    <script>
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
            You are connected to the Project 4 Enterprise System database as an 
            <span style="color:red; font-size: 16px;">accountant-level</span> user.
        </div>
        <div>Please select the operation you would like to perform from the list below.</div>
        <br>
        <br>
        <div class="multiple-choice">
            <form action="AccountantUser" method="post">
                <div class="radio-option">
                    <input type="radio" name="query" value="MaxValueServlet" required>
                    Get The Maximum Status Value Of All Suppliers <span style="color: black">(Returns a maximum value)</span>
                </div>
                <div class="radio-option">
                    <input type="radio" name="query" value="SumServlet">
                    Get The Total Weight Of All Parts <span style="color: black">(Returns a sum)</span>
                </div>
                <div class="radio-option">
                    <input type="radio" name="query" value="TotalNumberShipments">
                    Get The Total Number of Shipments <span style="color: black">(Returns the current number of shipments in total)</span>
                </div>
                <div class="radio-option">
                    <input type="radio" name="query" value="NameNumJobMostWorkers">
                    Get The Name and Number of Workers of the Job with the Most Workers <span style="color: black">(Returns two values)</span>
                </div>
                <div class="radio-option">
                    <input type="radio" name="query" value="NameStatusSupplier">
                    List The Name And Status Of Every Supplier <span style="color: black">(Returns a list of supplier names with staus)</span>
                </div>
                <br>
                <div style="text-align: center;">
                    <input type="submit" value="Execute Command">
                    <input type="button" value="Clear Results" onclick="clearResults()">
                </div>
            </form>
        </div>
        <br><br>
        <div>
            All execution results will appear below this line.
        </div>
        <div class="table-results">
            <hr>
            <div style="font-weight: bold;">
                Execution Results:
            </div>
            <div id="tableResults">
                <%= tableResults != null ? tableResults : ""%>
            </div>
        </div>
</body>
</html>