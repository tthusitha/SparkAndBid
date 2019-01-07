<%@ include file="header.jsp"%>
<body>
    <!-- Navigation -->
    <nav class="navbar navbar-expand-lg navbar-dark bg-dark fixed-top">
      <div class="container">
        <a class="navbar-brand" href="#">Spark And Bid</a>
        <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarResponsive" aria-controls="navbarResponsive" aria-expanded="false" aria-label="Toggle navigation">
          <span class="navbar-toggler-icon"></span>
        </button>
        <div class="collapse navbar-collapse" id="navbarResponsive">
          <ul class="navbar-nav ml-auto">
            <li class="nav-item active">
              <a class="nav-link" href="#">Bienvenue ${username}
                <span class="sr-only">(current)</span>
              </a>
            </li>
            <li class="nav-item">
              <a class="nav-link" href="LogOut">D�connexion</a>
            </li>
          </ul>
        </div>
      </div>
    </nav>
	<%@ include file="list.jsp"%>
	<%@ include file="pagination.jsp" %>
	<%@ include file="footer.jsp" %>
	</body>
</html>