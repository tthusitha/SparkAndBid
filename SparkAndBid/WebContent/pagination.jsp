
			<nav aria-label="Navigation for products">
				<ul class="pagination justify-content-center">
					<c:if test="${currentPage != 1}">
						<li class="page-item"><a class="page-link"
							href="ProductController?currentPage=${1}"><<</a></li>
						<li class="page-item"><a class="page-link"
							href="ProductController?currentPage=${currentPage-1}"><</a></li>
					</c:if>
					<c:if test="${currentPage lt nbPages}">
						<li class="page-item"><a class="page-link"
							href="ProductController?currentPage=${currentPage+1}">></a></li>
					</c:if>
					<c:if test="${currentPage != nbPages}">
						<li class="page-item"><a class="page-link"
							href="ProductController?currentPage=${nbPages}">>></a></li>
					</c:if>
				</ul>
			</nav>