<%@ include file="header.jsp"%>
<body>
<div class="container">
<div class="row">
<div class="col-md-4"></div>
  <div class="wrapper">
    <form class="form-signin" method ="post" action="InscriptionController">       
      <h2 class="form-signin-heading">Inscrivez-vous</h2>
      <input type="text" class="form-control" id="username" name="username" placeholder="Nom d'utilisateur" required="" autofocus="" />
      <br><input type="password" class="form-control" id="password" name="password" placeholder="Mot de passe" required=""/>
      <input type="text" class="form-control" id="adresse" name ="adresse" placeholder="Adresse" required="" autofocus="" />
      <br><input type="text" class="form-control" id="email" name ="email" placeholder="Mail" required="" autofocus="" />      
      <br><button class="btn btn-lg btn-primary btn-block" type="submit">S'inscrire</button>   
    </form>
  </div>
  </div>
  </div>
</body>
</html>