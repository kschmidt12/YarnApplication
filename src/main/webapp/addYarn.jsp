<%@include file="head.jsp"%>
<%@include file="taglib.jsp"%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
  <head>
    <title>Add Yarn</title>
  </head>
  <body>
    <form action="addYarn" method="post">
    <div class="mb-3">
        <label for="brandName" class="form-label">Brand Name</label>
        <input type="text" class="form-control"
               id="brandName" name="brandName"
               placeholder="e.g. Lion Brand" required>
    </div>
    <div class="mb-3">
        <label for="color" class="form-label">Color</label>
        <input type="text" class="form-control"
               id="color" name="color"
               placeholder="e.g. Purple" required>
    </div>
    <div class="mb-3">
        <label for="yarnAmount" class="form-label">Yarn Amount</label>
        <input type="number" class="form-control"
               id="yarnAmount" name="yarnAmount"
               placeholder="e.g. 4" min="1" step="1" required>
    </div>
    <div class="mb-3">
        <label for="yarnSize" class="form-label">Yarn Weight</label>
        <select class="form-select" id="yarnSize" name="yarnSize" required>
            <option value="" selected disabled>Select yarn weight</option>
            <option value="0 - Lace">0 - Lace</option>
            <option value="1 - Super Fine">1 - Super Fine</option>
            <option value="2 - Fine">2 - Fine</option>
            <option value="3 - Light">3 - Light</option>
            <option value="4 - Medium(Worsted)">4 - Medium (Worsted)</option>
            <option value="5 - Bulky">5 - Bulky</option>
            <option value="6 - Super Bulky">6 - Super Bulky</option>
            <option value="7 - Jumbo">7 - Jumbo</option>
        </select>
    </div>
    <div class="mb-3">
            <label for="hookSize" class="form-label">Recommended Hook Size</label>
            <input type="text" class="form-control"
                   id="hookSize" name="hookSize"
                   placeholder="e.g. 5.5mm" required>
        </div>
    <div class="mb-3">
            <label for="boughtFrom" class="form-label">Purchased From</label>
            <input type="text" class="form-control"
                   id="boughtFrom" name="boughtFrom"
                   placeholder="e.g. Hobby Lobby" required>
        </div>
    <button type="submit" class="btn btn-primary">Add Yarn</button>
    <a href="viewYarn" class="btn btn-secondary">Back to the Stash</a>
    </form>
  
  </body>
</html>
