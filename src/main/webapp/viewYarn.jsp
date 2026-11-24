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
      <a href="addYarn" class="btn btn-success mb-3">Add New Yarn</a>
     <table class ="table table-striped table-bordered">
             <thead>

             <tr>
                 <th>Brand Name</th>
                 <th>Color</th>
                 <th>Amount of Yarn</th>
                 <th>Yarn Weight/Size</th>
                 <th>Recommended Hook Size</th>
                 <th>Bought From</th>
                 <th>Update Yarn</th>
                 <th>Delete Yarn</th>
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
                  <td>
                    <a href="editYarn?id=${yarn.id}"
                    class="btn btn-warning btn-sm">Edit</a>
                 </td>
                <td>
                     <form action="deleteYarn" method="post" class="d-inline"
                           onsubmit="return confirm('Are you sure you want to delete this yarn?');">
                         <input type="hidden" name="id" value="${yarn.id}">
                         <button type="submit" class="btn btn-danger btn-sm">
                             Delete
                         </button>
                     </form>
                </td>
             </tr>
             </c:forEach>
             </tbody>
         </table>


  </div>

  </body>
</html>
