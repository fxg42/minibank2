<!DOCTYPE html>
<html>
<head>
  <meta charset="utf-8"/>
  <title>Create a new account holder</title>
  <link rel="stylesheet" href="/css/bootstrap.min.css"/>
</head>
<body>
  <div class="container">
    <div class="navbar">
      <div class="navbar-inner">
        <a class="brand" href="/">Minibank</a>
        <ul class="nav">
          <li><a href="/person/list">Account holder list</a></li>
        </ul>
      </div>
    </div>
    <div class="row">
      <div class="span2">
        <img src="/img/vault.gif"/>
      </div>
      <div class="span10">
        <form method="post" action="/person/create">
          <legend>Create a new account holder</legend>
          <% if (error) { %>
            <div class="alert alert-error"><%= error %></div>
          <% } %>
          <label for="name">name</label>
          <input name="personName" type="text" value=""><br>
          <br>   
          <button class="btn btn-success" type="submit">Submit</button> <a href="/person/list">Cancel</a>
        </form>        
      </div>
    </div>
  </div>
</body>
</html>

