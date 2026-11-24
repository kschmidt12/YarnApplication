<%@include file="head.jsp"%>
<%@include file="taglib.jsp"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Yarn Stash</title>
  </head>
  <body>
  <div class="container-fluid">
      <h2>Search Results: </h2>
     <table class ="table table-striped table-bordered">
             <thead>

             <tr>
                 <th>Brand Name</th>
                 <th>Color</th>
                 <th>Amount of Yarn</th>
                 <th>Yarn Weight/Size</th>
                 <th>Recommended Hook Size</th>
                 <th>Bought From</th>
             </tr>
             </thead>

             <tbody>
             <c:forEach var="yarn" items="${yarns}">
             <tr>
                  <td>${yarn.brandname}</td>
                  <td>${yarn.color}</td>
                  <td>${yarn.yarnamount}</td
                  <td>${yarn.yarnsize}</td>
                  <td>${yarn.hooksize}</td>
                  <td>${yarn.boughtfrom}</td>
             </tr>
             </c:forEach>
             </tbody>
         </table>


  </div>

  </body>
</html>
