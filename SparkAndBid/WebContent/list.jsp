<!-- Page Content -->
<div class="container">

	<!-- Jumbotron Header -->
	<header class="jumbotron my-4">
		<h1 class="display-3 text-center">Bienvenue sur notre site d'ench&egrave;res !</h1>
		<p class="lead">Pr&eacute;parez vos mises ! Nous avons ${length} produits &agrave; vous proposer !</p>
	</header>
	<!-- Page Features -->
	<div class="row text-center">
		<c:forEach items="${products}" var="product">
			<div class="col-lg-4 col-md-6 mb-4">
				<form name="formBid" action="ProductController" method="POST">
					<div class="card">
						<img class="card-img-top" src="http://placehold.it/500x325" alt="">
						<div class="card-body">
							<h4 class="card-title">${product.description}</h4>
							<p class="card-text">Prix de d&eacute;part : ${product.unitPrice} &euro;</p>
							<input type="hidden" id="existingPrice" value="${product.unitPrice}">
							<c:choose>
								<c:when test="${not empty product.bid.price}">
									<p class="card-text">Derni&egrave;re mise : ${product.bid.price} &euro; par ${product.bid.userName}</p>
									<input type="hidden"  id="lastBid" value="${product.bid.price}">
								</c:when>
								<c:otherwise>
									<p class="card-text">Pas d'ench&egrave;res encore</p>
								</c:otherwise>
							</c:choose>
						</div>
						<div class="card-footer">
							<input type="hidden" name="id" value="${product.id}">
							<label class="text">Votre mise : </label>
							<input type="text" name="bid"></input>
							<button type="submit" class="btn btn-primary mt-1">Ench&eacute;rir</button>
						</div>
					</div>
				</form>
			</div>
		</c:forEach>
	</div>
</div>