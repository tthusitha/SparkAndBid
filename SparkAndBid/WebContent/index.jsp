<%@ include file="header.jsp"%>
<body>
<div class="container">
<div class="row">
<div class="col-md-4"></div>
  <div class="wrapper">
    <form class="form-signin" method ="post" action="Login">       
      <h2 class="form-signin-heading">Connectez-vous</h2>
      <input type="text" class="form-control" id="username" name="username" placeholder="Nom d'utilisateur" required="" autofocus="" />
      <br><input type="password" class="form-control" id="password" name="password" placeholder="Mot de passe" required=""/>      
      <label class="text">Vous n'avez pas de compte ?</label>
      <a href="InscriptionController">S'inscrire</a>
      <button class="btn btn-lg btn-primary btn-block" type="submit">Se Connecter</button>   
    </form>
  </div>
  </div>
  </div>
</body>
</html>