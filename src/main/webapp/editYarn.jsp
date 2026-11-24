<%@include file="head.jsp"%>
<%@include file="taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Update Yarn</title>
  </head>
  <body>
    <form action="editYarn" method="post">
    <input type="hidden" name="id" value="${yarn.id}">
    <div class="mb-3">
        <label for="brandName" class="form-label">Brand Name</label>
        <input type="text" class="form-control"
               id="brandName" name="brandName"
               value="${yarn.brandName}" required>
    </div>
    <div class="mb-3">
        <label for="color" class="form-label">Color</label>
        <input type="text" class="form-control"
               id="color" name="color"
               value="${yarn.color}" required>
    </div>
    <div class="mb-3">
        <label for="yarnAmount" class="form-label">Yarn Amount</label>
        <input type="number" class="form-control"
               id="yarnAmount" name="yarnAmount"
               value="${yarn.yarnAmount}" min="1" step="1" required>
    </div>
    <div class="mb-3">
        <label for="yarnSize" class="form-label">Yarn Weight</label>
        <select class="form-select" id="yarnSize" name="yarnSize" required>
            <option value="0" ${yarn.yarnSize == 0 ? 'selected' : ''}>0 - Lace</option>
            <option value="1" ${yarn.yarnSize == 1 ? 'selected' : ''}>1 - Super Fine</option>
            <option value="2" ${yarn.yarnSize == 2 ? 'selected' : ''}>2 - Fine</option>
            <option value="3" ${yarn.yarnSize == 3 ? 'selected' : ''}>3 - Light</option>
            <option value="4" ${yarn.yarnSize == 4 ? 'selected' : ''}>4 - Medium (Worsted)</option>
            <option value="5" ${yarn.yarnSize == 5 ? 'selected' : ''}>5 - Bulky</option>
            <option value="6" ${yarn.yarnSize == 6 ? 'selected' : ''}>6 - Super Bulky</option>
            <option value="7" ${yarn.yarnSize == 7 ? 'selected' : ''}>7 - Jumbo</option>
        </select>
    </div>
    <div class="mb-3">
            <label for="hookSize" class="form-label">Recommended Hook Size</label>
            <input type="text" class="form-control"
                   id="hookSize" name="hookSize"
                   value="${yarn.hookSize}" required>
        </div>
    <div class="mb-3">
            <label for="boughtFrom" class="form-label">Purchased From</label>
            <input type="text" class="form-control"
                   id="boughtFrom" name="boughtFrom"
                   value="${yarn.boughtFrom}" required>
        </div>
    <button type="submit" class="btn btn-primary">Update Yarn</button>
    <a href="viewYarn" class="btn btn-secondary">Back to the Stash</a>
    </form>
  
  </body>
</html>
