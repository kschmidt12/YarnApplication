<%@include file="head.jsp"%>
<%@include file="taglib.jsp"%>

<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Yarn Stash</title>
  </head>
  <body>
  <div class="container-fluid">
      <h2>Yarn Stash </h2>
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
                  <td>${yarn.brandName}</td>
                  <td>${yarn.color}</td>
                  <td>${yarn.yarnAmount}</td>
                  <td>${yarn.yarnSize}</td>
                  <td>${yarn.hookSize}</td>
                  <td>${yarn.boughtFrom}</td>
             </tr>
             </c:forEach>
             </tbody>
         </table>


  </div>

  </body>
</html>
